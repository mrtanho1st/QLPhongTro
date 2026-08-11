package com.minhtan.qlptbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.RoomAmenity;

import java.util.List;

public interface RoomAmenityRepository extends JpaRepository<RoomAmenity, RoomAmenity.RoomAmenityId> {
    List<RoomAmenity> findByRoomId(Integer roomId);

    List<RoomAmenity> findByAmenityId(Integer amenityId);

    void deleteByRoomId(Integer roomId);

}