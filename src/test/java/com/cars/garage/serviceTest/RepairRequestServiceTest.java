package com.cars.garage.serviceTest;

import com.cars.garage.dto.RepairRequestDTO;
import com.cars.garage.entity.RepairRequest;
import com.cars.garage.entity.SparePart;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.exception.ResourceNotFoundException;
import com.cars.garage.repository.RepairRequestRepository;
import com.cars.garage.service.RepairRequestService;
import com.cars.garage.service.SparePartService;
import com.cars.garage.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepairRequestServiceTest {

    @InjectMocks
    private RepairRequestService repairRequestService;

    @Mock
    private RepairRequestRepository repairRequestRepository;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private SparePartService sparePartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRepairRequests() {
        RepairRequest req1 = new RepairRequest(1, null, "Brake issues", LocalDate.now(), "Pending", 150.0, null);
        RepairRequest req2 = new RepairRequest(2, null, "Engine noise", LocalDate.now(), "Completed", 300.0, null);

        when(repairRequestRepository.findAll()).thenReturn(Arrays.asList(req1, req2));

        List<RepairRequest> requests = repairRequestService.getAllRepairRequests();
        assertEquals(2, requests.size());
        verify(repairRequestRepository, times(1)).findAll();
    }

    @Test
    void getRepairRequestById() {
        RepairRequest req = new RepairRequest(1, null, "Brake issues", LocalDate.now(), "Pending", 150.0, null);

        when(repairRequestRepository.findById(1)).thenReturn(Optional.of(req));

        RepairRequest result = repairRequestService.getRepairRequestById(1);
        assertNotNull(result);
        assertEquals("Brake issues", result.getIssueDescription());
        verify(repairRequestRepository, times(1)).findById(1);
    }

    @Test
    void getRepairRequestByIdNotFound() {
        when(repairRequestRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            repairRequestService.getRepairRequestById(99);
        });

        assertEquals("RepairRequest not found with id: 99", exception.getMessage());
        verify(repairRequestRepository, times(1)).findById(99);
    }

    @Test
    void saveRepairRequest() {
        // Prepare DTO
        RepairRequestDTO repairRequestDTO = new RepairRequestDTO();
        repairRequestDTO.setVehicleId(1);
        repairRequestDTO.setIssueDescription("Brake issues");
        repairRequestDTO.setRequestDate(LocalDate.now());
        repairRequestDTO.setStatus("Pending");
        repairRequestDTO.setTotalCost(200.0);
        repairRequestDTO.setSparePartIds(Arrays.asList(101, 102));

        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Active", null, null, null, null);
        List<SparePart> spareParts = Arrays.asList(
                createSparePart(101, "Brake Pad", "Brake", 50.0, 10),
                createSparePart(102, "Oil Filter", "Filter", 20.0, 15)
        );

        // Mock behavior
        when(vehicleService.getVehicleById(1)).thenReturn(vehicle);
        when(sparePartService.getSparePartById(101)).thenReturn(spareParts.get(0));
        when(sparePartService.getSparePartById(102)).thenReturn(spareParts.get(1));

        RepairRequest savedRequest = new RepairRequest(1, vehicle, "Brake issues", LocalDate.now(), "Pending", 200.0, spareParts);
        when(repairRequestRepository.save(any(RepairRequest.class))).thenReturn(savedRequest);

        // Test the service
        RepairRequest result = repairRequestService.saveRepairRequest(repairRequestDTO);

        // Assertions
        assertNotNull(result);
        assertEquals("Brake issues", result.getIssueDescription());
        assertEquals(2, result.getSpareParts().size());
        verify(vehicleService, times(1)).getVehicleById(1);
        verify(sparePartService, times(2)).getSparePartById(anyInt());
        verify(repairRequestRepository, times(1)).save(any(RepairRequest.class));
    }

    @Test
    void updateRepairRequest() {
        // Prepare DTO
        RepairRequestDTO repairRequestDTO = new RepairRequestDTO();
        repairRequestDTO.setVehicleId(2);
        repairRequestDTO.setIssueDescription("Brake fixed");
        repairRequestDTO.setRequestDate(LocalDate.now());
        repairRequestDTO.setStatus("Completed");
        repairRequestDTO.setTotalCost(250.0);
        repairRequestDTO.setSparePartIds(Arrays.asList(103, 104));

        Vehicle vehicle = new Vehicle(2, "Honda", "Civic", 2018, "Sedan", 5, "Active", null, null, null, null);
        List<SparePart> spareParts = Arrays.asList(
                createSparePart(103, "Brake Disc", "Brake", 80.0, 8),
                createSparePart(104, "Air Filter", "Filter", 30.0, 20)
        );

        RepairRequest existingRequest = new RepairRequest();
        existingRequest.setId(1);
        existingRequest.setIssueDescription("Brake issues");
        existingRequest.setRequestDate(LocalDate.now());
        existingRequest.setStatus("Pending");
        existingRequest.setTotalCost(150.0);
        existingRequest.setSpareParts(new ArrayList<>());

        RepairRequest updatedRequest = new RepairRequest(1, vehicle, "Brake fixed", LocalDate.now(), "Completed", 250.0, spareParts);

        when(repairRequestRepository.findById(1)).thenReturn(Optional.of(existingRequest));
        when(vehicleService.getVehicleById(2)).thenReturn(vehicle);
        when(sparePartService.getSparePartById(103)).thenReturn(spareParts.get(0));
        when(sparePartService.getSparePartById(104)).thenReturn(spareParts.get(1));
        when(repairRequestRepository.save(existingRequest)).thenReturn(updatedRequest);

        RepairRequest result = repairRequestService.updateRepairRequest(1, repairRequestDTO);

        assertNotNull(result);
        assertEquals("Brake fixed", result.getIssueDescription());
        assertEquals(2, result.getSpareParts().size());
        verify(repairRequestRepository, times(1)).save(existingRequest);
    }


    @Test
    void deleteRepairRequest() {
        when(repairRequestRepository.existsById(1)).thenReturn(true);

        doNothing().when(repairRequestRepository).deleteById(1);

        repairRequestService.deleteRepairRequest(1);

        verify(repairRequestRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteRepairRequestNotFound() {
        when(repairRequestRepository.existsById(99)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            repairRequestService.deleteRepairRequest(99);
        });

        assertEquals("RepairRequest not found with id: 99", exception.getMessage());
        verify(repairRequestRepository, times(1)).existsById(99);
    }

    private SparePart createSparePart(Integer id, String name, String type, Double price, Integer stockQuantity) {
        SparePart sparePart = new SparePart();
        sparePart.setId(id);
        sparePart.setName(name);
        sparePart.setType(type);
        sparePart.setPrice(price);
        sparePart.setStockQuantity(stockQuantity);
        return sparePart;
    }
}
