package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteCandidateListResponseDTO {

    @Schema(description = "투표 목록 - 약속 후보 시간 리스트")
    private List<CandidateTimesDTO> candidateTimes;

    @Schema(description = "투표 모록 - 약속 후보 장소 리스트")
    private List<CandidatePlacesDTO> candidatePlaces;
}
