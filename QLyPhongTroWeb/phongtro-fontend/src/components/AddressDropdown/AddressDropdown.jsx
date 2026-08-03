import { MapPinIcon, ChevronDownIcon, CloseIcon } from '../Common/Icons.jsx';
import './AddressDropdown.css';

function AddressDropdown({
  open,
  districts,
  selectedDistrictId,
  onSelectDistrict,
  onClear,
  onClose,
}) {
  if (!open) {
    return null;
  }

  return (
    <section className="address-dropdown" aria-label="Chọn địa chỉ">
      <div className="address-dropdown__header">
        <div>
          <p className="address-dropdown__eyebrow">Tìm quanh địa chỉ</p>
          <h3 className="address-dropdown__title">Tất cả khu vực</h3>
        </div>
        <button className="address-dropdown__close" type="button" onClick={onClose}>
          <CloseIcon />
        </button>
      </div>

      <div className="address-dropdown__quick-list">
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