package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.Amenity;
import com.minhtan.qlptbackend.repository.AmenityRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @Cacheable(value = "amenities", key = "'all'")
    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    @Cacheable(value = "amenities", key = "'byName:' + #name")
    public List<Amenity> searchByName(String name) {
        return amenityRepository.findByName(name);
    }

    @Cacheable(value = "amenities", key = "'byId:' + #amenityId")
    public Optional<Amenity> getAmenityById(Integer amenityId) {
        return amenityRepository.findById(amenityId);
    }

    @CacheEvict(value = "amenities", allEntries = true)
    public Amenity createAmenity(Amenity amenity) {
        amenity.setAmenityId(null);
        return amenityRepository.save(amenity);
    }

    @CacheEvict(value = "amenities", allEntries = true)
    public Optional<Amenity> updateAmenity(Integer amenityId, Amenity amenityRequest) {
        return amenityRepository.findById(amenityId).map(existingAmenity -> {
            existingAmenity.setName(amenityRequest.getName());
            return amenityRepository.save(existingAmenity);
        });
    }

    @CacheEvict(value = "amenities", allEntries = true)
    public boolean deleteAmenity(Integer amenityId) {
        if (!amenityRepository.existsById(amenityId)) {
            return false;
        }

        amenityRepository.deleteById(amenityId);
        return true;
    }
}