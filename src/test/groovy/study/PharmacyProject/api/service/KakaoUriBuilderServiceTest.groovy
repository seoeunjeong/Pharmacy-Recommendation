package study.PharmacyProject.api.service

import spock.lang.Specification

import java.nio.charset.StandardCharsets

class KakaoUriBuilderServiceTest extends Specification {

    private KakaoUriBuilderService kakaoUriBuilderService;

    //단위테스트 자바클래스 직접 생성해서 사용

    def setup(){
        kakaoUriBuilderService=new KakaoUriBuilderService();
    }


    def "buildUriByAddressSearch-한글 파라미터의 경우 정상적으로 인코딩"(){
        given:
        String address="서울 성북구"

        def charset = StandardCharsets.UTF_8
        when:
        def uri = kakaoUriBuilderService.buildUriByAddressSearch(address)
        def decodeResult = URLDecoder.decode(uri.toString(), charset)

        then:
        decoderesult == "https://dapi.kakao.com/v2/local/search/address.json?query=서울 성북구"
    }

}

