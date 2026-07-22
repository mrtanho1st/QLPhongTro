package com.minhtan.qlptbackend.controller;

import com.minhtan.qlptbackend.entity.Commission;
import com.minhtan.qlptbackend.service.CommissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commissions")
public class CommissionController {

    private final CommissionService commissionService;

    public CommissionController(CommissionService commissionService) {
        this.commissionService = commissionService;
    }

    @GetMapping
    public List<Commission> getAllCommissions() {
        return commissionService.getAllCommissions();
    }

    @GetMapping("/search/building-id/{buildingId}")
    public List<Commission> searchByBuildingId(@PathVariable Integer buildingId) {
        return commissionService.searchByBuildingId(buildingId);
    }

    @GetMapping("/search/contract-month/{contractMonth}")
    public List<Commission> searchByContractMonth(@PathVariable Integer contractMonth) {
        return commissionService.searchByContractMonth(contractMonth);
    }

    @GetMapping("/search/commission-percent")
    public List<Commission> searchByCommissionPercent(@RequestParam java.math.BigDecimal commissionPercent) {
        return commissionService.searchByCommissionPercent(commissionPercent);
    }

    @GetMapping("/search/deposit")
    public List<Commission> searchByDeposit(@RequestParam java.math.BigDecimal deposit) {
        return commissionService.searchByDeposit(deposit);
    }

    @GetMapping("/{commissionId}")
    public ResponseEntity<Commission> getCommissionById(@PathVariable Integer commissionId) {
        return commissionService.getCommissionById(commissionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Commission> createCommission(@RequestBody Commission commission) {
        Commission createdCommission = commissionService.createCommission(commission);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommission);
    }

    @PutMapping("/{commissionId}")
    public ResponseEntity<Commission> updateCommission(@PathVariable Integer commissionId,
            @RequestBody Commission commission) {
        return commissionService.updateCommission(commissionId, commission)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{commissionId}")
    public ResponseEntity<Void> deleteCommission(@PathVariable Integer commissionId) {
        if (!commissionService.deleteCommission(commissionId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}