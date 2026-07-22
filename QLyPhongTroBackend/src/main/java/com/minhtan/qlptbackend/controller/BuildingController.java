package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.Building;
import com.minhtan.qlptbackend.service.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping
    public List<Building> getAllBuildings() {
        return buildingService.getAllBuildings();
    }

    @GetMapping("/search/district-id/{districtId}")
    public List<Building> searchByDistrictId(@PathVariable Integer districtId) {
        return buildingService.searchByDistrictId(districtId);
    }

    @GetMapping("/search/true-address")
    public List<Building> searchByTrueAddress(@RequestParam String trueAddress) {
        return buildingService.searchByTrueAddress(trueAddress);
    }

    @GetMapping("/search/fake-address")
    public List<Building> searchByFakeAddress(@RequestParam String fakeAddress) {
        return buildingService.searchByFakeAddress(fakeAddress);
    }

    @GetMapping("/search/note")
    public List<Building> searchByNote(@RequestParam String note) {
        return buildingService.searchByNote(note);
    }

    @GetMapping("/search/owner-phone")
    public List<Building> searchByOwnerPhone(@RequestParam String ownerPhone) {
        return buildingService.searchByOwnerPhone(ownerPhone);
    }

    @GetMapping("/{buildingId}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Integer buildingId) {
        return buildingService.getBuildingById(buildingId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Building> createBuilding(@RequestBody Building building) {
        Building createdBuilding = buildingService.createBuilding(building);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuilding);
    }

    @PutMapping("/{buildingId}")
    public ResponseEntity<Building> updateBuilding(@PathVariable Integer buildingId,
            @RequestBody Building building) {
        return buildingService.updateBuilding(buildingId, building)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{buildingId}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Integer buildingId) {
        if (!buildingService.deleteBuilding(buildingId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}