package com.minhtan.qlptclient.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomMedia {
    private Integer mediaId;
    private Integer roomId;
    private Room room;
    private Byte mediaType; // 1=Image, 2=Video
    private String url;
    private Integer sortOrder;

    public RoomMedia() {
    }

    public RoomMedia(Integer mediaId, Integer roomId, Room room, Byte mediaType, String url, Integer sortOrder) {
        this.mediaId = mediaId;
        this.roomId = roomId;
        this.room = room;
        this.mediaType = mediaType;
        this.url = url;
        this.sortOrder = sortOrder;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Byte getMediaType() {
        return mediaType;
    }

    public void setMediaType(Byte mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "RoomMedia{" +
                "mediaId=" + mediaId +
                ", roomId=" + roomId +
                ", mediaType=" + mediaType +
                ", url='" + url + '\'' +
                ", sortOrder=" + sortOrder +
                '}';
    }
}