package konkuk.kuit.baro.domain.user.service;

import konkuk.kuit.baro.domain.user.dto.request.UserUpdateProfileRequestDTO;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void updateProfile(UserUpdateProfileRequestDTO req) {
        User loginUser = userRepository.findById(1L).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        validateName(req.getNewName());

        loginUser.setName(req.getNewName());
        loginUser.setProfileImage(req.getNewProfileImage());
        userRepository.save(loginUser);
    }

    private void validateName(String name){
        if(name.length() > 12){
            throw new CustomException(ErrorCode.USER_NAME_LENGTH);
        }
    }
}
