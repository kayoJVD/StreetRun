package com.example.sr.controller;

import com.example.sr.config.JWTUserData;
import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import com.example.sr.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService service;

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> findActivityById(@PathVariable Long id, @AuthenticationPrincipal JWTUserData userData) {
        log.debug("Request to get activities by id: {}", id);

        ActivityResponse activity = service.searchActivityById(id, userData.userId());

        return ResponseEntity.status(HttpStatus.OK).body(activity);
    }

    @GetMapping("/me")
    public ResponseEntity<List<ActivityResponse>> listMyActivities(@AuthenticationPrincipal JWTUserData userData) {
        log.debug("Request to get all activities for logged user id: {}", userData);

        List<ActivityResponse> activities = service.listActivitiesByUser(userData.userId());

        return ResponseEntity.status(HttpStatus.OK).body(activities);
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> registerActivity(@RequestBody @Valid ActivityRequest request, @AuthenticationPrincipal JWTUserData userData) {
        log.debug("Request to create activity for user id: {}", userData.userId());

        ActivityResponse activity = service.registerActivity(request, userData.userId());

        return ResponseEntity.status(HttpStatus.CREATED).body(activity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityById(
            @PathVariable Long id,
            @AuthenticationPrincipal JWTUserData userData) {

        log.debug("Request to delete activity {} by user {}", id, userData.userId());

        service.deleteActivityById(id, userData.userId());
        return ResponseEntity.noContent().build();
    }
}
