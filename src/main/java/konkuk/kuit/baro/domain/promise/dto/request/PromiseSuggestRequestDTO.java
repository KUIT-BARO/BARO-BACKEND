package konkuk.kuit.baro.domain.promise.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PromiseSuggestRequestDTO {

    @NotBlank(message = "약속 이름은 비어있을 수 없습니다.")
    private String promiseName;

    @NotNull(message = "약속 제안 시작일은 비어있을 수 없습니다.")
    private LocalDate suggestedStartDate;

    @NotNull(message = "약속 제안 종료일은 비어있을 수 없습니다.")
    private LocalDate suggestedEndDate;

    @NotBlank(message = "제안된 지역은 비어있을 수 없습니다.")
    private String suggestedRegion;
}
