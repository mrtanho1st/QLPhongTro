package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.TypeRoom;
import com.minhtan.qlptbackend.repository.TypeRoomRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeRoomService {

    private final TypeRoomRepository typeRoomRepository;

    public TypeRoomService(TypeRoomRepository typeRoomRepository) {
        this.typeRoomRepository = typeRoomRepository;
    }

    @Cacheable(value = "typeRooms", key = "'all'")
    public List<TypeRoom> getAllTypeRooms() {
        return typeRoomRepository.findAll();
    }

    @Cacheable(value = "typeRooms", key = "'byName:' + #name")
    public List<TypeRoom> searchByName(String name) {
        return typeRoomRepository.findByTypeRoomNameContainingIgnoreCase(name);
    }

    @Cacheable(value = "typeRooms", key = "'byId:' + #typeRoomId")
    public Optional<TypeRoom> getTypeRoomById(Integer typeRoomId) {
        return typeRoomRepository.findById(typeRoomId);
    }

    @CacheEvict(value = "typeRooms", allEntries = true)
    public TypeRoom createTypeRoom(TypeRoom typeRoom) {
        typeRoom.setTypeRoomId(null);
        return typeRoomRepository.save(typeRoom);
    }

    @CacheEvict(value = "typeRooms", allEntries = true)
    public Optional<TypeRoom> updateTypeRoom(Integer typeRoomId, TypeRoom typeRoomRequest) {
        return typeRoomRepository.findById(typeRoomId).map(existingTypeRoom -> {
            existingTypeRoom.setTypeRoomName(typeRoomRequest.getTypeRoomName());
            return typeRoomRepository.save(existingTypeRoom);
        });
    }

    @CacheEvict(value = "typeRooms", allEntries = true)
    public boolean deleteTypeRoom(Integer typeRoomId) {
        if (!typeRoomRepository.existsById(typeRoomId)) {
            return false;
        }

        typeRoomRepository.deleteById(typeRoomId);
        return true;
    }
}