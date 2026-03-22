package com.example.sr.srMapper;

import com.example.sr.domain.User;
import com.example.sr.dto.request.UserRequest;
import com.example.sr.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toRequest(UserRequest request);

    UserResponse toResponse(User user);
}
