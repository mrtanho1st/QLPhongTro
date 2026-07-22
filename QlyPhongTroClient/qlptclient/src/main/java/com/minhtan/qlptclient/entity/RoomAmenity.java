package com.minhtan.qlptclient.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomAmenity {

    private Integer roomId;
    private Integer amenityId;
    private Room room;
    private Amenity amenity;

    public RoomAmenity() {
    }

    public RoomAmenity(Integer roomId, Integer amenityId, Room room, Amenity amenity) {
        this.roomId = roomId;
        this.amenityId = amenityId;
        this.room = room;
        this.amenity = amenity;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(Integer amenityId) {
        this.amenityId = amenityId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Amenity getAmenity() {
        return amenity;
    }

    public void setAmenity(Amenity amenity) {
        this.amenity = amenity;
    }

    @Override
    public String toString() {
        return "RoomAmenity{" +
                "roomId=" + roomId +
                ", amenityId=" + amenityId +
                '}';
    }

    public static class RoomAmenityId implements Serializable {
        private Integer roomId;
        private Integer amenityId;

        public RoomAmenityId() {
        }

        public RoomAmenityId(Integer roomId, Integer amenityId) {
            this.roomId = roomId;
            this.amenityId = amenityId;
        }

        public Integer getRoomId() {
            return roomId;
        }

        public void setRoomId(Integer roomId) {
            this.roomId = roomId;
        }

        public Integer getAmenityId() {
            return amenityId;
        }

        public void setAmenityId(Integer amenityId) {
            this.amenityId = amenityId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RoomAmenityId that = (RoomAmenityId) o;

            if (roomId != null ? !roomId.equals(that.roomId) : that.roomId != null) {
                return false;
            }
            return amenityId != null ? amenityId.equals(that.amenityId) : that.amenityId == null;
        }

        @Override
        public int hashCode() {
            int result = roomId != null ? roomId.hashCode() : 0;
            result = 31 * result + (amenityId != null ? amenityId.hashCode() : 0);
            return result;
        }
    }
}