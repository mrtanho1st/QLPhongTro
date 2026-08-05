package com.minhtan.qlptbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.RoomSaleOff;

public interface RoomSaleOffRepository extends JpaRepository<RoomSaleOff, RoomSaleOff.RoomSaleOffId> {
    List<RoomSaleOff> findByRoomId(Integer roomId);

    List<RoomSaleOff> findBySaleOffId(Integer saleOffId);

    void deleteByRoomId(Integer roomId);

    void deleteBySaleOffId(Integer saleOffId);
}
