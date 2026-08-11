package com.minhtan.qlptbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.RoomMedia;

import java.util.List;

public interface RoomMediaRepository extends JpaRepository<RoomMedia, Integer> {
    List<RoomMedia> findByMediaId(Integer mediaId);

    List<RoomMedia> findByRoomId(Integer roomId);

    List<RoomMedia> findByMediaType(Byte mediaType);

    List<RoomMedia> findByUrl(String url);

    List<RoomMedia> findBySortOrder(Integer sortOrder);

    void deleteByRoomId(Integer roomId);
}