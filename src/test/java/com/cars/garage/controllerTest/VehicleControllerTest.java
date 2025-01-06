package com.cars.garage.controllerTest;

import com.cars.garage.controller.VehicleController;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class VehicleControllerTest {

    @InjectMocks
    private VehicleController vehicleController;

    @Mock
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllVehicles() {
        Vehicle vehicle1 = new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Available", null, null, null, null);
        Vehicle vehicle2 = new Vehicle(2, "Ford", "Focus", 2018, "Hatchback", 5, "Available", null, null, null, null);
        when(vehicleService.getAllVehicles()).thenReturn(Arrays.asList(vehicle1, vehicle2));

        List<Vehicle> vehicles = vehicleController.getAllVehicles();
        assertEquals(2, vehicles.size());
        verify(vehicleService, times(1)).getAllVehicles();
    }

    @Test
    void getVehicleById() {
        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Available", null, null, null, null);
        when(vehicleService.getVehicleById(1)).thenReturn(vehicle);

        ResponseEntity<Vehicle> response = vehicleController.getVehicleById(1);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Toyota", response.getBody().getBrand());
        verify(vehicleService, times(1)).getVehicleById(1);
    }

    @Test
    void createVehicle() {
        Vehicle vehicle = new Vehicle(null, "Honda", "Civic", 2019, "Sedan", 5, "Available", null, null, null, null);
        when(vehicleService.saveVehicle(vehicle)).thenReturn(vehicle);

        Vehicle result = vehicleController.createVehicle(vehicle).getBody();
        assertNotNull(result);
        assertEquals("Honda", result.getBrand());
        verify(vehicleService, times(1)).saveVehicle(vehicle);
    }

    @Test
    void updateVehicle() {
        Vehicle existingVehicle = new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Available", null, null, null, null);
        Vehicle updatedDetails = new Vehicle(1, "Toyota", "Corolla", 2021, "Sedan", 5, "Available", null, null, null, null);

        when(vehicleService.updateVehicle(1, updatedDetails)).thenReturn(updatedDetails);

        ResponseEntity<Vehicle> response = vehicleController.updateVehicle(1, updatedDetails);
        assertEquals(OK, response.getStatusCode());
        assertEquals(2021, response.getBody().getProductionYear());
        verify(vehicleService, times(1)).updateVehicle(1, updatedDetails);
    }

    @Test
    void deleteVehicle() {
        doNothing().when(vehicleService).deleteVehicle(1);

        ResponseEntity<Void> response = vehicleController.deleteVehicle(1);
        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(vehicleService, times(1)).deleteVehicle(1);
    }
}