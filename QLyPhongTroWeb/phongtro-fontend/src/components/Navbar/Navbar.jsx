import SearchBar from '../SearchBar/SearchBar.jsx';
import AddressDropdown from '../AddressDropdown/AddressDropdown.jsx';
import FilterDropdown from '../FilterDropdown/FilterDropdown.jsx';
import { BrandIcon, ChevronDownIcon, HeartIcon, MenuIcon, UserIcon, MapPinIcon, FilterIcon, PhoneIcon } from '../Common/Icons.jsx';
import './Navbar.css';

function Navbar({
  searchValue,
  onSearchChange,
  onSearchSubmit,
  districts,
  landmarkTypes,
  landmarks,
  typeRooms,
  amenities,
  filters,
  onFiltersChange,
  onFiltersReset,
  addressOpen,
  filterOpen,
  onToggleAddress,
  onToggleFilter,
  onClosePanels,
  onSearchFocus,
  onSavedRoomsClick,
  onSelectDistrict,
  onClear,
  address,
  onAddressChange,
  onUseCurrentLocation,
  locating,
  locationError,
  radius,
  onRadiusChange,
  onApply,
  applying,
  applyError,
}) {
  const addressDropdownProps = {
    open: addressOpen,
    districts,
    landmarkTypes,
    landmarks,
    selectedDistrictId: filters.districtId,
    onSelectDistrict,
    onClear,
    onClose: onClosePanels,
    address,
    onAddressChange,
    onUseCurrentLocation,
    locating,
    locationError,
    radius,
    onRadiusChange,
    onApply,
    applying,
    applyError,
  };
  return (
    <header className="navbar" id="home">
      <div className="navbar__shell">
        <div className="navbar__top">
          <a className="navbar__brand" href="#home" aria-label="Phongtro Giá Rẻ">
            <span className="navbar__brand-mark">
              <BrandIcon />
            </span>
            <span className="navbar__brand-text">
              <strong>TANEzHouse</strong>
              <span>Phòng đẹp giá rẻ</span>
            </span>
          </a>

          <div className="navbar__desktop-row">
            <div className="navbar__picker-wrap">
              <button className="navbar__picker" type="button" onClick={onToggleAddress}>
                <MapPinIcon />
                <span>Chọn địa chỉ</span>
                <ChevronDownIcon />
              </button>
              <AddressDropdown {...addressDropdownProps} />
            </div>

            <div className="navbar__picker-wrap">
              <button className="navbar__picker" type="button" onClick={onToggleFilter}>
                <FilterIcon />
                <span>Lọc</span>
                <ChevronDownIcon />
              </button>
              <FilterDropdown
                open={filterOpen}
                filters={filters}
                onChange={onFiltersChange}
                onReset={onFiltersReset}
                onClose={onClosePanels}
                typeRooms={typeRooms}
                amenities={amenities}
                priceBands={PRICE_BANDS}
                areaBands={AREA_BANDS}
              />
            </div>

            <div className="navbar__search-wrap">
              <SearchBar
                inputId="navbar-search-input"
                value={searchValue}
                onChange={onSearchChange}
                onSubmit={onSearchSubmit}
                placeholder="Tìm theo từ khóa"
                compact
              />
            </div>

            <nav className="navbar__links" aria-label="Liên kết nhanh">
              <button className="navbar__link" type="button" onClick={onSavedRoomsClick}>
                <HeartIcon />
                <span>Phòng đã lưu</span>
              </button>
              <a className="navbar__link" href="#contact">
                <PhoneIcon />
                <span>Liên hệ</span>
              </a>
              <button className="navbar__link" type="button">
                <UserIcon />
                <span>Tài khoản</span>
              </button>
            </nav>
          </div>
        </div>

        <div className="navbar__mobile-row">
          <button className="navbar__icon-button" type="button" aria-label="Mở menu">
            <MenuIcon />
          </button>

          <a className="navbar__mobile-brand" href="#home">
            <span className="navbar__brand-mark navbar__brand-mark--mobile">
              <BrandIcon />
            </span>
            <span>
              <strong>TANEzHouse</strong>
              <small>Phòng đẹp giá rẻ</small>
            </span>
          </a>

          <button className="navbar__icon-button" type="button" aria-label="Tài khoản">
            <UserIcon />
          </button>
        </div>

        <div className="navbar__mobile-chips">
          <button className="navbar__chip" type="button" onClick={onToggleAddress}>
            <MapPinIcon />
            <span>Địa chỉ</span>
          </button>
          <button className="navbar__chip" type="button" onClick={onToggleFilter}>
            <FilterIcon />
            <span>Lọc</span>
          </button>
          <button className="navbar__chip" type="button" onClick={onSavedRoomsClick}>
            <HeartIcon />
            <span>Đã lưu</span>
          </button>
        </div>

        <div className="navbar__mobile-search">
          <SearchBar
            inputId="navbar-mobile-search-input"
            value={searchValue}
            onChange={onSearchChange}
            onSubmit={onSearchSubmit}
            placeholder="Tìm theo từ khóa, địa chỉ, tiện ích..."
          />
        </div>

        <div className="navbar__mobile-panels">
          <AddressDropdown {...addressDropdownProps} />
          <FilterDropdown
            open={filterOpen}
            filters={filters}
            onChange={onFiltersChange}
            onReset={onFiltersReset}
            onClose={onClosePanels}
            typeRooms={typeRooms}
            amenities={amenities}
            priceBands={PRICE_BANDS}
            areaBands={AREA_BANDS}
          />
        </div>
      </div>
    </header>
  );
}

const PRICE_BANDS = [
  { id: 'all', label: 'Tất cả mức giá' },
  { id: 'under-2m', label: 'Dưới 2 triệu' },
  { id: '2m-4m', label: '2 - 4 triệu' },
  { id: '4m-6m', label: '4 - 6 triệu' },
  { id: '6m-8m', label: '6 - 8 triệu' },
  { id: 'over-8m', label: 'Trên 8 triệu' },
];

const AREA_BANDS = [
  { id: 'all', label: 'Tất cả diện tích' },
  { id: 'under-20', label: 'Dưới 20 m²' },
  { id: '20-30', label: '20 - 30 m²' },
  { id: '30-40', label: '30 - 40 m²' },
  { id: 'over-40', label: 'Trên 40 m²' },
];

const AMENITY_BANDS = [
  { id: 'all', label: 'Tất cả tiện ích' },
  { id: 'low', label: 'Có điều hoà' },
  { id: 'medium', label: 'Máy lạnh, tủ lạnh, tủ đồ, cửa sổ' },
  { id: 'high', label: 'Máy lạnh, máy giặt, tủ bếp, tủ đồ, cửa sổ' },
  { id: 'full', label: 'Đầy đủ nội thất' },
];

const AMENITY_BAND_MATCHES = {
  all: [],
  low: ['Máy lạnh'],
  medium: ['Máy lạnh', 'Tủ lạnh', 'Tủ đồ', 'Cửa sổ trời', 'Cửa sổ hành lang'],
  high: [
    'Máy lạnh',
    'Máy giặt (chung)',
    'Máy giặt (riêng)',
    'Tủ bếp',
    'Tủ đồ',
    'Cửa sổ trời',
    'Cửa sổ hành lang',
  ],
  full: [
    'Máy lạnh',
    'Tủ lạnh',
    'Máy giặt (riêng)',
    'Tủ bếp',
    'Tủ đồ',
    'Giường nệm',
    'Bàn ghế',
  ],
};

export { PRICE_BANDS, AREA_BANDS, AMENITY_BANDS, AMENITY_BAND_MATCHES };

export default Navbar;