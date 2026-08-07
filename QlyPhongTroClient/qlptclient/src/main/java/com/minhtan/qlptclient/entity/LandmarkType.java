package com.minhtan.qlptclient.entity;

public class LandmarkType {
    
    private Integer landmarkTypeId;
    private String landmarkTypeName;

    public LandmarkType() {
    }

    public LandmarkType(Integer landmarkTypeId, String landmarkTypeName) {
        this.landmarkTypeId = landmarkTypeId;
        this.landmarkTypeName = landmarkTypeName;
    }

    public Integer getLandmarkTypeId() {
        return landmarkTypeId;
    }

    public void setLandmarkTypeId(Integer landmarkTypeId) {
        this.landmarkTypeId = landmarkTypeId;
    }

    public String getLandmarkTypeName() {
        return landmarkTypeName;
    }

    public void setLandmarkTypeName(String landmarkTypeName) {
        this.landmarkTypeName = landmarkTypeName;
    }

    @Override
    public String toString() {
        return "LandmarkType{" +
                "landmarkTypeId=" + landmarkTypeId +
                ", landmarkTypeName='" + landmarkTypeName + '\'' +
                '}';
    }
}