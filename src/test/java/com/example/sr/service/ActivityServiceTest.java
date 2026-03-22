package com.example.sr.service;

import com.example.sr.domain.Activity;
import com.example.sr.domain.Sports;
import com.example.sr.domain.User;
import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import com.example.sr.exception.BusinessRuleException;
import com.example.sr.repository.ActivtyRepository;
import com.example.sr.repository.SportsRepository;
import com.example.sr.repository.UserRepository;
import com.example.sr.srMapper.ActivityMapper;
import com.example.sr.commons.ActivityCreator;
import com.example.sr.commons.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @InjectMocks
    private ActivityService service;

    @Mock
    private ActivtyRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SportsRepository sportsRepository;

    @Mock
    private ActivityMapper mapper;

    private Activity activity;
    private ActivityRequest activityRequest;
    private ActivityResponse activityResponse;
    private User user;
    private Sports sports;

    @BeforeEach
    void setUp() {
        user = UserCreator.createValidUser();

        sports = new Sports();
        sports.setId(1L);
        sports.setName("Corrida");

        activity = ActivityCreator.createValidActivity();
        activityRequest = ActivityCreator.createValidActivityRequest();
        activityResponse = ActivityCreator.createValidActivityResponse();
    }

    @Test
    @DisplayName("registerActivity returns ActivityResponse when successful")
    void registerActivity_ReturnsActivityResponse_WhenSuccessful() {
        BDDMockito.when(userRepository.findById(activityRequest.userId())).thenReturn(Optional.of(user));
        BDDMockito.when(sportsRepository.findById(activityRequest.sportsId())).thenReturn(Optional.of(sports));
        BDDMockito.when(mapper.toRequest(activityRequest)).thenReturn(activity);
        BDDMockito.when(repository.save(any(Activity.class))).thenReturn(activity);
        BDDMockito.when(mapper.toResponse(activity)).thenReturn(activityResponse);

        ActivityResponse savedActivity = service.registerActivity(activityRequest);

        Assertions.assertThat(savedActivity).isNotNull();
        Assertions.assertThat(savedActivity.distance()).isEqualTo(activityRequest.distance());
        BDDMockito.verify(repository).save(any(Activity.class));
    }

    @Test
    @DisplayName("registerActivity throws BusinessRuleException when User not found")
    void registerActivity_ThrowsBusinessRuleException_WhenUserNotFound() {
        BDDMockito.when(userRepository.findById(activityRequest.userId())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                .isThrownBy(() -> service.registerActivity(activityRequest))
                .withMessage("User not found");

        BDDMockito.verify(sportsRepository, BDDMockito.never()).findById(any());
        BDDMockito.verify(repository, BDDMockito.never()).save(any(Activity.class));
    }

    @Test
    @DisplayName("registerActivity throws BusinessRuleException when Sport not found")
    void registerActivity_ThrowsBusinessRuleException_WhenSportNotFound() {
        BDDMockito.when(userRepository.findById(activityRequest.userId())).thenReturn(Optional.of(user));
        BDDMockito.when(sportsRepository.findById(activityRequest.sportsId())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                .isThrownBy(() -> service.registerActivity(activityRequest))
                .withMessage("Sport not found");

        BDDMockito.verify(repository, BDDMockito.never()).save(any(Activity.class));
    }

    @Test
    @DisplayName("listActivitiesByUser returns list of ActivityResponse when successful")
    void listActivitiesByUser_ReturnsListOfActivityResponse_WhenSuccessful() {
        BDDMockito.when(repository.findAllByUserId(user.getId())).thenReturn(List.of(activity));
        BDDMockito.when(mapper.toResponse(any(Activity.class))).thenReturn(activityResponse);

        List<ActivityResponse> activities = service.listActivitiesByUser(user.getId());

        Assertions.assertThat(activities)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("searchActivityById returns ActivityResponse when successful")
    void searchActivityById_ReturnsActivityResponse_WhenSuccessful() {
        BDDMockito.when(repository.findById(activity.getId())).thenReturn(Optional.of(activity));
        BDDMockito.when(mapper.toResponse(activity)).thenReturn(activityResponse);

        ActivityResponse foundActivity = service.searchActivityById(activity.getId());

        Assertions.assertThat(foundActivity).isNotNull();
    }

    @Test
    @DisplayName("searchActivityById throws BusinessRuleException when Activity not found")
    void searchActivityById_ThrowsBusinessRuleException_WhenActivityNotFound() {
        BDDMockito.when(repository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                .isThrownBy(() -> service.searchActivityById(99L))
                .withMessage("Activity not found");
    }

    @Test
    @DisplayName("deleteActivityById removes Activity when successful")
    void deleteActivityById_RemovesActivity_WhenSuccessful() {
        BDDMockito.when(repository.findById(activity.getId())).thenReturn(Optional.of(activity));

        Assertions.assertThatNoException().isThrownBy(() -> service.deleteActivityById(activity.getId()));
        BDDMockito.verify(repository).delete(activity);
    }

    @Test
    @DisplayName("deleteActivityById throws BusinessRuleException when Activity not found")
    void deleteActivityById_ThrowsBusinessRuleException_WhenActivityNotFound() {
        BDDMockito.when(repository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                .isThrownBy(() -> service.deleteActivityById(99L))
                .withMessage("Activity not found");

        BDDMockito.verify(repository, BDDMockito.never()).delete(any(Activity.class));
    }
}