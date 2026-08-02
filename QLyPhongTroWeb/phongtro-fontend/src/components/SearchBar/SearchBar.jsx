import { SearchIcon } from '../Common/Icons.jsx';
import './SearchBar.css';

function SearchBar({
  inputId = 'search-room-input',
  value,
  onChange,
  onSubmit,
  placeholder = 'Tìm theo từ khóa, địa chỉ, tiện ích...',
  compact = false,
}) {
  return (
    <form className={`search-bar ${compact ? 'search-bar--compact' : ''}`} onSubmit={onSubmit}>
      <label className="visually-hidden" htmlFor={inputId}>
        Tìm kiếm phòng
      </label>
      <div className="search-bar__field">
        <SearchIcon className="search-bar__icon" />
        <input
          id={inputId}
          className="search-bar__input"
          type="search"
          value={value}
          onChange={(event) => onChange(event.target.value)}
          placeholder={placeholder}
        />
      </div>
      <button className="search-bar__submit" type="submit">
        Tìm
      </button>
    </form>
  );
}

export default SearchBar;