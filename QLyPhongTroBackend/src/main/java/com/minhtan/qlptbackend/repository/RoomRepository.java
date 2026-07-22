package com.minhtan.qlptbackend.repository;

import com.minhtan.qlptbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByRoomId(Integer roomId);

    List<Room> findByBuildingId(Integer buildingId);

    List<Room> findByRoomCode(String roomCode);

    List<Room> findByPrice(BigDecimal price);

    List<Room> findByBedroom(Integer bedroom);

    List<Room> findByHasKitchen(Boolean hasKitchen);

    List<Room> findByPersonLimit(Integer personLimit);

    List<Room> findByPersonLimitGreaterThanEqual(Integer personLimit);

    List<Room> findByArea(BigDecimal area);

    List<Room> findByAreaGreaterThanEqual(BigDecimal area);

    List<Room> findByLocked(Boolean locked);

    List<Room> findByAvailableDate(LocalDate availableDate);

    List<Room> findByNote(String note);
}