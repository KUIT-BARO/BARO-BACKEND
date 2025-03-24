package konkuk.kuit.baro.domain.place.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlacesResponseDTO {

    @Schema(example = "1", description = "장소 ID")
    private Long placeId;

    @Schema(example = "37.5423265", description = "위도")
    private Double latitude;

    @Schema(example = "127.0759204", description = "경도")
    private Double longitude;
}
