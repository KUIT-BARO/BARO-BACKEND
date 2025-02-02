package com.example.baro.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "promise_keyword")
public class PromiseKeyword {

    @EmbeddedId
    private PromiseKeywordId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promise_personal_id", nullable = false)
    private PromisePersonal promisePersonal;

    @Builder
    public PromiseKeyword(Keyword keyword, PromisePersonal promisePersonal) {
        this.keyword = keyword;
        this.promisePersonal = promisePersonal;
    }
}
