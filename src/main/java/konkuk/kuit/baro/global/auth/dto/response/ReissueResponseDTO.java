package konkuk.kuit.baro.global.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReissueResponseDTO {

    @Schema(description = "엑세스토큰")
    @NotNull
    private String accessToken;

    @Schema(description = "리프레쉬토큰")
    @NotNull
    private String refreshToken;
}
