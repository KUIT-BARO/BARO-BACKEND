package konkuk.kuit.baro.domain.pin.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ParameterObject
public class PinRequestDTO {

    @Schema(description = "핀 등록 장소 이름", example = "스타벅스 강남점")
    @NotNull(message = "핀 등록 장소 이름은 비어있을 수 없습니다.")
    private String PlaceName;

    @Schema(description = "핀 등록 장소 주소", example = "서울특별시 강남구 강남대로 390")
    @NotNull(message = "핀 등록 장소 주소는 비어있을 수 없습니다.")
    private String PlaceAddress;

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

    @Schema(description = "리뷰 내용", example = "아주 좋은 장소였습니다.")
    @NotBlank(message = "리뷰 내용은 비어있을 수 없습니다.")
    private String review;

    @Schema(description = "평점", example = "4.5")
    @NotNull(message = "평점은 비어있을 수 없습니다.")
    private Short score;

}
