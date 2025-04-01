package konkuk.kuit.baro.domain.promise.model;

import jakarta.persistence.*;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.vote.model.PromisePlaceVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseTimeVoteHistory;
import konkuk.kuit.baro.global.common.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promise_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("status IN ('ACTIVE', 'PENDING', 'VOTING', 'CONFIRMED')")
public class PromiseMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_member_id", nullable = false)
    private Long id;

    @Column(name = "is_host", nullable = false, columnDefinition = "TINYINT")
    private Boolean isHost;

    @Column(name = "color", length = 20, nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promise_id", nullable = false)
    private Promise promise;

    // 약속 참여자가 삭제되었을 때, 제안된 약속 장소도 삭제하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "promiseMember", orphanRemoval = true)
    private List<PromiseSuggestedPlace> promiseSuggestedPlaces = new ArrayList<>();

    // 약속 참여자가 삭제되었을 때, 약속 가능 시간도 삭제하기 위한 양방향 연관 관계
    @OneToMany(mappedBy = "promiseMember", orphanRemoval = true)
    private List<PromiseAvailableTime> promiseAvailableTimes = new ArrayList<>();

    // 약속 시간 투표 내역 확인용
    @OneToMany(mappedBy = "promiseMember", orphanRemoval = true)
    private List<PromiseTimeVoteHistory> promiseTimeVoteHistories = new ArrayList<>();

    // 약속 장소 투표 내역 확인용
    @OneToMany(mappedBy = "promiseMember", orphanRemoval = true)
    private List<PromisePlaceVoteHistory> promisePlaceVoteHistories = new ArrayList<>();

    // 생성 메서드
    public static PromiseMember createPromiseMember(Boolean isHost, String color, User user, Promise promise) {
        PromiseMember promiseMember = new PromiseMember();
        promiseMember.isHost = isHost;
        promiseMember.color = color;
        promiseMember.setUser(user);
        promiseMember.setPromise(promise);

        return promiseMember;
    }

    // 연관 관계 편의 메서드
    private void setUser(User user) {
        this.user = user;
        user.addPromiseMember(this);
    }

    private void setPromise(Promise promise) {
        this.promise = promise;
        promise.addPromiseMember(this);
    }

    public void addPromiseSuggestedPlace(PromiseSuggestedPlace promiseSuggestedPlace) {
        this.promiseSuggestedPlaces.add(promiseSuggestedPlace);
    }

    public void addPromiseAvailableTime(PromiseAvailableTime promiseAvailableTime) {
        this.promiseAvailableTimes.add(promiseAvailableTime);
    }

    public void addPromiseTimeVoteHistory(PromiseTimeVoteHistory promiseTimeVoteHistory) {
        this.promiseTimeVoteHistories.add(promiseTimeVoteHistory);
    }

    public void addPromisePlaceVoteHistory(PromisePlaceVoteHistory promisePlaceVoteHistory) {
        this.promisePlaceVoteHistories.add(promisePlaceVoteHistory);
    }

}
