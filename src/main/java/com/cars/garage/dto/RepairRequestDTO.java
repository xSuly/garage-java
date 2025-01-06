package com.cars.garage.dto;

import java.time.LocalDate;
import java.util.List;

public class RepairRequestDTO {

    private Integer vehicleId;
    private String issueDescription;
    private LocalDate requestDate;
    private String status;
    private Double totalCost;
    private List<Integer> sparePartIds;

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
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

    public List<Integer> getSparePartIds() {
        return sparePartIds;
    }

    public void setSparePartIds(List<Integer> sparePartIds) {
        this.sparePartIds = sparePartIds;
    }
}

