package com.example.sr.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;


public record UserRequest(
        @NotBlank(message = "Nome é obrigatorio!")
        String name,
        @NotBlank(message = "E-mail é obrigatorio!")
        @Email(message = "Formato de e-mail invalido!")
        String email,
        @NotBlank(message = "A senha obrigatória!")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres!")
        String password,
        @Positive
        @NotNull(message = "Informe seu peso!")
        Double weight,
        @Positive
        @NotNull(message = "Informe sua altura!")
        Double height,
        @NotNull LocalDate birthDate) {
}
