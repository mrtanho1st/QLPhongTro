package com.minhtan.qlptclient.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commission {

    private Integer commissionId;
    private Integer buildingId;
    private Building building;
    private Integer contractMonth;
    private BigDecimal commissionPercent;
    private BigDecimal deposit;

    public Commission() {
    }

    public Commission(Integer commissionId, Integer buildingId, Building building, Integer contractMonth,
            BigDecimal commissionPercent, BigDecimal deposit) {
        this.commissionId = commissionId;
        this.buildingId = buildingId;
        this.building = building;
        this.contractMonth = contractMonth;
        this.commissionPercent = commissionPercent;
        this.deposit = deposit;
    }

    public Integer getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(Integer commissionId) {
        this.commissionId = commissionId;
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

    public Integer getContractMonth() {
        return contractMonth;
    }

    public void setContractMonth(Integer contractMonth) {
        this.contractMonth = contractMonth;
    }

    public BigDecimal getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(BigDecimal commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    @Override
    public String toString() {
        return "Commission{" +
                "commissionId=" + commissionId +
                ", buildingId=" + buildingId +
                ", contractMonth=" + contractMonth +
                ", commissionPercent=" + commissionPercent +
                ", deposit=" + deposit +
                '}';
    }
}