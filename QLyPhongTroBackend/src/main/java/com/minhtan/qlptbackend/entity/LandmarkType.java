package com.minhtan.qlptbackend.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "LandmarkTypes")
public class LandmarkType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LandmarkTypeId")
    private Integer landmarkTypeId;

    @Column(name = "LandmarkTypeName", length = 100)
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