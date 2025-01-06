package com.cars.garage.serviceTest;

import com.cars.garage.entity.SparePart;
import com.cars.garage.exception.ResourceNotFoundException;
import com.cars.garage.repository.SparePartRepository;
import com.cars.garage.service.SparePartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SparePartServiceTest {

    @InjectMocks
    private SparePartService sparePartService;

    @Mock
    private SparePartRepository sparePartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSpareParts() {
        SparePart part1 = new SparePart(1, "Brake Pad", "Brake", 50.0, 100, null);
        SparePart part2 = new SparePart(2, "Oil Filter", "Engine", 15.0, 200, null);

        when(sparePartRepository.findAll()).thenReturn(Arrays.asList(part1, part2));

        var parts = sparePartService.getAllSpareParts();
        assertEquals(2, parts.size());
        verify(sparePartRepository, times(1)).findAll();
    }

    @Test
    void getSparePartById() {
        SparePart part = new SparePart(1, "Brake Pad", "Brake", 50.0, 100, null);

        when(sparePartRepository.findById(1)).thenReturn(Optional.of(part));

        SparePart result = sparePartService.getSparePartById(1);
        assertNotNull(result);
        assertEquals("Brake Pad", result.getName());
        verify(sparePartRepository, times(1)).findById(1);
    }

    @Test
    void getSparePartByIdNotFound() {
        when(sparePartRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sparePartService.getSparePartById(99);
        });

        assertEquals("SparePart not found with id: 99", exception.getMessage());
        verify(sparePartRepository, times(1)).findById(99);
    }

    @Test
    void saveSparePart() {
        SparePart part = new SparePart(null, "Oil Filter", "Engine", 15.0, 200, null);

        when(sparePartRepository.save(part)).thenReturn(part);

        SparePart result = sparePartService.saveSparePart(part);
        assertNotNull(result);
        assertEquals("Oil Filter", result.getName());
        verify(sparePartRepository, times(1)).save(part);
    }

    @Test
    void updateSparePart() {
        SparePart existingPart = new SparePart(1, "Brake Pad", "Brake", 50.0, 100, null);
        SparePart updatedPart = new SparePart(1, "Brake Pad Premium", "Brake", 60.0, 150, null);

        when(sparePartRepository.findById(1)).thenReturn(Optional.of(existingPart));
        when(sparePartRepository.save(existingPart)).thenReturn(updatedPart);

        SparePart result = sparePartService.updateSparePart(1, updatedPart);

        assertEquals("Brake Pad Premium", result.getName());
        verify(sparePartRepository, times(1)).save(existingPart);
    }

    @Test
    void deleteSparePart() {
        when(sparePartRepository.existsById(1)).thenReturn(true);

        doNothing().when(sparePartRepository).deleteById(1);

        sparePartService.deleteSparePart(1);

        verify(sparePartRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteSparePartNotFound() {
        when(sparePartRepository.existsById(99)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sparePartService.deleteSparePart(99);
        });

        assertEquals("SparePart not found with id: 99", exception.getMessage());
        verify(sparePartRepository, times(1)).existsById(99);
    }
}
