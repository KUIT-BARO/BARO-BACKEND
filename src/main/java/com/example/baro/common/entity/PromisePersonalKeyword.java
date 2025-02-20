package com.example.baro.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "promise_personal_keyword")
public class PromisePersonalKeyword {

    @EmbeddedId
    private PromisePersonalKeywordId id;

    @MapsId("promisePersonalId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promise_personal_id", nullable = false)
    private PromisePersonal promisePersonal;

    @MapsId("keywordId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @Builder
    public PromisePersonalKeyword( PromisePersonal promisePersonal, Keyword keyword) {
        this.id = new PromisePersonalKeywordId(promisePersonal.getId(), keyword.getId());
        this.promisePersonal = promisePersonal;
        this.keyword = keyword;
    }
}
