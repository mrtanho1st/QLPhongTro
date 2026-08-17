package com.minhtan.qlptbackend.service;

import com.minhtan.qlptbackend.entity.SaleOff;
import com.minhtan.qlptbackend.repository.SaleOffRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleOffService {

    private final SaleOffRepository saleOffRepository;

    public SaleOffService(SaleOffRepository saleOffRepository) {
        this.saleOffRepository = saleOffRepository;
    }

    /**
     * Lấy tất cả chương trình khuyến mãi
     */
    @Cacheable(value = "saleOffs", key = "'all'")
    public List<SaleOff> findAll() {
        return saleOffRepository.findAll();
    }

    /**
     * Lấy theo ID
     */
    @Cacheable(value = "saleOffs", key = "'byId:' + #id")
    public SaleOff findById(Integer id) {
        return saleOffRepository.findById(id).orElse(null);
    }

    /**
     * Thêm mới
     */
    @CacheEvict(value = "saleOffs", allEntries = true)
    public SaleOff create(SaleOff saleOff) {
        validateSaleOff(saleOff);
        saleOff.setSaleOffId(null);
        return saleOffRepository.save(saleOff);
    }

    /**
     * Cập nhật
     */
    @CacheEvict(value = "saleOffs", allEntries = true)
    public SaleOff update(Integer id, SaleOff saleOff) {
        SaleOff existing = findById(id);

        if (existing == null) {
            return null;
        }

        validateSaleOff(saleOff);

        existing.setSaleOffName(saleOff.getSaleOffName());
        existing.setDiscountAmount(saleOff.getDiscountAmount());
        existing.setStartDate(saleOff.getStartDate());
        existing.setEndDate(saleOff.getEndDate());
        existing.setIsActive(saleOff.getIsActive());

        return saleOffRepository.save(existing);
    }

    /**
     * Xóa
     */
    @CacheEvict(value = "saleOffs", allEntries = true)
    public boolean delete(Integer id) {
        if (!saleOffRepository.existsById(id)) {
            return false;
        }

        saleOffRepository.deleteById(id);
        return true;
    }

    /**
     * Kiểm tra chương trình đang hoạt động
     */
    public boolean isAvailable(SaleOff saleOff) {
        LocalDateTime now = LocalDateTime.now();

        return Boolean.TRUE.equals(saleOff.getIsActive())
                && !now.isBefore(saleOff.getStartDate())
                && !now.isAfter(saleOff.getEndDate());
    }

    public List<SaleOff> findActiveSaleOffs() {
        LocalDateTime now = LocalDateTime.now();
        return saleOffRepository.findByStartDateBeforeAndEndDateAfter(now, now);
    }

    /**
     * Validate dữ liệu
     */
    private void validateSaleOff(SaleOff saleOff) {

        if (saleOff.getSaleOffName() == null || saleOff.getSaleOffName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được để trống.");
        }

        if (saleOff.getDiscountAmount() == null
                || saleOff.getDiscountAmount().signum() <= 0) {
            throw new IllegalArgumentException("Số tiền giảm phải lớn hơn 0.");
        }

        if (saleOff.getStartDate() == null || saleOff.getEndDate() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và ngày kết thúc không được để trống.");
        }

        if (saleOff.getStartDate().isAfter(saleOff.getEndDate())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc.");
        }

        if (saleOff.getIsActive() == null) {
            saleOff.setIsActive(true);
        }
    }

}