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
public class PromisePersonalKeywordId implements Serializable {
    @Column(name = "promise_personal_id", nullable = false)
    private Long promisePersonalId;

    @Column(name = "keyword_id", nullable = false)
    private Long keywordId;

    @Builder
    public PromisePersonalKeywordId(Long promisePersonalId, Long keywordId) {
        this.promisePersonalId = promisePersonalId;
        this.keywordId = keywordId;
    }
}
