package konkuk.kuit.baro.domain.pin.service;

import konkuk.kuit.baro.domain.category.model.Category;
import konkuk.kuit.baro.domain.category.model.PinCategory;
import konkuk.kuit.baro.domain.category.repository.CategoryRepository;
import konkuk.kuit.baro.domain.category.repository.PinCategoryRepository;
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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PinService {

    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;

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

        Point placePoint = GeometryUtil.createPoint(request.getLatitude(), request.getLongitude());
        Place place = placeRepository.findPlaceByLocation(placePoint)
                .orElseGet(() -> {
                    Place newPlace = Place.builder()
                            .placeName(request.getPlaceName())
                            .location(placePoint)
                            .placeAddress(request.getPlaceAddress())
                            .build();
                    return placeRepository.save(newPlace);
                });

        // 카테고리 이름 리스트로 Category 조회
        List<String> categoryNames = request.getCategoryNames();
        List<Category> categories = categoryRepository.findAllByCategoryNameIn(categoryNames)
                .orElseGet(Collections::emptyList);

        // Pin 생성
        Pin pin = Pin.createPin(
                request.getReview(),
                request.getScore(),
                user,
                place
        );

        // PinCategory를 만들어서 연결
        for (Category category : categories) {
            PinCategory pinCategory = PinCategory.createPinCategory(pin, category);
            pin.addPinCategory(pinCategory);
        }

        // 저장 (CascadeType.ALL 설정되어 있으니 자동 저장됨)
        pinRepository.save(pin);
    }
}

