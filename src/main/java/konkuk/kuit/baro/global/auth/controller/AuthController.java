package konkuk.kuit.baro.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.global.auth.dto.request.LoginRequestDTO;
import konkuk.kuit.baro.global.auth.dto.request.SignUpRequestDTO;
import konkuk.kuit.baro.global.auth.dto.response.ReissueResponseDTO;
import konkuk.kuit.baro.global.auth.dto.response.SignUpResponseDTO;
import konkuk.kuit.baro.global.auth.jwt.service.JwtService;
import konkuk.kuit.baro.global.auth.service.AuthService;
import konkuk.kuit.baro.global.auth.dto.response.LoginResponseDTO;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Auth", description = "Auth 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @Operation(
            summary = "회원가입",
            description = "회원가입을 합니다. 토큰이 필요하지 않습니다."
    )
    // @CustomExceptionDescription(SIGNUP)
    @PostMapping("/signup")
    public BaseResponse<SignUpResponseDTO> signup(@RequestBody SignUpRequestDTO request) {
        return BaseResponse.ok(authService.signup(request));
    }

    @Operation(
            summary = "로그인",
            description = "로그인합니다. 토큰이 필요하지 않습니다."
    )
    // @CustomExceptionDescription(LOGIN)
    @PostMapping("/login")
    public BaseResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return BaseResponse.ok(authService.login(request));
    }

    @Operation(
            summary = "토큰 재발급",
            description = "리프레시 토큰을 가지고 토큰을 재발급합니다. 리프레시 토큰이 필요합니다."
    )
    // @CustomExceptionDescription(REISSUE)
    @PostMapping("/reissue")
    public BaseResponse<ReissueResponseDTO> reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        return BaseResponse.ok(authService.reissueTokens(request, response));
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃합니다. 액세스 토큰과 리프레시 토큰이 필요합니다."
    )
    // @CustomExceptionDescription(LOGOUT)
    @PatchMapping("/logout")
    public BaseResponse<Void> logout(HttpServletRequest request) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        authService.logout(accessToken, refreshToken);
        return BaseResponse.ok(null);
    }
}