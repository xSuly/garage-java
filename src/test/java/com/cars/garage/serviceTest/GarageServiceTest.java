package com.cars.garage.serviceTest;

import com.cars.garage.entity.Garage;
import com.cars.garage.exception.ResourceNotFoundException;
import com.cars.garage.repository.GarageRepository;
import com.cars.garage.service.GarageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GarageServiceTest {

    @InjectMocks
    private GarageService garageService;

    @Mock
    private GarageRepository garageRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllGarages() {
        Garage garage1 = new Garage(1, "New York", 50, 10, null);
        Garage garage2 = new Garage(2, "London", 100, 20, null);

        when(garageRepository.findAll()).thenReturn(Arrays.asList(garage1, garage2));

        var garages = garageService.getAllGarages();
        assertEquals(2, garages.size());
        verify(garageRepository, times(1)).findAll();
    }

    @Test
    void getGarageById() {
        Garage garage = new Garage(1, "Paris", 80, 30, null);

        when(garageRepository.findById(1)).thenReturn(Optional.of(garage));

        Garage result = garageService.getGarageById(1);
        assertNotNull(result);
        assertEquals("Paris", result.getLocation());
        verify(garageRepository, times(1)).findById(1);
    }

    @Test
    void getGarageByIdNotFound() {
        when(garageRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            garageService.getGarageById(99);
        });

        assertEquals("Garage not found with id: 99", exception.getMessage());
        verify(garageRepository, times(1)).findById(99);
    }

    @Test
    void saveGarage() {
        Garage garage = new Garage(null, "Berlin", 120, 0, new ArrayList<>()); // Initialize vehicles list

        when(garageRepository.save(garage)).thenReturn(garage);

        Garage result = garageService.saveGarage(garage);
        assertNotNull(result);
        assertEquals("Berlin", result.getLocation());
        verify(garageRepository, times(1)).save(garage);
    }

    @Test
    void updateGarage() {
        Garage existingGarage = new Garage(1, "Paris", 80, 30, new ArrayList<>()); // Initialize vehicles
        Garage updatedGarage = new Garage(1, "Paris Updated", 100, 40, new ArrayList<>());

        when(garageRepository.findById(1)).thenReturn(Optional.of(existingGarage));
        when(garageRepository.save(existingGarage)).thenReturn(updatedGarage);

        Garage result = garageService.updateGarage(1, updatedGarage);

        assertEquals("Paris Updated", result.getLocation());
        verify(garageRepository, times(1)).save(existingGarage);
    }

    @Test
    void deleteGarage() {
        Garage existingGarage = new Garage(1, "Paris", 80, 30, new ArrayList<>()); // Initialize vehicles

        when(garageRepository.findById(1)).thenReturn(Optional.of(existingGarage));
        doNothing().when(garageRepository).delete(existingGarage);

        garageService.deleteGarage(1);

        verify(garageRepository, times(1)).delete(existingGarage);
    }


    @Test
    void deleteGarageNotFound() {
        when(garageRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            garageService.deleteGarage(99);
        });

        assertEquals("Garage not found with id: 99", exception.getMessage());
        verify(garageRepository, times(1)).findById(99);
    }
}
