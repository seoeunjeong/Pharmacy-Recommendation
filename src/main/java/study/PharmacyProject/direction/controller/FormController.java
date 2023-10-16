package study.PharmacyProject.direction.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import study.PharmacyProject.direction.dto.InputDto;
import study.PharmacyProject.pharmacy.service.PharmacyRecommendationService;


@Controller
@RequiredArgsConstructor
public class FormController {

    private final PharmacyRecommendationService pharmacyRecommendationService;

    @GetMapping("/")
    public String main(){
        return "main";
    }

    /*modelAndview를 생성해서 리턴하는 강의내용과 달리
     model을 주입받아서 데이터를 전달해도된다.*/
    @PostMapping("/search")
    public String post(@ModelAttribute InputDto inputDto,Model model){

        model.addAttribute("outputFormList", pharmacyRecommendationService.recommendPharmacyList(inputDto.getAddress()));

        return "output";
    }


}
