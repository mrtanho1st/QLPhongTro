package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.RoomAmenity;
import com.minhtan.qlptbackend.repository.RoomAmenityRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomAmenityService {

    private final RoomAmenityRepository roomAmenityRepository;

    public RoomAmenityService(RoomAmenityRepository roomAmenityRepository) {
        this.roomAmenityRepository = roomAmenityRepository;
    }

    @Cacheable(value = "roomAmenities", key = "'all'")
    public List<RoomAmenity> getAllRoomAmenities() {
        return roomAmenityRepository.findAll();
    }

    @Cacheable(value = "roomAmenities", key = "'byRoomId:' + #roomId")
    public List<RoomAmenity> searchByRoomId(Integer roomId) {
        return roomAmenityRepository.findByRoomId(roomId);
    }

    public List<RoomAmenity> searchByAmenityId(Integer amenityId) {
        return roomAmenityRepository.findByAmenityId(amenityId);
    }

    @Cacheable(value = "roomAmenities", key = "'byId:' + #roomId + ':' + #amenityId")
    public Optional<RoomAmenity> getRoomAmenityById(Integer roomId, Integer amenityId) {
        return roomAmenityRepository.findById(new RoomAmenity.RoomAmenityId(roomId, amenityId));
    }

    @CacheEvict(value = "roomAmenities", allEntries = true)
    public RoomAmenity createRoomAmenity(RoomAmenity roomAmenity) {
        return roomAmenityRepository.save(roomAmenity);
    }

    @CacheEvict(value = "roomAmenities", allEntries = true)
    public Optional<RoomAmenity> updateRoomAmenity(Integer roomId, Integer amenityId, RoomAmenity roomAmenityRequest) {
        return roomAmenityRepository.findById(new RoomAmenity.RoomAmenityId(roomId, amenityId))
                .map(existingRoomAmenity -> {
                    roomAmenityRepository.delete(existingRoomAmenity);
                    return roomAmenityRepository.save(roomAmenityRequest);
                });
    }

    @CacheEvict(value = "roomAmenities", allEntries = true)
    public boolean deleteRoomAmenity(Integer roomId, Integer amenityId) {
        RoomAmenity.RoomAmenityId id = new RoomAmenity.RoomAmenityId(roomId, amenityId);
        if (!roomAmenityRepository.existsById(id)) {
            return false;
        }

        roomAmenityRepository.deleteById(id);
        return true;
    }
}