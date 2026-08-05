package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.RoomSaleOff;
import com.minhtan.qlptbackend.repository.RoomSaleOffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomSaleOffService {

    private final RoomSaleOffRepository roomSaleOffRepository;

    public RoomSaleOffService(RoomSaleOffRepository roomSaleOffRepository) {
        this.roomSaleOffRepository = roomSaleOffRepository;
    }

    /**
     * Lấy tất cả
     */
    public List<RoomSaleOff> findAll() {
        return roomSaleOffRepository.findAll();
    }

    /**
     * Lấy theo khóa chính
     */
    public RoomSaleOff findById(Integer roomId, Integer saleOffId) {
        RoomSaleOff.RoomSaleOffId id = new RoomSaleOff.RoomSaleOffId(roomId, saleOffId);

        return roomSaleOffRepository.findById(id).orElse(null);
    }

    /**
     * Gán khuyến mãi cho phòng
     */
    public RoomSaleOff create(RoomSaleOff roomSaleOff) {

        RoomSaleOff.RoomSaleOffId id = new RoomSaleOff.RoomSaleOffId(
                roomSaleOff.getRoomId(),
                roomSaleOff.getSaleOffId());

        if (roomSaleOffRepository.existsById(id)) {
            throw new IllegalArgumentException("Khuyến mãi này đã được gán cho phòng.");
        }

        if (!roomSaleOffRepository.findByRoomId(roomSaleOff.getRoomId()).isEmpty()) {
            throw new IllegalArgumentException("Phòng này đã có khuyến mãi.");
        }

        return roomSaleOffRepository.save(roomSaleOff);
    }

    /**
     * Xóa khuyến mãi khỏi phòng
     */
    public boolean delete(Integer roomId, Integer saleOffId) {

        RoomSaleOff.RoomSaleOffId id = new RoomSaleOff.RoomSaleOffId(roomId, saleOffId);

        if (!roomSaleOffRepository.existsById(id)) {
            return false;
        }

        roomSaleOffRepository.deleteById(id);
        return true;
    }

    /**
     * Kiểm tra đã tồn tại
     */
    public boolean exists(Integer roomId, Integer saleOffId) {

        RoomSaleOff.RoomSaleOffId id = new RoomSaleOff.RoomSaleOffId(roomId, saleOffId);

        return roomSaleOffRepository.existsById(id);
    }

    public List<RoomSaleOff> findByRoomId(Integer roomId) {
        return roomSaleOffRepository.findByRoomId(roomId);
    }

    public List<RoomSaleOff> findBySaleOffId(Integer saleOffId) {
        return roomSaleOffRepository.findBySaleOffId(saleOffId);
    }

    public void deleteAllByRoom(Integer roomId) {
        roomSaleOffRepository.deleteByRoomId(roomId);
    }

    public void deleteAllBySaleOff(Integer saleOffId) {
        roomSaleOffRepository.deleteBySaleOffId(saleOffId);
    }
}