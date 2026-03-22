package com.example.sr.srMapper;

import com.example.sr.domain.Activity;
import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Activity toRequest(ActivityRequest request);

    @Mapping(source = "sports.name", target = "sportsName")
    ActivityResponse toResponse(Activity Activity);
}
