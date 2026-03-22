package com.example.sr.commons;

import com.example.sr.domain.Activity;
import com.example.sr.domain.Sports;
import com.example.sr.domain.User;
import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ActivityCreator {

    public static Activity createValidActivity() {
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setDistance(7.5);
        activity.setDate(LocalDate.of(2026, 3, 29));
        activity.setDuration(45);

        User user = new User();
        user.setId(1L);
        activity.setUser(user);

        Sports sports = new Sports();
        sports.setId(1L);
        sports.setName("Corrida");
        activity.setSports(sports);

        return activity;
    }

    public static ActivityRequest createValidActivityRequest() {
        return new ActivityRequest(
                7.5,
                LocalDate.of(2026, 3, 29),
                45,
                1L,
                1L
        );
    }

    public static ActivityResponse createValidActivityResponse() {
        return new ActivityResponse(
                1L,
                7.5,
                LocalDate.of(2026, 3, 29),
                45,
                "Corrida"
        );
    }
}