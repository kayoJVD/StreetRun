package com.example.sr.service;

import com.example.sr.commons.ActivityCreator;
import com.example.sr.commons.SportsCreator;
import com.example.sr.commons.UserCreator;
import com.example.sr.domain.Activity;
import com.example.sr.domain.Sports;
import com.example.sr.domain.User;
import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import com.example.sr.dto.response.PersonalBestsResponse;
import com.example.sr.exception.BusinessRuleException;
import com.example.sr.repository.ActivityRepository;
import com.example.sr.repository.SportsRepository;
import com.example.sr.repository.UserRepository;
import com.example.sr.srMapper.ActivityMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @InjectMocks
    private ActivityService service;

    @Mock
    private ActivityRepository repository;

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


    private final Long loggedInUserId = 1L;

    @BeforeEach
    void setUp() {
        activity = ActivityCreator.createValidActivity();
        activityRequest = ActivityCreator.createValidActivityRequest();
        activityResponse = ActivityCreator.createValidActivityResponse();
        user = UserCreator.createValidUser();
        sports = SportsCreator.createValidSport();

        activity.setUser(user);
        user.setId(loggedInUserId);
    }

    @Test
    @DisplayName("registerActivity returns ActivityResponse with calculated Pace when successful")
    void registerActivity_ReturnsActivityResponse_WhenSuccessful() {

        BDDMockito.when(userRepository.findById(loggedInUserId)).thenReturn(Optional.of(user));
        BDDMockito.when(sportsRepository.findById(activityRequest.sportsId())).thenReturn(Optional.of(sports));
        BDDMockito.when(mapper.toRequest(activityRequest)).thenReturn(activity);

        BDDMockito.when(repository.save(ArgumentMatchers.any(Activity.class))).thenReturn(activity);


        ActivityResponse savedActivity = service.registerActivity(activityRequest, loggedInUserId);

        Assertions.assertThat(savedActivity).isNotNull();
        Assertions.assertThat(savedActivity.id()).isEqualTo(activity.getId());

        Assertions.assertThat(savedActivity.pace()).isNotNull();
        Assertions.assertThat(savedActivity.pace()).contains("/km");

        BDDMockito.verify(repository).save(ArgumentMatchers.any(Activity.class));
    }

    @Test
    @DisplayName("registerActivity throws BusinessRuleException when user is not found")
    void registerActivity_ThrowsException_WhenUserNotFound() {

        BDDMockito.when(userRepository.findById(loggedInUserId)).thenReturn(Optional.empty());


        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
            .isThrownBy(() -> service.registerActivity(activityRequest, loggedInUserId))
            .withMessage("User not found");


        BDDMockito.verify(sportsRepository, BDDMockito.never()).findById(ArgumentMatchers.any());
        BDDMockito.verify(repository, BDDMockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("registerActivity throws BusinessRuleException when sport is not found")
    void registerActivity_ThrowsException_WhenSportNotFound() {

        BDDMockito.when(userRepository.findById(loggedInUserId)).thenReturn(Optional.of(user));
        BDDMockito.when(sportsRepository.findById(activityRequest.sportsId())).thenReturn(Optional.empty());


        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
            .isThrownBy(() -> service.registerActivity(activityRequest, loggedInUserId))
            .withMessage("Sport not found");


        BDDMockito.verify(repository, BDDMockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("listActivitiesByUser returns a page of activities when successful")
    void listActivitiesByUser_ReturnsPageOfActivities_WhenSuccessful() {

        Pageable pageable = PageRequest.of(0, 10);


        Page<Activity> activityPage = new PageImpl<>(List.of(activity));

        BDDMockito.when(repository.findAllByUserId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class)))
            .thenReturn(activityPage);

        BDDMockito.when(mapper.toResponse(ArgumentMatchers.any(Activity.class))).thenReturn(activityResponse);

        Page<ActivityResponse> result = service.listActivitiesByUser(loggedInUserId, pageable);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("searchActivityById returns ActivityResponse when user is the owner")
    void searchActivityById_ReturnsActivityResponse_WhenUserIsTheOwner() {
        BDDMockito.when(repository.findById(activity.getId())).thenReturn(Optional.of(activity));
        BDDMockito.when(mapper.toResponse(activity)).thenReturn(activityResponse);

        ActivityResponse foundActivity = service.searchActivityById(activity.getId(), loggedInUserId);

        Assertions.assertThat(foundActivity).isNotNull();
    }

    @Test
    @DisplayName("searchActivityById throws BusinessRuleException when user is NOT the owner")
    void searchActivityById_ThrowsException_WhenUserIsNotTheOwner() {
        BDDMockito.when(repository.findById(activity.getId())).thenReturn(Optional.of(activity));

        Long hackerId = 99L;

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
            .isThrownBy(() -> service.searchActivityById(activity.getId(), hackerId))
            .withMessage("Acesso negado: Esta corrida não pertence a você.");
    }

    @Test
    @DisplayName("deleteActivityById removes activity when user is the owner")
    void deleteActivityById_RemovesActivity_WhenUserIsTheOwner() {
        BDDMockito.when(repository.findById(activity.getId())).thenReturn(Optional.of(activity));

        Assertions.assertThatNoException().isThrownBy(() -> service.deleteActivityById(activity.getId(), loggedInUserId));

        BDDMockito.verify(repository).delete(activity);
    }

    @Test
    @DisplayName("deleteActivityById throws BusinessRuleException when user is NOT the owner")
    void deleteActivityById_ThrowsException_WhenUserIsNotTheOwner() {
        BDDMockito.when(repository.findById(activity.getId())).thenReturn(Optional.of(activity));

        Long hackerId = 99L;

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
            .isThrownBy(() -> service.deleteActivityById(activity.getId(), hackerId))
            .withMessage("Acesso negado: Você não pode deletar a corrida de outro usuário.");

        BDDMockito.verify(repository, BDDMockito.never()).delete(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("getDashboard returns populated DashboardResponse when user has activities")
    void getDashboard_ReturnsPopulatedDashboardResponse_WhenSuccessful() {

        Double dbTotalDistance = 50.456;
        Long dbTotalActivities = 10L;
        Double dbMonthlyDistance = 20.111; //
        Integer dbTotalDuration = 18000; //

        BDDMockito.when(repository.sumTotalDistanceByUserId(loggedInUserId)).thenReturn(dbTotalDistance);
        BDDMockito.when(repository.countTotalActivitiesByUserId(loggedInUserId)).thenReturn(dbTotalActivities);
        BDDMockito.when(repository.sumMonthlyDistanceByUserId(loggedInUserId)).thenReturn(dbMonthlyDistance);
        BDDMockito.when(repository.sumTotalDurationByUserId(loggedInUserId)).thenReturn(dbTotalDuration);


        var dashboard = service.getDashboard(loggedInUserId);

        Assertions.assertThat(dashboard).isNotNull();
        Assertions.assertThat(dashboard.totalDistance()).isEqualTo(50.46);
        Assertions.assertThat(dashboard.totalActivities()).isEqualTo(10L);
        Assertions.assertThat(dashboard.monthlyDistance()).isEqualTo(20.11);
        Assertions.assertThat(dashboard.averagePace()).isNotNull();
        Assertions.assertThat(dashboard.averagePace()).contains("/km");
    }

    @Test
    @DisplayName("getDashboard returns zeroed DashboardResponse when user has NO activities")
    void getDashboard_ReturnsZeroedResponse_WhenUserHasNoActivities() {

        BDDMockito.when(repository.sumTotalDistanceByUserId(loggedInUserId)).thenReturn(null);
        BDDMockito.when(repository.countTotalActivitiesByUserId(loggedInUserId)).thenReturn(null);
        BDDMockito.when(repository.sumMonthlyDistanceByUserId(loggedInUserId)).thenReturn(null);
        BDDMockito.when(repository.sumTotalDurationByUserId(loggedInUserId)).thenReturn(null);


        var dashboard = service.getDashboard(loggedInUserId);

        Assertions.assertThat(dashboard).isNotNull();
        Assertions.assertThat(dashboard.totalDistance()).isEqualTo(0.0);
        Assertions.assertThat(dashboard.totalActivities()).isEqualTo(0L);
        Assertions.assertThat(dashboard.monthlyDistance()).isEqualTo(0.0);
        Assertions.assertThat(dashboard.averagePace()).isEqualTo("00:00 /km");
    }

    @Test
    @DisplayName("getPersonalBests returns trophies when user has activities")
    void getPersonalBests_ReturnsTrophies_WhenSuccessful() {

        Activity longest = ActivityCreator.createValidActivity();
        longest.setDistance(21.1);
        longest.setDuration(7200);

        Activity fastest = ActivityCreator.createValidActivity();
        fastest.setDistance(5.0);
        fastest.setDuration(1200);


        BDDMockito.when(repository.findTopByUserIdOrderByDistanceDesc(loggedInUserId))
            .thenReturn(Optional.of(longest));


        Page<Activity> fastestPage = new PageImpl<>(List.of(fastest));
        BDDMockito.when(repository.findFastestRunByUserId(ArgumentMatchers.eq(loggedInUserId), ArgumentMatchers.any()))
            .thenReturn(fastestPage);

        PersonalBestsResponse response = service.getPersonalBests(loggedInUserId);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.longestRun().distance()).isEqualTo(21.1);
        Assertions.assertThat(response.fastestRun().distance()).isEqualTo(5.0);
        Assertions.assertThat(response.fastestRun().pace()).isEqualTo("04:00 /km");
    }

    @Test
    @DisplayName("getPersonalBests returns empty trophies when user has no activities")
    void getPersonalBests_ReturnsEmptyResponse_WhenUserHasNoActivities() {

        BDDMockito.when(repository.findTopByUserIdOrderByDistanceDesc(loggedInUserId))
            .thenReturn(Optional.empty());

        BDDMockito.when(repository.findFastestRunByUserId(ArgumentMatchers.eq(loggedInUserId), ArgumentMatchers.any()))
            .thenReturn(Page.empty());

        PersonalBestsResponse response = service.getPersonalBests(loggedInUserId);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.longestRun()).isNull();
        Assertions.assertThat(response.fastestRun()).isNull();
    }
}
