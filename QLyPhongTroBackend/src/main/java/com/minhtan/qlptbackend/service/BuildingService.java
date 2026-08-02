package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.Building;
import com.minhtan.qlptbackend.entity.Room;
import com.minhtan.qlptbackend.entity.RoomMedia;
import com.minhtan.qlptbackend.repository.BuildingFeeRepository;
import com.minhtan.qlptbackend.repository.BuildingRepository;
import com.minhtan.qlptbackend.repository.CommissionRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final RoomService roomService;
    private final BuildingFeeRepository buildingFeeRepository;
    private final CommissionRepository commissionRepository;

    public BuildingService(BuildingRepository buildingRepository, RoomService roomService,
            BuildingFeeRepository buildingFeeRepository, CommissionRepository commissionRepository) {
        this.buildingRepository = buildingRepository;
        this.roomService = roomService;
        this.buildingFeeRepository = buildingFeeRepository;
        this.commissionRepository = commissionRepository;
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
        return buildingRepository.findByNoteContainingIgnoreCase(note);
    }

    public List<Building> searchByOwnerPhone(String ownerPhone) {
        return buildingRepository.findByOwnerPhone(ownerPhone);
    }

    public List<Building> searchByDistrictId(Integer districtId) {
        return buildingRepository.findByDistrictId(districtId);
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

    @Transactional
    public boolean deleteBuilding(Integer buildingId) {
        if (!buildingRepository.existsById(buildingId)) {
            return false;
        }
        List<Room> rooms = roomService.searchByBuildingId(buildingId);

        for (Room room : rooms) {
            roomService.deleteRoom(room.getRoomId());
        }
        commissionRepository.deleteByBuildingId(buildingId);
        buildingFeeRepository.deleteByBuildingId(buildingId);
        buildingRepository.deleteById(buildingId);
        return true;
    }

}