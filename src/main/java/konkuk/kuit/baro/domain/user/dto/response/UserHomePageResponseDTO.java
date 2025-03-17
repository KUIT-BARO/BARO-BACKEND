package konkuk.kuit.baro.domain.user.dto.response;

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
public class UserHomePageResponseDTO {
    @Schema(description = "이름", example = "이정연")
    private String userName;

    @Schema(description = "약속까지 남은 시간(가장 빠른 약속)", example = "2")
    private Integer fastestDday;

    List<UserHomePagePromiseDTO> promiseDTOs;

    public UserHomePageResponseDTO(String userName) {
        this.userName = userName;
        this.fastestDday = null;
        this.promiseDTOs = null;
    }
}
