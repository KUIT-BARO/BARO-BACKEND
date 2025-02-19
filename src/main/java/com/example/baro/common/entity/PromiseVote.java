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
    @JoinColumn(name = "promis_personal_time_id", nullable = false)
    private PromisePersonalTime promisePersonalTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promise_personal_place_id", nullable = false)
    private PromisePersonalPlace promisePersonalPlace;

    @Builder
    public PromiseVote(Status status, Promise promise, PromisePersonalTime promisePersonalTime, PromisePersonalPlace promisePersonalPlace) {
        this.promise = promise;
        this.promisePersonalTime = promisePersonalTime;
        this.promisePersonalPlace = promisePersonalPlace;
    }

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = Status.SUSPENDED;
        }
    }

}
