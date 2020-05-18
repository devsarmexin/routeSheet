package com.sepfort.sheet.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "route_sheet")
public class RouteSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "waybill_number")
    private Long waybillNumber;

    @Column(name = "trip_date")
    private LocalDate tripDate;

    @Column(name = "fuel_start")
    private Double fuelStart;

    @Column(name = "fuel_finish")
    private Double fuelFinish;

    @Column(name = "mileage_start")
    private Long mileageStart;

    @Column(name = "mileage_finish")
    private Long mileageFinish;

    @Column(name = "fueling")
    private Long fueling;

    @Column(name = "consumption_norm")
    private Double consumptionNorm;

    @Column(name = "consumption_fact")
    private Double consumptionFact;

    @Column(name = "saving")
    private Double saving;

    @Column(name = "distance")
    private Long distance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usr_id")
    private User author;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_sheet_id")
    private List<Route> routes;

    public RouteSheet() {
    }

    public RouteSheet(LocalDate tripDate, Long fueling, Long distance, List<Route> routes) {
        this.tripDate = tripDate;
        this.fueling = fueling;
        this.distance = distance;
        this.routes = routes;
    }

    public RouteSheet(LocalDate tripDate, Long waybillNumber, Double fuelStart, Double fuelFinish, Long mileageStart, Long mileageFinish, Long fueling, Double consumptionNorm, Double consumptionFact, Double saving, Long distance, List<Route> routes
    ) {
        this.tripDate = tripDate;
        this.waybillNumber = waybillNumber;
        this.fuelStart = fuelStart;
        this.fuelFinish = fuelFinish;
        this.mileageStart = mileageStart;
        this.mileageFinish = mileageFinish;
        this.fueling = fueling;
        this.consumptionNorm = consumptionNorm;
        this.consumptionFact = consumptionFact;
        this.saving = saving;
        this.distance = distance;
        this.routes = routes;
    }

    public RouteSheet(LocalDate tripDate, Long waybillNumber, Double fuelStart, Double fuelFinish, Long mileageStart, Long mileageFinish, Long fueling, Double consumptionNorm, Double consumptionFact, Double saving) {
        this.tripDate = tripDate;
        this.waybillNumber = waybillNumber;
        this.fuelStart = fuelStart;
        this.fuelFinish = fuelFinish;
        this.mileageStart = mileageStart;
        this.mileageFinish = mileageFinish;
        this.fueling = fueling;
        this.consumptionNorm = consumptionNorm;
        this.consumptionFact = consumptionFact;
        this.saving = saving;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTripDate() {
        return tripDate;
    }

    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }

    public Double getFuelStart() {
        return fuelStart;
    }

    public void setFuelStart(Double fuelStart) {
        this.fuelStart = fuelStart;
    }

    public Double getFuelFinish() {
        return fuelFinish;
    }

    public void setFuelFinish(Double fuelFinish) {
        this.fuelFinish = fuelFinish;
    }

    public Long getMileageStart() {
        return mileageStart;
    }

    public void setMileageStart(Long mileageStart) {
        this.mileageStart = mileageStart;
    }

    public Long getMileageFinish() {
        return mileageFinish;
    }

    public void setMileageFinish(Long mileageFinish) {
        this.mileageFinish = mileageFinish;
    }

    public Long getFueling() {
        return fueling;
    }

    public void setFueling(Long fueling) {
        this.fueling = fueling;
    }

    public Double getConsumptionNorm() {
        return consumptionNorm;
    }

    public void setConsumptionNorm(Double consumptionNorm) {
        this.consumptionNorm = consumptionNorm;
    }

    public Double getConsumptionFact() {
        return consumptionFact;
    }

    public void setConsumptionFact(Double consumptionFact) {
        this.consumptionFact = consumptionFact;
    }

    public Double getSaving() {
        return saving;
    }

    public void setSaving(Double saving) {
        this.saving = saving;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public Long getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(Long number) {
        this.waybillNumber = number;
    }
}
