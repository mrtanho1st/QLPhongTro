import { MapPinIcon, ChevronDownIcon, CloseIcon, LocateIcon } from '../Common/Icons.jsx';
import './AddressDropdown.css';

function AddressDropdown({
  open,
  districts,
  selectedDistrictId,
  onSelectDistrict,
  onClear,
  onClose,
  address,
  onAddressChange,
  onUseCurrentLocation,
  locating,
  radius,
  onRadiusChange,
}) {
  if (!open) {
    return null;
  }

  return (
    <section className="address-dropdown" aria-label="Chọn địa chỉ">
      <div className="address-dropdown__header">
        <div>
          <h3 className="address-dropdown__title">Tìm quanh địa chỉ</h3>
        </div>
        <button className="address-dropdown__close" type="button" onClick={onClose}>
          <CloseIcon />
        </button>
      </div>

      <div className="address-dropdown__fields">
        <label className="address-dropdown__field">
          <span className="address-dropdown__field-label">Địa chỉ tìm kiếm</span>
          <div className="address-dropdown__field-input">
            <MapPinIcon />
            <input
              type="text"
              placeholder="Nhập địa chỉ"
              value={address}
              onChange={(event) => onAddressChange?.(event.target.value)}
            />
          </div>
          <button
            className="address-dropdown__locate-btn"
            type="button"
            onClick={onUseCurrentLocation}
            disabled={locating}
          >
            <LocateIcon />
            <span>{locating ? 'Đang xác định vị trí…' : 'Dùng vị trí hiện tại'}</span>
          </button>
        </label>

        <label className="address-dropdown__field">
          <span className="address-dropdown__field-label">Bán kính tìm kiếm</span>
          <div className="address-dropdown__field-input address-dropdown__field-input--radius">
            <input
              type="number"
              min="0"
              step="1"
              placeholder="Nhập bán kính"
              value={radius}
              onChange={(event) => onRadiusChange?.(event.target.value)}
            />
            <span className="address-dropdown__unit">km</span>
          </div>
        </label>
      </div>

      <div className="address-dropdown__quick-list">
        <h3 className="address-dropdown__title">Danh sách khu vực</h3>
        <button
          className={`address-dropdown__option ${selectedDistrictId === 'all' ? 'is-active' : ''}`}
          type="button"
          onClick={() => onSelectDistrict('all')}
        >
          <MapPinIcon />
          <span>Tất cả khu vực</span>
        </button>
        {districts.slice(0, 5).map((district) => (
          <button
            key={district.districtId}
            className={`address-dropdown__option ${String(selectedDistrictId) === String(district.districtId) ? 'is-active' : ''}`}
            type="button"
            onClick={() => onSelectDistrict(district.districtId)}
          >
            <span>{district.districtName}</span>
            <ChevronDownIcon />
          </button>
        ))}
      </div>

      <div className="address-dropdown__footer">
        <button className="address-dropdown__ghost" type="button" onClick={onClear}>
          Xóa lọc
        </button>
        <button className="address-dropdown__primary" type="button" onClick={onClose}>
          Áp dụng
        </button>
      </div>
    </section>
  );
}

export default AddressDropdown;