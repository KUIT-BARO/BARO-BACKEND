package com.example.baro.common.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "search")
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;


    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    private Integer score; // TINYINT UNSIGNED는 Java에서 Integer로 사용


    @Builder
    public Search(User user, Place place, Keyword keyword, String note, Integer score, String image) {
        this.user = user;
        this.place = place;
        this.note = note;
        this.score = score;
    }
}