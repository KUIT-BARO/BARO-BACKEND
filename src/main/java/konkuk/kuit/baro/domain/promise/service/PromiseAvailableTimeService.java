package konkuk.kuit.baro.domain.promise.service;

import konkuk.kuit.baro.domain.promise.dto.response.PromiseAvailableTimeResponseDTO;
import konkuk.kuit.baro.domain.promise.repository.PromiseMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PromiseAvailableTimeService {
    private final PromiseMemberRepository promiseMemberRepository;

    public PromiseAvailableTimeResponseDTO getPromiseAvailableTime(Long promiseId) {
        return new PromiseAvailableTimeResponseDTO(promiseMemberRepository.findPromiseMemberByPromiseId(promiseId));
    }

}
