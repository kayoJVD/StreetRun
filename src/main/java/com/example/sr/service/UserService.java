package com.example.sr.service;

import com.example.sr.domain.User;
import com.example.sr.dto.request.UserRequest;
import com.example.sr.dto.response.UserResponse;
import com.example.sr.exception.BusinessRuleException;
import com.example.sr.repository.UserRepository;
import com.example.sr.srMapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
        User save = repository.save(user);

        return mapper.toResponse(save);
    }

    public UserResponse loginUser(UserRequest request) {
        User userLogin = repository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessRuleException("User not found"));
        if (!userLogin.getPassword().equals(request.password())) {
            throw new BusinessRuleException("Wrong Password");
        }
        return mapper.toResponse(userLogin);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("User not found"));

        user.setName(request.name());
        user.setName(request.email());
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

