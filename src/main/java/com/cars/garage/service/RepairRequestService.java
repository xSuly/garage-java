package com.cars.garage.service;

import com.cars.garage.dto.RepairRequestDTO;
import com.cars.garage.entity.RepairRequest;
import com.cars.garage.entity.SparePart;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cars.garage.repository.RepairRequestRepository;

import java.util.List;

@Service
public class RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;
    private final VehicleService vehicleService;
    private final SparePartService sparePartService;

    @Autowired
    public RepairRequestService(RepairRequestRepository repairRequestRepository, VehicleService vehicleService, SparePartService sparePartService) {
        this.repairRequestRepository = repairRequestRepository;
        this.vehicleService = vehicleService;
        this.sparePartService = sparePartService;
    }

    public RepairRequest saveRepairRequest(RepairRequestDTO repairRequestDTO) {
        Vehicle vehicle = vehicleService.getVehicleById(repairRequestDTO.getVehicleId());
        List<SparePart> spareParts = repairRequestDTO.getSparePartIds().stream()
                .map(sparePartService::getSparePartById)
                .toList();

        RepairRequest repairRequest = new RepairRequest();
        repairRequest.setVehicle(vehicle);
        repairRequest.setIssueDescription(repairRequestDTO.getIssueDescription());
        repairRequest.setRequestDate(repairRequestDTO.getRequestDate());
        repairRequest.setStatus(repairRequestDTO.getStatus());
        repairRequest.setTotalCost(repairRequestDTO.getTotalCost());
        repairRequest.setSpareParts(spareParts);

        return repairRequestRepository.save(repairRequest);
    }

    public RepairRequest getRepairRequestById(Integer id) {
        return repairRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RepairRequest not found with id: " + id));
    }

    public List<RepairRequest> getAllRepairRequests() {
        return repairRequestRepository.findAll();
    }

    public void deleteRepairRequest(Integer id) {
        if (!repairRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("RepairRequest not found with id: " + id);
        }
        repairRequestRepository.deleteById(id);
    }

    public RepairRequest updateRepairRequest(Integer id, RepairRequestDTO repairRequestDTO) {
        RepairRequest existingRepairRequest = repairRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RepairRequest not found with id: " + id));

        Vehicle vehicle = vehicleService.getVehicleById(repairRequestDTO.getVehicleId());

        List<SparePart> spareParts = repairRequestDTO.getSparePartIds().stream()
                .map(sparePartService::getSparePartById)
                .toList();

        existingRepairRequest.setVehicle(vehicle);
        existingRepairRequest.setIssueDescription(repairRequestDTO.getIssueDescription());
        existingRepairRequest.setRequestDate(repairRequestDTO.getRequestDate());
        existingRepairRequest.setStatus(repairRequestDTO.getStatus());
        existingRepairRequest.setTotalCost(repairRequestDTO.getTotalCost());

        existingRepairRequest.getSpareParts().clear();
        existingRepairRequest.getSpareParts().addAll(spareParts);

        return repairRequestRepository.save(existingRepairRequest);
    }
}

