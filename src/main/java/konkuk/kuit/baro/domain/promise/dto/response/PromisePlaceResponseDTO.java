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

    @Schema(description = "중심좌표 기준 2km 내에 등록된 장소")
    List<PlaceSearchResponseDTO> places;

    @Schema(description = "약속 참여자의 프로필")
    List<PromiseMemberDTO> members;
}
