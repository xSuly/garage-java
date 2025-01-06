package com.cars.garage.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class RepairRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @NotNull(message = "Vehicle cannot be null")
    private Vehicle vehicle;

    @Size(min = 10, max = 500, message = "Issue description must be between 10 and 500 characters")
    private String issueDescription;

    @NotNull(message = "Request date cannot be null")
    @PastOrPresent(message = "Request date cannot be in the future")
    private LocalDate requestDate;

    @NotNull(message = "Status cannot be null")
    @NotEmpty(message = "Status cannot be empty")
    private String status;

    @NotNull(message = "Total cost cannot be null")
    @Positive(message = "Total cost must be positive")
    private Double totalCost;

    @ManyToMany
    @JoinTable(
            name = "repair_request_spare_part", // Tabela intermediară
            joinColumns = @JoinColumn(name = "repair_request_id"),
            inverseJoinColumns = @JoinColumn(name = "spare_part_id")
    ) //relatia many-to-many rezolvata
    private List<SparePart> spareParts;

    public RepairRequest() {
    }

    public RepairRequest(Integer id, Vehicle vehicle, String issueDescription, LocalDate requestDate, String status, Double totalCost, List<SparePart> spareParts) {
        this.id = id;
        this.vehicle = vehicle;
        this.issueDescription = issueDescription;
        this.requestDate = requestDate;
        this.status = status;
        this.totalCost = totalCost;
        this.spareParts = spareParts;
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

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public List<SparePart> getSpareParts() {
        return spareParts;
    }

    public void setSpareParts(List<SparePart> spareParts) {
        this.spareParts = spareParts;
    }
}
