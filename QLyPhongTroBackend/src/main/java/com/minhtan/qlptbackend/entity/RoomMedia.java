package com.minhtan.qlptbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "RoomMedia")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RoomMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MediaId")
    private Integer mediaId;

    @Column(name = "RoomId", nullable = false)
    private Integer roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoomId", insertable = false, updatable = false)
    @JsonIgnore
    private Room room;

    @Column(name = "MediaType", nullable = false)
    private Byte mediaType;

    @Column(name = "Url", length = 1000, nullable = false)
    private String url;

    @Column(name = "SortOrder")
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