package study.PharmacyProject.direction.service

import spock.lang.Specification
import study.PharmacyProject.api.dto.DocumentDto
import study.PharmacyProject.api.service.KakaoCategorySearchService
import study.PharmacyProject.direction.Repository.DirectionRepository
import study.PharmacyProject.pharmacy.dto.PharmacyDto
import study.PharmacyProject.pharmacy.service.PharmacySearchService


class DirectionServiceTest extends Specification {
    //단위테스트

    private PharmacySearchService pharmacySearchService = Mock()
    private final KakaoCategorySearchService kakaoCategorySearchService= Mock()
    private final DirectionRepository directionRepository =Mock()

    private DirectionService directionService = new DirectionService(pharmacySearchService,kakaoCategorySearchService,directionRepository)

    private List<PharmacyDto> pharmacyList

    def setup(){

        //pharmacySearchService에서 반환하는 약국 DTO list
        pharmacyList = new ArrayList<>()
        pharmacyList.addAll(
                PharmacyDto.builder()
                        .id(1L)
                        .pharmacyName("돌곶이온누리약국")
                        .pharmacyAddress("주소1")
                        .latitude(37.61040424)
                        .longitude(127.0569046)
                        .build(),
                PharmacyDto.builder()
                        .id(2L)
                        .pharmacyName("호수온누리약국")
                        .pharmacyAddress("주소2")
                        .latitude(37.60894036)
                        .longitude(127.029052)
                        .build()
        )
    }

    def "buildDirectionList-결과 값이 거리 순으로 정렬이 되는지 확인"(){
        given:
        //고객 주소 documentDto로 만들어서 전달
        def addressName = "서울 성북구 종암로10길"
        double inputLatitude = 37.5960650456809
        double inputLongitude = 127.037033003036

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()


        when:
        pharmacySearchService.searchPharmacyDtoList() >> pharmacyList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size()==2
        results.get(0).targetPharmacyName =="호수온누리약국"
        results.get(1).targetPharmacyName =="돌곶이온누리약국"


    }

    def "buildDirectionList-정해진 반경 10km 내에 검색이 되는지 확인"(){
        given:
        pharmacyList.add(
                PharmacyDto.builder()
                        .id(3L)
                        .pharmacyName("경기약국")
                        .pharmacyAddress("주소3")
                        .latitude(37.3825107393401)
                        .longitude(127.236707811313)
                        .build())

        def addressName="서울 성북구 종암로 10길"
        double inputLatitude = 37.5960650456809
        double inputLongitude = 127.037033003036

        def documentDto =DocumentDto.builder()
        .addressName(addressName)
        .latitude(inputLatitude)
        .longitude(inputLongitude)
        .build()

        when:
        pharmacySearchService.searchPharmacyDtoList()>>pharmacyList

        def result = directionService.buildDirectionList(documentDto)

        then:
        result.size()==2
        result.get(0).targetPharmacyName=="호수온누리약국"
        result.get(1).targetPharmacyName=="돌곶이온누리약국"

    }

}
