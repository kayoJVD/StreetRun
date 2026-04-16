package com.example.sr.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record ActivityResponse(
        Long id,
        @JsonProperty("distance_km")
        Double distance,
        LocalDate date,
        Integer duration,
        String sportsName,
        String pace,
        List<CoordinateDTO> route
) {
}
