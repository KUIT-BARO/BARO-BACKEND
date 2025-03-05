package konkuk.kuit.baro.global.common.response.status;

import lombok.Getter;

@Getter
public enum BaseStatus {

    ACTIVE,        // 유효한 데이터
    INACTIVE,      // 유효하지 않은 데이터 ( 삭제된 데이터 )
    PENDING,   // 약속 상태 -> 미정
    VOTING,   // 약속 상태 -> 투표중
    CONFIRMED;    // 약속 상태 -> 확정
}
