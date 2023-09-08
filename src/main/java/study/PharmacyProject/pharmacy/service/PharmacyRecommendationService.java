package study.PharmacyProject.pharmacy.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponentsBuilder;
import study.PharmacyProject.api.dto.DocumentDto;
import study.PharmacyProject.api.dto.KakaoApiResponseDto;
import study.PharmacyProject.api.service.KakaoAddressSearchService;
import study.PharmacyProject.direction.Repository.DirectionRepository;
import study.PharmacyProject.direction.dto.OutputDto;
import study.PharmacyProject.direction.entity.Direction;
import study.PharmacyProject.direction.service.DirectionService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private static final String ROAD_VIEW_BASE_URL ="https://map.kakao.com/link/roadview/";
    private static final String DIRECTION_BASE_URL ="https://map.kakao.com/link/map/";


    public List<OutputDto> recommendPharmacyList(String address){

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if(Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("PharmacyRecommendationService.recommendPharmacyList fail Input address={}",address);
            return Collections.emptyList();
        }

        //길안내

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        //공공기관 데이터이용
        return directionService.buildDirectionList(documentDto)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());

        //카카오 api장소검색이용(활용)
//       List<Direction> directions1 = directionService.buildDirectionListByCategoryApi(documentDto);

        //저장데이터를 통해 위치 안내를 해야하는이유? 왜저장을 한뒤에 안내해야하지?

//        return directionService.saveAll(directions)
//                .stream()
//                .map(this::convertToOutputDto)
//                .collect(Collectors.toList());

    }



    private OutputDto convertToOutputDto(Direction direction){
        String params = String.join(",", direction.getTargetPharmacyName(),
                String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getInputLongitude()));

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();//인코딩지원해준당

        log.info("directipn params:{},url:{}",params,result);

        return OutputDto.builder()
                .pharmacyAddress(direction.getInputAddress())
                .pharmacyName(direction.getTargetPharmacyName())
                .directionUrl(result)
                .roadViewUrl(ROAD_VIEW_BASE_URL+direction.getTargetLatitude()+","+direction.getTargetLongitude())
                .distance(String.format("%.2f km",direction.getDistance()))
                .build();
    }

}
