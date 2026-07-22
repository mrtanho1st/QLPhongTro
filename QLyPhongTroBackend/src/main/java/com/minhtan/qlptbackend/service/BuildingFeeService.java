package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.BuildingFee;
import com.minhtan.qlptbackend.repository.BuildingFeeRepository;
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

    public List<BuildingFee> getAllBuildingFees() {
        return buildingFeeRepository.findAll();
    }

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

    public Optional<BuildingFee> getBuildingFeeById(Integer feeId) {
        return buildingFeeRepository.findById(feeId);
    }

    public BuildingFee createBuildingFee(BuildingFee buildingFee) {
        buildingFee.setFeeId(null);
        return buildingFeeRepository.save(buildingFee);
    }

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

    public boolean deleteBuildingFee(Integer feeId) {
        if (!buildingFeeRepository.existsById(feeId)) {
            return false;
        }

        buildingFeeRepository.deleteById(feeId);
        return true;
    }
}