package konkuk.kuit.baro.domain.pin.service;

import konkuk.kuit.baro.domain.category.model.Category;
import konkuk.kuit.baro.domain.category.model.PinCategory;
import konkuk.kuit.baro.domain.category.repository.CategoryRepository;
import konkuk.kuit.baro.domain.category.repository.PinCategoryRepository;
import konkuk.kuit.baro.domain.pin.dto.request.PinRequestDTO;
import konkuk.kuit.baro.domain.pin.dto.response.CategoryDTO;
import konkuk.kuit.baro.domain.pin.dto.response.PinPageResponseDTO;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PinService {

    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;

    public PinPageResponseDTO getPinPageData(){
        List<Category> categories = categoryRepository.findAll();

        List<CategoryDTO> categoriesList = categories.stream()
                .map(category -> new CategoryDTO(category.getId(), category.getCategoryName()))
                .collect(Collectors.toList());

        return new PinPageResponseDTO(categoriesList);
    }

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

    @Transactional
    public void registerPinData(PinRequestDTO request, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow((() -> new CustomException(ErrorCode.USER_NOT_FOUND)));

        Place place;
        if (request.getPlaceId() == -1) {
            Point placePoint = GeometryUtil.createPoint(request.getLatitude(), request.getLongitude());
            place = Place.builder()
                    .placeName(request.getPlaceName())
                    .location(placePoint)
                    .placeAddress(request.getPlaceAddress())
                    .build();
            placeRepository.save(place);
        } else {
            place = placeRepository.findPlaceById(request.getPlaceId())
                    .orElseThrow((() -> new CustomException(ErrorCode.PLACE_NOT_FOUND)));
        }
          Pin pin = Pin.createPin(
                request.getReview(),
                request.getScore(),
                user,
                place
        );

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        for (Category category : categories) {
            PinCategory pinCategory = PinCategory.createPinCategory(pin, category);
            pin.addPinCategory(pinCategory);
        }

        // 저장 (CascadeType.ALL 설정되어 있으니 자동 저장됨)
        pinRepository.save(pin);
    }
}

