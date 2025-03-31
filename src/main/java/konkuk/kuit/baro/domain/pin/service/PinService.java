package konkuk.kuit.baro.domain.pin.service;

import konkuk.kuit.baro.domain.category.repository.PlaceCategoryRepository;
import konkuk.kuit.baro.domain.pin.dto.PinResponseDTO;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.domain.pin.repository.PinRepository;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PinService {

    private final PinRepository pinRepository;

    public PinResponseDTO getPinData(Long pinId){
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow((() -> new CustomException(ErrorCode.PIN_NOT_FOUND)));

        return new PinResponseDTO(
                pin.getUser().getName(),
                pin.getUser().getEmail(),
                pin.getUser().getProfileImage(),
                pin.getReview(),
                pin.getScore(),
                pin.getPlace().getPlaceName());
    }
}

