package com.example.sr.service;

import com.example.sr.domain.Activity;
import com.example.sr.domain.Sports;
import com.example.sr.domain.User;
import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import com.example.sr.exception.BusinessRuleException;
import com.example.sr.repository.ActivityRepository;
import com.example.sr.repository.SportsRepository;
import com.example.sr.repository.UserRepository;
import com.example.sr.srMapper.ActivityMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ActivityService {
    private ActivityRepository repository;
    private UserRepository userRepository;
    private SportsRepository sportsRepository;
    private ActivityMapper mapper;

    public ActivityResponse registerActivity(ActivityRequest request, Long authenticatedUserId) {
        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new BusinessRuleException("User not found"));

        Sports sports = sportsRepository.findById(request.sportsId())
                .orElseThrow(() -> new BusinessRuleException("Sport not found"));

        Activity activity = mapper.toRequest(request);

        activity.setUser(user);
        activity.setSports(sports);

        Activity savedActivity = repository.save(activity);

        return mapper.toResponse(savedActivity);
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
}
