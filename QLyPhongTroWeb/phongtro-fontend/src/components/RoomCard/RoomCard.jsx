import { AreaIcon, BedIcon, CalendarIcon, HeartIcon, LocationIcon } from '../Common/Icons.jsx';
import { resolveBackendUrl } from '../../services/api.js';
import { formatArea, formatCurrency, formatDate, toText } from '../../utils/format.js';
import { isRoomSaved, toggleSavedRoom } from '../../utils/savedRooms.js';
import './RoomCard.css';
import { useEffect, useState } from 'react';

function RoomCard({ room, onOpen, isSaved: controlledSaved, onToggleSaved }) {
  const [imageFailed, setImageFailed] = useState(false);
  const [isSaved, setIsSaved] = useState(typeof controlledSaved === 'boolean' ? controlledSaved : isRoomSaved(room));
  const imageUrl = room.imageUrl ? resolveBackendUrl(room.imageUrl) : '';
  const hasImage = Boolean(imageUrl) && !imageFailed;
  const availableDateValue = String(room.availableDate || '').slice(0, 10);
  const todayValue = new Date().toISOString().slice(0, 10);
  const isAvailableNow = !availableDateValue || availableDateValue <= todayValue;
  const availabilityLabel = isAvailableNow ? 'Còn trống' : formatDate(room.availableDate);

  useEffect(() => {
    if (typeof controlledSaved === 'boolean') {
      setIsSaved(controlledSaved);
      return;
    }

    setIsSaved(isRoomSaved(room));
  }, [controlledSaved, room]);

  const handleOpen = () => {
    if (typeof onOpen === 'function') {
      onOpen(room);
    }
  };

  const handleKeyDown = (event) => {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      handleOpen();
    }
  };

  const handleFavoriteClick = (event) => {
    event.stopPropagation();

    let nextSavedState = isSaved;

    if (typeof onToggleSaved === 'function') {
      nextSavedState = onToggleSaved(room, !isSaved) ?? !isSaved;
    } else {
      nextSavedState = toggleSavedRoom(room);
    }

    setIsSaved(Boolean(nextSavedState));
  };

  return (
    <article
      className="room-card"
      role="button"
      tabIndex={0}
      onClick={handleOpen}
      onKeyDown={handleKeyDown}
    >
      <div className="room-card__media">
        {hasImage ? (
          <img
            src={imageUrl}
            alt={room.roomCode || room.typeRoomName || 'Phòng trọ'}
            onError={() => setImageFailed(true)}
          />
        ) : (
          <div className="room-card__placeholder">
            <div className="room-card__placeholder-mark" />
            <span>Không có ảnh</span>
          </div>
        )}

        <button
          className={`room-card__favorite ${isSaved ? 'is-saved' : ''}`}
          type="button"
          aria-label={isSaved ? 'Bỏ đánh dấu yêu thích' : 'Đánh dấu yêu thích'}
          onClick={handleFavoriteClick}
        >
          <HeartIcon />
        </button>
      </div>

      <div className="room-card__body">
        <div className="room-card__price-row">
          <strong className="room-card__price">{formatCurrency(room.price)}</strong>
          <span className="room-card__period">/tháng</span>
        </div>

        <h3 className="room-card__title">{toText(room.title || room.roomCode || room.typeRoomName || 'Phòng trọ')}</h3>

        <p className="room-card__address">
          <LocationIcon />
          <span>{room.addressLabel}</span>
        </p>

        <div className="room-card__stats">
          <span>
            <AreaIcon />
            {formatArea(room.area)}
          </span>
          <span>
            <BedIcon />
            {room.bedroom ? `${room.bedroom} PN` : '1 PN'}
          </span>
          <span>
            <CalendarIcon />
            {availabilityLabel}
          </span>
        </div>

        <div className="room-card__tags">
          {room.typeRoomName ? <span className="room-card__tag room-card__tag--primary">{room.typeRoomName}</span> : null}
          {room.districtName ? <span className="room-card__tag">{room.districtName}</span> : null}
          {isAvailableNow ? <span className="room-card__tag room-card__tag--success">Còn trống</span> : null}
        </div>

        {room.amenityNames.length > 0 ? (
          <div className="room-card__amenities">
            {room.amenityNames.slice(0, 4).map((amenityName) => (
              <span key={amenityName} className="room-card__amenity">
                {amenityName}
              </span>
            ))}
          </div>
        ) : null}

        <button className="room-card__cta" type="button" onClick={handleOpen}>
          Xem chi tiết
        </button>
      </div>
    </article>
  );
}

export default RoomCard;