package com.minhtan.qlptbackend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.SaleOff;

public interface SaleOffRepository extends JpaRepository<SaleOff, Integer> {
    List<SaleOff> findByIsActiveTrue();

    List<SaleOff> findByEndDateAfter(LocalDateTime time);

    List<SaleOff> findByStartDateBeforeAndEndDateAfter(
            LocalDateTime start,
            LocalDateTime end);
}
