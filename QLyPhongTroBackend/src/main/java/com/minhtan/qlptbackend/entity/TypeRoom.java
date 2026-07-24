package com.minhtan.qlptbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "TypeRooms")
public class TypeRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TypeRoomId")
    private Integer typeRoomId;

    @Column(name = "TypeRoomName", length = 100)
    private String typeRoomName;

    public TypeRoom() {
    }

    public TypeRoom(Integer typeRoomId, String typeRoomName) {
        this.typeRoomId = typeRoomId;
        this.typeRoomName = typeRoomName;
    }

    public Integer getTypeRoomId() {
        return typeRoomId;
    }

    public void setTypeRoomId(Integer typeRoomId) {
        this.typeRoomId = typeRoomId;
    }

    public String getTypeRoomName() {
        return typeRoomName;
    }

    public void setTypeRoomName(String typeRoomName) {
        this.typeRoomName = typeRoomName;
    }

    @Override
    public String toString() {
        return "TypeRoom{" +
                "typeRoomId=" + typeRoomId +
                ", typeRoomName='" + typeRoomName + '\'' +
                '}';
    }
}