package com.example.sr.srMapper;

import com.example.sr.domain.Activity;
import com.example.sr.dto.request.ActivityRequest;
import com.example.sr.dto.response.ActivityResponse;
import com.example.sr.dto.response.CoordinateDTO;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Activity toRequest(ActivityRequest request);

    @Mapping(source = "sports.name", target = "sportsName")
    @Mapping(target = "pace", expression = "java(calculatePace(activity.getDuration(), activity.getDistance()))")
    ActivityResponse toResponse(Activity activity);

    default String calculatePace(Integer durationInSeconds, Double distanceInKm) {
        if (durationInSeconds == null || distanceInKm == null || distanceInKm <= 0) {
            return "00:00";
        }

        int paceInSeconds = (int) (durationInSeconds / distanceInKm);

        int minutes = paceInSeconds / 60;

        int seconds = paceInSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    default LineString mapToLineString(List<CoordinateDTO> route) {
        if (route == null || route.isEmpty()) {
            return null;
        }

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate[] coordinates = route.stream()
            .map(c -> new Coordinate(c.lng(), c.lat()))
            .toArray(Coordinate[]::new);

        return geometryFactory.createLineString(coordinates);
    }


    default List<CoordinateDTO> mapToCoordinateDTOList(LineString route) {
        if (route == null) {
            return new ArrayList<>();
        }

        List<CoordinateDTO> dtoList = new ArrayList<>();
        for (Coordinate c : route.getCoordinates()) {
            dtoList.add(new CoordinateDTO(c.y, c.x)); // Desinversão: Lat, Lng
        }
        return dtoList;
    }
}
