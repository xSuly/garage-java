package com.cars.garage.controller;

import com.cars.garage.dto.MaintenanceLogDTO;
import com.cars.garage.entity.MaintenanceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cars.garage.service.MaintenanceLogService;

import java.util.List;

@RestController
@RequestMapping("/maintenance-logs")
public class MaintenanceLogController {

    private final MaintenanceLogService maintenanceLogService;

    @Autowired
    public MaintenanceLogController(MaintenanceLogService maintenanceLogService) {
        this.maintenanceLogService = maintenanceLogService;
    }

    @PostMapping
    public ResponseEntity<MaintenanceLog> createMaintenanceLog(@RequestBody MaintenanceLogDTO maintenanceLogDTO) {
        MaintenanceLog savedLog = maintenanceLogService.saveMaintenanceLog(maintenanceLogDTO);
        return new ResponseEntity<>(savedLog, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceLog> getMaintenanceLogById(@PathVariable Integer id) {
        MaintenanceLog maintenanceLog = maintenanceLogService.getMaintenanceLogById(id);
        return ResponseEntity.ok(maintenanceLog);
    }

    @GetMapping
    public List<MaintenanceLog> getAllMaintenanceLogs() {
        return maintenanceLogService.getAllMaintenanceLogs();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceLog> updateMaintenanceLog(@PathVariable Integer id, @RequestBody MaintenanceLogDTO maintenanceLogDTO) {
        MaintenanceLog updatedLog = maintenanceLogService.updateMaintenanceLog(id, maintenanceLogDTO);
        return ResponseEntity.ok(updatedLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceLog(@PathVariable Integer id) {
        maintenanceLogService.deleteMaintenanceLog(id);
        return ResponseEntity.noContent().build();
    }
}


