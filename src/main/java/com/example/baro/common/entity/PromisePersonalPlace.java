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

    @EmbeddedId
    private PromisePersonalPlaceId id;

    @Column(nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status;

    @MapsId("promisePersonalId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promise_personal_id", nullable = false)
    private PromisePersonal promisePersonal;

    @MapsId("placeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;


    @Builder
    public PromisePersonalPlace(Status status, PromisePersonal promisePersonal, Place place) {
        this.promisePersonal = promisePersonal;
        this.place = place;

    }

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = Status.ACTIVE;
        }
    }
}
