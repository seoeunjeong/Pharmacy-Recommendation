package study.PharmacyProject.pharmacy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.PharmacyProject.pharmacy.cache.PharmacyRedisTemplateService;
import study.PharmacyProject.pharmacy.dto.PharmacyDto;
import study.PharmacyProject.pharmacy.entity.Pharmacy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService {

    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;
    public List<PharmacyDto> searchPharmacyDtoList(){

        //redis에서 약국데이터를 조회하고 문제발생시 데이터베이스에서 조회
        List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();
        if(!pharmacyDtoList.isEmpty()) {
            log.info("레디스 성공!");
            return pharmacyDtoList;
        }
        //db
        return pharmacyRepositoryService.findAll()
                .stream()
                .map(this::convertToPharmacyDto)
                .collect(Collectors.toList());

    }

    private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy){
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyName(pharmacy.getPharmacyName())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }

}
