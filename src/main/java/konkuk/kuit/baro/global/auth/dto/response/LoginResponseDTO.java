package konkuk.kuit.baro.global.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginResponseDTO {

    @Schema(description = "엑세스토큰")
    private String accessToken;
}
