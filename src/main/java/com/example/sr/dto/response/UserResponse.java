package com.example.sr.dto.response;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String name,
        String email,
        Double weight,
        Double height,
        LocalDate birthDate){
}
