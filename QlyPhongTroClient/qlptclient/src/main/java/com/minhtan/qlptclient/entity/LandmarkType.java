package com.minhtan.qlptclient.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LandmarkType {
    
    private Integer landmarkTypeId;
    private String name;

    public LandmarkType() {
    }

    public LandmarkType(Integer landmarkTypeId, String name) {
        this.landmarkTypeId = landmarkTypeId;
        this.name = name;
    }

    public Integer getLandmarkTypeId() {
        return landmarkTypeId;
    }

    public void setLandmarkTypeId(Integer landmarkTypeId) {
        this.landmarkTypeId = landmarkTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LandmarkType{" +
                "landmarkTypeId=" + landmarkTypeId +
                ", name='" + name + '\'' +
                '}';
    }
}