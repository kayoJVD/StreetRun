package com.example.sr.dto.request;

import com.example.sr.dto.response.CoordinateDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public record ActivityRequest(
        @NotNull
        @Positive
        Double distance,

        @NotNull
        LocalDate date,

        @NotNull
        @Positive
        Integer duration,

        @NotNull
        @Positive
        Long sportsId,

        List<CoordinateDTO> route
) {
}
