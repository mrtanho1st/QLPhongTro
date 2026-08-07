package com.minhtan.qlptclient.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomSaleOff {

    private Integer roomId;

    private Integer saleOffId;

    private Room room;

    private SaleOff saleOff;

    public RoomSaleOff() {
    }

    public RoomSaleOff(Integer roomId, Integer saleOffId, Room room, SaleOff saleOff) {
        this.roomId = roomId;
        this.saleOffId = saleOffId;
        this.room = room;
        this.saleOff = saleOff;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getSaleOffId() {
        return saleOffId;
    }

    public void setSaleOffId(Integer saleOffId) {
        this.saleOffId = saleOffId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public SaleOff getSaleOff() {
        return saleOff;
    }

    public void setSaleOff(SaleOff saleOff) {
        this.saleOff = saleOff;
    }

    @Override
    public String toString() {
        return "RoomSaleOff{" +
                "roomId=" + roomId +
                ", saleOffId=" + saleOffId +
                '}';
    }

}
