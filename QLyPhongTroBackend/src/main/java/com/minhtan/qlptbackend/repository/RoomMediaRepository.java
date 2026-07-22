package com.minhtan.qlptbackend.repository;

import com.minhtan.qlptbackend.entity.RoomMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomMediaRepository extends JpaRepository<RoomMedia, Integer> {
    List<RoomMedia> findByMediaId(Integer mediaId);

    List<RoomMedia> findByRoomId(Integer roomId);

    List<RoomMedia> findByMediaType(Byte mediaType);

    List<RoomMedia> findByUrl(String url);

    List<RoomMedia> findBySortOrder(Integer sortOrder);
}