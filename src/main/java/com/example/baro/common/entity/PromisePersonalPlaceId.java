package com.example.baro.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class PromisePersonalPlaceId implements Serializable {
    @Column(name = "promise_personal_id", nullable = false)
    private Long promisePersonalId;

    @JoinColumn(name = "place_id", nullable = false)
    private Long placeId;

    @Builder
    public PromisePersonalPlaceId(Long promisePersonalId, Long placeId) {
        this.promisePersonalId = promisePersonalId;
        this.placeId = placeId;
    }
}
