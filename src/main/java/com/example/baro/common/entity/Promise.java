package com.example.baro.common.entity;

import com.example.baro.common.Enum.promisePurpose.PromisePurpose;
import com.example.baro.common.Enum.promisePurpose.PromisePurposeConverter;
import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.Enum.status.StatusConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "promise")
public class Promise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false, length = 30)
    private String name;

    @Column
    private LocalDate date;

    @Column(nullable = false)
    private LocalDate dateStart;

    @Column(nullable = false)
    private LocalDate dateEnd;

    @Column(nullable = true)
    private LocalTime timeStart;

    @Column(nullable = true)
    private LocalTime timeEnd;

    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED")
    private Integer peopleNumber;

    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED")
    @Convert(converter = PromisePurposeConverter.class)
    private PromisePurpose purpose;


    @Column(nullable = false, length = 30)
    private String leaderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;


    @Builder
    public Promise(String status, String name, LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart,
                   LocalTime timeEnd, Integer peopleNumber, PromisePurpose purpose, String leaderName, Region region, Place place) {
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.peopleNumber = peopleNumber;
        this.purpose = purpose;
        this.leaderName = leaderName;
        this.region = region;
        this.place = place;
    }

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = Status.INACTIVE;
        }
    }

    public void confirm(LocalDate date, LocalTime timeStart, LocalTime timeEnd, Place place) {
        this.status = Status.ACTIVE;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.place = place;
    }

}
