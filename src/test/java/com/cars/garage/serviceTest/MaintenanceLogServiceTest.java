package com.cars.garage.serviceTest;

import com.cars.garage.dto.MaintenanceLogDTO;
import com.cars.garage.entity.MaintenanceLog;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.exception.ResourceNotFoundException;
import com.cars.garage.repository.MaintenanceLogRepository;
import com.cars.garage.service.MaintenanceLogService;
import com.cars.garage.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaintenanceLogServiceTest {

    @InjectMocks
    private MaintenanceLogService maintenanceLogService;

    @Mock
    private MaintenanceLogRepository maintenanceLogRepository;

    @Mock
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMaintenanceLogs() {
        MaintenanceLog log1 = new MaintenanceLog(1, null, LocalDate.now(), "Oil change", 50.0, LocalDate.now().plusMonths(6));
        MaintenanceLog log2 = new MaintenanceLog(2, null, LocalDate.now(), "Tire rotation", 30.0, LocalDate.now().plusMonths(6));

        when(maintenanceLogRepository.findAll()).thenReturn(Arrays.asList(log1, log2));

        var logs = maintenanceLogService.getAllMaintenanceLogs();
        assertEquals(2, logs.size());
        verify(maintenanceLogRepository, times(1)).findAll();
    }

    @Test
    void getMaintenanceLogById() {
        MaintenanceLog log = new MaintenanceLog(1, null, LocalDate.now(), "Oil change", 50.0, LocalDate.now().plusMonths(6));

        when(maintenanceLogRepository.findById(1)).thenReturn(Optional.of(log));

        MaintenanceLog result = maintenanceLogService.getMaintenanceLogById(1);
        assertNotNull(result);
        assertEquals("Oil change", result.getDescription());
        verify(maintenanceLogRepository, times(1)).findById(1);
    }

    @Test
    void getMaintenanceLogByIdNotFound() {
        when(maintenanceLogRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            maintenanceLogService.getMaintenanceLogById(99);
        });

        assertEquals("MaintenanceLog not found with id: 99", exception.getMessage());
        verify(maintenanceLogRepository, times(1)).findById(99);
    }

    @Test
    void saveMaintenanceLog() {
        MaintenanceLogDTO logDTO = new MaintenanceLogDTO();
        logDTO.setVehicleId(1);
        logDTO.setDate(LocalDate.now());
        logDTO.setDescription("Brake check");
        logDTO.setCost(60.0);
        logDTO.setNextScheduledDate(LocalDate.now().plusMonths(12));

        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Active", null, null, null, null);

        MaintenanceLog log = new MaintenanceLog(null, vehicle, LocalDate.now(), "Brake check", 60.0, LocalDate.now().plusMonths(12));

        when(vehicleService.getVehicleById(1)).thenReturn(vehicle);
        when(maintenanceLogRepository.save(any(MaintenanceLog.class))).thenReturn(log);

        MaintenanceLog result = maintenanceLogService.saveMaintenanceLog(logDTO);

        assertNotNull(result);
        assertEquals("Brake check", result.getDescription());
        verify(vehicleService, times(1)).getVehicleById(1);
        verify(maintenanceLogRepository, times(1)).save(any(MaintenanceLog.class));
    }

    @Test
    void updateMaintenanceLog() {
        MaintenanceLogDTO logDTO = new MaintenanceLogDTO();
        logDTO.setVehicleId(1);
        logDTO.setDate(LocalDate.now());
        logDTO.setDescription("Updated oil change");
        logDTO.setCost(55.0);
        logDTO.setNextScheduledDate(LocalDate.now().plusMonths(12));

        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Active", null, null, null, null);

        MaintenanceLog existingLog = new MaintenanceLog(1, vehicle, LocalDate.now(), "Oil change", 50.0, LocalDate.now().plusMonths(6));
        MaintenanceLog updatedLog = new MaintenanceLog(1, vehicle, LocalDate.now(), "Updated oil change", 55.0, LocalDate.now().plusMonths(12));

        when(maintenanceLogRepository.findById(1)).thenReturn(Optional.of(existingLog));
        when(vehicleService.getVehicleById(1)).thenReturn(vehicle);
        when(maintenanceLogRepository.save(existingLog)).thenReturn(updatedLog);

        MaintenanceLog result = maintenanceLogService.updateMaintenanceLog(1, logDTO);

        assertEquals("Updated oil change", result.getDescription());
        assertEquals(55.0, result.getCost());
        verify(maintenanceLogRepository, times(1)).save(existingLog);
    }

    @Test
    void deleteMaintenanceLog() {
        when(maintenanceLogRepository.existsById(1)).thenReturn(true);

        doNothing().when(maintenanceLogRepository).deleteById(1);

        maintenanceLogService.deleteMaintenanceLog(1);

        verify(maintenanceLogRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteMaintenanceLogNotFound() {
        when(maintenanceLogRepository.existsById(99)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            maintenanceLogService.deleteMaintenanceLog(99);
        });

        assertEquals("MaintenanceLog not found with id: 99", exception.getMessage());
        verify(maintenanceLogRepository, times(1)).existsById(99);
    }
}
