package konkuk.kuit.baro.domain.promise.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "promise_candidate_time")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class PromiseCandidateTime extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_candidate_time_id", nullable = false)
    private Long id;

    @Column(name = "promise_candidate_time_date", nullable = false, columnDefinition = "DATE")
    private LocalDate promiseCandidateTimeDate;

    @Column(name = "promise_candidate_time_start_time", nullable = false)
    private LocalTime promiseCandidateTimeStartTime;

    @Column(name = "promise_candidate_time_end_time", nullable = false)
    private LocalTime promiseCandidateTimeEndTime;



}
