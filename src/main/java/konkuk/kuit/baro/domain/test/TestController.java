package konkuk.kuit.baro.domain.test;

import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/health-check")
    public BaseResponse<Void> test() {
        return BaseResponse.ok(null);
    }

    @GetMapping("/error-test")
    public BaseResponse<Void> errorTest() {
        throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
    }
}
