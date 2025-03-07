package konkuk.kuit.baro.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.kuit.baro.domain.user.dto.request.UserUpdateProfileRequestDTO;
import konkuk.kuit.baro.domain.user.service.UserService;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription.*;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Tag(name = "My Page", description = "유저 마이페이지 관련 API")
    @Operation(summary = "유저 프로필 수정", description = "유저의 이름, 프로필 사진을 변경합니다.")
    @PostMapping("profile")
    @CustomExceptionDescription(USER_PROFILE_UPDATE)
    public BaseResponse<Void> updateProfile(@RequestBody @Validated UserUpdateProfileRequestDTO req){
        userService.updateProfile(req);
        return BaseResponse.ok(null);

    }

}
