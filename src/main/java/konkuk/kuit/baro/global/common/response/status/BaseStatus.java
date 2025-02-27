package konkuk.kuit.baro.global.common.response.status;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BaseStatus {

    ACTIVE(1),        // 유효한 데이터
    INACTIVE(2),      // 유효하지 않은 데이터 ( 삭제된 데이터 )
    BEFORE_VOTE(3),   // 약속 상태 -> 투표 시작 전
    DURING_VOTE(4),   // 약속 상태 -> 투표중
    AFTER_VOTE(5);    // 약속 상태 -> 투표 종료 후

    private final int code;

    BaseStatus(int code) {
        this.code = code;
    }

    public static BaseStatus ofCode(Integer code) {
        return Arrays.stream(values())
                .filter(v -> v.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
