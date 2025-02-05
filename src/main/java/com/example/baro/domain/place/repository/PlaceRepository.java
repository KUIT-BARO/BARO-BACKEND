package com.example.baro.domain.place.repository;

import com.example.baro.common.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
