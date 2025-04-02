package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PromiseMemberVoteStateDTO {

    @Schema(description = "투표에 참여했는지 여부", example = "true")
    private Boolean hasVoted;

    @Schema(description = "약속 제안자 여부", example = "true")
    private Boolean isHost;

    @Schema(description = "프로필 이미지", example = "DOG")
    private String profileImage;
}
