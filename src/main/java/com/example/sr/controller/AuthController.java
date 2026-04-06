package com.example.sr.controller;

import com.example.sr.config.TokenConfig;
import com.example.sr.domain.User;
import com.example.sr.dto.request.LoginRequest;
import com.example.sr.dto.request.UserRequest;
import com.example.sr.dto.response.LoginResponse;
import com.example.sr.dto.response.UserResponse;
import com.example.sr.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(user);

        User user1 = (User) authentication.getPrincipal();
        String token = tokenConfig.getToken(user1);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        log.info("request received to create user : {}", request);

        UserResponse userResponse = service.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
