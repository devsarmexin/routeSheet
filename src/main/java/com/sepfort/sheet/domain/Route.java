package com.sepfort.sheet.domain;

import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NonNull
    private String departurePoint;
    @NonNull
    private String destinationPoint;
    @NonNull
    private Short distance;

    public Route() {
    }

    public Route(String departurePoint, String destinationPoint, Short distance) {
        this.departurePoint = departurePoint;
        this.destinationPoint = destinationPoint;
        this.distance = distance;
    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters">

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Short getDistance() {
        return distance;
    }

    public void setDistance(Short distance) {
        this.distance = distance;
    }

    //</editor-fold>
}
