package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.Amenity;
import com.minhtan.qlptbackend.service.AmenityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public List<Amenity> getAllAmenities() {
        return amenityService.getAllAmenities();
    }

    @GetMapping("/search/name")
    public List<Amenity> searchByName(@RequestParam String name) {
        return amenityService.searchByName(name);
    }

    @GetMapping("/{amenityId}")
    public ResponseEntity<Amenity> getAmenityById(@PathVariable Integer amenityId) {
        return amenityService.getAmenityById(amenityId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Amenity> createAmenity(@RequestBody Amenity amenity) {
        Amenity createdAmenity = amenityService.createAmenity(amenity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAmenity);
    }

    @PutMapping("/{amenityId}")
    public ResponseEntity<Amenity> updateAmenity(@PathVariable Integer amenityId,
            @RequestBody Amenity amenity) {
        return amenityService.updateAmenity(amenityId, amenity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{amenityId}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Integer amenityId) {
        if (!amenityService.deleteAmenity(amenityId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}