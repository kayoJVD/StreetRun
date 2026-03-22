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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ActivityService {
    private ActivtyRepository repository;
    private UserRepository userRepository;
    private SportsRepository sportsRepository;
    private ActivityMapper mapper;

    public ActivityResponse registerActivity(ActivityRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessRuleException("User not found"));

        Sports sports = sportsRepository.findById(request.sportsId())
                .orElseThrow(() -> new BusinessRuleException("Sport not found"));

        Activity activity = mapper.toRequest(request);

        activity.setUser(user);
        activity.setSports(sports);

        Activity savedActivity = repository.save(activity);

        return mapper.toResponse(savedActivity);

    }

    public List<ActivityResponse> listActivitiesByUser(Long id) {
        List<Activity> activities = repository.findAllByUserId(id);

        return activities.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse searchActivityById(Long id) {
        Activity activity = repository.findById(id).orElseThrow(() -> new BusinessRuleException("Activity not found"));

        return mapper.toResponse(activity);
    }

    public void deleteActivityById(Long id) {
        Activity activity = repository.findById(id).orElseThrow(() -> new BusinessRuleException("Activity not found"));
        repository.delete(activity);
    }
}

