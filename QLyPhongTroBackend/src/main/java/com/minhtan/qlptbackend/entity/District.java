package com.minhtan.qlptbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "Districts")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DistrictId")
    private Integer districtId;

    @Column(name = "DistrictName", length = 100)
    private String districtName;

    public District() {
    }

    public District(Integer districtId, String districtName) {
        this.districtId = districtId;
        this.districtName = districtName;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @Override
    public String toString() {
        return "District{" +
                "districtId=" + districtId +
                ", districtName='" + districtName + '\'' +
                '}';
    }
}