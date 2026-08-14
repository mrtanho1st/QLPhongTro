package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.RoomMedia;
import com.minhtan.qlptbackend.repository.RoomMediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RoomMediaService {

    private final RoomMediaRepository roomMediaRepository;
    private final FileStorageService fileStorageService;

    public RoomMediaService(RoomMediaRepository roomMediaRepository,
            FileStorageService fileStorageService) {
        this.roomMediaRepository = roomMediaRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Chuyển url trong entity thành URL public.
     */
    private RoomMedia toPublic(RoomMedia roomMedia) {

        if (roomMedia != null && roomMedia.getUrl() != null) {
            roomMedia.setUrl(fileStorageService.toPublicUrl(roomMedia.getUrl()));
        }

        return roomMedia;
    }

    public List<RoomMedia> getAllRoomMedia() {

        List<RoomMedia> medias = roomMediaRepository.findAll();

        medias.forEach(this::toPublic);

        return medias;
    }

    public List<RoomMedia> searchByRoomId(Integer roomId) {

        List<RoomMedia> medias = roomMediaRepository.findByRoomId(roomId);

        medias.forEach(this::toPublic);

        return medias;
    }

    public List<RoomMedia> searchByMediaType(Byte mediaType) {

        List<RoomMedia> medias = roomMediaRepository.findByMediaType(mediaType);

        medias.forEach(this::toPublic);

        return medias;
    }

    public List<RoomMedia> searchByUrl(String absolutePath) {

        String relativePath = fileStorageService.toRelativePath(absolutePath);

        List<RoomMedia> medias = roomMediaRepository.findByUrl(relativePath);

        medias.forEach(this::toPublic);

        return medias;
    }

    public List<RoomMedia> searchBySortOrder(Integer sortOrder) {

        List<RoomMedia> medias = roomMediaRepository.findBySortOrder(sortOrder);

        medias.forEach(this::toPublic);

        return medias;
    }

    public Optional<RoomMedia> getRoomMediaById(Integer mediaId) {

        return roomMediaRepository.findById(mediaId)
                .map(this::toPublic);
    }

    /**
     * Upload file từ MultipartFile và lưu vào database.
     * 
     * Backend sẽ:
     * 1. Lưu file vào đĩa cứng: /var/www/PhongTro/uploads/{roomId}/...
     * 2. Lưu đường dẫn tương đối vào database: {roomId}/...
     */
    public RoomMedia uploadRoomMedia(Integer roomId, MultipartFile file, Byte mediaType, Integer sortOrder)
            throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }

        // Lưu file vào đĩa cứng
        String relativePath = fileStorageService.saveFile(roomId, file.getOriginalFilename(), file.getBytes());

        // Tạo entity và lưu vào database
        RoomMedia roomMedia = new RoomMedia();
        roomMedia.setRoomId(roomId);
        roomMedia.setUrl(relativePath);
        roomMedia.setMediaType(mediaType);
        roomMedia.setSortOrder(sortOrder);

        RoomMedia saved = roomMediaRepository.save(roomMedia);

        return toPublic(saved);
    }

    /**
     * Lưu vào database dưới dạng đường dẫn tương đối.
     */
    public RoomMedia createRoomMedia(RoomMedia roomMedia) {

        roomMedia.setMediaId(null);

        roomMedia.setUrl(
                fileStorageService.toRelativePath(roomMedia.getUrl()));

        RoomMedia saved = roomMediaRepository.save(roomMedia);

        return toPublic(saved);
    }

    /**
     * Cập nhật và lưu đường dẫn tương đối.
     */
    public Optional<RoomMedia> updateRoomMedia(Integer mediaId,
            RoomMedia roomMediaRequest) {

        return roomMediaRepository.findById(mediaId).map(existingRoomMedia -> {

            existingRoomMedia.setRoomId(roomMediaRequest.getRoomId());
            existingRoomMedia.setMediaType(roomMediaRequest.getMediaType());
            existingRoomMedia.setSortOrder(roomMediaRequest.getSortOrder());

            existingRoomMedia.setUrl(
                    fileStorageService.toRelativePath(roomMediaRequest.getUrl()));

            RoomMedia saved = roomMediaRepository.save(existingRoomMedia);

            return toPublic(saved);
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