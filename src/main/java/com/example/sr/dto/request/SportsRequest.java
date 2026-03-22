package com.example.sr.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SportsRequest(
        @NotBlank(message = "O nome do esporte é obrigatório!")
        String name
) {
}