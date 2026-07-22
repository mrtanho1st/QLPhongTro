package com.minhtan.qlptclient.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Amenity {
    private Integer amenityId;
    private String name;

    public Amenity() {
    }

    public Amenity(Integer amenityId, String name) {
        this.amenityId = amenityId;
        this.name = name;
    }

    public Integer getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(Integer amenityId) {
        this.amenityId = amenityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Amenity{" +
                "amenityId=" + amenityId +
                ", name='" + name + '\'' +
                '}';
    }
}