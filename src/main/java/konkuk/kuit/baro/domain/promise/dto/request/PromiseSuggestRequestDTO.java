package konkuk.kuit.baro.domain.promise.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PromiseSuggestRequestDTO {

    private String promiseName;

    private LocalDate suggestedStartDate;

    private LocalDate suggestedEndDate;

    private String suggestedRegion;
}
