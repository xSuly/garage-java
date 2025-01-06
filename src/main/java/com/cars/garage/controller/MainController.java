package com.cars.garage.controller;

import com.cars.garage.dto.RepairRequestDTO;
import com.cars.garage.entity.*;
import com.cars.garage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    private final GarageService garageService;
    private final VehicleService vehicleService;
    private final MaintenanceLogService maintenanceLogService;
    private final FuelLogService fuelLogService;
    private final SparePartService sparePartService;
    private final RepairRequestService repairRequestService;

    @Autowired
    public MainController(
            GarageService garageService,
            VehicleService vehicleService,
            MaintenanceLogService maintenanceLogService,
            FuelLogService fuelLogService,
            SparePartService sparePartService,
            RepairRequestService repairRequestService) {
        this.garageService = garageService;
        this.vehicleService = vehicleService;
        this.maintenanceLogService = maintenanceLogService;
        this.fuelLogService = fuelLogService;
        this.sparePartService = sparePartService;
        this.repairRequestService = repairRequestService;
    }

    @GetMapping
    public String index(Model model) {
        List<Garage> garages = garageService.getAllGarages();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        List<MaintenanceLog> maintenanceLogs = maintenanceLogService.getAllMaintenanceLogs();
        List<FuelLog> fuelLogs = fuelLogService.getAllFuelLogs();
        List<SparePart> spareParts = sparePartService.getAllSpareParts();
        List<RepairRequest> repairRequests = repairRequestService.getAllRepairRequests();

        vehicles.sort(Comparator.comparingInt(Vehicle::getId));

        model.addAttribute("garages", garages);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("maintenanceLogs", maintenanceLogs);
        model.addAttribute("fuelLogs", fuelLogs);
        model.addAttribute("spareParts", spareParts);
        model.addAttribute("repairRequests", repairRequests);
        return "index";
    }

    @PostMapping("/addGarage")
    public String addGarage(@ModelAttribute Garage garage) {
        garage.setOccupiedSpaces(0);
        garageService.saveGarage(garage);
        return "redirect:/";
    }

    @PostMapping("/addVehicle")
    public String addVehicle(@ModelAttribute Vehicle vehicle) {
        vehicleService.saveVehicle(vehicle);
        return "redirect:/";
    }

    @PostMapping("/addMaintenanceLog")
    public String addMaintenanceLog(@ModelAttribute MaintenanceLog maintenanceLog, @RequestParam("vehicleId") Integer vehicleId) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (vehicle == null) {
            throw new IllegalArgumentException("Invalid vehicle ID: " + vehicleId);
        }

        maintenanceLog.setVehicle(vehicle);

        maintenanceLogService.saveMaintenanceLog(maintenanceLog);
        return "redirect:/";
    }


    @PostMapping("/addFuelLog")
    public String addFuelLog(@ModelAttribute FuelLog fuelLog, @RequestParam("vehicleId") Integer vehicleId) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (vehicle == null) {
            throw new IllegalArgumentException("Invalid vehicle ID: " + vehicleId);
        }

        fuelLog.setVehicle(vehicle);

        fuelLogService.saveFuelLog(fuelLog);
        return "redirect:/";
    }


    @PostMapping("/addSparePart")
    public String addSparePart(@ModelAttribute SparePart sparePart) {
        sparePartService.saveSparePart(sparePart);
        return "redirect:/";
    }

    @PostMapping("/addRepairRequest")
    public String addRepairRequest(@ModelAttribute RepairRequestDTO repairRequestDTO) {
        repairRequestService.saveRepairRequest(repairRequestDTO);
        return "redirect:/";
    }

}
