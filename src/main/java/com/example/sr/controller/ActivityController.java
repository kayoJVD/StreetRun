package com.example.sr.controller;

import com.example.sr.config.JWTUserData;
import com.example.sr.domain.Activity;
import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import com.example.sr.dto.response.DashboardResponse;
import com.example.sr.dto.response.PersonalBestsResponse;
import com.example.sr.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<ActivityResponse>> listMyActivities(
        @AuthenticationPrincipal JWTUserData userData,
        @PageableDefault(size = 10, sort = {"date"}, direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {

        log.debug("Request to get paginated activities for user id: {}", userData.userId());


        Page<ActivityResponse> activities = service.listActivitiesByUser(userData.userId(), pageable);
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

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(@AuthenticationPrincipal JWTUserData userData) {
        return ResponseEntity.ok(service.getDashboard(userData.userId()));
    }

    @GetMapping("/personal-bests")
    public ResponseEntity<PersonalBestsResponse> getPersonalBests(@AuthenticationPrincipal JWTUserData userData) {
        return ResponseEntity.ok(service.getPersonalBests(userData.userId()));
    }
}
