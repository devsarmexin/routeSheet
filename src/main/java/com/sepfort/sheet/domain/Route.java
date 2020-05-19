package com.sepfort.sheet.domain;

import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String departurePoint;
    @NonNull
    private String destinationPoint;
    @NonNull
    private Long distance;

    public Route() {
    }

    public Route(String departurePoint, String destinationPoint, Long distance) {
        this.departurePoint = departurePoint;
        this.destinationPoint = destinationPoint;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeparture_point() {
        return departurePoint;
    }

    public void setDeparture_point(String departure_point) {
        this.departurePoint = departure_point;
    }

    public String getDestination() {
        return destinationPoint;
    }

    public void setDestination(String destination) {
        this.destinationPoint = destination;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
