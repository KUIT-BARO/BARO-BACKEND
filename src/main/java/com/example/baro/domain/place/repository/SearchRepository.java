package com.example.baro.domain.place.repository;

import com.example.baro.common.entity.Place;
import com.example.baro.common.entity.Search;
import com.example.baro.common.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
	List<Search> findAllByUser(User user);

	@Query("SELECT s.place FROM Search s WHERE s.user.id = :userId ORDER BY s.id DESC")
	List<Place> findPlacesByUserId(@Param("userId") Long userId, Pageable pageable);
}
