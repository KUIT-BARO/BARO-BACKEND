package konkuk.kuit.baro.domain.promise.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.vote.model.PromiseTimeVoteHistory;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promise_available_time")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class PromiseAvailableTime extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_available_time_id", nullable = false)
    private Long id;

    @Column(name = "available_date", nullable = false, columnDefinition = "DATE")
    private LocalDate availableDate;

    @Column(name = "available_start_time", nullable = false)
    private LocalTime availableStartTime;

    @Column(name = "available_end_time", nullable = false)
    private LocalTime availableEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_member_id", nullable = false)
    private PromiseMember promiseMember;


    // 생성 메서드
    public static PromiseAvailableTime createPromiseAvailableTime(LocalDate availableDate, LocalTime availableStartTime, LocalTime availableEndTime, PromiseMember promiseMember) {
        PromiseAvailableTime promiseAvailableTime = new PromiseAvailableTime();
        promiseAvailableTime.availableDate = availableDate;
        promiseAvailableTime.availableStartTime = availableStartTime;
        promiseAvailableTime.availableEndTime = availableEndTime;
        promiseAvailableTime.setPromiseMember(promiseMember);

        return promiseAvailableTime;
    }

    // 연관 관계 편의 메서드
    private void setPromiseMember(PromiseMember promiseMember) {
        this.promiseMember = promiseMember;
        promiseMember.addPromiseAvailableTime(this);
    }

}
