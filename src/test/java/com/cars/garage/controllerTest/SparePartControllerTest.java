package com.cars.garage.controllerTest;

import com.cars.garage.controller.SparePartController;
import com.cars.garage.entity.SparePart;
import com.cars.garage.service.SparePartService;
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

class SparePartControllerTest {

    @InjectMocks
    private SparePartController sparePartController;

    @Mock
    private SparePartService sparePartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSpareParts() {
        SparePart part1 = new SparePart(1, "Brake Pad", "Brake", 50.0, 100, null);
        SparePart part2 = new SparePart(2, "Oil Filter", "Engine", 15.0, 200, null);
        when(sparePartService.getAllSpareParts()).thenReturn(Arrays.asList(part1, part2));

        List<SparePart> parts = sparePartController.getAllSpareParts();
        assertEquals(2, parts.size());
        verify(sparePartService, times(1)).getAllSpareParts();
    }

    @Test
    void getSparePartById() {
        SparePart part = new SparePart(1, "Brake Pad", "Brake", 50.0, 100, null);
        when(sparePartService.getSparePartById(1)).thenReturn(part);

        ResponseEntity<SparePart> response = sparePartController.getSparePartById(1);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Brake Pad", response.getBody().getName());
        verify(sparePartService, times(1)).getSparePartById(1);
    }

    @Test
    void createSparePart() {
        SparePart part = new SparePart(null, "Oil Filter", "Engine", 15.0, 200, null);
        when(sparePartService.saveSparePart(part)).thenReturn(part);

        SparePart result = sparePartController.createSparePart(part).getBody();
        assertNotNull(result);
        assertEquals("Oil Filter", result.getName());
        verify(sparePartService, times(1)).saveSparePart(part);
    }

    @Test
    void updateSparePart() {
        SparePart existingPart = new SparePart(1, "Brake Pad", "Brake", 50.0, 100, null);
        SparePart updatedPart = new SparePart(1, "Brake Pad Premium", "Brake", 60.0, 150, null);

        when(sparePartService.updateSparePart(1, updatedPart)).thenReturn(updatedPart);

        ResponseEntity<SparePart> response = sparePartController.updateSparePart(1, updatedPart);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Brake Pad Premium", response.getBody().getName());
        verify(sparePartService, times(1)).updateSparePart(1, updatedPart);
    }

    @Test
    void deleteSparePart() {
        doNothing().when(sparePartService).deleteSparePart(1);

        ResponseEntity<Void> response = sparePartController.deleteSparePart(1);
        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(sparePartService, times(1)).deleteSparePart(1);
    }
}
