package com.example.sr.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ActivityRequest(
        @NotNull
        @Positive
        Double distance,
        @NotNull
        LocalDate date,
        @NotNull
        @Positive
        Integer duration,
        @Positive
        @NotNull
        Long userId,
        @NotNull
        @Positive
        Long sportsId
) {
}
