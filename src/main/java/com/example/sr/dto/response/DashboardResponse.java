package com.example.sr.dto.response;

public record DashboardResponse(
    Double totalDistance,
    Long totalActivities,
    Double monthlyDistance,
    String averagePace
) {
}
