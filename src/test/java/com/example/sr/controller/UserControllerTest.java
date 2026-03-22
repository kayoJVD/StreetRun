package com.example.sr.controller;

import com.example.sr.commons.UserCreator;
import com.example.sr.domain.User;
import com.example.sr.dto.request.UserRequest;
import com.example.sr.dto.response.UserResponse;
import com.example.sr.service.UserService;
import com.example.sr.srMapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private UserMapper mapper;

    @Autowired
    private ResourceLoader resourceLoader;

    private User user;
    private UserRequest request;
    private UserResponse response;

    @BeforeEach
    void init() {
        user = UserCreator.createValidUser();
        request = UserCreator.createValidUserRequest();
        response = UserCreator.createValidUserResponse();
    }

    @Test
    @DisplayName("GET /api/v1/users returns UserResponse when successful")
    void findUserByEmail_ReturnsUserResponse_WhenSuccessful() throws Exception {
        BDDMockito.when(service.findByEmail(user.getEmail())).thenReturn(user);
        BDDMockito.when(mapper.toResponse(user)).thenReturn(response);

        String responseJson = readFile("users/get-user-by-email-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("email", user.getEmail()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseJson));
    }

    @Test
    @DisplayName("POST /api/v1/users creates User when successful")
    void registerUser_CreatedUser_WhenSuccessful() throws Exception {
        String requestJson = readFile("users/post-request-user.json");
        String responseJson = readFile("users/post-response-user-201.json");

        BDDMockito.when(service.registerUser(ArgumentMatchers.any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(responseJson));
    }

    @Test
    @DisplayName("PUT /api/v1/users/{id} updates User when successful")
    void updateUser_UpdatesUser_WhenSuccessful() throws Exception {
        String requestJson = readFile("users/put-request-user.json");
        String responseJson = readFile("users/put-response-user-200.json");

        BDDMockito.when(service.updateUser(ArgumentMatchers.eq(user.getId()), ArgumentMatchers.any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{id}", user.getId())
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseJson));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} removes User when successful")
    void deleteUser_RemovesUser_WhenSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", user.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @ParameterizedTest
    @MethodSource("badRequestSource")
    @DisplayName("POST /api/v1/users returns 400 Bad Request when fields are invalid")
    void registerUser_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName) throws Exception {
        String requestJson = readFile(fileName);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("badRequestSource")
    @DisplayName("PUT /api/v1/users/{id} returns 400 Bad Request when fields are invalid")
    void updateUser_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName) throws Exception {
        String requestJson = readFile(fileName);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{id}", user.getId())
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> badRequestSource() {
        return java.util.stream.Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("users/request-user-blank-name-400.json"),
                org.junit.jupiter.params.provider.Arguments.of("users/request-user-invalid-email-400.json")
        );
    }

    private String readFile(String fileName) throws IOException {
        java.io.InputStream inputStream = resourceLoader.getResource("classpath:%s".formatted(fileName)).getInputStream();
        return new String(inputStream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
    }
}