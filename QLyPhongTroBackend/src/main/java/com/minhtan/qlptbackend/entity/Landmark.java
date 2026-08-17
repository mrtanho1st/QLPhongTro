package com.minhtan.qlptbackend.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Landmarks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Landmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LandmarkId")
    private Integer landmarkId;

    @Column(name = "Name", length = 200)
    private String landmarkName;

    @Column(name = "LandmarkTypesId", nullable = false)
    private Integer landmarkTypesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LandmarkTypesId", insertable = false, updatable = false)
    @JsonIgnore
    private LandmarkType landmarkType;

    @Column(name = "Address", length = 300)
    private String address;

    @Column(name = "Latitude", precision = 10, scale = 8, nullable = false)
    private BigDecimal latitude;

    @Column(name = "Longitude", precision = 11, scale = 8, nullable = false)
    private BigDecimal longitude;

    @Column(name = "Description", length = 500)
    private String description;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;
    
    public Landmark() {
    }

    public Landmark(Integer landmarkId, String landmarkName, Integer landmarkTypesId, LandmarkType landmarkType, String address, BigDecimal latitude, BigDecimal longitude, String description, Boolean isActive) {
        this.landmarkId = landmarkId;
        this.landmarkName = landmarkName;
        this.landmarkTypesId = landmarkTypesId;
        this.landmarkType = landmarkType;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.isActive = isActive;
    }

    public Integer getLandmarkId() {
        return landmarkId;
    }

    public void setLandmarkId(Integer landmarkId) {
        this.landmarkId = landmarkId;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public void setLandmarkName(String landmarkName) {
        this.landmarkName = landmarkName;
    }

    public Integer getLandmarkTypesId() {
        return landmarkTypesId;
    }

    public void setLandmarkTypesId(Integer landmarkTypesId) {
        this.landmarkTypesId = landmarkTypesId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LandmarkType getLandmarkType() {
        return landmarkType;
    }

    public void setLandmarkType(LandmarkType landmarkType) {
        this.landmarkType = landmarkType;
    }

    @Override
    public String toString() {
        return "Landmarks{" +
                "landmarkId=" + landmarkId +
                ", landmarkName='" + landmarkName + '\'' +
                ", landmarkTypesId=" + landmarkTypesId +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
