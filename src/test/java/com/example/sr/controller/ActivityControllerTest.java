package com.example.sr.controller;

import com.example.sr.commons.ActivityCreator;
import com.example.sr.config.JWTUserData;
import com.example.sr.config.SecurityFilter;
import com.example.sr.config.TokenConfig;
import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import com.example.sr.service.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebMvcTest(ActivityController.class)
@AutoConfigureMockMvc(addFilters = false)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActivityService service;

    @MockitoBean
    private TokenConfig tokenConfig;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Autowired
    private ResourceLoader resourceLoader;

    private ActivityResponse response;

    private final Long loggedInUserId = 1L;

    @BeforeEach
    void init() {
        response = ActivityCreator.createValidActivityResponse();

        JWTUserData userData = JWTUserData.builder().userId(loggedInUserId).email("kayo@alves.com").build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userData, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("GET /api/v1/activities/{id} returns ActivityResponse when successful")
    void findActivityById_ReturnsActivityResponse_WhenSuccessful() throws Exception {

        BDDMockito.when(service.searchActivityById(ArgumentMatchers.eq(1L), ArgumentMatchers.any())).thenReturn(response);

        String responseJson = readFile("activities/get-activity-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/activities/{id}", 1L))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(responseJson));
    }

    @Test
    @DisplayName("GET /api/v1/activities/me returns paginated ActivityResponse when successful")
    void listMyActivities_ReturnsPaginatedResponse_WhenSuccessful() throws Exception {

        org.springframework.data.domain.Page<ActivityResponse> pageResponse = new org.springframework.data.domain.PageImpl<>(List.of(response));

        BDDMockito.when(service.listActivitiesByUser(ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenReturn(pageResponse);

        String responseJson = readFile("activities/list-activities-paginated-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/activities/me"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(responseJson));
    }

    @Test
    @DisplayName("POST /api/v1/activities creates Activity when successful")
    void registerActivity_CreatesActivity_WhenSuccessful() throws Exception {
        String requestJson = readFile("activities/post-request-activity.json");
        String responseJson = readFile("activities/post-response-activity-201.json");

        BDDMockito.when(service.registerActivity(ArgumentMatchers.any(ActivityRequest.class), ArgumentMatchers.any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/activities")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(responseJson));
    }

    @Test
    @DisplayName("DELETE /api/v1/activities/{id} removes Activity when successful")
    void deleteActivityById_RemovesActivity_WhenSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/activities/{id}", 1L))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @ParameterizedTest
    @MethodSource("badRequestSource")
    @DisplayName("POST /api/v1/activities returns 422 Unprocessable Entity when fields are invalid")
    void registerActivity_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName) throws Exception {
        String requestJson = readFile(fileName);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/activities")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    private static java.util.stream.Stream<Arguments> badRequestSource() {
        return java.util.stream.Stream.of(
            Arguments.of("activities/post-request-activity-invalid-400.json")
        );
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = resourceLoader.getResource("classpath:%s".formatted(fileName)).getInputStream();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
