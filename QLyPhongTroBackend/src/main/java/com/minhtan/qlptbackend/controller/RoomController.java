package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.Building;
import com.minhtan.qlptbackend.entity.District;
import com.minhtan.qlptbackend.entity.Room;
import com.minhtan.qlptbackend.entity.TypeRoom;
import com.minhtan.qlptbackend.service.RoomService;
import com.minhtan.qlptbackend.service.TypeRoomService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final TypeRoomService typeRoomService;

    public RoomController(RoomService roomService, TypeRoomService typeRoomService) {
        this.roomService = roomService;
        this.typeRoomService = typeRoomService;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/search/building-id/{buildingId}")
    public List<Room> searchByBuildingId(@PathVariable Integer buildingId) {
        return roomService.searchByBuildingId(buildingId);
    }

    @GetMapping("/search/type-room-name")
    public List<Room> searchByTypeRoomName(@RequestParam String typeRoomName) {
        List<TypeRoom> typeRooms = typeRoomService.searchByName(typeRoomName);
        List<Room> rooms = new ArrayList<>();
        for (TypeRoom typeRoom : typeRooms) {
            List<Room> roomsWithTypeRoom = roomService.searchByTypeRoomId(typeRoom.getTypeRoomId());
            rooms.addAll(roomsWithTypeRoom);
        }
        return rooms;
    }

    @GetMapping("/search/room-code")
    public List<Room> searchByRoomCode(@RequestParam String roomCode) {
        return roomService.searchByRoomCode(roomCode);
    }

    @GetMapping("/search/price")
    public List<Room> searchByPrice(@RequestParam BigDecimal price) {
        return roomService.searchByPrice(price);
    }

    @GetMapping("/search/bedroom/{bedroom}")
    public List<Room> searchByBedroom(@PathVariable Integer bedroom) {
        return roomService.searchByBedroom(bedroom);
    }

    @GetMapping("/search/person-limit-ge/{personLimit}")
    public List<Room> searchByPersonLimitGreaterThanEqual(@PathVariable Integer personLimit) {
        return roomService.searchByPersonLimitGreaterThanEqual(personLimit);
    }

    @GetMapping("/search/area-ge")
    public List<Room> searchByAreaGreaterThanEqual(@RequestParam BigDecimal area) {
        return roomService.searchByAreaGreaterThanEqual(area);
    }

    @GetMapping("/search/locked/{locked}")
    public List<Room> searchByLocked(@PathVariable Boolean locked) {
        System.out.println("locked = " + locked);
        return roomService.searchByLocked(locked);
    }

    @GetMapping("/search/available-date/{availableDate}")
    public List<Room> searchByAvailableDate(@PathVariable LocalDate availableDate) {
        return roomService.searchByAvailableDate(availableDate);
    }

    @GetMapping("/search/note")
    public List<Room> searchByNote(@RequestParam String note) {
        return roomService.searchByNote(note);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer roomId) {
        return roomService.getRoomById(roomId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room createdRoom = roomService.createRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable Integer roomId,
            @RequestBody Room room) {
        return roomService.updateRoom(roomId, room)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer roomId) {
        if (!roomService.deleteRoom(roomId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}