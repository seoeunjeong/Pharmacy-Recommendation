package study.PharmacyProject.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class KakaoUriBuilderService {
    //카카오톡 api 사용을 위한 uri만들기
    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String KAKAO_LOCAL_CATEGORY_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/category";


    public URI buildUriByAddressSearch(String address){

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL);
        uriBuilder.queryParam("query",address);

        URI uri = uriBuilder.build().encode().toUri();
        log.info("[KakaoUriBuilderService.buildUriByAddressSearch] address:{} uri:{}",address,uri);
        return uri;

    }

    public URI buildUriByCategorySearch(double latitude, double longitude, double radius, String category) {
        //km 사용했었는데 카카오에서는 meter 단위
        double meterRadius = radius * 1000;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_CATEGORY_SEARCH_URL);
        uriBuilder.queryParam("category_group_code",category);
        uriBuilder.queryParam("x",longitude);
        uriBuilder.queryParam("y",latitude);
        uriBuilder.queryParam("radius",meterRadius);
        uriBuilder.queryParam("sort","distance");

        URI uri = uriBuilder.build().encode().toUri();

        log.info("[KakaoAddressSearchService buildUriByCategorySearch] uri: {} ", uri);

        return uri;
    }

}
