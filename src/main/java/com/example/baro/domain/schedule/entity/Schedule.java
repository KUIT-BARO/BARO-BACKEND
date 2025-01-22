package com.example.baro.domain.schedule.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "schedule_TB")
@Getter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Time time_start;

    @Column(nullable = false)
    private Time time_end;

    @Builder
    public Schedule(Long id, Long userId, String name,
                    Date date, Time time_start, Time time_end) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.date = date;
        this.time_start = time_start;
        this.time_end = time_end;
    }
}
