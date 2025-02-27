package konkuk.kuit.baro.domain.schedule.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.global.common.converter.DayOfWeekConverter;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalTime;


@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("status IN (1, 3, 4, 5)")
public class Schedule extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Long id;

    @Column(name = "schedule_name", length = 30, nullable = false)
    private String scheduleName;

    @Convert(converter = DayOfWeekConverter.class)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "place_name", length = 50, nullable = false)
    private String placeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 생성 메서드
    public static Schedule createSchedule(String scheduleName, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, String placeName, User user) {
        Schedule schedule = new Schedule();
        schedule.scheduleName = scheduleName;
        schedule.dayOfWeek = dayOfWeek;
        schedule.startTime = startTime;
        schedule.endTime = endTime;
        schedule.placeName = placeName;
        schedule.setUser(user);

        return schedule;
    }

    // 연관 관계 편의 메서드
    private void setUser(User user) {
        this.user = user;
        user.addSchedule(this);
    }



}
