package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.SaleOff;
import com.minhtan.qlptbackend.service.SaleOffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saleoffs")
public class SaleOffController {

    private final SaleOffService saleOffService;

    public SaleOffController(SaleOffService saleOffService) {
        this.saleOffService = saleOffService;
    }

    /**
     * Lấy tất cả chương trình khuyến mãi
     */
    @GetMapping
    public ResponseEntity<List<SaleOff>> getAll() {
        return ResponseEntity.ok(saleOffService.findAll());
    }

    /**
     * Lấy chương trình khuyến mãi theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaleOff> getById(@PathVariable Integer id) {
        SaleOff saleOff = saleOffService.findById(id);

        if (saleOff == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(saleOff);
    }

    /**
     * Lấy các chương trình khuyến mãi đang hoạt động
     */
    @GetMapping("/active")
    public ResponseEntity<List<SaleOff>> getActiveSaleOffs() {
        return ResponseEntity.ok(saleOffService.findActiveSaleOffs());
    }

    /**
     * Thêm mới chương trình khuyến mãi
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SaleOff saleOff) {
        try {
            SaleOff created = saleOffService.create(saleOff);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Cập nhật chương trình khuyến mãi
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody SaleOff saleOff) {

        try {
            SaleOff updated = saleOffService.update(id, saleOff);

            if (updated == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Xóa chương trình khuyến mãi
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        if (!saleOffService.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}