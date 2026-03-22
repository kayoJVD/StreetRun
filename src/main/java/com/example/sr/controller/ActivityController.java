package com.example.sr.controller;

import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import com.example.sr.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService service;

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> findActivityById(@PathVariable Long id) {
        log.debug("Request to get activities by id: {}", id);

        ActivityResponse activity = service.searchActivityById(id);

        return ResponseEntity.status(HttpStatus.OK).body(activity);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ActivityResponse>> listActivitiesByUser(@PathVariable Long id) {
        log.debug("Request to get all activities by id: {}", id);

        List<ActivityResponse> activities = service.listActivitiesByUser(id);

        return ResponseEntity.status(HttpStatus.OK).body(activities);
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> registerActivity(@RequestBody @Valid ActivityRequest request) {
        log.debug("Request to create activity: {}", request);

        ActivityResponse activity = service.registerActivity(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(activity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityById(@PathVariable Long id) {
        log.debug("Request to delete activities by id: {}", id);
        service.deleteActivityById(id);

        return ResponseEntity.noContent().build();
    }
}
