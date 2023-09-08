package study.PharmacyProject.pharmacy.service

import org.testcontainers.shaded.com.google.common.collect.Lists
import spock.lang.Specification
import study.PharmacyProject.pharmacy.cache.PharmacyRedisTemplateService
import study.PharmacyProject.pharmacy.entity.Pharmacy

class PharmacySearchServiceTest extends Specification {

    private PharmacySearchService pharmacySearchService;

    //단위테스트 진행 의존성이 필요한것들은 모킹하기
    private PharmacyRepositoryService pharmacyRepositoryService = Mock()
    private PharmacyRedisTemplateService pharmacyRedisTemplateService = Mock()

    private List<Pharmacy> pharmacyList;

    def setup(){
        pharmacySearchService= new PharmacySearchService(pharmacyRepositoryService,pharmacyRedisTemplateService);

        pharmacyList = Lists.newArrayList(
                Pharmacy.builder()
                        .id(1L)
                        .pharmacyName("호수온누리약국")
                        .latitude(37.60894036)
                        .longitude(127.029052)
                        .build(),
                Pharmacy.builder()
                        .id(2L)
                        .pharmacyName("돌곶이온누리약국")
                        .latitude(37.61040424)
                        .longitude(127.0569046)
                        .build())
    }

    def "레디스 장에시 DB를 이용하여 약국 데이터 조회"(){
        when:
        pharmacyRedisTemplateService.findAll()>> []
        pharmacyRepositoryService.findAll()>>  pharmacyList;

        def result = pharmacySearchService.searchPharmacyDtoList()

        then:
        result.size()== 2


    }
}
