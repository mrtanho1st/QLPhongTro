package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.District;
import com.minhtan.qlptbackend.repository.DistrictRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DistrictService {

    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @Cacheable(value = "districts", key = "'all'")
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    @Cacheable(value = "districts", key = "'byName:' + #name")
    public List<District> searchByName(String name) {
        return districtRepository.findByDistrictNameContainingIgnoreCase(name);
    }

    @Cacheable(value = "districts", key = "'byId:' + #districtId")
    public Optional<District> getDistrictById(Integer districtId) {
        return districtRepository.findById(districtId);
    }

    @CacheEvict(value = "districts", allEntries = true)
    public District createDistrict(District district) {
        district.setDistrictId(null);
        return districtRepository.save(district);
    }

    @CacheEvict(value = "districts", allEntries = true)
    public Optional<District> updateDistrict(Integer districtId, District districtRequest) {
        return districtRepository.findById(districtId).map(existingDistrict -> {
            existingDistrict.setDistrictName(districtRequest.getDistrictName());
            return districtRepository.save(existingDistrict);
        });
    }

    @CacheEvict(value = "districts", allEntries = true)
    public boolean deleteDistrict(Integer districtId) {
        if (!districtRepository.existsById(districtId)) {
            return false;
        }
        districtRepository.deleteById(districtId);
        return true;
    }
}