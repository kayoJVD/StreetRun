package com.example.sr.controller;

import com.example.sr.domain.User;
import com.example.sr.dto.request.LoginRequest;
import com.example.sr.dto.request.UserRequest;
import com.example.sr.dto.response.UserResponse;
import com.example.sr.service.UserService;
import com.example.sr.srMapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<UserResponse> findUserByEmail(@RequestParam String email) {
        log.info("request received to email, param email : {}", email);
        User user = service.findByEmail(email);

        UserResponse response = mapper.toResponse(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Valid LoginRequest request) {
        log.info("request received to login user : {}", request.email());
        String token = service.loginUser(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest request){
        log.info("request received to create user : {}", request);

        UserResponse userResponse = service.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest request){
        log.info("request received to update user : {}", request);
        UserResponse userResponse = service.updateUser(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Valid Long id){
        log.info("request received to delete user : {}", id);
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
