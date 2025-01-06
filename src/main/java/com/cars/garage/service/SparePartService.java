package com.cars.garage.service;

import com.cars.garage.entity.SparePart;
import com.cars.garage.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cars.garage.repository.SparePartRepository;

import java.util.List;

@Service
public class SparePartService {

    private final SparePartRepository sparePartRepository;

    @Autowired
    public SparePartService(SparePartRepository sparePartRepository) {
        this.sparePartRepository = sparePartRepository;
    }

    public SparePart saveSparePart(SparePart sparePart) {
        return sparePartRepository.save(sparePart);
    }

    public SparePart getSparePartById(Integer id) {
        return sparePartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SparePart not found with id: " + id));
    }

    public List<SparePart> getAllSpareParts() {
        return sparePartRepository.findAll();
    }

    public void deleteSparePart(Integer id) {
        if (!sparePartRepository.existsById(id)) {
            throw new ResourceNotFoundException("SparePart not found with id: " + id);
        }
        sparePartRepository.deleteById(id);
    }

    public SparePart updateSparePart(Integer id, SparePart sparePart) {
        SparePart existingSparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SparePart not found with id: " + id));

        existingSparePart.setName(sparePart.getName());
        existingSparePart.setType(sparePart.getType());
        existingSparePart.setPrice(sparePart.getPrice());
        existingSparePart.setStockQuantity(sparePart.getStockQuantity());

        return sparePartRepository.save(existingSparePart);
    }
}
