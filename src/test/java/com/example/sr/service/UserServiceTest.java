package com.example.sr.service;

import com.example.sr.commons.UserCreator;
import com.example.sr.domain.User;
import com.example.sr.dto.request.UserRequest;
import com.example.sr.dto.response.UserResponse;
import com.example.sr.exception.BusinessRuleException;
import com.example.sr.repository.UserRepository;
import com.example.sr.srMapper.UserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = UserCreator.createValidUser();
        userRequest = UserCreator.createValidUserRequest();
        userResponse = UserCreator.createValidUserResponse();
    }


    @Test
    @DisplayName("findByEmail returns User when successful")
    void findByEmailReturnsUserWhenSuccessful() {
        BDDMockito.when(repository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User userFound = service.findByEmail("kayo@email.com");

        Assertions.assertThat(userFound).isNotNull();
        Assertions.assertThat(userFound.getId()).isEqualTo(user.getId());
        Assertions.assertThat(userFound.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("findByEmail throws BusinessRuleException when email not found")
    void findByEmailThrowsBusinessRuleExceptionWhenEmailNotFound() {
        BDDMockito.when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                .isThrownBy(() -> service.findByEmail("ghost@email.com"))
                .withMessage("User not found");
    }


    @Test
    @DisplayName("registerUser returns UserResponse when successful")
    void registerUser_ReturnsUserResponse_WhenSuccessful() {
        BDDMockito.when(repository.findByEmail(userRequest.email())).thenReturn(Optional.empty());
        BDDMockito.when(mapper.toRequest(userRequest)).thenReturn(user);
        BDDMockito.when(repository.save(any(User.class))).thenReturn(user);
        BDDMockito.when(mapper.toResponse(user)).thenReturn(userResponse);

        UserResponse savedUser = service.registerUser(userRequest);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.email()).isEqualTo(userRequest.email());
        BDDMockito.verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("registerUser throws BusinessRuleException when email already exists")
    void registerUser_ThrowsBusinessRuleException_WhenEmailAlreadyExists() {
        BDDMockito.when(repository.findByEmail(userRequest.email())).thenReturn(Optional.of(user));

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                .isThrownBy(() -> service.registerUser(userRequest))
                .withMessage("Email already exists");

        BDDMockito.verify(repository, BDDMockito.never()).save(any(User.class));
    }


    @Test
    @DisplayName("loginUser returns UserResponse when credentials are correct")
    void loginUser_ReturnsUserResponse_WhenCredentialsAreCorrect() {
        BDDMockito.when(repository.findByEmail(userRequest.email())).thenReturn(Optional.of(user));
        BDDMockito.when(mapper.toResponse(user)).thenReturn(userResponse);

        UserResponse loginResponse = service.loginUser(userRequest);

        Assertions.assertThat(loginResponse).isNotNull();
        Assertions.assertThat(loginResponse.email()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("loginUser throws BusinessRuleException when password is wrong")
    void loginUser_ThrowsBusinessRuleException_WhenPasswordIsWrong() {
        BDDMockito.when(repository.findByEmail(userRequest.email())).thenReturn(Optional.of(user));

        UserRequest wrongPasswordRequest = new UserRequest(
                "Kayo", "kayo@email.com", "senha-errada", 1.79, 88.0, LocalDate.of(1998, 9, 29)
        );

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                .isThrownBy(() -> service.loginUser(wrongPasswordRequest))
                .withMessage("Wrong Password");
    }

    @Test
    @DisplayName("loginUser throws BusinessRuleException when user not found")
    void loginUser_ThrowsBusinessRuleException_WhenUserNotFound() {
        BDDMockito.when(repository.findByEmail(userRequest.email())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                .isThrownBy(() -> service.loginUser(userRequest))
                .withMessage("User not found");
    }


    @Test
    @DisplayName("updateUser returns UserResponse when successful")
    void updateUser_ReturnsUserResponse_WhenSuccessful() {
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        BDDMockito.when(repository.save(any(User.class))).thenReturn(user);
        BDDMockito.when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        UserResponse updatedUser = service.updateUser(user.getId(), userRequest);

        Assertions.assertThat(updatedUser).isNotNull();
        BDDMockito.verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("updateUser throws BusinessRuleException when user not found")
    void updateUser_ThrowsBusinessRuleException_WhenUserNotFound() {
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                .isThrownBy(() -> service.updateUser(user.getId(), userRequest))
                .withMessage("User not found");

        BDDMockito.verify(repository, BDDMockito.never()).save(any(User.class));
    }


    @Test
    @DisplayName("deleteUser removes user when successful")
    void deleteUser_RemovesUser_WhenSuccessful() {
        BDDMockito.when(repository.existsById(user.getId())).thenReturn(true);

        Assertions.assertThatNoException().isThrownBy(() -> service.deleteUser(user.getId()));
        BDDMockito.verify(repository).deleteById(user.getId());
    }

    @Test
    @DisplayName("deleteUser throws BusinessRuleException when user does not exist")
    void deleteUser_ThrowsBusinessRuleException_WhenUserDoesNotExist() {
        BDDMockito.when(repository.existsById(user.getId())).thenReturn(false);

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                .isThrownBy(() -> service.deleteUser(user.getId()))
                .withMessage("User not found");

        BDDMockito.verify(repository, BDDMockito.never()).deleteById(any());
    }
}