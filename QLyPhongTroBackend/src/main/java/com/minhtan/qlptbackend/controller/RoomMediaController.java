package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.RoomMedia;
import com.minhtan.qlptbackend.service.RoomMediaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/room-media")
public class RoomMediaController {

    private final RoomMediaService roomMediaService;

    public RoomMediaController(RoomMediaService roomMediaService) {
        this.roomMediaService = roomMediaService;
    }

    @GetMapping
    public List<RoomMedia> getAllRoomMedia() {
        return roomMediaService.getAllRoomMedia();
    }

    @GetMapping("/search/room-id/{roomId}")
    public List<RoomMedia> searchByRoomId(@PathVariable Integer roomId) {
        return roomMediaService.searchByRoomId(roomId);
    }

    @GetMapping("/search/media-type/{mediaType}")
    public List<RoomMedia> searchByMediaType(@PathVariable Byte mediaType) {
        return roomMediaService.searchByMediaType(mediaType);
    }

    @GetMapping("/search/url")
    public List<RoomMedia> searchByUrl(@RequestParam String url) {
        return roomMediaService.searchByUrl(url);
    }

    @GetMapping("/search/sort-order/{sortOrder}")
    public List<RoomMedia> searchBySortOrder(@PathVariable Integer sortOrder) {
        return roomMediaService.searchBySortOrder(sortOrder);
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<RoomMedia> getRoomMediaById(@PathVariable Integer mediaId) {
        return roomMediaService.getRoomMediaById(mediaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoomMedia> createRoomMedia(@RequestBody RoomMedia roomMedia) {
        RoomMedia createdRoomMedia = roomMediaService.createRoomMedia(roomMedia);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoomMedia);
    }

    @PostMapping("/upload")
    public ResponseEntity<RoomMedia> uploadRoomMedia(
            @RequestParam Integer roomId,
            @RequestParam MultipartFile file,
            @RequestParam Byte mediaType,
            @RequestParam(required = false) Integer sortOrder) {
        try {
            RoomMedia createdRoomMedia = roomMediaService.uploadRoomMedia(roomId, file, mediaType, sortOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoomMedia);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{mediaId}")
    public ResponseEntity<RoomMedia> updateRoomMedia(@PathVariable Integer mediaId,
            @RequestBody RoomMedia roomMedia) {
        return roomMediaService.updateRoomMedia(mediaId, roomMedia)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteRoomMedia(@PathVariable Integer mediaId) {
        if (!roomMediaService.deleteRoomMedia(mediaId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}