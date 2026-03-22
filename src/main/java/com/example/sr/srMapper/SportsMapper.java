package com.example.sr.srMapper;

import com.example.sr.domain.Sports;
import com.example.sr.dto.request.SportsRequest;
import com.example.sr.dto.response.SportsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SportsMapper {
    Sports toSports(SportsRequest request);
    SportsResponse toResponse(Sports sports);
}