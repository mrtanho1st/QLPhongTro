import { useEffect, useId, useState } from 'react';
import { CloseIcon } from '../Common/Icons.jsx';
import { formatNumberInput } from '../../utils/format.js';
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
  const availabilityGroupName = useId();
  const [priceMin, setPriceMin] = useState('');
  const [priceMax, setPriceMax] = useState('');
  const [areaMin, setAreaMin] = useState('');
  const [areaMax, setAreaMax] = useState('');
  const [availabilityMode, setAvailabilityMode] = useState('');
  const [availableFrom, setAvailableFrom] = useState('');

  useEffect(() => {
    setPriceMin(filters.priceMin ?? '');
    setPriceMax(filters.priceMax ?? '');
    setAreaMin(filters.areaMin ?? '');
    setAreaMax(filters.areaMax ?? '');

    setAvailabilityMode(filters.availabilityMode ?? '');
    setAvailableFrom(filters.availableFrom ?? '');

  }, [filters.priceMin, filters.priceMax, filters.areaMin, filters.areaMax, filters.availabilityMode, filters.availableFrom]);
  
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
    setAvailabilityMode('');
    setAvailableFrom('');
  };

  const updateRangeValue = (field, value) => {
    const normalized = value === '' ? '' : String(value).replace(/\D/g, '');
    onChange({ [field]: normalized });
  };

  const updateAreaRangeValue = (field, value) => {
    const normalized = value === '' ? '' : String(value).replace(/[^\d.]/g, '');
    onChange({ [field]: normalized });
  };

  const handleAvailabilityModeChange = (mode) => {
    if (mode === 'date') {
        const date =
            availableFrom ||
            new Date().toISOString().split('T')[0];

        setAvailableFrom(date);
        setAvailabilityMode('date');

        onChange({
            availabilityMode: 'date',
            availableFrom: date,
        });
    } else if (mode === 'now') {
        setAvailableFrom('');
        setAvailabilityMode('now');

        onChange({
            availabilityMode: 'now',
            availableFrom: '',
        });
    } else {
        setAvailableFrom('');
        setAvailabilityMode('');

        onChange({
            availabilityMode: '',
            availableFrom: '',
        });
    }
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
                type="text"
                inputMode="numeric"
                placeholder="Nhập min"
                value={formatNumberInput(priceMin)}
                onChange={(event) => {
                  const rawValue = event.target.value.replace(/\D/g, '');
                  setPriceMin(rawValue);
                  updateRangeValue('priceMin', rawValue);
                }}
              />
            </label>
            <label className="filter-dropdown__field">
              <span>Đến</span>
              <input
                type="text"
                inputMode="numeric"
                placeholder="Nhập max"
                value={formatNumberInput(priceMax)}
                onChange={(event) => {
                  const rawValue = event.target.value.replace(/\D/g, '');
                  setPriceMax(rawValue);
                  updateRangeValue('priceMax', rawValue);
                }}
              />
            </label>
          </div>
        </section>

        <section className="filter-dropdown__section">
          <h4>Diện tích (m²)</h4>
          <div className="filter-dropdown__range-row">
            <label className="filter-dropdown__field">
              <span>Từ</span>
              <input
                type="number"
                inputMode="numeric"
                placeholder="Nhập min"
                value={areaMin}
                onChange={(event) => {
                  const rawValue = event.target.value;
                  setAreaMin(rawValue);
                  updateAreaRangeValue('areaMin', rawValue);
                }}
              />
            </label>
            <label className="filter-dropdown__field">
              <span>Đến</span>
              <input
                type="number"
                inputMode="numeric"
                placeholder="Nhập max"
                value={areaMax}
                onChange={(event) => {
                  const rawValue = event.target.value;
                  setAreaMax(rawValue);
                  updateAreaRangeValue('areaMax', rawValue);
                }}
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
            <div className="filter-dropdown__radio-row">
              <input
                  type="radio"
                  name={availabilityGroupName}
                  checked={availabilityMode === ''}
                  onChange={() => handleAvailabilityModeChange('')}
              />
              <span>Không lọc</span>
            </div>

          <div className="filter-dropdown__availability">
            <div className="filter-dropdown__radio-row">
              <input
                type="radio"
                name={availabilityGroupName}
                checked={availabilityMode === 'now'}
                onChange={() => handleAvailabilityModeChange('now')}
              />
              <span>Trống liền</span>
            </div>

            <div className="filter-dropdown__radio-row filter-dropdown__radio-row--date">
              <div className="filter-dropdown__radio-inline">
                <input
                  type="radio"
                  name={availabilityGroupName}
                  checked={availabilityMode === 'date'}
                  onChange={() => handleAvailabilityModeChange('date')}
                />
                <span>Ngày khác</span>
              </div>

              <input
                className="filter-dropdown__date-input"
                type="date"
                value={availableFrom}
                onChange={(event) => setAvailableFrom(event.target.value)}
                disabled={availabilityMode !== 'date'}
              />
            </div>
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