import { FilterIcon } from '../Common/Icons.jsx';
import './SidebarFilter.css';

function SidebarFilter({ filters, onChange, onReset, typeRooms, amenityBands, priceBands, areaBands }) {
  return (
    <aside className="sidebar-filter" aria-label="Bộ lọc bên phải">
      <div className="sidebar-filter__hero">
        <span className="sidebar-filter__eyebrow">
          <FilterIcon />
          Lọc nhanh
        </span>
        <h3 className="sidebar-filter__title">Tìm đúng căn phù hợp</h3>
        <p className="sidebar-filter__copy">
          Sàng lọc theo loại phòng, ngân sách, diện tích và tiện ích ngay từ cột bên phải.
        </p>
      </div>

      <section className="sidebar-filter__section">
        <h4>Theo giá</h4>
        <div className="sidebar-filter__stack">
          {priceBands.map((band) => (
            <button
              key={band.id}
              type="button"
              className={`sidebar-filter__option ${filters.priceBand === band.id ? 'is-active' : ''}`}
              onClick={() => onChange({ priceBand: band.id })}
            >
              {band.label}
            </button>
          ))}
        </div>
      </section>

      <section className="sidebar-filter__section">
        <h4>Theo diện tích</h4>
        <div className="sidebar-filter__stack">
          {areaBands.map((band) => (
            <button
              key={band.id}
              type="button"
              className={`sidebar-filter__option ${filters.areaBand === band.id ? 'is-active' : ''}`}
              onClick={() => onChange({ areaBand: band.id })}
            >
              {band.label}
            </button>
          ))}
        </div>
      </section>

      <section className="sidebar-filter__section">
        <h4>Loại phòng</h4>
        <div className="sidebar-filter__stack">
          <button
            type="button"
            className={`sidebar-filter__option ${filters.typeRoomId === 'all' ? 'is-active' : ''}`}
            onClick={() => onChange({ typeRoomId: 'all' })}
          >
            Tất cả
          </button>
          {typeRooms.map((typeRoom) => (
            <button
              key={typeRoom.typeRoomId}
              type="button"
              className={`sidebar-filter__option ${String(filters.typeRoomId) === String(typeRoom.typeRoomId) ? 'is-active' : ''}`}
              onClick={() => onChange({ typeRoomId: typeRoom.typeRoomId })}
            >
              {typeRoom.typeRoomName}
            </button>
          ))}
        </div>
      </section>

      <section className="sidebar-filter__section">
        <h4>Tiện ích nổi bật</h4>
        <div className="sidebar-filter__stack">
          {amenityBands.map((band) => (
            <button
              key={band.id}
              type="button"
              className={`sidebar-filter__option ${filters.amenityBand === band.id ? 'is-active' : ''}`}
              onClick={() => onChange({ amenityBand: band.id })}
            >
              {band.label}
            </button>
          ))}
        </div>
      </section>

      <label className="sidebar-filter__checkbox sidebar-filter__checkbox--highlight">
        <input
          type="checkbox"
          checked={Boolean(filters.onlyAvailable)}
          onChange={(event) => onChange({ onlyAvailable: event.target.checked })}
        />
        <span>Chỉ phòng còn trống</span>
      </label>

      <button className="sidebar-filter__reset" type="button" onClick={onReset}>
        Xóa tất cả bộ lọc
      </button>
    </aside>
  );
}

export default SidebarFilter;