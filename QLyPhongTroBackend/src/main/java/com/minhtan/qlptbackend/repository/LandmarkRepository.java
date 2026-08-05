package com.minhtan.qlptbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.Landmark;

public interface LandmarkRepository extends JpaRepository<Landmark, Integer> {
    List<Landmark> findByLandmarkTypesId(Integer landmarkTypesId);

    List<Landmark> findByIsActiveTrue();

    List<Landmark> findByLandmarkNameContainingIgnoreCase(String keyword);

    List<Landmark> findByAddressContainingIgnoreCase(String keyword);
}
