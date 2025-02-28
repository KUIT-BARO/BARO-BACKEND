package konkuk.kuit.baro.domain.user.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.schedule.model.Schedule;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET status = 2 WHERE user_id = ?")
@SQLRestriction("status IN ('ACTIVE', 'BEFORE_VOTE', 'DURING_VOTE', 'AFTER_VOTE')")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Lob
    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "color", length = 20, nullable = false)
    private String color;

    // 일정을 확인하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    // 핀을 확인하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Pin> pins = new ArrayList<>();

    // 참가한 약속을 확인하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<PromiseMember> promiseMembers = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String profileImage, String color) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage;
        this.color = color;
    }

    // 연관 관계 편의 메서드
    public void addSchedule(Schedule schedule) { this.schedules.add(schedule); }

    public void addPin(Pin pin) { this.pins.add(pin); }

    public void addPromiseMember(PromiseMember promiseMember) { this.promiseMembers.add(promiseMember); }
}
