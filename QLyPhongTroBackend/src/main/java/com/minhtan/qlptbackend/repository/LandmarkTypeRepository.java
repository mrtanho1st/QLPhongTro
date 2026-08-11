package com.minhtan.qlptbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.LandmarkType;

public interface LandmarkTypeRepository extends JpaRepository<LandmarkType, Integer> {
    boolean existsByNameIgnoreCase(String landmarkTypeName);

    List<LandmarkType> findByNameContainingIgnoreCase(String keyword);
}
