package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.Landmark;
import com.minhtan.qlptbackend.repository.LandmarkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LandmarkService {

    private final LandmarkRepository landmarkRepository;

    public LandmarkService(LandmarkRepository landmarkRepository) {
        this.landmarkRepository = landmarkRepository;
    }

    /**
     * Lấy tất cả địa điểm
     */
    public List<Landmark> findAll() {
        return landmarkRepository.findAll();
    }

    /**
     * Lấy theo ID
     */
    public Landmark findById(Integer id) {
        return landmarkRepository.findById(id).orElse(null);
    }

    /**
     * Thêm mới
     */
    public Landmark create(Landmark landmark) {

        validate(landmark);

        landmark.setLandmarkId(null);

        return landmarkRepository.save(landmark);
    }

    /**
     * Cập nhật
     */
    public Landmark update(Integer id, Landmark landmark) {

        Landmark existing = findById(id);

        if (existing == null) {
            return null;
        }

        validate(landmark);

        existing.setLandmarkName(landmark.getLandmarkName());
        existing.setLandmarkTypesId(landmark.getLandmarkTypesId());
        existing.setAddress(landmark.getAddress());
        existing.setLatitude(landmark.getLatitude());
        existing.setLongitude(landmark.getLongitude());
        existing.setDescription(landmark.getDescription());
        existing.setIsActive(landmark.getIsActive());

        return landmarkRepository.save(existing);
    }

    /**
     * Xóa
     */
    public boolean delete(Integer id) {

        if (!landmarkRepository.existsById(id)) {
            return false;
        }

        landmarkRepository.deleteById(id);

        return true;
    }

    public List<Landmark> findByType(Integer landmarkTypeId) {
        return landmarkRepository.findByLandmarkTypesId(landmarkTypeId);
    }

    public List<Landmark> findActive() {
        return landmarkRepository.findByIsActiveTrue();
    }

    public List<Landmark> searchByName(String keyword) {
        return landmarkRepository.findByLandmarkNameContainingIgnoreCase(keyword);
    }

    public List<Landmark> searchByAddress(String keyword) {
        return landmarkRepository.findByAddressContainingIgnoreCase(keyword);
    }

    /**
     * Kiểm tra dữ liệu
     */
    private void validate(Landmark landmark) {

        if (landmark.getLandmarkName() == null ||
                landmark.getLandmarkName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên địa điểm không được để trống.");
        }

        if (landmark.getLandmarkTypesId() == null) {
            throw new IllegalArgumentException("Loại địa điểm không được để trống.");
        }

        if (landmark.getAddress() == null ||
                landmark.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống.");
        }

        if (landmark.getLatitude() == null) {
            throw new IllegalArgumentException("Latitude không được để trống.");
        }

        if (landmark.getLongitude() == null) {
            throw new IllegalArgumentException("Longitude không được để trống.");
        }

        if (landmark.getIsActive() == null) {
            landmark.setIsActive(true);
        }
    }
}