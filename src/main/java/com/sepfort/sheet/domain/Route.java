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

    /** @noinspection checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public Integer getId() {
        return id;
    }

    /** @noinspection checkstyle:HiddenField, checkstyle:FinalParameters, checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @noinspection checkstyle:MethodName, checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public String getDeparture_point() {
        return departurePoint;
    }

    /** @noinspection checkstyle:ParameterName, checkstyle:FinalParameters, checkstyle:MethodName, checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public void setDeparture_point(String departure_point) {
        this.departurePoint = departure_point;
    }

    /** @noinspection checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public String getDestination() {
        return destinationPoint;
    }

    /** @noinspection checkstyle:FinalParameters, checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public void setDestination(String destination) {
        this.destinationPoint = destination;
    }

    /** @noinspection checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public Short getDistance() {
        return distance;
    }

    /** @noinspection checkstyle:HiddenField, checkstyle:FinalParameters, checkstyle:DesignForExtension, checkstyle:MissingJavadocMethod */
    public void setDistance(Short distance) {
        this.distance = distance;
    }

    //</editor-fold>
}
