package com.example.baro.domain.promise.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "promise_TB")
@Getter
public class Promise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Long regionId;

    @Column(nullable = true)
    private Long placeId;

    @Column(nullable = true)
    private String status;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date_start;

    @Column(nullable = false)
    private LocalDate date_end;

    @Column(nullable = false)
    private LocalTime time_start;

    @Column(nullable = false)
    private LocalTime time_end;

    @Column(nullable = false)
    private int peopleNum;

    @Column(nullable = false)
    private String purpose;

    @Builder
    public Promise(String name, Date date, LocalDate date_start, LocalDate date_end,
                   LocalTime time_start, LocalTime time_end, int peopleNum, String purpose) {
        this.name = name;
        this.date_start = date_start;
        this.date_end = date_end;
        this.time_start = time_start;
        this.time_end = time_end;
        this.peopleNum = peopleNum;
        this.purpose = purpose;
    }
}
