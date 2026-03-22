package com.example.sr.controller;

import com.example.sr.dto.request.SportsRequest;
import com.example.sr.dto.response.SportsResponse;
import com.example.sr.service.SportsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sports")
public class SportsController {

    private final SportsService service;

    @GetMapping
    public ResponseEntity<List<SportsResponse>> getAllSports() {
        return ResponseEntity.ok(service.findAllSports());
    }

    @PostMapping
    public ResponseEntity<SportsResponse> createSport(@RequestBody @Valid SportsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSports(request));
    }
}