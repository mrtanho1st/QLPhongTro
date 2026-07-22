package com.minhtan.qlptclient.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Building {

    private Integer buildingId;
    private Integer districtId;
    private District district;
    private String trueAddress;
    private String fakeAddress;
    private String note;
    private String ownerPhone;

    public Building() {
    }

    public Building(Integer buildingId, Integer districtId, District district, String trueAddress, String fakeAddress, String note, String ownerPhone) {
        this.buildingId = buildingId;
        this.districtId = districtId;
        this.district = district;
        this.trueAddress = trueAddress;
        this.fakeAddress = fakeAddress;
        this.note = note;
        this.ownerPhone = ownerPhone;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
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

    @Override
    public String toString() {
        return "Building{" +
                "buildingId=" + buildingId +
                ", districtId=" + districtId +
                ", fakeAddress='" + fakeAddress + '\'' +
                ", note='" + note + '\'' +
                ", ownerPhone='" + ownerPhone + '\'' +
                '}';
    }
}
