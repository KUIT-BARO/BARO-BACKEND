package konkuk.kuit.baro.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.baro.domain.user.dto.request.UserUpdatePasswordRequestDTO;
import konkuk.kuit.baro.domain.user.dto.request.UserUpdateProfileRequestDTO;
import konkuk.kuit.baro.domain.user.dto.response.UserHomePageResponseDTO;
import konkuk.kuit.baro.domain.user.dto.response.UserProfileResponseDTO;
import konkuk.kuit.baro.domain.user.dto.response.UserProfileSettingResponseDTO;
import konkuk.kuit.baro.domain.user.service.UserService;
import konkuk.kuit.baro.global.auth.dto.request.SignUpRequestDTO;
import konkuk.kuit.baro.global.auth.resolver.CurrentUserId;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription.*;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "회원가입",
            description = "회원가입을 합니다. 토큰이 필요하지 않습니다."
    )
    @CustomExceptionDescription(USER_SIGNUP)
    @PostMapping("/signup")
    public BaseResponse<Void> signup(@RequestBody SignUpRequestDTO request) {
        userService.signup(request);
        return BaseResponse.ok(null);
    }

    @Tag(name = "My Page", description = "유저 마이페이지 관련 API")
    @Operation(summary = "유저 프로필 수정", description = "유저의 이름, 프로필 사진을 변경합니다.")
    @PostMapping("profile")
    @CustomExceptionDescription(USER_PROFILE_UPDATE)
    public BaseResponse<Void> updateProfile(@RequestBody @Validated UserUpdateProfileRequestDTO req){
        userService.updateProfile(req);
        return BaseResponse.ok(null);

    }

    @Tag(name = "My Page", description = "유저 마이페이지 관련 API")
    @Operation(summary = "유저 프로필 수정 화면", description = "프로필 수정 기능 호출 시 프로필을 조회하는 기능입니다.")
    @GetMapping("profile")
    @CustomExceptionDescription(USER_PROFILE)
    public BaseResponse<UserProfileResponseDTO> getProfile(@CurrentUserId Long userId){
        return BaseResponse.ok(userService.getProfile(userId));
    }

    @Tag(name = "My Page", description = "유저 마이페이지 관련 API")
    @Operation(summary = "마이페이지 설정 화면", description = "마이페이지에서 설정 기능 호출 시 프로필을 조회하는 기능입니다.")
    @GetMapping("profile-setting")
    @CustomExceptionDescription(USER_PROFILE)
    public BaseResponse<UserProfileSettingResponseDTO> getProfileSetting(Long userId){
        return BaseResponse.ok(userService.getProfileSetting(userId));
    }

    @Tag(name = "My Page", description = "유저 마이페이지 관련 API")
    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.")
    @PostMapping("password")
    @CustomExceptionDescription(USER_PASSWORD_UPDATE)
    public BaseResponse<Void> updatePassword(@RequestBody @Validated UserUpdatePasswordRequestDTO req){
        userService.updatePassword(req);
        return BaseResponse.ok(null);
    }

    @Tag(name = "My Page", description = "유저 마이페이지 관련 API")
    @Operation(summary = "회원 탈퇴", description = "사용자가 회원 탈퇴를 합니다.")
    @DeleteMapping()
    @CustomExceptionDescription(USER_DELETE)
    public BaseResponse<Void> delete(Long userId){
        userService.deleteUser(userId);
        return BaseResponse.ok(null);
    }

    @Tag(name = "Home Page", description = "홈 화면 API")
    @Operation(summary = "홈 화면 조회", description = "메인 홈 화면을 조회합니다.")
    @GetMapping()
    @CustomExceptionDescription(USER_HOME)
    public BaseResponse<UserHomePageResponseDTO> getHomePage(){
        return BaseResponse.ok(userService.getHomePage(1L));
    }
}
