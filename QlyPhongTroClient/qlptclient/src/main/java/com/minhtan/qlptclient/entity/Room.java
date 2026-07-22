package com.minhtan.qlptclient.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Room {
    private Integer roomId;
    private Integer buildingId;
    private Building building;
    private String roomCode;
    private BigDecimal price;
    private Integer bedroom;
    private Boolean hasKitchen;
    private Integer personLimit;
    private BigDecimal area;
    private Boolean locked;
    private LocalDate availableDate;
    private String note;

    public Room() {
    }

    public Room(Integer roomId, Integer buildingId, Building building, String roomCode, BigDecimal price,
            Integer bedroom,
            Boolean hasKitchen, Integer personLimit, BigDecimal area, Boolean locked, LocalDate availableDate,
            String note) {
        this.roomId = roomId;
        this.buildingId = buildingId;
        this.building = building;
        this.roomCode = roomCode;
        this.price = price;
        this.bedroom = bedroom;
        this.hasKitchen = hasKitchen;
        this.personLimit = personLimit;
        this.area = area;
        this.locked = locked;
        this.availableDate = availableDate;
        this.note = note;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getBedroom() {
        return bedroom;
    }

    public void setBedroom(Integer bedroom) {
        this.bedroom = bedroom;
    }

    public Boolean getHasKitchen() {
        return hasKitchen;
    }

    public void setHasKitchen(Boolean hasKitchen) {
        this.hasKitchen = hasKitchen;
    }

    public Integer getPersonLimit() {
        return personLimit;
    }

    public void setPersonLimit(Integer personLimit) {
        this.personLimit = personLimit;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", buildingId=" + buildingId +
                ", roomCode='" + roomCode + '\'' +
                ", price=" + price +
                ", bedroom=" + bedroom +
                ", hasKitchen=" + hasKitchen +
                ", personLimit=" + personLimit +
                ", area=" + area +
                ", locked=" + locked +
                ", availableDate=" + availableDate +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Room other)) {
            return false;
        }
        return Objects.equals(this.roomId, other.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }
}