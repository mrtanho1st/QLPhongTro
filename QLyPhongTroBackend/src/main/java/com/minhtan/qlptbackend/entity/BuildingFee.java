package com.minhtan.qlptbackend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "BuildingFees")
public class BuildingFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeeId")
    private Integer feeId;

    @Column(name = "BuildingId")
    private Integer buildingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BuildingId", insertable = false, updatable = false)
    private Building building;

    @Column(name = "ElectricityPrice", precision = 10, scale = 0)
    private BigDecimal electricityPrice;

    @Column(name = "WaterPrice", precision = 10, scale = 0)
    private BigDecimal waterPrice;

    @Column(name = "ServiceFee", precision = 10, scale = 0)
    private BigDecimal serviceFee;

    @Column(name = "ParkingFee", precision = 10, scale = 0)
    private BigDecimal parkingFee;

    @Column(name = "OtherFee", precision = 10, scale = 0)
    private BigDecimal otherFee;

    @Column(name = "FreeParking")
    private Integer freeParking;

    public BuildingFee() {
    }

    public BuildingFee(Integer feeId, Integer buildingId, Building building, BigDecimal electricityPrice, BigDecimal waterPrice,
            BigDecimal serviceFee, BigDecimal parkingFee, BigDecimal otherFee, Integer freeParking) {
        this.feeId = feeId;
        this.buildingId = buildingId;
        this.building = building;
        this.electricityPrice = electricityPrice;
        this.waterPrice = waterPrice;
        this.serviceFee = serviceFee;
        this.parkingFee = parkingFee;
        this.otherFee = otherFee;
        this.freeParking = freeParking;
    }

    public Integer getFeeId() {
        return feeId;
    }

    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public BigDecimal getElectricityPrice() {
        return electricityPrice;
    }

    public void setElectricityPrice(BigDecimal electricityPrice) {
        this.electricityPrice = electricityPrice;
    }

    public BigDecimal getWaterPrice() {
        return waterPrice;
    }

    public void setWaterPrice(BigDecimal waterPrice) {
        this.waterPrice = waterPrice;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(BigDecimal parkingFee) {
        this.parkingFee = parkingFee;
    }

    public BigDecimal getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }

    public Integer getFreeParking() {
        return freeParking;
    }

    public void setFreeParking(Integer freeParking) {
        this.freeParking = freeParking;
    }

    @Override
    public String toString() {
        return "BuildingFee{" +
                "feeId=" + feeId +
                ", buildingId=" + buildingId +
                ", electricityPrice=" + electricityPrice +
                ", waterPrice=" + waterPrice +
                ", serviceFee=" + serviceFee +
                ", parkingFee=" + parkingFee +
                ", otherFee=" + otherFee +
                ", freeParking=" + freeParking +
                '}';
    }
}