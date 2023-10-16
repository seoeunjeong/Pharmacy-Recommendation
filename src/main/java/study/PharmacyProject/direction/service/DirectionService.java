package study.PharmacyProject.direction.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;
import study.PharmacyProject.api.dto.DocumentDto;
import study.PharmacyProject.api.service.KakaoCategorySearchService;
import study.PharmacyProject.direction.Repository.DirectionRepository;
import study.PharmacyProject.direction.entity.Direction;
import study.PharmacyProject.pharmacy.service.PharmacySearchService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
/*길안내 거리제공 서비스 Direction 을 저장해놓아야하는 이유 ?*/
public class DirectionService {

    private final PharmacySearchService pharmacySearchService;

    //최대 검색 갯수
    private static final int MAX_SEARCH_COUNT=3;
    //반경 10 km
    private static final double RADIUS_KM=10.0;
    private static final String DIRECTION_BASE_URL ="https://map.kakao.com/link/map/";

    private final KakaoCategorySearchService kakaoCategorySearchService;

    private final DirectionRepository directionRepository;

    private final Base62Service base62Service;



    //가까운 약국 3개 Direction 반환
    public List<Direction> buildDirectionList(DocumentDto documentDto) {

        if (Objects.isNull(documentDto)) return Collections.emptyList();

        // DB에 저장된 공공기관약국데이터를 조회 후
        // 거리 계산 알고리즘을 이용하여 ,고객과 약국 사잉의 거리(distance)계산하여 sort , direction 저장
        return pharmacySearchService.searchPharmacyDtoList()
                .stream()
                .map(pharmacyDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLatitude(documentDto.getLatitude())
                                .inputLongitude(documentDto.getLongitude())
                                .targetAddress(pharmacyDto.getPharmacyAddress())
                                .targetPharmacyName(pharmacyDto.getPharmacyName())
                                .targetLatitude(pharmacyDto.getLatitude())
                                .targetLongitude(pharmacyDto.getLongitude())
                                .distance(
                                        calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(),
                                                pharmacyDto.getLatitude(), pharmacyDto.getLongitude())
                                )
                                .build())
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());

    }

    // kakao api를 이용한 약국데이터를 조회후
    // kakao가 제공하는 distance를 통해 가까운 약국정보 direction 저장
    public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto) {

        if(Objects.isNull(inputDocumentDto)) return Collections.emptyList();

        return kakaoCategorySearchService
                .requestPharmacyCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
                .getDocumentList()
                .stream()
                .map(resultDocumentDto ->
                        Direction.builder()
                                .inputAddress(inputDocumentDto.getAddressName())
                                .inputLatitude(inputDocumentDto.getLatitude())
                                .inputLongitude(inputDocumentDto.getLongitude())
                                .targetPharmacyName(resultDocumentDto.getPlaceName())
                                .targetAddress(resultDocumentDto.getAddressName())
                                .targetLatitude(resultDocumentDto.getLatitude())
                                .targetLongitude(resultDocumentDto.getLongitude())
                                .distance(resultDocumentDto.getDistance() * 0.001) // m 단위 -> km 단위
                                .build())
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }

//    추천결과를 저장하는 기능 꼭 필요할까?
    @Transactional
    public List<Direction> saveAll(List<Direction> directionList){

        if(CollectionUtils.isEmpty(directionList))
            return Collections.emptyList();

        return directionRepository.saveAll(directionList);
    }

    public String findDirectionUrlById(String encodedId){
        Long decodedId = base62Service.decodeDirectionId(encodedId);
        Direction direction= directionRepository.findById(decodedId).orElse(null);
        String params = String.join(",", direction.getTargetPharmacyName(),
                String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getInputLongitude()));

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();//인코딩지원해준당
        return result;
    }

    //Haversine formula 고객의 위도 경도와 약국의 위도 경도사이의 거리를 구하는 메소드
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371;

        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }
}
