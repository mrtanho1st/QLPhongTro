package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.District;
import com.minhtan.qlptbackend.service.DistrictService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/districts")
public class DistrictController {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping
    public List<District> getAllDistricts() {
        return districtService.getAllDistricts();
    }

    @GetMapping("/search/name")
    public List<District> searchByName(@RequestParam String name) {
        return districtService.searchByName(name);
    }

    @GetMapping("/{districtId}")
    public ResponseEntity<District> getDistrictById(@PathVariable Integer districtId) {
        return districtService.getDistrictById(districtId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<District> createDistrict(@RequestBody District district) {
        District createdDistrict = districtService.createDistrict(district);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDistrict);
    }

    @PutMapping("/{districtId}")
    public ResponseEntity<District> updateDistrict(@PathVariable Integer districtId,
            @RequestBody District district) {
        return districtService.updateDistrict(districtId, district)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{districtId}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable Integer districtId) {
        if (!districtService.deleteDistrict(districtId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}