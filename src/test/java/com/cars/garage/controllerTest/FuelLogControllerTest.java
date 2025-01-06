package com.cars.garage.controllerTest;

import com.cars.garage.controller.FuelLogController;
import com.cars.garage.dto.FuelLogDTO;
import com.cars.garage.entity.FuelLog;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.service.FuelLogService;
import com.cars.garage.service.VehicleService;
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

class FuelLogControllerTest {

    @InjectMocks
    private FuelLogController fuelLogController;

    @Mock
    private FuelLogService fuelLogService;

    @Mock
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllFuelLogs() {
        FuelLog log1 = new FuelLog(1, null, LocalDate.now(), 50.0, 5.0, 250.0);
        FuelLog log2 = new FuelLog(2, null, LocalDate.now(), 60.0, 5.5, 330.0);
        when(fuelLogService.getAllFuelLogs()).thenReturn(Arrays.asList(log1, log2));

        ResponseEntity<List<FuelLog>> response = fuelLogController.getAllFuelLogs();
        assertEquals(OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(fuelLogService, times(1)).getAllFuelLogs();
    }

    @Test
    void getFuelLogById() {
        FuelLog log = new FuelLog(1, null, LocalDate.now(), 50.0, 5.0, 250.0);
        when(fuelLogService.getFuelLogById(1)).thenReturn(log);

        ResponseEntity<FuelLog> response = fuelLogController.getFuelLogById(1);
        assertEquals(OK, response.getStatusCode());
        assertEquals(50.0, response.getBody().getFuelAddedLiters());
        verify(fuelLogService, times(1)).getFuelLogById(1);
    }

    @Test
    void createFuelLog() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setProductionYear(2020);
        vehicle.setType("Sedan");
        vehicle.setCapacity(5);
        vehicle.setStatus("Available");

        when(vehicleService.getVehicleById(1)).thenReturn(vehicle);

        FuelLogDTO fuelLogDTO = new FuelLogDTO();
        fuelLogDTO.setVehicleId(1);
        fuelLogDTO.setDate(LocalDate.now());
        fuelLogDTO.setFuelAddedLiters(50.0);
        fuelLogDTO.setCostPerLiter(5.0);
        fuelLogDTO.setTotalCost(250.0);

        FuelLog expectedLog = new FuelLog(null, vehicle, LocalDate.now(), 50.0, 5.0, 250.0);
        when(fuelLogService.saveFuelLog(any(FuelLog.class))).thenReturn(expectedLog);

        ResponseEntity<FuelLog> response = fuelLogController.createFuelLog(fuelLogDTO);
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(50.0, response.getBody().getFuelAddedLiters());
        verify(vehicleService, times(1)).getVehicleById(1);
        verify(fuelLogService, times(1)).saveFuelLog(any(FuelLog.class));
    }

    @Test
    void updateFuelLog() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setProductionYear(2020);
        vehicle.setType("Sedan");
        vehicle.setCapacity(5);
        vehicle.setStatus("Available");

        when(vehicleService.getVehicleById(1)).thenReturn(vehicle);

        FuelLogDTO fuelLogDTO = new FuelLogDTO();
        fuelLogDTO.setVehicleId(1);
        fuelLogDTO.setDate(LocalDate.now());
        fuelLogDTO.setFuelAddedLiters(60.0);
        fuelLogDTO.setCostPerLiter(5.5);
        fuelLogDTO.setTotalCost(330.0);

        FuelLog updatedLog = new FuelLog(1, vehicle, LocalDate.now(), 60.0, 5.5, 330.0);
        when(fuelLogService.updateFuelLog(eq(1), any(FuelLog.class))).thenReturn(updatedLog);

        ResponseEntity<FuelLog> response = fuelLogController.updateFuelLog(1, fuelLogDTO);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(60.0, response.getBody().getFuelAddedLiters());
        verify(vehicleService, times(1)).getVehicleById(1);
        verify(fuelLogService, times(1)).updateFuelLog(eq(1), any(FuelLog.class));
    }

    @Test
    void deleteFuelLog() {
        doNothing().when(fuelLogService).deleteFuelLog(1);

        ResponseEntity<Void> response = fuelLogController.deleteFuelLog(1);
        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(fuelLogService, times(1)).deleteFuelLog(1);
    }
}
