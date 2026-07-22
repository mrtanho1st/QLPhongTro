package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.RoomAmenity;
import com.minhtan.qlptbackend.service.RoomAmenityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-amenities")
public class RoomAmenityController {

    private final RoomAmenityService roomAmenityService;

    public RoomAmenityController(RoomAmenityService roomAmenityService) {
        this.roomAmenityService = roomAmenityService;
    }

    @GetMapping
    public List<RoomAmenity> getAllRoomAmenities() {
        return roomAmenityService.getAllRoomAmenities();
    }

    @GetMapping("/search/room-id/{roomId}")
    public List<RoomAmenity> searchByRoomId(@PathVariable Integer roomId) {
        return roomAmenityService.searchByRoomId(roomId);
    }

    @GetMapping("/search/amenity-id/{amenityId}")
    public List<RoomAmenity> searchByAmenityId(@PathVariable Integer amenityId) {
        return roomAmenityService.searchByAmenityId(amenityId);
    }

    @GetMapping("/{roomId}/{amenityId}")
    public ResponseEntity<RoomAmenity> getRoomAmenityById(@PathVariable Integer roomId,
            @PathVariable Integer amenityId) {
        return roomAmenityService.getRoomAmenityById(roomId, amenityId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoomAmenity> createRoomAmenity(@RequestBody RoomAmenity roomAmenity) {
        RoomAmenity createdRoomAmenity = roomAmenityService.createRoomAmenity(roomAmenity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoomAmenity);
    }

    @PutMapping("/{roomId}/{amenityId}")
    public ResponseEntity<RoomAmenity> updateRoomAmenity(@PathVariable Integer roomId,
            @PathVariable Integer amenityId,
            @RequestBody RoomAmenity roomAmenity) {
        return roomAmenityService.updateRoomAmenity(roomId, amenityId, roomAmenity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{roomId}/{amenityId}")
    public ResponseEntity<Void> deleteRoomAmenity(@PathVariable Integer roomId,
            @PathVariable Integer amenityId) {
        if (!roomAmenityService.deleteRoomAmenity(roomId, amenityId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}