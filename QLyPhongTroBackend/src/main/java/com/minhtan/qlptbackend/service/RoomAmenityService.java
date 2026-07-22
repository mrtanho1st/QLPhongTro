package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.RoomAmenity;
import com.minhtan.qlptbackend.repository.RoomAmenityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomAmenityService {

    private final RoomAmenityRepository roomAmenityRepository;

    public RoomAmenityService(RoomAmenityRepository roomAmenityRepository) {
        this.roomAmenityRepository = roomAmenityRepository;
    }

    public List<RoomAmenity> getAllRoomAmenities() {
        return roomAmenityRepository.findAll();
    }

    public List<RoomAmenity> searchByRoomId(Integer roomId) {
        return roomAmenityRepository.findByRoomId(roomId);
    }

    public List<RoomAmenity> searchByAmenityId(Integer amenityId) {
        return roomAmenityRepository.findByAmenityId(amenityId);
    }

    public Optional<RoomAmenity> getRoomAmenityById(Integer roomId, Integer amenityId) {
        return roomAmenityRepository.findById(new RoomAmenity.RoomAmenityId(roomId, amenityId));
    }

    public RoomAmenity createRoomAmenity(RoomAmenity roomAmenity) {
        return roomAmenityRepository.save(roomAmenity);
    }

    public Optional<RoomAmenity> updateRoomAmenity(Integer roomId, Integer amenityId, RoomAmenity roomAmenityRequest) {
        return roomAmenityRepository.findById(new RoomAmenity.RoomAmenityId(roomId, amenityId))
                .map(existingRoomAmenity -> {
                    roomAmenityRepository.delete(existingRoomAmenity);
                    return roomAmenityRepository.save(roomAmenityRequest);
                });
    }

    public boolean deleteRoomAmenity(Integer roomId, Integer amenityId) {
        RoomAmenity.RoomAmenityId id = new RoomAmenity.RoomAmenityId(roomId, amenityId);
        if (!roomAmenityRepository.existsById(id)) {
            return false;
        }

        roomAmenityRepository.deleteById(id);
        return true;
    }
}