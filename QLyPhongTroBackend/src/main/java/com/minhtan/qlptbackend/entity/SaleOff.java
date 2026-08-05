package com.minhtan.qlptbackend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "SaleOffs")
public class SaleOff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SaleOffId")
    private Integer saleOffId;

    @Column(name = "SaleOffName", length = 100, nullable = false)
    private String saleOffName;

    @Column(name = "DiscountAmount", precision = 18, scale = 2, nullable = false)
    private BigDecimal discountAmount;

    @Column(name = "StartDate", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    public SaleOff() {
    }

    public SaleOff(Integer saleOffId, String saleOffName, BigDecimal discountAmount, LocalDateTime startDate,
            LocalDateTime endDate, Boolean isActive) {
        this.saleOffId = saleOffId;
        this.saleOffName = saleOffName;
        this.discountAmount = discountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public Integer getSaleOffId() {
        return saleOffId;
    }

    public void setSaleOffId(Integer saleOffId) {
        this.saleOffId = saleOffId;
    }

    public String getSaleOffName() {
        return saleOffName;
    }

    public void setSaleOffName(String saleOffName) {
        this.saleOffName = saleOffName;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "SaleOff{" +
                "saleOffId=" + saleOffId +
                ", saleOffName='" + saleOffName + '\'' +
                ", discountAmount=" + discountAmount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isActive=" + isActive +
                '}';
    }

}
