package com.minhtan.qlptbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Amenities")
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AmenityId")
    private Integer amenityId;

    @Column(name = "Name", length = 100)
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