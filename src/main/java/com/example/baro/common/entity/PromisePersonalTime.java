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
@Table(name = "promise_personal_time")
public class PromisePersonalTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime timeStart;

    @Column(nullable = false)
    private LocalTime timeEnd;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promise_personal_id", nullable = false)
    private PromisePersonal promisePersonal;

    @Column(nullable = false)
    private int voteCount;

    @Builder
    public PromisePersonalTime(Status status, LocalDate date, LocalTime timeStart, LocalTime timeEnd, PromisePersonal promisePersonal, int voteCount) {
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.promisePersonal = promisePersonal;
        this.voteCount = voteCount;
    }

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = Status.INACTIVE;
        }
        this.voteCount = 0;
    }

    public void vote(){
        this.status = Status.SUSPENDED;
        this.voteCount++;
    }
}
