package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PromiseAvailableTimeResponseDTO {
    @Schema(description = "호스트가 제안한 약속 시작 날짜", example = "2025/03/01")
    private LocalDate suggestedStartDate;

    @Schema(description = "호스트가 제안한 약속 종료 날짜", example = "2025/04/01")
    private LocalDate suggestedEndDate;

    @Schema(description = "약속 참여자들 프로필")
    List<PromiseMemberDTO> promiseMembers;

    @Schema(description = "약속 참여자들이 선택한 시간대")
    List<PromiseMemberAvailableTimeDTO> promiseAvailableTimes;

}
