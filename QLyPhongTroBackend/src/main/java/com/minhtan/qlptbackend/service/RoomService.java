package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.Room;
import com.minhtan.qlptbackend.entity.TypeRoom;
import com.minhtan.qlptbackend.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> searchByBuildingId(Integer buildingId) {
        return roomRepository.findByBuildingId(buildingId);
    }

    public List<Room> searchByRoomCode(String roomCode) {
        return roomRepository.findByRoomCode(roomCode);
    }

    public List<Room> searchByPrice(BigDecimal price) {
        return roomRepository.findByPrice(price);
    }

    public List<Room> searchByTypeRoomId(Integer typeRoomId) {
        return roomRepository.findByTypeRoomId(typeRoomId);
    }

    public List<Room> searchByBedroom(Integer bedroom) {
        return roomRepository.findByBedroom(bedroom);
    }

    public List<Room> searchByHasKitchen(Boolean hasKitchen) {
        return roomRepository.findByHasKitchen(hasKitchen);
    }

    public List<Room> searchByPersonLimit(Integer personLimit) {
        return roomRepository.findByPersonLimit(personLimit);
    }

    public List<Room> searchByPersonLimitGreaterThanEqual(Integer personLimit) {
        return roomRepository.findByPersonLimitGreaterThanEqual(personLimit);
    }

    public List<Room> searchByArea(BigDecimal area) {
        return roomRepository.findByArea(area);
    }

    public List<Room> searchByAreaGreaterThanEqual(BigDecimal area) {
        return roomRepository.findByAreaGreaterThanEqual(area);
    }

    public List<Room> searchByLocked(Boolean locked) {
        return roomRepository.findByLocked(locked);
    }

    public List<Room> searchByAvailableDate(LocalDate availableDate) {
        return roomRepository.findByAvailableDate(availableDate);
    }

    public List<Room> searchByNote(String note) {
        return roomRepository.findByNote(note);
    }

    public Optional<Room> getRoomById(Integer roomId) {
        return roomRepository.findById(roomId);
    }

    public Room createRoom(Room room) {
        room.setRoomId(null);
        return roomRepository.save(room);
    }

    public Optional<Room> updateRoom(Integer roomId, Room roomRequest) {
        return roomRepository.findById(roomId).map(existingRoom -> {
            existingRoom.setBuildingId(roomRequest.getBuildingId());
            existingRoom.setTypeRoomId(roomRequest.getTypeRoomId());
            existingRoom.setRoomCode(roomRequest.getRoomCode());
            existingRoom.setPrice(roomRequest.getPrice());
            existingRoom.setBedroom(roomRequest.getBedroom());
            existingRoom.setPersonLimit(roomRequest.getPersonLimit());
            existingRoom.setArea(roomRequest.getArea());
            existingRoom.setLocked(roomRequest.getLocked());
            existingRoom.setAvailableDate(roomRequest.getAvailableDate());
            existingRoom.setNote(roomRequest.getNote());
            return roomRepository.save(existingRoom);
        });
    }

    public boolean deleteRoom(Integer roomId) {
        if (!roomRepository.existsById(roomId)) {
            return false;
        }

        roomRepository.deleteById(roomId);
        return true;
    }
}