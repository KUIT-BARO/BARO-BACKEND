package com.example.baro.domain.place.repository;

import com.example.baro.common.entity.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query(value = "SELECT p FROM Place p ORDER BY FUNCTION('RAND')", nativeQuery = true)
    List<Place> findRandomPlaces(Pageable pageable);
}
