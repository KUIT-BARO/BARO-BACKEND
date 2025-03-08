package konkuk.kuit.baro.domain.user.service;

import konkuk.kuit.baro.domain.user.dto.request.UserUpdatePasswordRequestDTO;
import konkuk.kuit.baro.domain.user.dto.request.UserUpdateProfileRequestDTO;
import konkuk.kuit.baro.domain.user.dto.response.UserProfileResponseDTO;
import konkuk.kuit.baro.domain.user.dto.response.UserProfileSettingResponseDTO;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
        if(!req.getNewPassword().equals(req.getConfirmPassword())){
            throw new CustomException(USER_NEW_PASSWORD_NOT_MATCH);
        }
    }

    private void validateCurrentPassword(UserUpdatePasswordRequestDTO req, User loginUser) {
        if(loginUser.getPassword().equals(req.getNewPassword())){
            throw new CustomException(USER_NEW_PASSWORD_SAME);
        }
        if(!loginUser.getPassword().equals(req.getCurrentPassword())){
            throw new CustomException(USER_CURRENT_PASSWORD_WRONG);
        }

    }

    public void deleteUser(Long userId){
        User loginUser = userRepository.findById(1L).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        userRepository.delete(loginUser);
        log.info("User deleted: {}", loginUser.getName());
        userRepository.flush();
    }


}
