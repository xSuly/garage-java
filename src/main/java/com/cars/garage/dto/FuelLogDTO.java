package com.cars.garage.dto;

import java.time.LocalDate;

public class FuelLogDTO {
    private Integer vehicleId;
    private LocalDate date;
    private Double fuelAddedLiters;
    private Double costPerLiter;
    private Double totalCost;

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getFuelAddedLiters() {
        return fuelAddedLiters;
    }

    public void setFuelAddedLiters(Double fuelAddedLiters) {
        this.fuelAddedLiters = fuelAddedLiters;
    }

    public Double getCostPerLiter() {
        return costPerLiter;
    }

    public void setCostPerLiter(Double costPerLiter) {
        this.costPerLiter = costPerLiter;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}

