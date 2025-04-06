package konkuk.kuit.baro.domain.pin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("PlaceName")
    @NotNull(message = "핀 등록 장소 이름은 비어있을 수 없습니다.")
    private String placeName;

    @JsonProperty("PlaceAddress")
    @NotNull(message = "핀 등록 장소 주소는 비어있을 수 없습니다.")
    private String placeAddress;

    @JsonProperty("latitude")
    @NotNull(message = "latitude 값이 존재하지 않습니다.")
    @Min(value = -90, message = "latitude는 -90 이상이어야합니다.")
    @Max(value = 90, message = "latitude는 90 이하여야합니다.")
    private Double latitude;

    @JsonProperty("longitude")
    @NotNull(message = "longitude 값이 존재하지 않습니다.")
    @Min(value = -180, message = "longitude는 -180 이상이어야합니다.")
    @Max(value = 180, message = "longitude는 180 이하여야합니다.")
    private Double longitude;

    @JsonProperty("review")
    @NotBlank(message = "리뷰 내용은 비어있을 수 없습니다.")
    private String review;

    @JsonProperty("score")
    @NotNull(message = "평점은 비어있을 수 없습니다.")
    private Short score;
}

