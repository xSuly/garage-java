package com.cars.garage.service;

import com.cars.garage.dto.MaintenanceLogDTO;
import com.cars.garage.entity.MaintenanceLog;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cars.garage.repository.MaintenanceLogRepository;

import java.util.List;

@Service
public class MaintenanceLogService {

    private final MaintenanceLogRepository maintenanceLogRepository;
    private final VehicleService vehicleService;

    @Autowired
    public MaintenanceLogService(MaintenanceLogRepository maintenanceLogRepository, VehicleService vehicleService) {
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.vehicleService = vehicleService;
    }

    public MaintenanceLog saveMaintenanceLog(MaintenanceLog maintenanceLog) {
        return maintenanceLogRepository.save(maintenanceLog);
    }

    public MaintenanceLog saveMaintenanceLog(MaintenanceLogDTO maintenanceLogDTO) {
        Vehicle vehicle = vehicleService.getVehicleById(maintenanceLogDTO.getVehicleId());

        MaintenanceLog maintenanceLog = new MaintenanceLog();
        maintenanceLog.setVehicle(vehicle);
        maintenanceLog.setDate(maintenanceLogDTO.getDate());
        maintenanceLog.setDescription(maintenanceLogDTO.getDescription());
        maintenanceLog.setCost(maintenanceLogDTO.getCost());
        maintenanceLog.setNextScheduledDate(maintenanceLogDTO.getNextScheduledDate());

        return maintenanceLogRepository.save(maintenanceLog);
    }

    public MaintenanceLog getMaintenanceLogById(Integer id) {
        return maintenanceLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaintenanceLog not found with id: " + id));
    }

    public List<MaintenanceLog> getAllMaintenanceLogs() {
        return maintenanceLogRepository.findAll();
    }

    public void deleteMaintenanceLog(Integer id) {
        if (!maintenanceLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("MaintenanceLog not found with id: " + id);
        }
        maintenanceLogRepository.deleteById(id);
    }

    public MaintenanceLog updateMaintenanceLog(Integer id, MaintenanceLogDTO maintenanceLogDTO) {
        MaintenanceLog existingMaintenanceLog = maintenanceLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaintenanceLog not found with id: " + id));

        Vehicle vehicle = vehicleService.getVehicleById(maintenanceLogDTO.getVehicleId());

        existingMaintenanceLog.setVehicle(vehicle);
        existingMaintenanceLog.setDate(maintenanceLogDTO.getDate());
        existingMaintenanceLog.setDescription(maintenanceLogDTO.getDescription());
        existingMaintenanceLog.setCost(maintenanceLogDTO.getCost());
        existingMaintenanceLog.setNextScheduledDate(maintenanceLogDTO.getNextScheduledDate());

        return maintenanceLogRepository.save(existingMaintenanceLog);
    }
}