package konkuk.kuit.baro.domain.promise.service;

import konkuk.kuit.baro.domain.promise.dto.request.PromiseSuggestRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseStatusResponseDTO;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.repository.PromiseMemberRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import konkuk.kuit.baro.global.common.util.ColorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromiseService {

    private final PromiseRepository promiseRepository;
    private final PromiseMemberRepository promiseMemberRepository;
    private final UserRepository userRepository;

    private final ColorUtil colorUtil;

    @Transactional
    public void promiseSuggest(PromiseSuggestRequestDTO request, Long loginUserId) {

        // 약속 엔티티 생성
        Promise promise = Promise.builder()
                .promiseName(request.getPromiseName())
                .suggestedRegion(request.getSuggestedRegion())
                .suggestedStartDate(request.getSuggestedStartDate())
                .suggestedEndDate(request.getSuggestedEndDate())
                .build();

        // 약속 엔티티 저장
        Promise savedPromise = promiseRepository.save(promise);

        // 로그인한 유저 찾기
        User loginUser = findLoginUser(loginUserId);

        // 테두리 색상 설정
        String borderColor = colorUtil.generateRandomHexColor(savedPromise.getId());

        // 약속 참여자 엔티티 생성
        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, borderColor, loginUser, savedPromise);

        // 약속 참여자 엔티티 저장
        promiseMemberRepository.save(promiseMember);
    }

    // 약속 상태 확인
    public PromiseStatusResponseDTO getPromiseStatus(Long userId, Long promiseId) {
        PromiseMember findPromiseMember = findPromiseMember(userId, promiseId);

        return new PromiseStatusResponseDTO(extractPromiseStatus(findPromiseMember), findPromiseMember.getIsHost());
    }


    private User findLoginUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private PromiseMember findPromiseMember(Long userId, Long promiseId) {

        if (!userIsExist(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if(!promiseIsExist(promiseId)) {
            throw new CustomException(ErrorCode.PROMISE_NOT_FOUND);
        }

        PromiseMember findPromiseMember = promiseMemberRepository.findByUserIdAndPromiseId(userId, promiseId);

        if (findPromiseMember == null) {
            throw new CustomException(ErrorCode.PROMISE_MEMBER_NOT_FOUND);
        }

        return findPromiseMember;
    }

    private boolean userIsExist(Long userId) {
        return userRepository.existsById(userId);
    }

    private boolean promiseIsExist(Long promiseId) {
        return promiseRepository.existsById(promiseId);
    }

    private String extractPromiseStatus(PromiseMember promiseMember) {
        return promiseMember.getPromise().getStatus().name();
    }


}
