package com.minhtan.qlptbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.Amenity;

import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Integer> {
    List<Amenity> findByName(String name);
}