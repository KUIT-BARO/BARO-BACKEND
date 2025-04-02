package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PromiseVoteRemainingTimeResponseDTO {

    @Schema(description = "투표 잔여 시간", example = "D-3 OR 1시간 1분 10초")
    private String promiseVoteRemainingTime;
}
