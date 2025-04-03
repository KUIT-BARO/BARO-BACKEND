package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidatePlacesDTO {

    @Schema(description = "약속 후보 장소 ID", example = "1")
    private Long promiseCandidatePlaceId;

    @Schema(description = "약속 후보 장소 이름", example = "탐앤탐스 건대입구점")
    private String placeName;

    @Schema(description = "기존에 해당 약속 후보 장소에 대해 투표했는지 여부", example = "true")
    private Boolean isSelected;
}
