package konkuk.kuit.baro.global.common.response.status;

import lombok.Getter;

@Getter
public enum BaseStatus {

    ACTIVE,        // 유효한 데이터
    INACTIVE,      // 유효하지 않은 데이터 ( 삭제된 데이터 )
    BEFORE_VOTE,   // 약속 상태 -> 투표 시작 전
    DURING_VOTE,   // 약속 상태 -> 투표중
    AFTER_VOTE;    // 약속 상태 -> 투표 종료 후
}
