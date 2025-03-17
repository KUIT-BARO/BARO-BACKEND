package konkuk.kuit.baro.domain.user.service;

import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.repository.PromiseMemberRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseRepository;
import konkuk.kuit.baro.domain.user.dto.request.UserUpdatePasswordRequestDTO;
import konkuk.kuit.baro.domain.user.dto.request.UserUpdateProfileRequestDTO;
import konkuk.kuit.baro.domain.user.dto.response.UserHomePagePromiseDTO;
import konkuk.kuit.baro.domain.user.dto.response.UserHomePageResponseDTO;
import konkuk.kuit.baro.domain.user.dto.response.UserProfileResponseDTO;
import konkuk.kuit.baro.domain.user.dto.response.UserProfileSettingResponseDTO;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PromiseMemberRepository promiseMemberRepository;
    private final PromiseRepository promiseRepository;

    @Transactional
    public void updateProfile(UserUpdateProfileRequestDTO req) {
        User loginUser = userRepository.findById(1L).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        validateName(req.getNewName());

        loginUser.setName(req.getNewName());
        loginUser.setProfileImage(req.getNewProfileImage());
        userRepository.save(loginUser);
    }

    private void validateName(String name){
        if(name.length() > 12){
            throw new CustomException(USER_NAME_LENGTH);
        }
    }

    public UserProfileResponseDTO getProfile(Long userId){
        User loginUser = userRepository.findById(1L).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return new UserProfileResponseDTO(loginUser.getName(), loginUser.getProfileImage());
    }

    public UserProfileSettingResponseDTO getProfileSetting(Long userId){
        User loginUser = userRepository.findById(1L).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return new UserProfileSettingResponseDTO(loginUser.getName(), loginUser.getEmail(), loginUser.getProfileImage());
    }

    @Transactional
    public void updatePassword(UserUpdatePasswordRequestDTO req){
        User loginUser = userRepository.findById(1L).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        validateCurrentPassword(req, loginUser);
        validateNewPassword(req);

        loginUser.setPassword(req.getNewPassword());
        userRepository.save(loginUser);
    }

    private void validateNewPassword(UserUpdatePasswordRequestDTO req) {
        if(req.getNewPassword().length() < 8){
            throw new CustomException(USER_PASSWORD_LENGTH);
        }

        if(!req.getNewPassword().equals(req.getConfirmPassword())){
            throw new CustomException(USER_NEW_PASSWORD_NOT_MATCH);
        }
    }

    private void validateCurrentPassword(UserUpdatePasswordRequestDTO req, User loginUser) {
        if(!loginUser.getPassword().equals(req.getCurrentPassword())){
            throw new CustomException(USER_CURRENT_PASSWORD_WRONG);
        }

        if(loginUser.getPassword().equals(req.getNewPassword())){
            throw new CustomException(USER_NEW_PASSWORD_SAME);
        }

    }

    @Transactional
    public void deleteUser(Long userId){
        User loginUser = userRepository.findById(1L).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        userRepository.delete(loginUser);
    }

    public UserHomePageResponseDTO getHomePage(Long userId){
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        List<PromiseMember> findPromiseMember = promiseMemberRepository.findAllByUserId(loginUser.getId());
        if(findPromiseMember.isEmpty()){
            return new UserHomePageResponseDTO(loginUser.getName()); // 사용자 이름만 반환
        }
        List<UserHomePagePromiseDTO> promiseDTO = new ArrayList<>();
        int fastestDday = Integer.MIN_VALUE;
        for (PromiseMember promiseMember : findPromiseMember) {
            Promise promise = promiseMemberRepository.findByPromiseMemberId(promiseMember.getId());
            LocalDate promiseDate = promise.getFixedDate();
            String hostName = promiseMemberRepository.findHostNameByPromiseId(promise.getId());
            int numberOfPromiseMember = promiseMemberRepository.findNumberOfPromiseMemberById(promise.getId());
            String promiseMembers = hostName + "외 " + (numberOfPromiseMember - 1) + "명";
            int Dday = promiseDate.getDayOfYear() - LocalDate.now().getDayOfYear();
            if(Dday > fastestDday){
                fastestDday = Dday;
            }
            promiseDTO.add(new UserHomePagePromiseDTO(
                    promise.getPlace().getPlaceName(),
                    promise.getPromiseName(),
                    promise.getFixedDate(),
                    promise.getFixedDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN),
                    promiseMembers,
                    Dday
            ));
        }

        return new UserHomePageResponseDTO(loginUser.getName(), fastestDday, promiseDTO);
    }


}
