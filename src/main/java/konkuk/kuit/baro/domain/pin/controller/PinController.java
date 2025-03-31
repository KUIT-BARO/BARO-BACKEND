package konkuk.kuit.baro.domain.pin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import konkuk.kuit.baro.domain.place.dto.request.PlaceSearchRequestDTO;
import konkuk.kuit.baro.domain.place.dto.response.PlaceSearchResponseDTO;
import konkuk.kuit.baro.domain.place.service.PlaceService;
import konkuk.kuit.baro.global.common.annotation.CustomExceptionDescription;
import konkuk.kuit.baro.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static konkuk.kuit.baro.global.common.config.swagger.SwaggerResponseDescription.PLACE_SEARCH;


@Tag(name = "핀 API", description = "핀 관련 API")
@RestController
@RequestMapping("/pin")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;

    @Operation(summary = "핀 열람", description = "핀을 열람합니다.")
    @GetMapping("/{pinId}")
    // @CustomExceptionDescription(PLACE_SEARCH)
    public BaseResponse<List<PinResponseDTO>> placeSearch(@PathVariable Long pinId) {
        return BaseResponse.ok(pinService.getPinData(pinId));
    }
}
