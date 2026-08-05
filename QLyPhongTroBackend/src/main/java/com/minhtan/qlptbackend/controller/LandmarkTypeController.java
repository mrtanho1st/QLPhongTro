package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.LandmarkType;
import com.minhtan.qlptbackend.service.LandmarkTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landmark-types")
public class LandmarkTypeController {

    private final LandmarkTypeService landmarkTypeService;

    public LandmarkTypeController(LandmarkTypeService landmarkTypeService) {
        this.landmarkTypeService = landmarkTypeService;
    }

    /**
     * Lấy tất cả loại địa điểm
     */
    @GetMapping
    public ResponseEntity<List<LandmarkType>> getAll() {
        return ResponseEntity.ok(landmarkTypeService.findAll());
    }

    /**
     * Lấy theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LandmarkType> getById(@PathVariable Integer id) {

        LandmarkType landmarkType = landmarkTypeService.findById(id);

        if (landmarkType == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(landmarkType);
    }

    /**
     * Thêm mới loại địa điểm
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody LandmarkType landmarkType) {

        try {
            LandmarkType created = landmarkTypeService.create(landmarkType);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Cập nhật loại địa điểm
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody LandmarkType landmarkType) {

        try {
            LandmarkType updated = landmarkTypeService.update(id, landmarkType);

            if (updated == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Xóa loại địa điểm
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        if (!landmarkTypeService.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}