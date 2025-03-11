package konkuk.kuit.baro.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetSchedulesResponseDTO {
    @Schema(description = "유저 프로필 사진", example = "image")
    private String profileImage;

    @Schema(description = "이름", example = "이정연")
    private String userName;

    @Schema(description = "이메일", example = "dlwjddus1112@naver.com")
    private String email;

    List<SchedulesDTO> schedules;

}
