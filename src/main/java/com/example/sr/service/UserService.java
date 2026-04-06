package com.example.sr.service;

import com.example.sr.config.TokenConfig;
import com.example.sr.domain.User;
import com.example.sr.dto.request.LoginRequest;
import com.example.sr.dto.request.UserRequest;
import com.example.sr.dto.response.UserResponse;
import com.example.sr.exception.BusinessRuleException;
import com.example.sr.repository.UserRepository;
import com.example.sr.srMapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenConfig tokenConfig;

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder, TokenConfig tokenConfig) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenConfig = tokenConfig;
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new BusinessRuleException("User not found"));
    }


    public UserResponse registerUser(UserRequest request) {
        if (repository.findByEmail(request.email()).isPresent()) {
            throw new BusinessRuleException("Email already exists");
        }
        User user = mapper.toRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User save = repository.save(user);

        return mapper.toResponse(save);
    }

    public String loginUser(LoginRequest request) {
        User userLogin = repository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessRuleException("User not found"));
        if (!passwordEncoder.matches(request.password(), userLogin.getPassword())) {
            throw new BusinessRuleException("Wrong Password");
        }
        return tokenConfig.getToken(userLogin);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("User not found"));

        user.setName(request.name());
        user.setEmail(request.email());
        user.setHeight(request.height());
        user.setWeight(request.weight());
        user.setBirthDate(request.birthDate());
        User save = repository.save(user);
        return mapper.toResponse(save);
    }

    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessRuleException("User not found");
        }
        repository.deleteById(id);
    }
}

