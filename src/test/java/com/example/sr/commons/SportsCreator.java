package com.example.sr.commons;

import com.example.sr.domain.Sports;
import com.example.sr.dto.request.SportsRequest;
import com.example.sr.dto.response.SportsResponse;
import org.springframework.stereotype.Component;

@Component
public class SportsCreator {

    public static Sports createValidSport() {
        Sports sports = new Sports();
        sports.setId(1L);
        sports.setName("Corrida");
        return sports;
    }

    public static SportsRequest createValidSportsRequest() {
        return new SportsRequest("Corrida");
    }

    public static SportsResponse createValidSportsResponse() {
        return new SportsResponse(1L, "Corrida");
    }
}