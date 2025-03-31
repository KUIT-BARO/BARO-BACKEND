package konkuk.kuit.baro.domain.pin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.baro.domain.pin.service.PinService;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "핀 API", description = "핀 관련 API")
@RestController
@RequestMapping("/pin")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;

    @Operation(summary = "핀 열람", description = "핀을 열람합니다.")
    @GetMapping("/{pinId}")
    // @CustomExceptionDescription(GET_PIN_DATA)
    public BaseResponse<List<PinResponseDTO>> getPin(@PathVariable Long pinId) {
        return BaseResponse.ok(pinService.getPinData(pinId));
    }
}
