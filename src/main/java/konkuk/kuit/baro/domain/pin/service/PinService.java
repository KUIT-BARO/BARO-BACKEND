package konkuk.kuit.baro.domain.pin.service;

import konkuk.kuit.baro.domain.pin.dto.request.PinRequestDTO;
import konkuk.kuit.baro.domain.pin.dto.response.PinResponseDTO;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.domain.pin.repository.PinRepository;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import konkuk.kuit.baro.global.common.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PinService {

    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

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

    public void registerPinData(PinRequestDTO request, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow((() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
        Place place = placeRepository.findByLocation(request.getLatitude(), request.getLongitude())
                .orElseGet(() -> {
                    Place newPlace = Place.builder()
                            .placeName(request.getPlaceName())
                            .location(GeometryUtil.createPoint(request.getLatitude(), request.getLongitude()))
                            .placeAddress(request.getPlaceAddress())
                            .build();
                    return placeRepository.save(newPlace);
                });

        Pin pin = Pin.createPin(
                request.getReview(),
                request.getScore(),
                user,
                place
        );
        pinRepository.save(pin);
    }
}

