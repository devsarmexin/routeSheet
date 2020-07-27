package com.sepfort.sheet.dto;

public class RouteSheetDto {
    private String tripDate;
    private Integer waybillNumber;
    private Double fuelStart;
    private Double fuelFinish;
    private Integer mileageStart;
    private Integer mileageFinish;
    private Short fueling;
    private Double consumptionNorm;
    private Double consumptionFact;
    private Double saving;
    private Integer distance;

    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public Integer getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(Integer waybillNumber) {
        this.waybillNumber = waybillNumber;
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

    public Integer getMileageStart() {
        return mileageStart;
    }

    public void setMileageStart(Integer mileageStart) {
        this.mileageStart = mileageStart;
    }

    public Integer getMileageFinish() {
        return mileageFinish;
    }

    public void setMileageFinish(Integer mileageFinish) {
        this.mileageFinish = mileageFinish;
    }

    public Short getFueling() {
        return fueling;
    }

    public void setFueling(Short fueling) {
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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    //</editor-fold>
}
