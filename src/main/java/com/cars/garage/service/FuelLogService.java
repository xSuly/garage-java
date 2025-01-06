package com.cars.garage.service;

import com.cars.garage.entity.FuelLog;
import com.cars.garage.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cars.garage.repository.FuelLogRepository;

import java.util.List;

@Service
public class FuelLogService {

    private final FuelLogRepository fuelLogRepository;

    @Autowired
    public FuelLogService(FuelLogRepository fuelLogRepository) {
        this.fuelLogRepository = fuelLogRepository;
    }

    public FuelLog saveFuelLog(FuelLog fuelLog) {
        return fuelLogRepository.save(fuelLog);
    }

    public FuelLog getFuelLogById(Integer id) {
        return fuelLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FuelLog not found with id: " + id));
    }

    public List<FuelLog> getAllFuelLogs() {
        return fuelLogRepository.findAll();
    }

    public void deleteFuelLog(Integer id) {
        if (!fuelLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("FuelLog not found with id: " + id);
        }
        fuelLogRepository.deleteById(id);
    }

    public FuelLog updateFuelLog(Integer id, FuelLog fuelLog) {
        FuelLog existingFuelLog = fuelLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FuelLog not found with id: " + id));

        existingFuelLog.setVehicle(fuelLog.getVehicle());
        existingFuelLog.setDate(fuelLog.getDate());
        existingFuelLog.setFuelAddedLiters(fuelLog.getFuelAddedLiters());
        existingFuelLog.setCostPerLiter(fuelLog.getCostPerLiter());
        existingFuelLog.setTotalCost(fuelLog.getTotalCost());

        return fuelLogRepository.save(existingFuelLog);
    }
}
