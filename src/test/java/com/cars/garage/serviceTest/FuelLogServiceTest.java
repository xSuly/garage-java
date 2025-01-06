package com.cars.garage.serviceTest;

import com.cars.garage.entity.FuelLog;
import com.cars.garage.exception.ResourceNotFoundException;
import com.cars.garage.repository.FuelLogRepository;
import com.cars.garage.service.FuelLogService;
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

class FuelLogServiceTest {

    @InjectMocks
    private FuelLogService fuelLogService;

    @Mock
    private FuelLogRepository fuelLogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllFuelLogs() {
        FuelLog log1 = new FuelLog(1, null, LocalDate.now(), 50.0, 5.0, 250.0);
        FuelLog log2 = new FuelLog(2, null, LocalDate.now(), 60.0, 5.5, 330.0);

        when(fuelLogRepository.findAll()).thenReturn(Arrays.asList(log1, log2));

        var logs = fuelLogService.getAllFuelLogs();
        assertEquals(2, logs.size());
        verify(fuelLogRepository, times(1)).findAll();
    }

    @Test
    void getFuelLogById() {
        FuelLog log = new FuelLog(1, null, LocalDate.now(), 50.0, 5.0, 250.0);

        when(fuelLogRepository.findById(1)).thenReturn(Optional.of(log));

        FuelLog result = fuelLogService.getFuelLogById(1);
        assertNotNull(result);
        assertEquals(50.0, result.getFuelAddedLiters());
        verify(fuelLogRepository, times(1)).findById(1);
    }

    @Test
    void getFuelLogByIdNotFound() {
        when(fuelLogRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            fuelLogService.getFuelLogById(99);
        });

        assertEquals("FuelLog not found with id: 99", exception.getMessage());
        verify(fuelLogRepository, times(1)).findById(99);
    }

    @Test
    void saveFuelLog() {
        FuelLog log = new FuelLog(null, null, LocalDate.now(), 50.0, 5.0, 250.0);

        when(fuelLogRepository.save(log)).thenReturn(log);

        FuelLog result = fuelLogService.saveFuelLog(log);
        assertNotNull(result);
        assertEquals(50.0, result.getFuelAddedLiters());
        verify(fuelLogRepository, times(1)).save(log);
    }

    @Test
    void updateFuelLog() {
        FuelLog existingLog = new FuelLog(1, null, LocalDate.now(), 50.0, 5.0, 250.0);
        FuelLog updatedLog = new FuelLog(1, null, LocalDate.now(), 60.0, 5.5, 330.0);

        when(fuelLogRepository.findById(1)).thenReturn(Optional.of(existingLog));
        when(fuelLogRepository.save(existingLog)).thenReturn(updatedLog);

        FuelLog result = fuelLogService.updateFuelLog(1, updatedLog);

        assertEquals(60.0, result.getFuelAddedLiters());
        assertEquals(5.5, result.getCostPerLiter());
        verify(fuelLogRepository, times(1)).save(existingLog);
    }

    @Test
    void deleteFuelLog() {
        when(fuelLogRepository.existsById(1)).thenReturn(true);

        doNothing().when(fuelLogRepository).deleteById(1);

        fuelLogService.deleteFuelLog(1);

        verify(fuelLogRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteFuelLogNotFound() {
        when(fuelLogRepository.existsById(99)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            fuelLogService.deleteFuelLog(99);
        });

        assertEquals("FuelLog not found with id: 99", exception.getMessage());
        verify(fuelLogRepository, times(1)).existsById(99);
    }
}
