package konkuk.kuit.baro.domain.place.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ParameterObject
public class PlaceSearchRequestDTO {

    @NotEmpty(message = "placeCategoryIds 값이 존재하지 않습니다.")
    @Parameter(example = "1,2,3,4", description = "필터링에 사용할 장소 카테고리들의 ID")
    private List<Long> placeCategoryIds;

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
