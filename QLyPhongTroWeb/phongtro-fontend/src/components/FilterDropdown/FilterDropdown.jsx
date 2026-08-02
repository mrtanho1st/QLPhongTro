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

      <div className="filter-dropdown__grid">
        <div className="filter-dropdown__block">
          <h4>Loại phòng</h4>
          <div className="filter-dropdown__chips">
            <button
              className={`filter-dropdown__chip ${filters.typeRoomId === 'all' ? 'is-active' : ''}`}
              type="button"
              onClick={() => onChange({ typeRoomId: 'all' })}
            >
              Tất cả
            </button>
            {typeRooms.map((typeRoom) => (
              <button
                key={typeRoom.typeRoomId}
                className={`filter-dropdown__chip ${String(filters.typeRoomId) === String(typeRoom.typeRoomId) ? 'is-active' : ''}`}
                type="button"
                onClick={() => onChange({ typeRoomId: typeRoom.typeRoomId })}
              >
                {typeRoom.typeRoomName}
              </button>
            ))}
          </div>
        </div>

        <div className="filter-dropdown__block">
          <h4>Giá (đ/tháng)</h4>
          <div className="filter-dropdown__chips filter-dropdown__chips--stacked">
            {priceBands.map((band) => (
              <button
                key={band.id}
                className={`filter-dropdown__chip ${filters.priceBand === band.id ? 'is-active' : ''}`}
                type="button"
                onClick={() => onChange({ priceBand: band.id })}
              >
                {band.label}
              </button>
            ))}
          </div>
        </div>

        <div className="filter-dropdown__block">
          <h4>Diện tích</h4>
          <div className="filter-dropdown__chips filter-dropdown__chips--stacked">
            {areaBands.map((band) => (
              <button
                key={band.id}
                className={`filter-dropdown__chip ${filters.areaBand === band.id ? 'is-active' : ''}`}
                type="button"
                onClick={() => onChange({ areaBand: band.id })}
              >
                {band.label}
              </button>
            ))}
          </div>
        </div>

        <div className="filter-dropdown__block">
          <h4>Tiện ích nổi bật</h4>
          <div className="filter-dropdown__chips filter-dropdown__chips--stacked">
            {amenities.map((amenity) => (
              <button
                key={amenity.amenityId}
                className={`filter-dropdown__chip ${filters.amenityIds.includes(String(amenity.amenityId)) ? 'is-active' : ''}`}
                type="button"
                onClick={() => toggleAmenity(amenity.amenityId)}
              >
                {amenity.name}
              </button>
            ))}
          </div>
        </div>
      </div>

      <label className="filter-dropdown__check">
        <input
          type="checkbox"
          checked={Boolean(filters.onlyAvailable)}
          onChange={(event) => onChange({ onlyAvailable: event.target.checked })}
        />
        Chỉ hiển thị phòng còn trống
      </label>

      <div className="filter-dropdown__footer">
        <button className="filter-dropdown__ghost" type="button" onClick={onReset}>
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