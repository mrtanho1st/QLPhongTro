package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.BuildingFee;
import com.minhtan.qlptbackend.repository.BuildingFeeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BuildingFeeService {

    private final BuildingFeeRepository buildingFeeRepository;

    public BuildingFeeService(BuildingFeeRepository buildingFeeRepository) {
        this.buildingFeeRepository = buildingFeeRepository;
    }

    @Cacheable(value = "buildingFees", key = "'all'")
    public List<BuildingFee> getAllBuildingFees() {
        return buildingFeeRepository.findAll();
    }

    @Cacheable(value = "buildingFees", key = "'byBuildingId:' + #buildingId")
    public List<BuildingFee> searchByBuildingId(Integer buildingId) {
        return buildingFeeRepository.findByBuildingId(buildingId);
    }

    public List<BuildingFee> searchByElectricityPrice(BigDecimal electricityPrice) {
        return buildingFeeRepository.findByElectricityPrice(electricityPrice);
    }

    public List<BuildingFee> searchByWaterPrice(BigDecimal waterPrice) {
        return buildingFeeRepository.findByWaterPrice(waterPrice);
    }

    public List<BuildingFee> searchByServiceFee(BigDecimal serviceFee) {
        return buildingFeeRepository.findByServiceFee(serviceFee);
    }

    public List<BuildingFee> searchByParkingFee(BigDecimal parkingFee) {
        return buildingFeeRepository.findByParkingFee(parkingFee);
    }

    public List<BuildingFee> searchByOtherFee(BigDecimal otherFee) {
        return buildingFeeRepository.findByOtherFee(otherFee);
    }

    public List<BuildingFee> searchByFreeParking(Integer freeParking) {
        return buildingFeeRepository.findByFreeParking(freeParking);
    }

    @Cacheable(value = "buildingFees", key = "'byId:' + #feeId")
    public Optional<BuildingFee> getBuildingFeeById(Integer feeId) {
        return buildingFeeRepository.findById(feeId);
    }

    @CacheEvict(value = "buildingFees", allEntries = true)
    public BuildingFee createBuildingFee(BuildingFee buildingFee) {
        buildingFee.setFeeId(null);
        return buildingFeeRepository.save(buildingFee);
    }

    @CacheEvict(value = "buildingFees", allEntries = true)
    public Optional<BuildingFee> updateBuildingFee(Integer feeId, BuildingFee buildingFeeRequest) {
        return buildingFeeRepository.findById(feeId).map(existingBuildingFee -> {
            existingBuildingFee.setBuildingId(buildingFeeRequest.getBuildingId());
            existingBuildingFee.setElectricityPrice(buildingFeeRequest.getElectricityPrice());
            existingBuildingFee.setWaterPrice(buildingFeeRequest.getWaterPrice());
            existingBuildingFee.setServiceFee(buildingFeeRequest.getServiceFee());
            existingBuildingFee.setParkingFee(buildingFeeRequest.getParkingFee());
            existingBuildingFee.setOtherFee(buildingFeeRequest.getOtherFee());
            existingBuildingFee.setFreeParking(buildingFeeRequest.getFreeParking());
            return buildingFeeRepository.save(existingBuildingFee);
        });
    }

    @CacheEvict(value = "buildingFees", allEntries = true)
    public boolean deleteBuildingFee(Integer feeId) {
        if (!buildingFeeRepository.existsById(feeId)) {
            return false;
        }

        buildingFeeRepository.deleteById(feeId);
        return true;
    }
}