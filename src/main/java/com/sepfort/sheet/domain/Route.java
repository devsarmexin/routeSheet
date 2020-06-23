package com.sepfort.sheet.domain;

import lombok.NonNull;

import javax.persistence.*;

/** @noinspection checkstyle:LineLength*/
@Entity
@Table(name = "route")
public class Route {
    /** @noinspection checkstyle:JavadocVariable*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /** @noinspection checkstyle:JavadocVariable*/
    @NonNull
    private String departurePoint;
    /** @noinspection checkstyle:JavadocVariable*/
    @NonNull
    private String destinationPoint;
    /** @noinspection checkstyle:JavadocVariable*/
    @NonNull
    private Short distance;

    /** @noinspection checkstyle:MissingJavadocMethod*/
    public Route() {
    }

    /** @noinspection checkstyle:HiddenField, checkstyle:FinalParameters, checkstyle:HiddenField, checkstyle:FinalParameters, checkstyle:HiddenField, checkstyle:FinalParameters, checkstyle:MissingJavadocMethod */
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
