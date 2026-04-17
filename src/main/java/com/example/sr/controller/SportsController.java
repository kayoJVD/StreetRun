package com.example.sr.controller;

import com.example.sr.dto.request.SportsRequest;
import com.example.sr.dto.response.SportsResponse;
import com.example.sr.service.SportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sports")
@Tag(name = "Sports", description = "Endpoints for managing running sports and modalities")
public class SportsController {

    private final SportsService service;

    @GetMapping
    @Operation(summary = "List all sports", description = "Retrieves a list of all registered street racing sports")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<List<SportsResponse>> getAllSports() {
        return ResponseEntity.ok(service.findAllSports());
    }

    @PostMapping
    @Operation(summary = "Register a new sport", description = "Creates a new street racing sport modality in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sport created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "422", description = "Validation error in provided fields"),
        @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<SportsResponse> createSport(@RequestBody @Valid SportsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSports(request));
    }
}
