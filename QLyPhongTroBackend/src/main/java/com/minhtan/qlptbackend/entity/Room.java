package com.minhtan.qlptbackend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomId")
    private Integer roomId;

    @Column(name = "BuildingId", nullable = false)
    private Integer buildingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BuildingId", insertable = false, updatable = false)
    private Building building;

    @Column(name = "TypeRoomId", nullable = false)
    private Integer typeRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TypeRoomId", insertable = false, updatable = false)
    private TypeRoom typeRoom;

    @Column(name = "RoomCode", length = 20)
    private String roomCode;

    @Column(name = "Price", precision = 12, scale = 0)
    private BigDecimal price;

    @Column(name = "Bedroom")
    private Integer bedroom;

    @Column(name = "PersonLimit")
    private Integer personLimit;

    @Column(name = "Area", precision = 5, scale = 2)
    private BigDecimal area;

    @Column(name = "Locked")
    private Boolean locked;

    @Column(name = "AvailableDate")
    private LocalDate availableDate;

    @Column(name = "Note")
    private String note;

    public Room() {
    }

    public Room(Integer roomId, Integer buildingId, Building building, Integer typeRoomId, TypeRoom typeRoom, String roomCode, BigDecimal price,
            Integer bedroom, Integer personLimit, BigDecimal area, Boolean locked, LocalDate availableDate,
            String note) {
        this.roomId = roomId;
        this.buildingId = buildingId;
        this.building = building;
        this.typeRoomId = typeRoomId;
        this.typeRoom = typeRoom;
        this.roomCode = roomCode;
        this.price = price;
        this.bedroom = bedroom;
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

    public Integer getTypeRoomId() {
        return typeRoomId;
    }

    public void setTypeRoomId(Integer typeRoomId) {
        this.typeRoomId = typeRoomId;
    }

    public TypeRoom getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(TypeRoom typeRoom) {
        this.typeRoom = typeRoom;
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
                ", typeRoomId=" + typeRoomId +
                ", roomCode='" + roomCode + '\'' +
                ", price=" + price +
                ", bedroom=" + bedroom +
                ", personLimit=" + personLimit +
                ", area=" + area +
                ", locked=" + locked +
                ", availableDate=" + availableDate +
                ", note='" + note + '\'' +
                '}';
    }
}