package com.example.sr.service;

import com.example.sr.domain.Activity;
import com.example.sr.domain.Sports;
import com.example.sr.domain.User;
import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import com.example.sr.dto.response.DashboardResponse;
import com.example.sr.dto.response.PersonalBestsResponse;
import com.example.sr.exception.BusinessRuleException;
import com.example.sr.repository.ActivityRepository;
import com.example.sr.repository.SportsRepository;
import com.example.sr.repository.UserRepository;
import com.example.sr.srMapper.ActivityMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class ActivityService {
    private ActivityRepository repository;
    private UserRepository userRepository;
    private SportsRepository sportsRepository;
    private ActivityMapper mapper;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public ActivityResponse registerActivity(ActivityRequest request, Long authenticatedUserId) {
        User user = userRepository.findById(authenticatedUserId)
            .orElseThrow(() -> new BusinessRuleException("User not found"));

        Sports sports = sportsRepository.findById(request.sportsId())
            .orElseThrow(() -> new BusinessRuleException("Sport not found"));


        Activity activity = mapper.toRequest(request);
        activity.setUser(user);
        activity.setSports(sports);


        if (request.route() != null && !request.route().isEmpty()) {
            Coordinate[] coordinates = request.route().stream()
                .map(c -> new Coordinate(c.lng(), c.lat()))
                .toArray(Coordinate[]::new);

            LineString lineString = geometryFactory.createLineString(coordinates);
            activity.setRoute(lineString);


            Double trueDistance = calculateDistanceInKm(lineString);
            activity.setDistance(trueDistance);
        }

        Activity savedActivity = repository.save(activity);

        String calculatedPace = calculatePace(savedActivity.getDuration(), savedActivity.getDistance());

        return new ActivityResponse(
            savedActivity.getId(),
            savedActivity.getDistance(),
            savedActivity.getDate(),
            savedActivity.getDuration(),
            savedActivity.getSports().getName(),
            calculatedPace,
            request.route()
        );
    }


    public Page<ActivityResponse> listActivitiesByUser(Long authenticatedUserId, Pageable pageable) {
        Page<Activity> activitiesPage = repository.findAllByUserId(authenticatedUserId, pageable);

        return activitiesPage.map(mapper::toResponse);
    }



    public ActivityResponse searchActivityById(Long id, Long authenticatedUserId) {
        Activity activity = repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Activity not found"));

        if (!activity.getUser().getId().equals(authenticatedUserId)) {
            throw new BusinessRuleException("Acesso negado: Esta corrida não pertence a você.");
        }

        return mapper.toResponse(activity);
    }

    public void deleteActivityById(Long id, Long authenticatedUserId) {
        Activity activity = repository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Activity not found"));

        if (!activity.getUser().getId().equals(authenticatedUserId)) {
            throw new BusinessRuleException("Acesso negado: Você não pode deletar a corrida de outro usuário.");
        }

        repository.delete(activity);
    }

    private Double calculateDistanceInKm(LineString route) {
        if (route == null || route.isEmpty()) {
            return 0.0;
        }

        double distanceInDegrees = route.getLength();
        double distanceInKm = distanceInDegrees * 111.32;

        return Math.round(distanceInKm * 100.0) / 100.0;
    }


    private String calculatePace(Integer durationInSeconds, Double distanceInKm) {
        if (durationInSeconds == null || durationInSeconds == 0 || distanceInKm == null || distanceInKm <= 0) {
            return "00:00 /km";
        }

        double totalMinutes = durationInSeconds / 60.0;
        double decimalPace = totalMinutes / distanceInKm;

        int paceMinutes = (int) decimalPace;

        int paceSeconds = (int) Math.round((decimalPace - paceMinutes) * 60);

        if (paceSeconds == 60) {
            paceMinutes++;
            paceSeconds = 0;
        }

        return String.format("%02d:%02d /km", paceMinutes, paceSeconds);
    }

    public DashboardResponse getDashboard(Long authenticatedUserId) {
        Double totalDist = repository.sumTotalDistanceByUserId(authenticatedUserId);
        Long totalActs = repository.countTotalActivitiesByUserId(authenticatedUserId);
        Double monthlyDist = repository.sumMonthlyDistanceByUserId(authenticatedUserId);
        Integer totalSecs = repository.sumTotalDurationByUserId(authenticatedUserId);

        totalDist = totalDist != null ? Math.round(totalDist * 100.0) / 100.0 : 0.0;
        totalActs = totalActs != null ? totalActs : 0L;
        monthlyDist = monthlyDist != null ? Math.round(monthlyDist * 100.0) / 100.0 : 0.0;


        String avgPace = calculatePace(totalSecs, totalDist);

        return new DashboardResponse(totalDist, totalActs, monthlyDist, avgPace);
    }

    public PersonalBestsResponse getPersonalBests(Long authenticatedUserId) {

        ActivityResponse longestRunResponse = repository.findTopByUserIdOrderByDistanceDesc(authenticatedUserId)
            .map(this::buildActivityResponseWithCalculatedPace)
            .orElse(null);


        Page<Activity> fastestRunPage = repository.findFastestRunByUserId(authenticatedUserId, PageRequest.of(0, 1));
        ActivityResponse fastestRunResponse = fastestRunPage.isEmpty() ? null :
            buildActivityResponseWithCalculatedPace(fastestRunPage.getContent().get(0));

        return new PersonalBestsResponse(longestRunResponse, fastestRunResponse);
    }


    private ActivityResponse buildActivityResponseWithCalculatedPace(Activity activity) {
        String calculatedPace = calculatePace(activity.getDuration(), activity.getDistance());
        return new ActivityResponse(
            activity.getId(),
            activity.getDistance(),
            activity.getDate(),
            activity.getDuration(),
            activity.getSports().getName(),
            calculatedPace,
            null
        );
    }
}
