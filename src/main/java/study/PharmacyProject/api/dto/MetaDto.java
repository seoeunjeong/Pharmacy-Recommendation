package study.PharmacyProject.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MetaDto {
//    json으로 요청을 받을때 필드명과 바로 매핑이 되는데 응답은 매핑설정을 해줘야하나요?
//    json으로 응답을 받을때 json과 해당 필드를 매핑
    @JsonProperty("total_count")
    private Integer totalCount;

}
