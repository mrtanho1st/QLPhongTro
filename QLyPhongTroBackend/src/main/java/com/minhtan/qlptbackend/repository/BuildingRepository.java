package com.minhtan.qlptbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Integer> {

    List<Building> findByTrueAddressContainingIgnoreCase(String trueAddress);

    List<Building> findByFakeAddressContainingIgnoreCase(String fakeAddress);

    List<Building> findByNote(String note);

    List<Building> findByOwnerPhone(String ownerPhone);

}
