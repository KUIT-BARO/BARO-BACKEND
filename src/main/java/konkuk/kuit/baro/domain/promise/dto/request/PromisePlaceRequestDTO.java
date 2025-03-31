package konkuk.kuit.baro.domain.promise.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromisePlaceRequestDTO {
    @Schema(description = "약속 ID", example = "1")
    @NotNull
    private Long promiseId;

    @NotNull(message = "latitude 값이 존재하지 않습니다.")
    @Min(value = -90, message = "latitude는 -90 이상이어야합니다.")
    @Max(value = 90, message = "latitude는 90 이하여야합니다.")
    @Parameter(example = "37.1233712", description = "위도")
    private Double latitude;

    @NotNull(message = "longitude 값이 존재하지 않습니다.")
    @Min(value = -180, message = "longitude는 -180 이상이어야합니다.")
    @Max(value = 180, message = "longitude는 180 이하여야합니다.")
    @Parameter(example = "121.12371231", description = "경도")
    private Double longitude;
}
