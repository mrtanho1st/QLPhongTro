package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.Amenity;
import com.minhtan.qlptbackend.repository.AmenityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    public List<Amenity> searchByName(String name) {
        return amenityRepository.findByName(name);
    }

    public Optional<Amenity> getAmenityById(Integer amenityId) {
        return amenityRepository.findById(amenityId);
    }

    public Amenity createAmenity(Amenity amenity) {
        amenity.setAmenityId(null);
        return amenityRepository.save(amenity);
    }

    public Optional<Amenity> updateAmenity(Integer amenityId, Amenity amenityRequest) {
        return amenityRepository.findById(amenityId).map(existingAmenity -> {
            existingAmenity.setName(amenityRequest.getName());
            return amenityRepository.save(existingAmenity);
        });
    }

    public boolean deleteAmenity(Integer amenityId) {
        if (!amenityRepository.existsById(amenityId)) {
            return false;
        }

        amenityRepository.deleteById(amenityId);
        return true;
    }
}