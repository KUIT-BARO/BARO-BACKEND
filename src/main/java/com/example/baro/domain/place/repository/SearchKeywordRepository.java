package com.example.baro.domain.user.repository;

import com.example.baro.common.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, SearchKeywordId>{
	@Query("SELECT sk.keyword FROM SearchKeyword sk " +
			"JOIN sk.search s " +
			"WHERE s.user.id = :userId")
	List<Keyword> findKeywordsByUserId(@Param("userId") Long userId);
}
