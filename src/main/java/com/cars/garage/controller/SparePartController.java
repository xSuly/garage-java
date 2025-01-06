package com.cars.garage.controller;

import com.cars.garage.entity.SparePart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cars.garage.service.SparePartService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/spare-parts")
public class SparePartController {

    private final SparePartService sparePartService;

    @Autowired
    public SparePartController(SparePartService sparePartService) {
        this.sparePartService = sparePartService;
    }

    @PostMapping
    public ResponseEntity<SparePart> createSparePart(@RequestBody SparePart sparePart) {
        SparePart savedPart = sparePartService.saveSparePart(sparePart);
        return new ResponseEntity<>(savedPart, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SparePart> getSparePartById(@PathVariable Integer id) {
        Optional<SparePart> sparePart = Optional.ofNullable(sparePartService.getSparePartById(id));
        return sparePart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<SparePart> getAllSpareParts() {
        return sparePartService.getAllSpareParts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SparePart> updateSparePart(@PathVariable Integer id, @RequestBody SparePart sparePart) {
        SparePart updatedPart = sparePartService.updateSparePart(id, sparePart);
        return updatedPart != null ? ResponseEntity.ok(updatedPart) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSparePart(@PathVariable Integer id) {
        sparePartService.deleteSparePart(id);
        return ResponseEntity.noContent().build();
    }
}

