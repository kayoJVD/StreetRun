package com.example.sr.controller;

import com.example.sr.commons.SportsCreator;
import com.example.sr.config.SecurityFilter;
import com.example.sr.config.TokenConfig;
import com.example.sr.dto.request.SportsRequest;
import com.example.sr.dto.response.SportsResponse;
import com.example.sr.service.SportsService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebMvcTest(SportsController.class)
@AutoConfigureMockMvc(addFilters = false)
class SportsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SportsService service;

    @MockitoBean
    private TokenConfig tokenConfig;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Autowired
    private ResourceLoader resourceLoader;

    private SportsResponse response;

    @BeforeEach
    void init() {
        response = SportsCreator.createValidSportsResponse();
    }

    @Test
    @DisplayName("GET /api/v1/sports returns list of SportsResponse when successful")
    void getAllSports_ReturnsListOfSportsResponse_WhenSuccessful() throws Exception {
        BDDMockito.when(service.findAllSports()).thenReturn(List.of(response));

        String responseJson = readFile("sports/get-sports-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/sports"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(responseJson));
    }

    @Test
    @DisplayName("POST /api/v1/sports creates Sport when successful")
    void createSport_CreatesSport_WhenSuccessful() throws Exception {
        String requestJson = readFile("sports/post-request-sport.json");
        String responseJson = readFile("sports/post-response-sport-201.json");

        BDDMockito.when(service.createSports(ArgumentMatchers.any(SportsRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sports")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(responseJson));
    }

    @ParameterizedTest
    @MethodSource("badRequestSource")
    @DisplayName("POST /api/v1/sports returns 400 Bad Request when fields are invalid")
    void createSport_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName) throws Exception {
        String requestJson = readFile(fileName);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sports")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    private static java.util.stream.Stream<Arguments> badRequestSource() {
        return java.util.stream.Stream.of(
            Arguments.of("sports/post-request-sport-blank-name-400.json")
        );
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = resourceLoader.getResource("classpath:%s".formatted(fileName)).getInputStream();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
