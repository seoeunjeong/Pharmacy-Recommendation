package study.PharmacyProject.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDto {
    //jsonProperty를 꼭 써야하나? 스프링이 json ->객체로 바꿔주는데?이름이다를경우!
    //카카오 카테고리검색시 약국이름 받을수있는필드 추가
    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("y")
    private double latitude;

    @JsonProperty("x")
    private double longitude;

    //카카오 카테고리검색시 요청주소와의 거리도 반환받을수있다.
    @JsonProperty("distance")
    private double distance;

}
