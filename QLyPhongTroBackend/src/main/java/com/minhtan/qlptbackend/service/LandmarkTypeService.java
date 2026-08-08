package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.LandmarkType;
import com.minhtan.qlptbackend.repository.LandmarkTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LandmarkTypeService {

    private final LandmarkTypeRepository landmarkTypeRepository;

    public LandmarkTypeService(LandmarkTypeRepository landmarkTypeRepository) {
        this.landmarkTypeRepository = landmarkTypeRepository;
    }

    /**
     * Lấy tất cả loại địa điểm
     */
    public List<LandmarkType> findAll() {
        return landmarkTypeRepository.findAll();
    }

    /**
     * Lấy theo ID
     */
    public LandmarkType findById(Integer id) {
        return landmarkTypeRepository.findById(id).orElse(null);
    }

    public List<LandmarkType> findByName(String keyword) {
        return landmarkTypeRepository.findByLandmarkTypeNameContainingIgnoreCase(keyword);
    }

    /**
     * Thêm mới
     */
    public LandmarkType create(LandmarkType landmarkType) {
        if (landmarkTypeRepository.existsByLandmarkTypeNameIgnoreCase(
                landmarkType.getLandmarkTypeName().trim())) {
            throw new IllegalArgumentException("Loại địa điểm đã tồn tại.");
        }

        validate(landmarkType);

        landmarkType.setLandmarkTypeId(null);

        return landmarkTypeRepository.save(landmarkType);
    }

    /**
     * Cập nhật
     */
    public LandmarkType update(Integer id, LandmarkType landmarkType) {

        LandmarkType existing = findById(id);

        if (existing == null) {
            return null;
        }

        validate(landmarkType);

        existing.setLandmarkTypeName(landmarkType.getLandmarkTypeName());

        return landmarkTypeRepository.save(existing);
    }

    /**
     * Xóa
     */
    public boolean delete(Integer id) {

        if (!landmarkTypeRepository.existsById(id)) {
            return false;
        }

        landmarkTypeRepository.deleteById(id);
        return true;
    }

    /**
     * Kiểm tra dữ liệu
     */
    private void validate(LandmarkType landmarkType) {

        if (landmarkType.getLandmarkTypeName() == null
                || landmarkType.getLandmarkTypeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại địa điểm không được để trống.");
        }
    }
}