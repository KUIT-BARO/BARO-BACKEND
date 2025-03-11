package konkuk.kuit.baro.domain.promise.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.kuit.baro.domain.promise.dto.request.PromiseSuggestRequestDTO;
import konkuk.kuit.baro.domain.promise.service.PromiseService;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription.*;

@Slf4j
@RestController
@RequestMapping("/promises")
@RequiredArgsConstructor
public class PromiseController {

    private final PromiseService promiseService;

    @Tag(name = "약속 제안 API", description = "약속 제안 관련 API")
    @Operation(summary = "약속 제안", description = "약속 이름, 날짜, 장소등을 입력하여 약속을 제안합니다.")
    @PostMapping
    @CustomExceptionDescription(PROMISE_SUGGEST)
    public BaseResponse<Void> suggestPromise(@Valid @RequestBody PromiseSuggestRequestDTO request) {
        // 현재 로그인한 유저를 토큰에서 꺼낸 후, host 로써 약속 참여자 테이블에 저장해야함.
        // 아직 토큰 로직이 부재하기에 userId 1번을 집어넣음
        promiseService.promiseSuggest(request, 1L);

        return BaseResponse.ok(null);
    }
}
