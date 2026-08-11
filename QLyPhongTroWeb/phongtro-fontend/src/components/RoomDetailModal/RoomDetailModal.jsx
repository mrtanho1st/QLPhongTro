import { useEffect } from 'react';
import { resolveBackendUrl } from '../../services/api.js';
import { formatArea, formatCurrency, formatDate, toText } from '../../utils/format.js';
import './RoomDetailModal.css';

function RoomDetailModal({ room, onClose }) {
  useEffect(() => {
    const handleEscape = (event) => {
      if (event.key === 'Escape') {
        onClose?.();
      }
    };

    document.body.style.overflow = 'hidden';
    window.addEventListener('keydown', handleEscape);

    return () => {
      document.body.style.overflow = '';
      window.removeEventListener('keydown', handleEscape);
    };
  }, [onClose]);

  if (!room) {
    return null;
  }

  const gallery = [...new Set([...(room.gallery || []), room.imageUrl].filter(Boolean).map((url) => resolveBackendUrl(url)))];
  const primaryImage = gallery[0] || '';
  const availabilityText = !room.availableDate || String(room.availableDate).slice(0, 10) <= new Date().toISOString().slice(0, 10)
    ? 'Còn trống'
    : formatDate(room.availableDate);
  const featureItems = [
    { label: 'Loại phòng', value: room.typeRoomName || 'Chưa cập nhật' },
    { label: 'Diện tích', value: formatArea(room.area) },
    { label: 'Số phòng ngủ', value: room.bedroom ? `${room.bedroom} PN` : '1 PN' },
    { label: 'Số người tối đa', value: room.personLimit ? `${room.personLimit} người` : '2 người' },
    { label: 'Ngày có sẵn', value: availabilityText },
    { label: 'Khu vực', value: room.districtName || 'Chưa cập nhật' },
  ];

  return (
    <div className="room-detail-modal" role="dialog" aria-modal="true" aria-label={toText(room.title || room.roomCode || 'Chi tiết phòng')} onClick={onClose}>
      <div className="room-detail-modal__dialog" onClick={(event) => event.stopPropagation()}>
        <button type="button" className="room-detail-modal__close" onClick={onClose} aria-label="Đóng chi tiết phòng">
          ×
        </button>

        <div className="room-detail-modal__header">
          <div className="room-detail-modal__gallery">
            {primaryImage ? (
              <img className="room-detail-modal__main-image" src={primaryImage} alt={room.title || room.roomCode || 'Phòng trọ'} />
            ) : (
              <div className="room-detail-modal__placeholder">
                <span>Không có ảnh</span>
              </div>
            )}

            <div className="room-detail-modal__thumbnails">
              {gallery.slice(1, 4).map((imageUrl, index) => (
                <img key={`${imageUrl}-${index}`} src={imageUrl} alt={`${room.title || 'Phòng trọ'} ${index + 2}`} />
              ))}
            </div>
          </div>

          <div className="room-detail-modal__summary">
            <span className="room-detail-modal__badge">Phòng cho thuê</span>
            <h2>{toText(room.title || room.roomCode || 'Phòng trọ')}</h2>

            <div className="room-detail-modal__price-row">
              <strong>{formatCurrency(room.price)}</strong>
              <span>/tháng</span>
            </div>

            <p className="room-detail-modal__location">{room.addressLabel || 'Chưa cập nhật địa chỉ'}</p>

            <div className="room-detail-modal__meta">
              <span>{room.typeRoomName || 'Phòng trọ'}</span>
              <span>{formatArea(room.area)}</span>
              <span>{room.districtName || 'Khu vực'}</span>
            </div>

            <div className="room-detail-modal__tags">
              {room.typeRoomName ? <span className="room-detail-modal__tag room-detail-modal__tag--primary">{room.typeRoomName}</span> : null}
              {room.districtName ? <span className="room-detail-modal__tag">{room.districtName}</span> : null}
              <span className="room-detail-modal__tag room-detail-modal__tag--success">{availabilityText}</span>
            </div>

            <div className="room-detail-modal__actions">
              <button type="button" className="room-detail-modal__primary">Liên hệ</button>
              <button type="button" className="room-detail-modal__secondary">Chia sẻ</button>
            </div>
          </div>
        </div>

        <div className="room-detail-modal__content">
          <section className="room-detail-modal__panel">
            <h3>Thông tin chi tiết</h3>
            <div className="room-detail-modal__grid">
              {featureItems.map((item) => (
                <div key={item.label} className="room-detail-modal__item">
                  <span>{item.label}</span>
                  <strong>{item.value}</strong>
                </div>
              ))}
            </div>
          </section>

          <section className="room-detail-modal__panel">
            <h3>Tiện ích</h3>
            <div className="room-detail-modal__chips">
              {(room.amenityNames && room.amenityNames.length > 0 ? room.amenityNames : ['Không có tiện ích']).map((amenity) => (
                <span key={amenity} className="room-detail-modal__chip">
                  {amenity}
                </span>
              ))}
            </div>
          </section>

          <section className="room-detail-modal__panel room-detail-modal__panel--wide">
            <h3>Mô tả</h3>
            <p>
              {room.note || 'Chủ nhà chưa cập nhật mô tả chi tiết cho phòng này. Vui lòng liên hệ để biết thêm thông tin.'}
            </p>
          </section>
        </div>
      </div>
    </div>
  );
}

export default RoomDetailModal;
