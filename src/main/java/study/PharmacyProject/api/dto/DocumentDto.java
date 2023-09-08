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
    //jsonProperty를 꼭 써야하나? 스프링이 json ->객체로 바꿔주는데?(그건 컨트롤러에서 들어올때야)
    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("y")
    private double latitude;
    @JsonProperty("x")
    private double longitude;

    @JsonProperty("distance")
    private double distance;

}
