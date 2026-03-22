package com.example.sr.service;

import com.example.sr.commons.SportsCreator;
import com.example.sr.domain.Sports;
import com.example.sr.dto.request.SportsRequest;
import com.example.sr.dto.response.SportsResponse;
import com.example.sr.repository.SportsRepository;
import com.example.sr.srMapper.SportsMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class SportsServiceTest {

    @InjectMocks
    private SportsService service;

    @Mock
    private SportsRepository repository;

    @Mock
    private SportsMapper mapper;

    private Sports sports;
    private SportsRequest request;
    private SportsResponse response;

    @BeforeEach
    void setUp() {
        sports = SportsCreator.createValidSport();
        request = SportsCreator.createValidSportsRequest();
        response = SportsCreator.createValidSportsResponse();
    }

    @Test
    @DisplayName("createSports returns SportsResponse when successful")
    void createSports_ReturnsSportsResponse_WhenSuccessful() {
        BDDMockito.when(mapper.toSports(request)).thenReturn(sports);
        BDDMockito.when(repository.save(ArgumentMatchers.any(Sports.class))).thenReturn(sports);
        BDDMockito.when(mapper.toResponse(sports)).thenReturn(response);

        SportsResponse savedSports = service.createSports(request);

        Assertions.assertThat(savedSports).isNotNull();
        Assertions.assertThat(savedSports.id()).isEqualTo(response.id());
        Assertions.assertThat(savedSports.name()).isEqualTo(response.name());
    }

    @Test
    @DisplayName("findAllSports returns a list of SportsResponse when successful")
    void findAllSports_ReturnsListOfSportsResponse_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(List.of(sports));
        BDDMockito.when(mapper.toResponse(ArgumentMatchers.any(Sports.class))).thenReturn(response);

        List<SportsResponse> sportsList = service.findAllSports();

        Assertions.assertThat(sportsList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(sportsList.get(0).name()).isEqualTo(response.name());
    }
}