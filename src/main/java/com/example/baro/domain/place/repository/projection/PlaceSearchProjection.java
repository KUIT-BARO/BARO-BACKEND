package com.example.baro.domain.place.repository.projection;

public interface PlaceSearchProjection {
    Long getPlaceId();
    String getPlaceName();
    String getPlaceAddress();
    double getPlaceLatitude();
    double getPlaceLongitude();
    Long getKeywordId();
    String getKeywordName();
    int getKeywordCount();
}