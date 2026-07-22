package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.Building;
import com.minhtan.qlptbackend.repository.BuildingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    public List<Building> searchByTrueAddress(String trueAddress) {
        return buildingRepository.findByTrueAddressContainingIgnoreCase(trueAddress);
    }

    public List<Building> searchByFakeAddress(String fakeAddress) {
        return buildingRepository.findByFakeAddressContainingIgnoreCase(fakeAddress);
    }

    public List<Building> searchByNote(String note) {
        return buildingRepository.findByNote(note);
    }

    public List<Building> searchByOwnerPhone(String ownerPhone) {
        return buildingRepository.findByOwnerPhone(ownerPhone);
    }

    public Optional<Building> getBuildingById(Integer buildingId) {
        return buildingRepository.findById(buildingId);
    }

    public Building createBuilding(Building building) {
        building.setBuildingId(null);
        return buildingRepository.save(building);
    }

    public Optional<Building> updateBuilding(Integer buildingId, Building buildingRequest) {
        return buildingRepository.findById(buildingId).map(existingBuilding -> {
            existingBuilding.setTrueAddress(buildingRequest.getTrueAddress());
            existingBuilding.setFakeAddress(buildingRequest.getFakeAddress());
            existingBuilding.setDistrictId(buildingRequest.getDistrictId());
            existingBuilding.setNote(buildingRequest.getNote());
            existingBuilding.setOwnerPhone(buildingRequest.getOwnerPhone());
            return buildingRepository.save(existingBuilding);
        });
    }

    public boolean deleteBuilding(Integer buildingId) {
        if (!buildingRepository.existsById(buildingId)) {
            return false;
        }

        buildingRepository.deleteById(buildingId);
        return true;
    }

}