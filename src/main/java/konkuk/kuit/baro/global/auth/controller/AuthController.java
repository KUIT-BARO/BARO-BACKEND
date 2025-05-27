package konkuk.kuit.baro.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.domain.user.service.UserService;
import konkuk.kuit.baro.global.auth.dto.request.LoginRequestDTO;
import konkuk.kuit.baro.global.auth.jwt.service.JwtService;
import konkuk.kuit.baro.global.auth.resolver.CurrentUserId;
import konkuk.kuit.baro.global.auth.service.AuthService;
import konkuk.kuit.baro.global.auth.dto.response.LoginResponseDTO;
import konkuk.kuit.baro.global.auth.service.MailService;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import konkuk.kuit.baro.global.auth.dto.request.CodeCheckRequestDTO;
import konkuk.kuit.baro.global.auth.dto.request.MailRequestDTO;

import java.util.Optional;

import static konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription.*;

@Tag(name = "Auth API", description = "Auth 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;

    @Operation(
            summary = "로그인",
            description = "로그인합니다. 토큰이 필요하지 않습니다."
    )
    @CustomExceptionDescription(LOGIN)
    @PostMapping("/login")
    public BaseResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return BaseResponse.ok(authService.login(request));
    }

    @Operation(
            summary = "토큰 재발급",
            description = "리프레시 토큰을 가지고 토큰을 재발급합니다. 리프레시 토큰이 필요합니다."
    )
    @CustomExceptionDescription(REISSUE)
    @PostMapping("/reissue")
    public BaseResponse<Void> reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        authService.reissueTokens(request, response);
        return BaseResponse.ok(null);
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃합니다. 액세스 토큰과 리프레시 토큰이 필요합니다."
    )
    @CustomExceptionDescription(LOGOUT)
    @PatchMapping("/logout")
    public BaseResponse<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return BaseResponse.ok(null);
    }

    @Operation(
            summary = "인증번호 전송",
            description = "메일로 인증번호를 전송합니다. 액세스 토큰이 필요합니다."
    )
    @PostMapping("/mail/send")
    public BaseResponse<Void> sendAuthCodeMail(@RequestBody MailRequestDTO mailRequestDTO) {
        mailService.sendMail(mailRequestDTO);
        return BaseResponse.ok(null);
    }

    @Operation(
            summary = "인증번호 검증",
            description = "메일로 받은 인증번호를 검증합니다. 액세스 토큰이 필요합니다."
    )
    @GetMapping("/mail/check")
    public BaseResponse<Boolean> checkAuthCode(@RequestBody CodeCheckRequestDTO checkCodeRequestDTO) {
        mailService.checkAuthCode(checkCodeRequestDTO);
        return BaseResponse.ok(null);
    }
}