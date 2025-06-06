package konkuk.kuit.baro.domain.promise.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import konkuk.kuit.baro.global.common.response.status.BaseStatus;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "promise")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class Promise extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_id", nullable = false)
    private Long id;

    @Column(name = "promise_name", length = 30, nullable = false)
    private String promiseName;

    @Column(name = "suggested_region", length = 50, nullable = false)
    private String suggestedRegion;

    @Column(name = "suggested_start_date", nullable = false, columnDefinition = "DATE")
    private LocalDate suggestedStartDate;

    @Column(name = "suggested_end_date", nullable = false, columnDefinition = "DATE")
    private LocalDate suggestedEndDate;

    @Setter
    @Column(name = "fixed_date", columnDefinition = "DATE")
    private LocalDate fixedDate;

    @Setter
    @Column(name = "fixed_time")
    private LocalTime fixedTime;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;  // 확정된 장소

    @Setter
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "promise_vote_id")
    private PromiseVote promiseVote;

    // 약속에 참여한 유저를 확인하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "promise", orphanRemoval = true)
    private List<PromiseMember> promiseMembers = new ArrayList<>();

    @Builder
    public Promise(String promiseName, String suggestedRegion, LocalDate suggestedStartDate, LocalDate suggestedEndDate) {
        this.promiseName = promiseName;
        this.suggestedRegion = suggestedRegion;
        this.suggestedStartDate = suggestedStartDate;
        this.suggestedEndDate = suggestedEndDate;
        this.setStatus(BaseStatus.PENDING);
    }

    public String extractFixedDateAndTime() {
        // 날짜 형식 변환
        String datePart = fixedDate.format(DateTimeFormatter.ofPattern("M/d"));

        // 요일 변환 (한글 요일)
        String dayOfWeek = fixedDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        // 시간 변환 (h시)
        String timePart = fixedTime.format(DateTimeFormatter.ofPattern("h시"));

        return datePart + "(" + dayOfWeek + ") " + timePart;
    }

    // 연관 관계 편의 메서드
    public void addPromiseMember(PromiseMember promiseMember) { this.promiseMembers.add(promiseMember); }


}
