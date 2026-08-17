package com.minhtan.qlptbackend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "RoomSaleOffs")
@IdClass(RoomSaleOff.RoomSaleOffId.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RoomSaleOff {
    @Id
    @Column(name = "RoomId")
    private Integer roomId;

    @Id
    @Column(name = "SaleOffId")
    private Integer saleOffId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoomId", insertable = false, updatable = false)
    @JsonIgnore
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SaleOffId", insertable = false, updatable = false)
    @JsonIgnore
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

    public static class RoomSaleOffId implements Serializable {
        private Integer roomId;
        private Integer saleOffId;

        public RoomSaleOffId() {
        }

        public RoomSaleOffId(Integer roomId, Integer saleOffId) {
            this.roomId = roomId;
            this.saleOffId = saleOffId;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RoomSaleOffId that = (RoomSaleOffId) o;

            if (roomId != null ? !roomId.equals(that.roomId) : that.roomId != null) {
                return false;
            }
            return saleOffId != null ? saleOffId.equals(that.saleOffId) : that.saleOffId == null;
        }

        @Override
        public int hashCode() {
            int result = roomId != null ? roomId.hashCode() : 0;
            result = 31 * result + (saleOffId != null ? saleOffId.hashCode() : 0);
            return result;
        }
    }
}