package com.example.baro.common.entity;
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
@Table(name = "promise_personal_time")
public class PromisePersonalTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime timeStart;

    @Column(nullable = false)
    private LocalTime timeEnd;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promise_individual_id", nullable = false)
    private PromisePersonal promisePersonal;

    @Builder
    public PromisePersonalTime(LocalDate date, LocalTime timeStart, LocalTime timeEnd, PromisePersonal promisePersonal) {
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.promisePersonal = promisePersonal;
    }
}
