package konkuk.kuit.baro.domain.promise.controller;

import jakarta.validation.Valid;
import konkuk.kuit.baro.domain.promise.dto.request.PromiseSuggestRequestDTO;
import konkuk.kuit.baro.domain.promise.service.PromiseService;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import konkuk.kuit.baro.global.common.response.status.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/promise")
@RequiredArgsConstructor
public class PromiseController {

    private final PromiseService promiseService;

    @PostMapping
    public BaseResponse<Void> suggestPromise(@Valid @RequestBody PromiseSuggestRequestDTO request) {
        // 현재 로그인한 유저를 토큰에서 꺼낸 후, host 로써 약속 참여자 테이블에 저장해야함.
        // 아직 토큰 로직이 부재하기에 userId 1번을 집어넣음
        promiseService.promiseSuggest(request, 1L);

        return new BaseResponse<>(SuccessCode.PROMISE_SUGGEST_SUCCESS, null);
    }
}
