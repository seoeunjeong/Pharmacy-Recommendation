package study.PharmacyProject.direction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;
import study.PharmacyProject.direction.entity.Direction;
import study.PharmacyProject.direction.service.DirectionService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DirectionController {

    private final DirectionService directionService;
    private static final String DIRECTION_BASE_URL ="https://map.kakao.com/link/map/";


    @GetMapping("/dir/{encodedId}")
    public String searchDirection(@PathVariable("encodedId") String encodedId){
        Direction resultDirection = directionService.findbyId(encodedId);

        String params = String.join(",", resultDirection.getTargetPharmacyName(),
                String.valueOf(resultDirection.getTargetLatitude()), String.valueOf(resultDirection.getInputLongitude()));

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();//인코딩지원해준당

        log.info("directipn params:{},url:{}",params,result);

        return"redirect:"+result;

    }

}
