package com.cars.garage.service;

import com.cars.garage.entity.Garage;
import com.cars.garage.entity.Vehicle;
import com.cars.garage.exception.BadRequestException;
import com.cars.garage.exception.ResourceNotFoundException;
import com.cars.garage.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cars.garage.repository.GarageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GarageService {

    private final GarageRepository garageRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public GarageService(GarageRepository garageRepository, VehicleRepository vehicleRepository) {
        this.garageRepository = garageRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Garage saveGarage(Garage garage) {
        for (Vehicle vehicle : garage.getVehicles()) {
            vehicle.setGarage(garage);
        }

        if (garage.getVehicles().size() > garage.getCapacity()) {
            throw new BadRequestException("The number of vehicles exceeds the garage capacity.");
        }

        garage.setOccupiedSpaces(garage.getVehicles().size());

        return garageRepository.save(garage);
    }


    public Garage getGarageById(Integer id) {
        return garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id));
    }

    public List<Garage> getAllGarages() {
        return garageRepository.findAll();
    }

    public void deleteGarage(Integer id) {
        Garage existingGarage = garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id));

        for (Vehicle vehicle : existingGarage.getVehicles()) {
            vehicle.setGarage(null);
            vehicleRepository.save(vehicle);
        }

        garageRepository.delete(existingGarage);
    }

    public Garage updateGarage(Integer id, Garage updatedGarage) {
        Garage existingGarage = garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id));

        // sterg toate masinile din garaj
        for (Vehicle vehicle : new ArrayList<>(existingGarage.getVehicles())) {
            vehicle.setGarage(null); // le setez garajul pe null pt. a nu se sterge masina cu totul
        }
        existingGarage.getVehicles().clear();

        for (Vehicle vehicle : updatedGarage.getVehicles()) {
            vehicle.setGarage(existingGarage);
            existingGarage.addVehicle(vehicle);
        }

        if (existingGarage.getVehicles().size() > existingGarage.getCapacity()) {
            throw new BadRequestException("The number of vehicles exceeds the garage capacity.");
        }

        existingGarage.setOccupiedSpaces(existingGarage.getVehicles().size());

        return garageRepository.save(existingGarage);
    }
}
