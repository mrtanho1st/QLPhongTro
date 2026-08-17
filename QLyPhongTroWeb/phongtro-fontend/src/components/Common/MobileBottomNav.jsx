import { HeartIcon, HomeIcon, SearchIcon, PhoneIcon } from './Icons.jsx';
import './MobileBottomNav.css';

function MobileBottomNav({ onSearchFocus, onSavedRoomsClick, onScrollToContact }) {
  return (
    <nav className="mobile-bottom-nav" aria-label="Thanh điều hướng di động">
      <button className="mobile-bottom-nav__item is-active" type="button">
        <HomeIcon />
        <span>Trang chủ</span>
      </button>
      <button className="mobile-bottom-nav__item" type="button" onClick={onSearchFocus}>
        <SearchIcon />
        <span>Tìm phòng</span>
      </button>
      <button className="mobile-bottom-nav__item" type="button" onClick={onSavedRoomsClick}>
        <HeartIcon />
        <span>Đã lưu</span>
      </button>
      <button className="mobile-bottom-nav__item" type="button" onClick={onScrollToContact}>
        <PhoneIcon />
        <span>Liên hệ</span>
      </button>
    </nav>
  );
}

export default MobileBottomNav;