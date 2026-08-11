package com.minhtan.qlptbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.Building;
import com.minhtan.qlptbackend.entity.BuildingFee;

import java.math.BigDecimal;
import java.util.List;

public interface BuildingFeeRepository extends JpaRepository<BuildingFee, Integer> {
    List<BuildingFee> findByFeeId(Integer feeId);

    List<BuildingFee> findByBuildingId(Integer buildingId);

    List<BuildingFee> findByBuilding(Building building);
    List<BuildingFee> findByElectricityPrice(BigDecimal electricityPrice);

    List<BuildingFee> findByWaterPrice(BigDecimal waterPrice);

    List<BuildingFee> findByServiceFee(BigDecimal serviceFee);
    List<BuildingFee> findByParkingFee(BigDecimal parkingFee);
    List<BuildingFee> findByOtherFee(BigDecimal otherFee);

    List<BuildingFee> findByFreeParking(Integer freeParking);

    void deleteByBuildingId(Integer buildingId);
}