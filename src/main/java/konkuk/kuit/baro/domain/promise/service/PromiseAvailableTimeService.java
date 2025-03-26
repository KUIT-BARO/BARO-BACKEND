package konkuk.kuit.baro.domain.promise.service;

import konkuk.kuit.baro.domain.promise.dto.request.SetPromiseAvailableTimeRequestDTO;
import konkuk.kuit.baro.domain.promise.dto.request.TimeDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseAvailableTimeResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseMemberAvailableTimeDTO;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseAvailableTime;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.repository.PromiseAvailableTimeRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseMemberRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.util.ColorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PromiseAvailableTimeService {
    private final PromiseMemberRepository promiseMemberRepository;
    private final UserRepository userRepository;
    private final ColorUtil colorUtil;
    private final PromiseRepository promiseRepository;
    private final PromiseAvailableTimeRepository promiseAvailableTimeRepository;

    public PromiseAvailableTimeResponseDTO getPromiseAvailableTime(Long promiseId) {
        List<PromiseMember> promiseMembers = promiseMemberRepository.findAllByPromiseId(promiseId);
        List<List<PromiseMemberAvailableTimeDTO>> availableTimes = new ArrayList<>();
        for (PromiseMember promiseMember : promiseMembers) {
            availableTimes.add(promiseAvailableTimeRepository.findAllByPromiseMemberId(promiseMember.getId()));
        }
        return new PromiseAvailableTimeResponseDTO(promiseMemberRepository.findPromiseMemberDTOByPromiseId(promiseId), availableTimes);
    }

    @Transactional
    public void setPromiseAvailableTime(SetPromiseAvailableTimeRequestDTO req, Long userId, Long promiseId) {
        User loginUser = userRepository.findById(1L).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Promise promise = promiseRepository.findById(promiseId).orElseThrow(() -> new CustomException(PROMISE_NOT_FOUND));

        addPromiseMember(promiseId, loginUser, promise);

        List<TimeDTO> times = req.getTimes();
        for (TimeDTO time : times) {
            PromiseAvailableTime promiseAvailableTime = PromiseAvailableTime.createPromiseAvailableTime(
                    time.getDate(),
                    time.getStartTime(),
                    time.getEndTime(),
                    promiseMemberRepository.findByUserIdAndPromiseId(loginUser.getId(), promiseId)
            );

            promiseAvailableTimeRepository.save(promiseAvailableTime);
        }


    }


    private void addPromiseMember(Long promiseId, User loginUser, Promise promise) {
        List<Long> userIdList = promiseMemberRepository.findUserIdListByPromiseid(promiseId);
        if(!userIdList.contains(loginUser.getId())) {
            PromiseMember.createPromiseMember(false,
                    colorUtil.generateRandomHexColor(promiseId),
                    loginUser,
                    promise);
        } // 약속 수락이 안 된 경우, PromiseMember 엔티티 추가
    }


}
