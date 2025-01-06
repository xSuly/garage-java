package com.cars.garage.controllerTest;

import com.cars.garage.controller.MaintenanceLogController;
import com.cars.garage.dto.MaintenanceLogDTO;
import com.cars.garage.entity.MaintenanceLog;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.service.MaintenanceLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class MaintenanceLogControllerTest {

    @InjectMocks
    private MaintenanceLogController maintenanceLogController;

    @Mock
    private MaintenanceLogService maintenanceLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMaintenanceLogs() {
        MaintenanceLog log1 = new MaintenanceLog(1, null, LocalDate.now(), "Oil change", 50.0, LocalDate.now().plusMonths(6));
        MaintenanceLog log2 = new MaintenanceLog(2, null, LocalDate.now(), "Tire replacement", 100.0, LocalDate.now().plusMonths(12));
        when(maintenanceLogService.getAllMaintenanceLogs()).thenReturn(Arrays.asList(log1, log2));

        List<MaintenanceLog> logs = maintenanceLogController.getAllMaintenanceLogs();
        assertEquals(2, logs.size());
        verify(maintenanceLogService, times(1)).getAllMaintenanceLogs();
    }

    @Test
    void getMaintenanceLogById() {
        MaintenanceLog log = new MaintenanceLog(1, null, LocalDate.now(), "Oil change", 50.0, LocalDate.now().plusMonths(6));
        when(maintenanceLogService.getMaintenanceLogById(1)).thenReturn(log);

        ResponseEntity<MaintenanceLog> response = maintenanceLogController.getMaintenanceLogById(1);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Oil change", response.getBody().getDescription());
        verify(maintenanceLogService, times(1)).getMaintenanceLogById(1);
    }

    @Test
    void createMaintenanceLog() {
        MaintenanceLogDTO logDTO = new MaintenanceLogDTO();
        logDTO.setVehicleId(1);
        logDTO.setDate(LocalDate.now());
        logDTO.setDescription("Tire rotation");
        logDTO.setCost(30.0);
        logDTO.setNextScheduledDate(LocalDate.now().plusMonths(6));

        MaintenanceLog log = new MaintenanceLog(1, new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Active", null, null, null, null),
                LocalDate.now(), "Tire rotation", 30.0, LocalDate.now().plusMonths(6));
        when(maintenanceLogService.saveMaintenanceLog(logDTO)).thenReturn(log);

        ResponseEntity<MaintenanceLog> response = maintenanceLogController.createMaintenanceLog(logDTO);

        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Tire rotation", response.getBody().getDescription());
        verify(maintenanceLogService, times(1)).saveMaintenanceLog(logDTO);
    }

    @Test
    void updateMaintenanceLog() {
        MaintenanceLogDTO logDTO = new MaintenanceLogDTO();
        logDTO.setVehicleId(1);
        logDTO.setDate(LocalDate.now());
        logDTO.setDescription("Oil change updated");
        logDTO.setCost(60.0);
        logDTO.setNextScheduledDate(LocalDate.now().plusMonths(12));

        MaintenanceLog updatedLog = new MaintenanceLog(1, new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Active", null, null, null, null),
                LocalDate.now(), "Oil change updated", 60.0, LocalDate.now().plusMonths(12));

        when(maintenanceLogService.updateMaintenanceLog(1, logDTO)).thenReturn(updatedLog);

        ResponseEntity<MaintenanceLog> response = maintenanceLogController.updateMaintenanceLog(1, logDTO);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Oil change updated", response.getBody().getDescription());
        verify(maintenanceLogService, times(1)).updateMaintenanceLog(1, logDTO);
    }

    @Test
    void deleteMaintenanceLog() {
        doNothing().when(maintenanceLogService).deleteMaintenanceLog(1);

        ResponseEntity<Void> response = maintenanceLogController.deleteMaintenanceLog(1);
        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(maintenanceLogService, times(1)).deleteMaintenanceLog(1);
    }
}
