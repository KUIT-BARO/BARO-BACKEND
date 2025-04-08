package konkuk.kuit.baro.domain.promise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.kuit.baro.domain.promise.dto.request.TimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromiseMemberAvailableTimeDTO {
    @Schema(description = "약속 참여자 ID", example = "1")
    private Long promiseMemberId;

    List<TimeDTO> availableTimes;
}
