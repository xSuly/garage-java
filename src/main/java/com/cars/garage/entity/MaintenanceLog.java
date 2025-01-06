package com.cars.garage.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class MaintenanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @NotNull(message = "Vehicle cannot be null")
    private Vehicle vehicle;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    @NotNull(message = "Cost cannot be null")
    @Positive(message = "Cost must be positive")
    private Double cost;

    @FutureOrPresent(message = "Next scheduled date must be today or in the future")
    private LocalDate nextScheduledDate;

    public MaintenanceLog() {
    }

    public MaintenanceLog(Integer id, Vehicle vehicle, LocalDate date, String description, Double cost, LocalDate nextScheduledDate) {
        this.id = id;
        this.vehicle = vehicle;
        this.date = date;
        this.description = description;
        this.cost = cost;
        this.nextScheduledDate = nextScheduledDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public LocalDate getNextScheduledDate() {
        return nextScheduledDate;
    }

    public void setNextScheduledDate(LocalDate nextScheduledDate) {
        this.nextScheduledDate = nextScheduledDate;
    }
}
