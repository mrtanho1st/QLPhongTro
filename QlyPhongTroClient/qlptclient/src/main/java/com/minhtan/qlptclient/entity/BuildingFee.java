package com.minhtan.qlptclient.entity;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildingFee {

    private Integer feeId;
    private Integer buildingId;
    private Building building;
    private BigDecimal electricityPrice;
    private BigDecimal waterPrice;
    private BigDecimal serviceFee;
    private BigDecimal parkingFee;
    private BigDecimal otherFee;
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