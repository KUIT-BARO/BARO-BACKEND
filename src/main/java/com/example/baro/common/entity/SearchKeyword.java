package com.example.baro.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "search_keyword")
public class SearchKeyword {
    @EmbeddedId
    private SearchKeywordId id;

    @MapsId("searchId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "search_id", nullable = false)
    private Search search;

    @MapsId("keywordId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;


    @Builder
    public SearchKeyword(Search search, Keyword keyword) {
        this.search = search;
        this.keyword = keyword;
    }
}
