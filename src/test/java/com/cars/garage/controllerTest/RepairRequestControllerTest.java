package com.cars.garage.controllerTest;

import com.cars.garage.controller.RepairRequestController;
import com.cars.garage.dto.RepairRequestDTO;
import com.cars.garage.entity.RepairRequest;
import com.cars.garage.entity.SparePart;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.service.RepairRequestService;
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

class RepairRequestControllerTest {

    @InjectMocks
    private RepairRequestController repairRequestController;

    @Mock
    private RepairRequestService repairRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRepairRequests() {
        RepairRequest req1 = new RepairRequest(1, null, "Brake issues", LocalDate.now(), "Pending", 150.0, null);
        RepairRequest req2 = new RepairRequest(2, null, "Engine noise", LocalDate.now(), "Completed", 300.0, null);

        when(repairRequestService.getAllRepairRequests()).thenReturn(Arrays.asList(req1, req2));

        List<RepairRequest> requests = repairRequestController.getAllRepairRequests();
        assertEquals(2, requests.size());
        verify(repairRequestService, times(1)).getAllRepairRequests();
    }

    @Test
    void getRepairRequestById() {
        RepairRequest req = new RepairRequest(1, null, "Brake issues", LocalDate.now(), "Pending", 150.0, null);

        when(repairRequestService.getRepairRequestById(1)).thenReturn(req);

        ResponseEntity<RepairRequest> response = repairRequestController.getRepairRequestById(1);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Brake issues", response.getBody().getIssueDescription());
        verify(repairRequestService, times(1)).getRepairRequestById(1);
    }

    @Test
    void createRepairRequest() {
        RepairRequestDTO repairRequestDTO = new RepairRequestDTO();
        repairRequestDTO.setVehicleId(1);
        repairRequestDTO.setIssueDescription("Engine noise");
        repairRequestDTO.setRequestDate(LocalDate.now());
        repairRequestDTO.setStatus("Pending");
        repairRequestDTO.setTotalCost(200.0);
        repairRequestDTO.setSparePartIds(Arrays.asList(101, 102));

        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", 2020, "Sedan", 5, "Active", null, null, null, null);
        List<SparePart> spareParts = Arrays.asList(
                createSparePart(101, "Brake Pad", "Brake", 50.0, 10),
                createSparePart(102, "Oil Filter", "Filter", 20.0, 15)
        );
        RepairRequest savedRequest = new RepairRequest(1, vehicle, "Engine noise", LocalDate.now(), "Pending", 200.0, spareParts);

        when(repairRequestService.saveRepairRequest(any(RepairRequestDTO.class))).thenReturn(savedRequest);

        ResponseEntity<RepairRequest> response = repairRequestController.createRepairRequest(repairRequestDTO);

        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Engine noise", response.getBody().getIssueDescription());
        assertEquals(2, response.getBody().getSpareParts().size());
        verify(repairRequestService, times(1)).saveRepairRequest(repairRequestDTO);
    }

    @Test
    void updateRepairRequest() {
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
        RepairRequest updatedRequest = new RepairRequest(1, vehicle, "Brake fixed", LocalDate.now(), "Completed", 250.0, spareParts);

        when(repairRequestService.updateRepairRequest(eq(1), any(RepairRequestDTO.class))).thenReturn(updatedRequest);

        ResponseEntity<RepairRequest> response = repairRequestController.updateRepairRequest(1, repairRequestDTO);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Brake fixed", response.getBody().getIssueDescription());
        assertEquals(2, response.getBody().getSpareParts().size());
        verify(repairRequestService, times(1)).updateRepairRequest(eq(1), eq(repairRequestDTO));
    }

    @Test
    void deleteRepairRequest() {
        doNothing().when(repairRequestService).deleteRepairRequest(1);

        ResponseEntity<Void> response = repairRequestController.deleteRepairRequest(1);
        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(repairRequestService, times(1)).deleteRepairRequest(1);
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
