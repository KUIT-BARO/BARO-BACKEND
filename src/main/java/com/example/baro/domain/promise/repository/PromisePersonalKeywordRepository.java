package com.example.baro.domain.promise.repository;

import com.example.baro.common.entity.Keyword;
import com.example.baro.common.entity.PromisePersonalKeyword;
import com.example.baro.common.entity.PromisePersonalKeywordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromisePersonalKeywordRepository extends JpaRepository<PromisePersonalKeyword, PromisePersonalKeywordId>{
	@Query("SELECT sk.keyword FROM SearchKeyword sk " +
			"JOIN sk.search s " +
			"WHERE s.user.id = :userId")
	List<Keyword> findKeywordsByUserId(@Param("userId") Long userId);

	@Query("SELECT sk.keyword FROM Search s " +
			"JOIN SearchKeyword sk ON sk.search.id = s.id " +
			"WHERE s.id = :searchId")
	List<Keyword> findKeywordsBySearchId(@Param("searchId") Long searchId);
}
