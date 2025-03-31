package konkuk.kuit.baro.domain.promise.dto.response;

import konkuk.kuit.baro.domain.place.dto.response.PlaceSearchResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PromisePlaceResponseDTO {
    List<PlaceSearchResponseDTO> places;
    List<PromiseMemberDTO> members;
}
