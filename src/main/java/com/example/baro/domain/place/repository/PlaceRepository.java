package com.example.baro.domain.place.repository;

import com.example.baro.common.entity.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
     @Query(value = "SELECT * FROM place ORDER BY RAND()", nativeQuery = true)
    List<Place> findRandomPlaces(Pageable pageable);

    Optional<Place> findByAddress(String address);

    Optional<Place> findByName(String Name);
}
