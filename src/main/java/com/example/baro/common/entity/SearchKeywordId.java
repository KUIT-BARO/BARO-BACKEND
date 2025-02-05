package com.example.baro.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class SearchKeywordId implements Serializable {
    @Column(name = "search_id", nullable = false)
    private Long searchId;

    @JoinColumn(name = "keyword_id", nullable = false)
    private Long keywordId;

    @Builder
    public SearchKeywordId(Long searchId, Long keywordId) {
        this.searchId = searchId;
        this.keywordId = keywordId;
    }
}
