package com.example.sr.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;

import java.time.LocalDate;


@Getter @Setter
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double distance;
    private LocalDate date;
    private Integer duration;

    @Column(columnDefinition = "geometry(LineString, 4326)")
    private LineString route;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sports sports;


}
