package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PromiseStatusConfirmedPromiseResponseDTO {

    @Schema(description = "약속 이름", example = "KUIT BARO 2차 회의")
    private String promiseName;

    @Schema(description = "약속 확정 장소 이름", example = "건대입구역")
    private String confirmedPlace;

    @Schema(description = "약속 확정 날짜", example = "3/14(금) 3시")
    private String confirmedDate;

    @Schema(description = "위도", example = "37.566545")
    private Double latitude;

    @Schema(description = "경도", example = "126.978078")
    private Double longitude;
}
