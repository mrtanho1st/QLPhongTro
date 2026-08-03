import { useState } from 'react';
import { CloseIcon } from '../Common/Icons.jsx';
import './FilterDropdown.css';

function FilterDropdown({
  open,
  filters,
  onChange,
  onReset,
  onClose,
  typeRooms,
  amenities,
  priceBands,
  areaBands,
}) {
  const [priceMin, setPriceMin] = useState('');
  const [priceMax, setPriceMax] = useState('');
  const [areaMin, setAreaMin] = useState('');
  const [areaMax, setAreaMax] = useState('');
  const [availabilityMode, setAvailabilityMode] = useState('now');
  const [availableFrom, setAvailableFrom] = useState('');

  if (!open) {
    return null;
  }

  const toggleAmenity = (amenityId) => {
    const amenityKey = String(amenityId);
    const nextAmenities = filters.amenityIds.includes(amenityKey)
      ? filters.amenityIds.filter((currentId) => currentId !== amenityKey)
      : [...filters.amenityIds, amenityKey];

    onChange({ amenityIds: nextAmenities });
  };

  const toggleTypeRoom = (typeRoomId) => {
    onChange({ typeRoomId: String(filters.typeRoomId) === String(typeRoomId) ? 'all' : typeRoomId });
  };

  const resetLocalInputs = () => {
    setPriceMin('');
    setPriceMax('');
    setAreaMin('');
    setAreaMax('');
    setAvailabilityMode('now');
    setAvailableFrom('');
  };

  const handleReset = () => {
    resetLocalInputs();
    onReset();
  };

  return (
    <section className="filter-dropdown" aria-label="Bộ lọc phòng">
      <div className="filter-dropdown__header">
        <div>
          <p className="filter-dropdown__eyebrow">Bộ lọc nhanh</p>
          <h3 className="filter-dropdown__title">Tinh chỉnh tìm kiếm</h3>
        </div>
        <button className="filter-dropdown__close" type="button" onClick={onClose}>
          <CloseIcon />
        </button>
      </div>

      <div className="filter-dropdown__sections">
        <section className="filter-dropdown__section">
          <h4>Loại phòng</h4>
          <div className="filter-dropdown__choice-list">
            <label className="filter-dropdown__choice">
              <input
                type="checkbox"
                checked={filters.typeRoomId === 'all'}
                onChange={() => onChange({ typeRoomId: 'all' })}
              />
              <span>Tất cả</span>
            </label>
            {typeRooms.map((typeRoom) => (
              <label key={typeRoom.typeRoomId} className="filter-dropdown__choice">
                <input
                  type="checkbox"
                  checked={String(filters.typeRoomId) === String(typeRoom.typeRoomId)}
                  onChange={() => toggleTypeRoom(typeRoom.typeRoomId)}
                />
                <span>{typeRoom.typeRoomName}</span>
              </label>
            ))}
          </div>
        </section>

        <section className="filter-dropdown__section">
          <h4>Giá (đồng)</h4>
          <div className="filter-dropdown__range-row">
            <label className="filter-dropdown__field">
              <span>Từ</span>
              <input
                type="number"
                inputMode="numeric"
                placeholder="Nhập min"
                value={priceMin}
                onChange={(event) => setPriceMin(event.target.value)}
              />
            </label>
            <label className="filter-dropdown__field">
              <span>Đến</span>
              <input
                type="number"
                inputMode="numeric"
                placeholder="Nhập max"
                value={priceMax}
                onChange={(event) => setPriceMax(event.target.value)}
              />
            </label>
          </div>
        </section>

        <section className="filter-dropdown__section">
          <h4>Diện tích</h4>
          <div className="filter-dropdown__range-row">
            <label className="filter-dropdown__field">
              <span>Từ</span>
              <input
                type="number"
                inputMode="numeric"
                placeholder="Nhập min"
                value={areaMin}
                onChange={(event) => setAreaMin(event.target.value)}
              />
            </label>
            <label className="filter-dropdown__field">
              <span>Đến</span>
              <input
                type="number"
                inputMode="numeric"
                placeholder="Nhập max"
                value={areaMax}
                onChange={(event) => setAreaMax(event.target.value)}
              />
            </label>
          </div>
        </section>

        <section className="filter-dropdown__section">
          <h4>Tiện ích</h4>
          <div className="filter-dropdown__amenities-grid">
            {amenities.map((amenity) => (
              <label
                key={amenity.amenityId}
                className="filter-dropdown__choice filter-dropdown__choice--inline"
              >
                <input
                  type="checkbox"
                  checked={filters.amenityIds.includes(String(amenity.amenityId))}
                  onChange={() => toggleAmenity(amenity.amenityId)}
                />
                <span>{amenity.name}</span>
              </label>
            ))}
          </div>
        </section>

        <section className="filter-dropdown__section">
          <h4>Ngày phòng trống</h4>
          <div className="filter-dropdown__availability">
            <label className="filter-dropdown__radio-row">
              <input
                type="radio"
                name="availability-mode"
                checked={availabilityMode === 'now'}
                onChange={() => setAvailabilityMode('now')}
              />
              <span>Trống liền</span>
            </label>

            <label className="filter-dropdown__radio-row filter-dropdown__radio-row--date">
              <input
                type="radio"
                name="availability-mode"
                checked={availabilityMode === 'date'}
                onChange={() => setAvailabilityMode('date')}
              />
              <span>Ngày khác</span>
              <input
                className="filter-dropdown__date-input"
                type="date"
                value={availableFrom}
                onChange={(event) => setAvailableFrom(event.target.value)}
                disabled={availabilityMode !== 'date'}
              />
            </label>
          </div>
        </section>
      </div>

      <div className="filter-dropdown__footer">
        <button className="filter-dropdown__ghost" type="button" onClick={handleReset}>
          Xóa lọc
        </button>
        <button className="filter-dropdown__primary" type="button" onClick={onClose}>
          Áp dụng
        </button>
      </div>
    </section>
  );
}

export default FilterDropdown;