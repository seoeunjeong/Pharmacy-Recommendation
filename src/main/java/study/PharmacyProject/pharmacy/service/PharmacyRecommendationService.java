package study.PharmacyProject.pharmacy.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import study.PharmacyProject.api.dto.DocumentDto;
import study.PharmacyProject.api.dto.KakaoApiResponseDto;
import study.PharmacyProject.api.service.KakaoAddressSearchService;
import study.PharmacyProject.direction.dto.OutputDto;
import study.PharmacyProject.direction.entity.Direction;
import study.PharmacyProject.direction.service.Base62Service;
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

    private final Base62Service base62Service;

    @Value("${pharmacy.recommendation.base.url}")
    private String baseUrl;
    private static final String ROAD_VIEW_BASE_URL ="https://map.kakao.com/link/roadview/";
//    private static final String DIRECTION_BASE_URL ="https://map.kakao.com/link/map/";


    public List<OutputDto> recommendPharmacyList(String address){

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if(Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("PharmacyRecommendationService.recommendPharmacyList fail Input address={}",address);
            return Collections.emptyList();
        }

        //길안내
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

/*        ToDo !!!카카오 카테고리 api 를 사용하여 더 많은 약국추천 가능 */
//        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);

        List<Direction> directionList = directionService.buildDirectionList(documentDto);
        //저장데이터를 통해 위치 안내를 해야하는이유? 왜저장을 한뒤에 안내해야하지?
        //direction 의 Id값을 이용하여 shortenUrl을 만들어서 제공하지않는다면 저장할 필요없지않을까?

        List<Direction> directions = directionService.saveAll(directionList);
        return directions
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());

    }


    private OutputDto convertToOutputDto(Direction direction){
        return OutputDto.builder()
                .pharmacyAddress(direction.getTargetAddress())
                .pharmacyName(direction.getTargetPharmacyName())
                .directionUrl(baseUrl+base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL+direction.getTargetLatitude()+","+direction.getTargetLongitude())
                .distance(String.format("%.2f km",direction.getDistance()))
                .build();
    }

}
