package com.cars.garage.serviceTest;

import com.cars.garage.entity.Vehicle;
import com.cars.garage.exception.ResourceNotFoundException;
import com.cars.garage.repository.VehicleRepository;
import com.cars.garage.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllVehicles() {
        Vehicle vehicle1 = new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Available", null, null, null, null);
        Vehicle vehicle2 = new Vehicle(2, "Ford", "Focus", 2018, "Hatchback", 5, "Available", null, null, null, null);

        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(vehicle1, vehicle2));

        var vehicles = vehicleService.getAllVehicles();
        assertEquals(2, vehicles.size());
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void getVehicleById() {
        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Available", null, null, null, null);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.getVehicleById(1);
        assertNotNull(result);
        assertEquals("Toyota", result.getBrand());
        verify(vehicleRepository, times(1)).findById(1);
    }

    @Test
    void getVehicleByIdNotFound() {
        when(vehicleRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            vehicleService.getVehicleById(99);
        });

        assertEquals("Vehicle not found with id: 99", exception.getMessage());
        verify(vehicleRepository, times(1)).findById(99);
    }

    @Test
    void saveVehicle() {
        Vehicle vehicle = new Vehicle(null, "Honda", "Civic", 2019, "Sedan", 5, "Available", null, null, null, null);

        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle result = vehicleService.saveVehicle(vehicle);
        assertNotNull(result);
        assertEquals("Honda", result.getBrand());
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void updateVehicle() {
        Vehicle existingVehicle = new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Available", null, null, null, null);
        Vehicle updatedVehicle = new Vehicle(1, "Toyota", "Corolla", 2021, "Sedan", 5, "Available", null, null, null, null);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(existingVehicle)).thenReturn(updatedVehicle);

        Vehicle result = vehicleService.updateVehicle(1, updatedVehicle);

        assertEquals(2021, result.getProductionYear());
        verify(vehicleRepository, times(1)).save(existingVehicle);
    }

    @Test
    void deleteVehicle() {
        when(vehicleRepository.existsById(1)).thenReturn(true);

        doNothing().when(vehicleRepository).deleteById(1);

        vehicleService.deleteVehicle(1);

        verify(vehicleRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteVehicleNotFound() {
        when(vehicleRepository.existsById(99)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            vehicleService.deleteVehicle(99);
        });

        assertEquals("Vehicle not found with id: 99", exception.getMessage());
        verify(vehicleRepository, times(1)).existsById(99);
    }
}