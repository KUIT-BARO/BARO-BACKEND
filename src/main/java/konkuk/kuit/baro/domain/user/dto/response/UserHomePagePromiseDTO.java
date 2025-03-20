package konkuk.kuit.baro.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserHomePagePromiseDTO {
    @Schema(description = "약속 ID", example = "1")
    private Long promiseId;

    @Schema(description = "약속 장소명", example = "스타벅스 건대입구점")
    private String placeName;

    @Schema(description = "약속명", example = "KUIT 2차 회의")
    private String promiseName;

    @Schema(description = "약속 날짜", example = "2/24")
    private LocalDate promiseDate;

    @Schema(description = "약속 날짜 요일", example = "토")
    private String promiseDay;

    @Schema(description = "약속 참여자", example = "이지환 외 2명")
    private String promiseMember;

    @Schema(description = "약속까지 남은 시간", example = "2")
    private int promiseDday;
}
