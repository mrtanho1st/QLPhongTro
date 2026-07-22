package com.minhtan.qlptbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.District;
import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findByDistrictName(String districtName);
}
