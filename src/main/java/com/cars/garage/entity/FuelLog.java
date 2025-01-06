package com.cars.garage.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class FuelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @NotNull(message = "Vehicle cannot be null") // Validare pentru vehicle
    private Vehicle vehicle;

    @NotNull(message = "Date cannot be null") // Validare pentru date
    private LocalDate date;

    @NotNull(message = "Fuel added liters cannot be null") // Validare pentru fuelAddedLiters
    @Positive(message = "Fuel added liters must be positive") // Validare nr pozitiv
    private Double fuelAddedLiters;

    @NotNull(message = "Cost per liter cannot be null") // Validare pentru costPerLiter
    @Positive(message = "Cost per liter must be positive") // Validare nr pozitiv
    private Double costPerLiter;

    @NotNull(message = "Total cost cannot be null") // Validare pentru totalCost
    @Positive(message = "Total cost must be positive") // Validare nr pozitiv
    private Double totalCost;

    public FuelLog() {
    }

    public FuelLog(Integer id, Vehicle vehicle, LocalDate date, Double fuelAddedLiters, Double costPerLiter, Double totalCost) {
        this.id = id;
        this.vehicle = vehicle;
        this.date = date;
        this.fuelAddedLiters = fuelAddedLiters;
        this.costPerLiter = costPerLiter;
        this.totalCost = totalCost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
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
