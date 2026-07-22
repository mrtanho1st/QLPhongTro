package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.RoomMedia;
import com.minhtan.qlptbackend.repository.RoomMediaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomMediaService {

    private final RoomMediaRepository roomMediaRepository;

    public RoomMediaService(RoomMediaRepository roomMediaRepository) {
        this.roomMediaRepository = roomMediaRepository;
    }

    public List<RoomMedia> getAllRoomMedia() {
        return roomMediaRepository.findAll();
    }

    public List<RoomMedia> searchByRoomId(Integer roomId) {
        return roomMediaRepository.findByRoomId(roomId);
    }

    public List<RoomMedia> searchByMediaType(Byte mediaType) {
        return roomMediaRepository.findByMediaType(mediaType);
    }

    public List<RoomMedia> searchByUrl(String url) {
        return roomMediaRepository.findByUrl(url);
    }

    public List<RoomMedia> searchBySortOrder(Integer sortOrder) {
        return roomMediaRepository.findBySortOrder(sortOrder);
    }

    public Optional<RoomMedia> getRoomMediaById(Integer mediaId) {
        return roomMediaRepository.findById(mediaId);
    }

    public RoomMedia createRoomMedia(RoomMedia roomMedia) {
        roomMedia.setMediaId(null);
        return roomMediaRepository.save(roomMedia);
    }

    public Optional<RoomMedia> updateRoomMedia(Integer mediaId, RoomMedia roomMediaRequest) {
        return roomMediaRepository.findById(mediaId).map(existingRoomMedia -> {
            existingRoomMedia.setRoomId(roomMediaRequest.getRoomId());
            existingRoomMedia.setMediaType(roomMediaRequest.getMediaType());
            existingRoomMedia.setUrl(roomMediaRequest.getUrl());
            existingRoomMedia.setSortOrder(roomMediaRequest.getSortOrder());
            return roomMediaRepository.save(existingRoomMedia);
        });
    }

    public boolean deleteRoomMedia(Integer mediaId) {
        if (!roomMediaRepository.existsById(mediaId)) {
            return false;
        }

        roomMediaRepository.deleteById(mediaId);
        return true;
    }
}