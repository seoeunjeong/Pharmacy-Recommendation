package study.PharmacyProject.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.PharmacyProject.pharmacy.cache.PharmacyRedisTemplateService;
import study.PharmacyProject.pharmacy.dto.PharmacyDto;
import study.PharmacyProject.pharmacy.entity.Pharmacy;
import study.PharmacyProject.pharmacy.service.PharmacyRepositoryService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PharmacyController {

    //db데이터를 redis로 동기화하는 메소드 만들기
    private final PharmacyRepositoryService pharmacyRepositoryService;

    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    // 데이터 초기 셋팅을 위한 임시 메소드 해당 요청이 오면 모든약국 정보 레디스에 추가.
    @GetMapping("/csv/save")
    public String saveCsv() {
        //saveCsvToDatabase();
        saveCsvToRedis();

        return "success save";
    }

    public void saveCsvToRedis() {

        List<PharmacyDto> pharmacyDtoList =
                pharmacyRepositoryService.findAll()
                        .stream()
                        .map(pharmacy -> PharmacyDto.builder()
                                .id(pharmacy.getId())
                                .pharmacyName(pharmacy.getPharmacyName())
                                .pharmacyAddress(pharmacy.getPharmacyAddress())
                                .latitude(pharmacy.getLatitude())
                                .longitude(pharmacy.getLongitude())
                                .build())
                        .collect(Collectors.toList());

        pharmacyDtoList.forEach(pharmacyRedisTemplateService::save);
    }

}