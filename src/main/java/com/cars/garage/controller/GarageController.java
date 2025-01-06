package com.cars.garage.controller;

import com.cars.garage.dto.GarageDTO;
import com.cars.garage.entity.Garage;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.exception.BadRequestException;
import com.cars.garage.exception.ResourceNotFoundException;
import com.cars.garage.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cars.garage.service.GarageService;

import java.util.List;

@RestController
@RequestMapping("/garages")
public class GarageController {

    private final GarageService garageService;
    private final VehicleService vehicleService;

    @Autowired
    public GarageController(GarageService garageService, VehicleService vehicleService) {
        this.garageService = garageService;
        this.vehicleService = vehicleService;
    }

    // Creare garaj nou
    @PostMapping
    public ResponseEntity<Garage> createGarage(@RequestBody GarageDTO garageDTO) {
        try {
            Garage garage = new Garage();
            garage.setLocation(garageDTO.getLocation());
            garage.setCapacity(garageDTO.getCapacity());

            // adaugam masini in garaj
            if (garageDTO.getVehicleIds() != null) {
                List<Vehicle> vehicles = garageDTO.getVehicleIds().stream()
                        .map(vehicleService::getVehicleById)
                        .toList();
                garage.setVehicles(vehicles);
            }

            Garage savedGarage = garageService.saveGarage(garage);
            return new ResponseEntity<>(savedGarage, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Garage> getGarageById(@PathVariable Integer id) {
        try {
            Garage garage = garageService.getGarageById(id);
            return ResponseEntity.ok(garage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Garage>> getAllGarages() {
        List<Garage> garages = garageService.getAllGarages();
        return ResponseEntity.ok(garages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Garage> updateGarage(@PathVariable Integer id, @RequestBody GarageDTO garageDTO) {
        try {
            Garage existingGarage = garageService.getGarageById(id);
            existingGarage.setLocation(garageDTO.getLocation());
            existingGarage.setCapacity(garageDTO.getCapacity());

            List<Vehicle> newVehicles = garageDTO.getVehicleIds().stream()
                    .map(vehicleService::getVehicleById)
                    .toList();

            // fac un garaj nou in care adaug masinile actualizate
            Garage updatedGarage = new Garage();
            updatedGarage.setId(existingGarage.getId());
            updatedGarage.setLocation(existingGarage.getLocation());
            updatedGarage.setCapacity(existingGarage.getCapacity());
            updatedGarage.setVehicles(newVehicles);

            Garage savedGarage = garageService.updateGarage(id, updatedGarage);

            return ResponseEntity.ok(savedGarage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Integer id) {
        try {
            garageService.deleteGarage(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}