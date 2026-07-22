package com.minhtan.qlptclient.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Building {

    private Integer buildingId;
    private String trueAddress;
    private String fakeAddress;
    private String note;
    private String ownerPhone;

    public Building() {
    }

    public Building(Integer buildingId, String trueAddress, String fakeAddress, String note, String ownerPhone) {
        this.buildingId = buildingId;
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

    @Override
    public String toString() {
        return "Building{" +
                "buildingId=" + buildingId +
                ", fakeAddress='" + fakeAddress + '\'' +
                ", note='" + note + '\'' +
                ", ownerPhone='" + ownerPhone + '\'' +
                '}';
    }
}
