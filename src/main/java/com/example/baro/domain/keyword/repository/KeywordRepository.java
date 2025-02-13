package com.example.baro.domain.keyword.repository;

import com.example.baro.common.entity.Keyword;
import com.example.baro.common.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}
