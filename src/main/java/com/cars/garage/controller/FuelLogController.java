package com.cars.garage.controller;

import com.cars.garage.dto.FuelLogDTO;
import com.cars.garage.entity.FuelLog;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.service.FuelLogService;
import com.cars.garage.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuel-logs")
public class FuelLogController {

    private final FuelLogService fuelLogService;
    private final VehicleService vehicleService;

    @Autowired
    public FuelLogController(FuelLogService fuelLogService, VehicleService vehicleService) {
        this.fuelLogService = fuelLogService;
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<FuelLog> createFuelLog(@RequestBody FuelLogDTO fuelLogDTO) {
        Vehicle vehicle = vehicleService.getVehicleById(fuelLogDTO.getVehicleId());
        if (vehicle == null) {
            return ResponseEntity.badRequest().build();
        }

        FuelLog fuelLog = new FuelLog();
        fuelLog.setVehicle(vehicle);
        fuelLog.setDate(fuelLogDTO.getDate());
        fuelLog.setFuelAddedLiters(fuelLogDTO.getFuelAddedLiters());
        fuelLog.setCostPerLiter(fuelLogDTO.getCostPerLiter());
        fuelLog.setTotalCost(fuelLogDTO.getTotalCost());

        FuelLog savedLog = fuelLogService.saveFuelLog(fuelLog);
        return new ResponseEntity<>(savedLog, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelLog> updateFuelLog(@PathVariable Integer id, @RequestBody FuelLogDTO fuelLogDTO) {
        Vehicle vehicle = vehicleService.getVehicleById(fuelLogDTO.getVehicleId());
        if (vehicle == null) {
            return ResponseEntity.badRequest().build();
        }

        FuelLog fuelLog = new FuelLog();
        fuelLog.setVehicle(vehicle);
        fuelLog.setDate(fuelLogDTO.getDate());
        fuelLog.setFuelAddedLiters(fuelLogDTO.getFuelAddedLiters());
        fuelLog.setCostPerLiter(fuelLogDTO.getCostPerLiter());
        fuelLog.setTotalCost(fuelLogDTO.getTotalCost());

        FuelLog updatedLog = fuelLogService.updateFuelLog(id, fuelLog);
        return updatedLog != null ? ResponseEntity.ok(updatedLog) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelLog> getFuelLogById(@PathVariable Integer id) {
        FuelLog fuelLog = fuelLogService.getFuelLogById(id);
        return fuelLog != null ? ResponseEntity.ok(fuelLog) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<FuelLog>> getAllFuelLogs() {
        List<FuelLog> fuelLogs = fuelLogService.getAllFuelLogs();
        return ResponseEntity.ok(fuelLogs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuelLog(@PathVariable Integer id) {
        fuelLogService.deleteFuelLog(id);
        return ResponseEntity.noContent().build();
    }
}