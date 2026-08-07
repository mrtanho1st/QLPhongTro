package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.Landmark;
import com.minhtan.qlptbackend.service.LandmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/landmarks")
public class LandmarkController {

    private final LandmarkService landmarkService;

    public LandmarkController(LandmarkService landmarkService) {
        this.landmarkService = landmarkService;
    }

    /**
     * Lấy tất cả địa điểm
     */
    @GetMapping
    public ResponseEntity<List<Landmark>> getAll() {
        return ResponseEntity.ok(landmarkService.findAll());
    }

    /**
     * Lấy địa điểm theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Landmark> getById(@PathVariable Integer id) {
        Landmark landmark = landmarkService.findById(id);

        if (landmark == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(landmark);
    }

    /**
     * Thêm mới địa điểm
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Landmark landmark) {
        try {
            Landmark created = landmarkService.create(landmark);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Cập nhật địa điểm
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody Landmark landmark) {

        try {
            Landmark updated = landmarkService.update(id, landmark);

            if (updated == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Xóa địa điểm
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        if (!landmarkService.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Tìm theo loại địa điểm
     */
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<Landmark>> getByType(@PathVariable Integer typeId) {
        return ResponseEntity.ok(landmarkService.findByType(typeId));
    }

    /**
     * Chỉ lấy các địa điểm đang hoạt động
     */
    @GetMapping("/active")
    public ResponseEntity<List<Landmark>> getActive() {
        return ResponseEntity.ok(landmarkService.findActive());
    }

    /**
     * Tìm theo tên
     * Ví dụ:
     * GET /api/landmarks/search/name?keyword=văn lang
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<Landmark>> searchByName(
            @RequestParam String keyword) {

        return ResponseEntity.ok(landmarkService.searchByName(keyword));
    }

    /**
     * Tìm theo địa chỉ
     * Ví dụ:
     * GET /api/landmarks/search/address?keyword=gò vấp
     */
    @GetMapping("/search/address")
    public ResponseEntity<List<Landmark>> searchByAddress(
            @RequestParam String keyword) {

        return ResponseEntity.ok(landmarkService.searchByAddress(keyword));
    }

    @GetMapping("/search/is-active")
    public ResponseEntity<List<Landmark>> searchByActive(
            @RequestParam Boolean keyword) {

        return ResponseEntity.ok(landmarkService.searchByActive(keyword));
    }

}