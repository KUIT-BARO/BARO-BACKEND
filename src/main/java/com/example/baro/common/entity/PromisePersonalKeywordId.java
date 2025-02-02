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
public class PromiseKeywordId implements Serializable {
    @Column(name = "promise_id", nullable = false)
    private Long searchId;

    @JoinColumn(name = "keyword_id", nullable = false)
    private Long keywordId;

    @Builder
    public PromiseKeywordId(Long searchId, Long keywordId) {
        this.searchId = searchId;
        this.keywordId = keywordId;
    }
}
