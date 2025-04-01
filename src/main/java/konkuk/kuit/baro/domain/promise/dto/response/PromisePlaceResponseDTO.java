package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.kuit.baro.domain.place.dto.response.PlaceSearchResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PromisePlaceResponseDTO {
    @Schema(description = "호스트가 제안한 지역", example = "건대입구")
    private String suggestedRegion;
    List<PlaceSearchResponseDTO> places;
    List<PromiseMemberDTO> members;
}
