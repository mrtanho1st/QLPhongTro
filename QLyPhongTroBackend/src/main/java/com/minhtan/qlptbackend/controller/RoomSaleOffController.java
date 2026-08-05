package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.RoomSaleOff;
import com.minhtan.qlptbackend.service.RoomSaleOffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-saleoffs")
public class RoomSaleOffController {

    private final RoomSaleOffService roomSaleOffService;

    public RoomSaleOffController(RoomSaleOffService roomSaleOffService) {
        this.roomSaleOffService = roomSaleOffService;
    }

    /**
     * Lấy tất cả RoomSaleOff
     */
    @GetMapping
    public ResponseEntity<List<RoomSaleOff>> getAll() {
        return ResponseEntity.ok(roomSaleOffService.findAll());
    }

    /**
     * Lấy theo khóa chính (RoomId + SaleOffId)
     */
    @GetMapping("/{roomId}/{saleOffId}")
    public ResponseEntity<RoomSaleOff> getById(
            @PathVariable Integer roomId,
            @PathVariable Integer saleOffId) {

        RoomSaleOff roomSaleOff = roomSaleOffService.findById(roomId, saleOffId);

        if (roomSaleOff == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(roomSaleOff);
    }

    /**
     * Gán khuyến mãi cho phòng
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoomSaleOff roomSaleOff) {

        try {
            RoomSaleOff created = roomSaleOffService.create(roomSaleOff);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Xóa khuyến mãi khỏi phòng
     */
    @DeleteMapping("/{roomId}/{saleOffId}")
    public ResponseEntity<?> delete(
            @PathVariable Integer roomId,
            @PathVariable Integer saleOffId) {

        if (!roomSaleOffService.delete(roomId, saleOffId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Kiểm tra phòng đã có khuyến mãi này chưa
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(
            @RequestParam Integer roomId,
            @RequestParam Integer saleOffId) {

        return ResponseEntity.ok(
                roomSaleOffService.exists(roomId, saleOffId));
    }

    /**
     * Lấy các khuyến mãi của một phòng
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<RoomSaleOff>> getByRoomId(
            @PathVariable Integer roomId) {

        return ResponseEntity.ok(
                roomSaleOffService.findByRoomId(roomId));
    }

    /**
     * Lấy các phòng áp dụng một chương trình khuyến mãi
     */
    @GetMapping("/saleoff/{saleOffId}")
    public ResponseEntity<List<RoomSaleOff>> getBySaleOffId(
            @PathVariable Integer saleOffId) {

        return ResponseEntity.ok(
                roomSaleOffService.findBySaleOffId(saleOffId));
    }

    /**
     * Xóa tất cả khuyến mãi của một phòng
     */
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteAllByRoom(
            @PathVariable Integer roomId) {

        roomSaleOffService.deleteAllByRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Xóa tất cả phòng của một chương trình khuyến mãi
     */
    @DeleteMapping("/saleoff/{saleOffId}")
    public ResponseEntity<Void> deleteAllBySaleOff(
            @PathVariable Integer saleOffId) {

        roomSaleOffService.deleteAllBySaleOff(saleOffId);
        return ResponseEntity.noContent().build();
    }
}