package com.minhtan.qlptbackend.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "Buildings")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BuildingId")
    private Integer buildingId;

    @Column(name = "DistrictId", nullable = false)
    private Integer districtId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DistrictId", insertable = false, updatable = false)
    @JsonIgnore
    private District district;

    @Column(name = "TrueAddress", nullable = false, length = 300)
    private String trueAddress;

    @Column(name = "FakeAddress", nullable = false, length = 300)
    private String fakeAddress;

    @Column(name = "Note")
    private String note;

    @Column(name = "OwnerPhone", length = 20)
    private String ownerPhone;

    @Column(name = "Latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "Longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    public Building() {
    }

    public Building(Integer buildingId, Integer districtId, String trueAddress, String fakeAddress, String note, String ownerPhone, BigDecimal latitude, BigDecimal longitude) {
        this.buildingId = buildingId;
        this.districtId = districtId;
        this.trueAddress = trueAddress;
        this.fakeAddress = fakeAddress;
        this.note = note;
        this.ownerPhone = ownerPhone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getTrueAddress() {
        return trueAddress;
    }

    public void setTrueAddress(String trueAddress) {
        this.trueAddress = trueAddress;
    }

    public String getFakeAddress() {
        return fakeAddress;
    }

    public void setFakeAddress(String fakeAddress) {
        this.fakeAddress = fakeAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Building{" +
                "buildingId=" + buildingId +
                ", districtId=" + districtId +
                ", trueAddress='" + trueAddress + '\'' +
                ", fakeAddress='" + fakeAddress + '\'' +
                ", note='" + note + '\'' +
                ", ownerPhone='" + ownerPhone + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
