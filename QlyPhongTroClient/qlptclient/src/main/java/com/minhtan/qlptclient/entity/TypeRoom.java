package com.minhtan.qlptclient.entity;

public class TypeRoom {
    
    private Integer typeRoomId;
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
