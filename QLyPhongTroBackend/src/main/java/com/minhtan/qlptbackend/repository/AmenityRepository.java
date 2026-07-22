package com.minhtan.qlptbackend.repository;

import com.minhtan.qlptbackend.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Integer> {
    List<Amenity> findByAmenityId(Integer amenityId);

    List<Amenity> findByName(String name);
}