package com.example.sr.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email) {
}
