package com.minhtan.qlptbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhtan.qlptbackend.entity.TypeRoom;

public interface TypeRoomRepository extends JpaRepository<TypeRoom, Integer> {
    List<TypeRoom> findByTypeRoomNameContainingIgnoreCase(String typeRoomName);
}
