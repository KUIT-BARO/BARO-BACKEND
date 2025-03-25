package konkuk.kuit.baro.domain.place.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlaceSearchRequestDTO {

    @NotEmpty(message = "placeCategoryIds 값이 존재하지 않습니다.")
    private List<Long> placeCategoryIds;

    @NotNull(message = "latitude 값이 존재하지 않습니다.")
    @Min(value = -90, message = "latitude는 -90 이상이어야합니다.")
    @Max(value = 90, message = "latitude는 90 이하여야합니다.")
    private Double latitude;

    @NotNull(message = "longitude 값이 존재하지 않습니다.")
    @Min(value = -180, message = "longitude는 -180 이상이어야합니다.")
    @Max(value = 180, message = "longitude는 180 이하여야합니다.")
    private Double longitude;

}
