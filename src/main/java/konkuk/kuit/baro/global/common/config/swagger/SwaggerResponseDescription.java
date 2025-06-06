package konkuk.kuit.baro.global.common.config.swagger;

import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

import static konkuk.kuit.baro.global.common.response.status.ErrorCode.*;

@Getter
public enum SwaggerResponseDescription {

    //Auth
    LOGIN(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            LOGIN_FAILED
    ))),
    REISSUE(new LinkedHashSet<>(Set.of(
            REFRESH_TOKEN_REQUIRED
    ))),
    LOGOUT(new LinkedHashSet<>(Set.of(
            SECURITY_INVALID_ACCESS_TOKEN,
            REFRESH_TOKEN_REQUIRED
    ))),

    //User
    USER_SIGNUP(new LinkedHashSet<>(Set.of(
            USER_DUPLICATE_EMAIL
    ))),
    USER_PROFILE_UPDATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_NAME_LENGTH
    ))),
    USER_PROFILE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    USER_PASSWORD_UPDATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_CURRENT_PASSWORD_WRONG,
            USER_NEW_PASSWORD_NOT_MATCH,
            USER_NEW_PASSWORD_SAME
    ))),
    USER_DELETE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    USER_HOME(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),

    //Promise
    PROMISE_SUGGEST(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    SET_AVAILALBLE_TIME(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            PROMISE_NOT_FOUND
    ))),
    PROMISE_MANAGING_PAGE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    HOST_PROMISE_MANAGING_PAGE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    GET_SUGGESTED_PLACE(new LinkedHashSet<>(Set.of(
            PROMISE_NOT_FOUND,
            INVALID_LOCATION
    ))),
    PLACE_CATEGORY_SEARCH(new LinkedHashSet<>(Set.of(
            INVALID_LOCATION
    ))),
    SET_SUGGESTED_PLACE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            PROMISE_NOT_FOUND,
            PLACE_NOT_FOUND
    ))),

    //Schedule
    SCHEDULE_ADD(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            INVALID_SCHEDULE_NAME,
            SCHEDULE_CONFLICT,
            INVALID_SCHEDULE_TIME
    ))),
    SCHEDULE_UPDATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            INVALID_SCHEDULE_NAME,
            SCHEDULE_CONFLICT,
            INVALID_SCHEDULE_TIME,
            SCHEDULE_NOT_FOUND
    ))),
    GET_SCHEDULES(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    PLACE_SEARCH(new LinkedHashSet<>(Set.of(
            INVALID_LOCATION
    ))),
    PLACE_SUMMARY_INFO(new LinkedHashSet<>(Set.of(
            PLACE_NOT_FOUND
    ))),
    PLACE_PIN_LIST(new LinkedHashSet<>(Set.of(
            PLACE_NOT_FOUND
    ))),

    PROMISE_STATUS(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            PROMISE_NOT_FOUND,
            PROMISE_MEMBER_NOT_FOUND
    ))),

    PENDING_PROMISE_STATUS(new LinkedHashSet<>(Set.of(
            PROMISE_NOT_FOUND
    ))),

    VOTING_PROMISE_STATUS(new LinkedHashSet<>(Set.of(
            PROMISE_NOT_FOUND,
            PROMISE_VOTE_NOT_IN_PROGRESS
    ))),

    CONFIRMED_PROMISE_STATUS(new LinkedHashSet<>(Set.of(
            PROMISE_NOT_FOUND,
            PROMISE_NOT_CONFIRMED
    ))),

    PROMISE_SUGGEST_REMAINING_TIME(new LinkedHashSet<>(Set.of(
            PROMISE_NOT_FOUND,
            TIME_EXCEED
    ))),

    PROMISE_VOTE_REMAINING_TIME(new LinkedHashSet<>(Set.of(
            PROMISE_NOT_FOUND,
            PROMISE_VOTE_NOT_IN_PROGRESS,
            TIME_EXCEED
    ))),

    PROMISE_MEMBER_HAS_VOTED(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            PROMISE_NOT_FOUND,
            PROMISE_MEMBER_NOT_FOUND,
            PROMISE_VOTE_NOT_IN_PROGRESS
    ))),

    VOTE_CANDIDATE_LIST(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            PROMISE_NOT_FOUND,
            PROMISE_MEMBER_NOT_FOUND,
            PROMISE_VOTE_NOT_IN_PROGRESS
    ))),

    VOTE_INIT(new LinkedHashSet<>(Set.of(
            PROMISE_NOT_FOUND,
            PROMISE_AVAILABLE_TIME_NOT_FOUND,
            PROMISE_SUGGESTED_PLACE_NOT_FOUND
    ))),

    VOTE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            PROMISE_NOT_FOUND,
            PROMISE_MEMBER_NOT_FOUND,
            PROMISE_VOTE_NOT_IN_PROGRESS,
            PROMISE_CANDIDATE_TIME_NOT_FOUND,
            PROMISE_CANDIDATE_PLACE_NOT_FOUND
    ))),

    CLOSE_VOTE(new LinkedHashSet<>(Set.of(
            PROMISE_NOT_FOUND,
            PROMISE_VOTE_NOT_IN_PROGRESS,
            PROMISE_TIME_NOT_CONFIRMED,
            PROMISE_PLACE_NOT_CONFIRMED
    ))),
    PIN_VIEW(new LinkedHashSet<>(Set.of(
            PIN_NOT_FOUND
    ))),
    PIN_REGISTER(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    )));
    private final Set<ErrorCode> errorCodeList;
    SwaggerResponseDescription(Set<ErrorCode> errorCodeList) {
        // 공통 에러
        errorCodeList.addAll(new LinkedHashSet<>(Set.of(
                ILLEGAL_ARGUMENT,
                NOT_FOUND,
                METHOD_NOT_ALLOWED,
                SERVER_ERROR,
                UNAUTHORIZED,
                FORBIDDEN
        )));


        this.errorCodeList = errorCodeList;
    }
}
