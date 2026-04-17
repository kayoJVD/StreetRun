package com.example.sr.service;

import com.example.sr.exception.ErrorMessages;
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
            .orElseThrow(() -> new BusinessRuleException(ErrorMessages.USER_NOT_FOUND));
    }

    public UserResponse registerUser(UserRequest request) {
        validateEmailDoesNotExist(request.email());

        User user = mapper.toRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = repository.save(user);

        return mapper.toResponse(savedUser);
    }

    public String loginUser(LoginRequest request) {
        User user = findByEmail(request.email());

        validatePassword(request.password(), user.getPassword());

        return tokenConfig.getToken(user);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User user = repository.findById(id)
            .orElseThrow(() -> new BusinessRuleException(ErrorMessages.USER_NOT_FOUND));

        user.setName(request.name());
        user.setEmail(request.email());
        user.setHeight(request.height());
        user.setWeight(request.weight());
        user.setBirthDate(request.birthDate());

        User savedUser = repository.save(user);
        return mapper.toResponse(savedUser);
    }

    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessRuleException(ErrorMessages.USER_NOT_FOUND);
        }
        repository.deleteById(id);
    }


    private void validateEmailDoesNotExist(String email) {
        if (repository.findByEmail(email).isPresent()) {
            throw new BusinessRuleException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BusinessRuleException(ErrorMessages.WRONG_PASSWORD);
        }
    }
}
