package com.sepfort.sheet.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "routesheet")
public class RouteSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long number;
    private LocalDate data;
    private Double fuelStart;
    private Double fuelFinish;
    private Long mileageStart;
    private Long mileageFinish;
    private Long fueling;
    private Double consumptionNorm;
    private Double consumptionFact;
    private Double saving;
    private Long distance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "routesheet_id")
    private List<Addresses> address;

    public RouteSheet() {
    }

//    public RouteSheet(LocalDate data, Long fueling, Long distance, List<Addresses> address) {
//        this.data = data;
//        this.fueling = fueling;
//        this.distance = distance;
//        this.address = address;
//    }

    public RouteSheet(LocalDate data, Long number, Double fuelStart, Double fuelFinish, Long mileageStart, Long mileageFinish, Long fueling, Double consumptionNorm, Double consumptionFact, Double saving, Long distance, List<Addresses> address
    ) {
        this.data = data;
        this.number = number;
        this.fuelStart = fuelStart;
        this.fuelFinish = fuelFinish;
        this.mileageStart = mileageStart;
        this.mileageFinish = mileageFinish;
        this.fueling = fueling;
        this.consumptionNorm = consumptionNorm;
        this.consumptionFact = consumptionFact;
        this.saving = saving;
        this.distance = distance;
        this.address = address;
    }

    public RouteSheet(LocalDate data, Long number, Double fuelStart, Double fuelFinish, Long mileageStart, Long mileageFinish, Long fueling, Double consumptionNorm, Double consumptionFact, Double saving) {
        this.data = data;
        this.number = number;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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

    public List<Addresses> getAddress() {
        return address;
    }

    public void setAddress(List<Addresses> address) {
        this.address = address;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
