package com.minhtan.qlptbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.Commission;

import java.math.BigDecimal;
import java.util.List;

public interface CommissionRepository extends JpaRepository<Commission, Integer> {
    List<Commission> findByCommissionId(Integer commissionId);

    List<Commission> findByBuildingId(Integer buildingId);

    List<Commission> findByContractMonth(Integer contractMonth);

    List<Commission> findByCommissionPercent(BigDecimal commissionPercent);

    List<Commission> findByDeposit(BigDecimal deposit);

    void deleteByBuildingId(Integer buildingId);
}