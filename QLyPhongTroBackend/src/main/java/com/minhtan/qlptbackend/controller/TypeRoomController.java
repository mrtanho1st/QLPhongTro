package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.TypeRoom;
import com.minhtan.qlptbackend.service.TypeRoomService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/type-rooms")
public class TypeRoomController {

    private final TypeRoomService typeRoomService;

    public TypeRoomController(TypeRoomService typeRoomService) {
        this.typeRoomService = typeRoomService;
    }

    @GetMapping
    public List<TypeRoom> getAllTypeRooms() {
        return typeRoomService.getAllTypeRooms();
    }

    @GetMapping("/search/name")
    public List<TypeRoom> searchByName(@RequestParam String name) {
        return typeRoomService.searchByName(name);
    }

    @GetMapping("/{typeRoomId}")
    public ResponseEntity<TypeRoom> getTypeRoomById(@PathVariable Integer typeRoomId) {
        return typeRoomService.getTypeRoomById(typeRoomId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TypeRoom> createTypeRoom(@RequestBody TypeRoom typeRoom) {
        TypeRoom createdTypeRoom = typeRoomService.createTypeRoom(typeRoom);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTypeRoom);
    }

    @PutMapping("/{typeRoomId}")
    public ResponseEntity<TypeRoom> updateTypeRoom(@PathVariable Integer typeRoomId,
            @RequestBody TypeRoom typeRoom) {
        return typeRoomService.updateTypeRoom(typeRoomId, typeRoom)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{typeRoomId}")
    public ResponseEntity<Void> deleteTypeRoom(@PathVariable Integer typeRoomId) {
        if (!typeRoomService.deleteTypeRoom(typeRoomId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}