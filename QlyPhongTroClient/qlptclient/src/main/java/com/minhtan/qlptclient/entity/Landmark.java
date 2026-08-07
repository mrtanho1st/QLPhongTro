package com.minhtan.qlptclient.entity;

import java.math.BigDecimal;

public class Landmark {

    private Integer landmarkId;

    private String landmarkName;

    private Integer landmarkTypesId;

    private LandmarkType landmarkType;

    private String address;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String description;

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
