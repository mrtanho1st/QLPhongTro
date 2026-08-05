package com.minhtan.qlptbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.LandmarkType;

public interface LandmarkTypeRepository extends JpaRepository<LandmarkType, Integer> {
    boolean existsByLandmarkTypeNameIgnoreCase(String landmarkTypeName);

    LandmarkType findByLandmarkTypeNameIgnoreCase(String landmarkTypeName);
}
