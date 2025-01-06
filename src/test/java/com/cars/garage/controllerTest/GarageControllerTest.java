package com.cars.garage.controllerTest;

import com.cars.garage.controller.GarageController;
import com.cars.garage.dto.GarageDTO;
import com.cars.garage.entity.Garage;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.exception.ResourceNotFoundException;
import com.cars.garage.service.GarageService;
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

class GarageControllerTest {

    @InjectMocks
    private GarageController garageController;

    @Mock
    private GarageService garageService;

    @Mock
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllGarages() {
        Garage garage1 = new Garage();
        garage1.setId(1);
        garage1.setLocation("New York");
        garage1.setCapacity(50);
        garage1.setOccupiedSpaces(2);

        Garage garage2 = new Garage();
        garage2.setId(2);
        garage2.setLocation("London");
        garage2.setCapacity(100);
        garage2.setOccupiedSpaces(3);

        when(garageService.getAllGarages()).thenReturn(Arrays.asList(garage1, garage2));

        ResponseEntity<List<Garage>> response = garageController.getAllGarages();
        assertEquals(OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(garageService, times(1)).getAllGarages();
    }

    @Test
    void getGarageById() {
        Garage garage = new Garage();
        garage.setId(1);
        garage.setLocation("Paris");
        garage.setCapacity(80);
        garage.setOccupiedSpaces(3);

        when(garageService.getGarageById(1)).thenReturn(garage);

        ResponseEntity<Garage> response = garageController.getGarageById(1);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Paris", response.getBody().getLocation());
        assertEquals(3, response.getBody().getOccupiedSpaces());
        verify(garageService, times(1)).getGarageById(1);
    }

    @Test
    void getGarageByIdNotFound() {
        when(garageService.getGarageById(99)).thenThrow(new ResourceNotFoundException("Garage not found with id: 99"));

        ResponseEntity<Garage> response = garageController.getGarageById(99);
        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(garageService, times(1)).getGarageById(99);
    }

    @Test
    void createGarage() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        when(vehicleService.getVehicleById(1)).thenReturn(vehicle);

        GarageDTO garageDTO = new GarageDTO();
        garageDTO.setLocation("Berlin");
        garageDTO.setCapacity(120);
        garageDTO.setVehicleIds(Arrays.asList(1));

        Garage garage = new Garage();
        garage.setId(1);
        garage.setLocation("Berlin");
        garage.setCapacity(120);
        garage.setOccupiedSpaces(1);
        garage.setVehicles(List.of(vehicle));

        when(garageService.saveGarage(any(Garage.class))).thenReturn(garage);

        ResponseEntity<?> response = garageController.createGarage(garageDTO);

        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Garage);
        Garage responseBody = (Garage) response.getBody();
        assertEquals("Berlin", responseBody.getLocation());
        assertEquals(1, responseBody.getOccupiedSpaces());
        verify(vehicleService, times(1)).getVehicleById(1);
        verify(garageService, times(1)).saveGarage(any(Garage.class));
    }


    @Test
    void updateGarage() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setId(1);
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setId(2);
        when(vehicleService.getVehicleById(1)).thenReturn(vehicle1);
        when(vehicleService.getVehicleById(2)).thenReturn(vehicle2);

        GarageDTO garageDTO = new GarageDTO();
        garageDTO.setLocation("Paris Updated");
        garageDTO.setCapacity(100);
        garageDTO.setVehicleIds(Arrays.asList(1, 2));

        Garage existingGarage = new Garage();
        existingGarage.setId(1);
        existingGarage.setLocation("Paris");
        existingGarage.setCapacity(80);
        existingGarage.setOccupiedSpaces(1);

        when(garageService.getGarageById(1)).thenReturn(existingGarage);

        Garage updatedGarage = new Garage();
        updatedGarage.setId(1);
        updatedGarage.setLocation("Paris Updated");
        updatedGarage.setCapacity(100);
        updatedGarage.setOccupiedSpaces(2);
        updatedGarage.setVehicles(List.of(vehicle1, vehicle2));

        when(garageService.updateGarage(eq(1), any(Garage.class))).thenReturn(updatedGarage);

        ResponseEntity<?> response = garageController.updateGarage(1, garageDTO);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Garage);
        Garage responseBody = (Garage) response.getBody();
        assertEquals("Paris Updated", responseBody.getLocation());
        assertEquals(2, responseBody.getOccupiedSpaces());
        verify(vehicleService, times(2)).getVehicleById(anyInt());
        verify(garageService, times(1)).updateGarage(eq(1), any(Garage.class));
    }


    @Test
    void deleteGarage() {
        doNothing().when(garageService).deleteGarage(1);

        ResponseEntity<Void> response = garageController.deleteGarage(1);
        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(garageService, times(1)).deleteGarage(1);
    }
}
