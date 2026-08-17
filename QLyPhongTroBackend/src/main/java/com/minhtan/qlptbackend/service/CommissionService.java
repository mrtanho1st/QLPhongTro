package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.Commission;
import com.minhtan.qlptbackend.repository.CommissionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CommissionService {

    private final CommissionRepository commissionRepository;

    public CommissionService(CommissionRepository commissionRepository) {
        this.commissionRepository = commissionRepository;
    }

    @Cacheable(value = "commissions", key = "'all'")
    public List<Commission> getAllCommissions() {
        return commissionRepository.findAll();
    }

    @Cacheable(value = "commissions", key = "'byBuildingId:' + #buildingId")
    public List<Commission> searchByBuildingId(Integer buildingId) {
        return commissionRepository.findByBuildingId(buildingId);
    }

    public List<Commission> searchByContractMonth(Integer contractMonth) {
        return commissionRepository.findByContractMonth(contractMonth);
    }

    public List<Commission> searchByCommissionPercent(BigDecimal commissionPercent) {
        return commissionRepository.findByCommissionPercent(commissionPercent);
    }

    public List<Commission> searchByDeposit(BigDecimal deposit) {
        return commissionRepository.findByDeposit(deposit);
    }

    @Cacheable(value = "commissions", key = "'byId:' + #commissionId")
    public Optional<Commission> getCommissionById(Integer commissionId) {
        return commissionRepository.findById(commissionId);
    }

    @CacheEvict(value = "commissions", allEntries = true)
    public Commission createCommission(Commission commission) {
        commission.setCommissionId(null);
        return commissionRepository.save(commission);
    }

    @CacheEvict(value = "commissions", allEntries = true)
    public Optional<Commission> updateCommission(Integer commissionId, Commission commissionRequest) {
        return commissionRepository.findById(commissionId).map(existingCommission -> {
            existingCommission.setBuildingId(commissionRequest.getBuildingId());
            existingCommission.setContractMonth(commissionRequest.getContractMonth());
            existingCommission.setCommissionPercent(commissionRequest.getCommissionPercent());
            existingCommission.setDeposit(commissionRequest.getDeposit());
            return commissionRepository.save(existingCommission);
        });
    }

    @CacheEvict(value = "commissions", allEntries = true)
    public boolean deleteCommission(Integer commissionId) {
        if (!commissionRepository.existsById(commissionId)) {
            return false;
        }

        commissionRepository.deleteById(commissionId);
        return true;
    }
}