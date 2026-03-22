package com.example.sr.service;

import com.example.sr.domain.Sports;
import com.example.sr.dto.request.SportsRequest;
import com.example.sr.dto.response.SportsResponse;
import com.example.sr.repository.SportsRepository;
import com.example.sr.srMapper.SportsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SportsService {

    private final SportsRepository repository;
    private final SportsMapper mapper;

    public SportsResponse createSports(SportsRequest request) {
        Sports sports = mapper.toSports(request);
        Sports savedSports = repository.save(sports);
        return mapper.toResponse(savedSports);
    }

    public List<SportsResponse> findAllSports() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}