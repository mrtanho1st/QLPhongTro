package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.District;
import com.minhtan.qlptbackend.repository.DistrictRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DistrictService {

    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    public List<District> searchByName(String name) {
        return districtRepository.findByDistrictNameContainingIgnoreCase(name);
    }

    public Optional<District> getDistrictById(Integer districtId) {
        return districtRepository.findById(districtId);
    }

    public District createDistrict(District district) {
        district.setDistrictId(null);
        return districtRepository.save(district);
    }

    public Optional<District> updateDistrict(Integer districtId, District districtRequest) {
        return districtRepository.findById(districtId).map(existingDistrict -> {
            existingDistrict.setDistrictName(districtRequest.getDistrictName());
            return districtRepository.save(existingDistrict);
        });
    }

    public boolean deleteDistrict(Integer districtId) {
        if (!districtRepository.existsById(districtId)) {
            return false;
        }
        districtRepository.deleteById(districtId);
        return true;
    }
}