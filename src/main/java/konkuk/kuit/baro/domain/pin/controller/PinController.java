package konkuk.kuit.baro.domain.pin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.kuit.baro.domain.pin.dto.request.PinRequestDTO;
import konkuk.kuit.baro.domain.pin.dto.response.PinPageResponseDTO;
import konkuk.kuit.baro.domain.pin.dto.response.PinResponseDTO;
import konkuk.kuit.baro.domain.pin.service.PinService;
import konkuk.kuit.baro.global.auth.resolver.CurrentUserId;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription.PIN_VIEW;
import static konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription.PIN_REGISTER;


@Tag(name = "핀 API", description = "핀 관련 API")
@RestController
@RequestMapping("/pin")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;

    @Operation(summary = "핀 등록 페이지", description = "핀 등록 페이지를 열람합니다.")
    @GetMapping("/page")
    public BaseResponse<PinPageResponseDTO> getPinPage() {
        return BaseResponse.ok(pinService.getPinPageData());
    }

    @Operation(summary = "핀 열람", description = "핀을 열람합니다.")
    @GetMapping("/{pinId}")
    @CustomExceptionDescription(PIN_VIEW)
    public BaseResponse<PinResponseDTO> getPin(@PathVariable Long pinId) {
        return BaseResponse.ok(pinService.getPinData(pinId));
    }

    @Operation(summary = "핀 등록", description = "새로운 핀을 등록합니다.")
    @PostMapping
    @CustomExceptionDescription(PIN_REGISTER)
    public BaseResponse<Void> registerPin(@RequestBody @Valid PinRequestDTO request,
                                          @CurrentUserId Long userId) {
        pinService.registerPinData(request, userId);
        return BaseResponse.ok(null);
    }
}
