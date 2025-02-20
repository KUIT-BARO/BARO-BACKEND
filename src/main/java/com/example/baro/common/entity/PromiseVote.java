package com.example.baro.common.entity;

import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.Enum.status.StatusConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "promise_vote")
public class PromiseVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promise_id", nullable = false)
    private Promise promise;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promise_personal_time_id", nullable = false)
    private PromisePersonalTime promisePersonalTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Builder
    public PromiseVote(Status status, Promise promise, PromisePersonalTime promisePersonalTime, Place place) {
        this.promise = promise;
        this.promisePersonalTime = promisePersonalTime;
        this.place = place;
    }

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = Status.SUSPENDED;
        }
    }
}
