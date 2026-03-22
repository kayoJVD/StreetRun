package com.example.sr.commons;

import com.example.sr.domain.User;
import com.example.sr.dto.request.UserRequest;
import com.example.sr.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserCreator {

    public static User createValidUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Kayo");
        user.setEmail("kayo@email.com");
        user.setPassword("123456");
        user.setWeight(88.0);
        user.setHeight(1.79);
        user.setBirthDate(LocalDate.of(1998, 9, 29));
        return user;
    }

    public static UserRequest createValidUserRequest() {
        return new UserRequest(
                "Kayo", "kayo@email.com", "123456", 88.0, 1.79, LocalDate.of(1998, 9, 29)
        );
    }

    public static UserResponse createValidUserResponse() {
        return new UserResponse(
                1L, "Kayo", "kayo@email.com", 88.0, 1.79, LocalDate.of(1998, 9, 29)
        );
    }
}