package com.cars.garage.controller;

import com.cars.garage.dto.RepairRequestDTO;
import com.cars.garage.entity.RepairRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cars.garage.service.RepairRequestService;

import java.util.List;

@RestController
@RequestMapping("/repair-requests")
public class RepairRequestController {

    private final RepairRequestService repairRequestService;

    @Autowired
    public RepairRequestController(RepairRequestService repairRequestService) {
        this.repairRequestService = repairRequestService;
    }

    @PostMapping
    public ResponseEntity<RepairRequest> createRepairRequest(@RequestBody RepairRequestDTO repairRequestDTO) {
        RepairRequest savedRequest = repairRequestService.saveRepairRequest(repairRequestDTO);
        return new ResponseEntity<>(savedRequest, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairRequest> getRepairRequestById(@PathVariable Integer id) {
        RepairRequest repairRequest = repairRequestService.getRepairRequestById(id);
        return ResponseEntity.ok(repairRequest);
    }

    @GetMapping
    public List<RepairRequest> getAllRepairRequests() {
        return repairRequestService.getAllRepairRequests();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairRequest> updateRepairRequest(@PathVariable Integer id, @RequestBody RepairRequestDTO repairRequestDTO) {
        RepairRequest updatedRequest = repairRequestService.updateRepairRequest(id, repairRequestDTO);
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepairRequest(@PathVariable Integer id) {
        repairRequestService.deleteRepairRequest(id);
        return ResponseEntity.noContent().build();
    }
}


