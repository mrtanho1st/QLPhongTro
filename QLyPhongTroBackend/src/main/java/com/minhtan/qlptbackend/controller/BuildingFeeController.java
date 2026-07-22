package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.BuildingFee;
import com.minhtan.qlptbackend.service.BuildingFeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

import java.util.List;

@RestController
@RequestMapping("/api/building-fees")
public class BuildingFeeController {

    private final BuildingFeeService buildingFeeService;

    public BuildingFeeController(BuildingFeeService buildingFeeService) {
        this.buildingFeeService = buildingFeeService;
    }

    @GetMapping
    public List<BuildingFee> getAllBuildingFees() {
        return buildingFeeService.getAllBuildingFees();
    }

    @GetMapping("/search/building-id/{buildingId}")
    public List<BuildingFee> searchByBuildingId(@PathVariable Integer buildingId) {
        return buildingFeeService.searchByBuildingId(buildingId);
    }

    @GetMapping("/search/electricity-price")
    public List<BuildingFee> searchByElectricityPrice(@RequestParam BigDecimal electricityPrice) {
        return buildingFeeService.searchByElectricityPrice(electricityPrice);
    }

    @GetMapping("/search/water-price")
    public List<BuildingFee> searchByWaterPrice(@RequestParam BigDecimal waterPrice) {
        return buildingFeeService.searchByWaterPrice(waterPrice);
    }

    @GetMapping("/search/service-fee")
    public List<BuildingFee> searchByServiceFee(@RequestParam BigDecimal serviceFee) {
        return buildingFeeService.searchByServiceFee(serviceFee);
    }

    @GetMapping("/search/parking-fee")
    public List<BuildingFee> searchByParkingFee(@RequestParam BigDecimal parkingFee) {
        return buildingFeeService.searchByParkingFee(parkingFee);
    }

    @GetMapping("/search/other-fee")
    public List<BuildingFee> searchByOtherFee(@RequestParam BigDecimal otherFee) {
        return buildingFeeService.searchByOtherFee(otherFee);
    }

    @GetMapping("/search/free-parking/{freeParking}")
    public List<BuildingFee> searchByFreeParking(@PathVariable Integer freeParking) {
        return buildingFeeService.searchByFreeParking(freeParking);
    }

    @GetMapping("/{feeId}")
    public ResponseEntity<BuildingFee> getBuildingFeeById(@PathVariable Integer feeId) {
        return buildingFeeService.getBuildingFeeById(feeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BuildingFee> createBuildingFee(@RequestBody BuildingFee buildingFee) {
        BuildingFee createdBuildingFee = buildingFeeService.createBuildingFee(buildingFee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuildingFee);
    }

    @PutMapping("/{feeId}")
    public ResponseEntity<BuildingFee> updateBuildingFee(@PathVariable Integer feeId,
            @RequestBody BuildingFee buildingFee) {
        return buildingFeeService.updateBuildingFee(feeId, buildingFee)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{feeId}")
    public ResponseEntity<Void> deleteBuildingFee(@PathVariable Integer feeId) {
        if (!buildingFeeService.deleteBuildingFee(feeId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}