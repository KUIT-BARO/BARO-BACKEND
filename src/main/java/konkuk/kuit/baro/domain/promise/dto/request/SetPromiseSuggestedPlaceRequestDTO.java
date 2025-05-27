package konkuk.kuit.baro.domain.promise.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SetPromiseSuggestedPlaceRequestDTO {
    @Schema(description = "약속 ID", example = "1")
    @NotNull(message = "promiseId 값이 입력되지 않았습니다.")
    private Long promiseId;

    @Schema(description = "장소 ID", example = "1")
    @NotNull(message = "placeId 값이 입력되지 않았습니다.")
    private Long placeId;

    @Schema(description = "위도", example = "37.1233712")
    @Min(value = -90, message = "latitude는 -90 이상이어야합니다.")
    @Max(value = 90, message = "latitude는 90 이하여야합니다.")
    private Double latitude;

    @Schema(example = "121.12371231", description = "경도")
    @Min(value = -180, message = "longitude는 -180 이상이어야합니다.")
    @Max(value = 180, message = "longitude는 180 이하여야합니다.")
    private Double longitude;

    @Schema(description = "장소명", example = "홍콩포차")
    private String placeName;

    @Schema(description = "주소", example = "서울특별시 광진구 화양동")
    private String address;
}
