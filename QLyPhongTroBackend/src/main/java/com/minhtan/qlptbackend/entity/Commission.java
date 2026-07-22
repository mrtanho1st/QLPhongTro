package com.minhtan.qlptbackend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Commissions")
public class Commission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CommissionId")
    private Integer commissionId;

    @Column(name = "BuildingId")
    private Integer buildingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BuildingId", insertable = false, updatable = false)
    private Building building;

    @Column(name = "ContractMonth")
    private Integer contractMonth;

    @Column(name = "CommissionPercent", precision = 5, scale = 2)
    private BigDecimal commissionPercent;

    @Column(name = "Deposit", precision = 3, scale = 1)
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