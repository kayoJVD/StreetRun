package com.example.sr.dto.response;

public record PersonalBestsResponse(
    ActivityResponse longestRun,
    ActivityResponse fastestRun
) {
}
