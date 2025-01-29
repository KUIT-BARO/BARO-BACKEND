package com.example.baro.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_promise")
public class UserPromise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promise_id", nullable = false)
    private Promise promise;

    @Column(nullable = false)
    private boolean display;

    @Builder
    public UserPromise(User user, Promise promise, boolean display) {
        this.user = user;
        this.promise = promise;
        this.display = display;
    }
}
