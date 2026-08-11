package com.minhtan.qlptbackend.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "LandmarkTypes")
public class LandmarkType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LandmarkTypesId")
    private Integer landmarkTypeId;

    @Column(name = "Name", length = 100)
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