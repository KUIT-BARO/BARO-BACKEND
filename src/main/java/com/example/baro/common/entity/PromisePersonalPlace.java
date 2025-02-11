package com.example.baro.common.entity;

import com.example.baro.common.Enum.status.Status;
import com.example.baro.common.Enum.status.StatusConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "promise_personal_place")
public class PromisePersonalPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promise_personal_id", nullable = false)
    private PromisePersonal promisePersonal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private int voteCount;

    @Builder
    public PromisePersonalPlace(Status status, PromisePersonal promisePersonal, Place place, int voteCount) {
        this.promisePersonal = promisePersonal;
        this.place = place;
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
