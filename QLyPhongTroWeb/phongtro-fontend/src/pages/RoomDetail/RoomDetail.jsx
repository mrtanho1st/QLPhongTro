import { useMemo, useState } from 'react';
import { ChevronLeftIcon, ChevronRightIcon, MapPinIcon, AreaIcon, BedIcon, CalendarIcon, HomeIcon, PhoneIcon, CloseIcon } from '../../components/Common/Icons.jsx';
import { resolveBackendUrl } from '../../services/api.js';
import { formatArea, formatCurrency, formatDate } from '../../utils/format.js';
import './RoomDetail.css';

const CONTACT_PHONE = '0349099412'; // TODO: thay bằng số điện thoại tư vấn thật
const CONTACT_ZALO_URL = `https://zalo.me/${CONTACT_PHONE}`;

function RoomDetail({ room, onBack, onViewLocation, onConsult }) {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [isConsultOpen, setIsConsultOpen] = useState(false);

  const galleryImages = useMemo(() => {
    const imagesFromMedia = (room?.mediaList || [])
      .map((media) => media?.url)
      .filter(Boolean)
      .map((url) => resolveBackendUrl(url));

    if (imagesFromMedia.length > 0) {
      return imagesFromMedia;
    }

    return room?.imageUrl ? [resolveBackendUrl(room.imageUrl)] : [];
  }, [room]);

  const primaryImage = galleryImages[currentImageIndex] || '';
  const roomTypeName = room?.typeRoomName || room?.typeRoom?.typeRoomName || 'Chưa cập nhật';
  const districtName = room?.districtName || room?.building?.district?.districtName || 'Chưa cập nhật';
  const buildingAddress = room?.building?.fakeAddress || room?.addressLabel || 'Chưa cập nhật';
  const buildingNote = room?.building?.note || 'Không có mô tả thêm';
  const commissionDeposit = room?.commission?.deposit ?? null;
  const commissionMonth = room?.commission?.contractMonth ?? null;

  const goToPreviousImage = () => {
    if (galleryImages.length === 0) {
      return;
    }

    setCurrentImageIndex((currentIndex) => (currentIndex === 0 ? galleryImages.length - 1 : currentIndex - 1));
  };

  const goToNextImage = () => {
    if (galleryImages.length === 0) {
      return;
    }

    setCurrentImageIndex((currentIndex) => (currentIndex + 1) % galleryImages.length);
  };

  const handleViewLocation = () => {
    if (typeof onViewLocation === 'function') {
      onViewLocation(room);
      return;
    }

    const lat = room?.building?.latitude;
    const lng = room?.building?.longitude;
    const query = lat && lng ? `${lat},${lng}` : buildingAddress;
    window.open(
      `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(query)}`,
      '_blank',
      'noopener,noreferrer'
    );
  };

  const handleConsult = () => {
    if (typeof onConsult === 'function') {
      onConsult(room);
      return;
    }

    setIsConsultOpen(true);
  };

  return (
    <main className="room-detail">
      <div className="room-detail__shell">
        <button type="button" className="room-detail__back" onClick={onBack}>
          <ChevronLeftIcon />
          Quay lại danh sách
        </button>

        <section className="room-detail__gallery">
          <div className="room-detail__gallery-main">
            {primaryImage ? (
              <img src={primaryImage} alt={room?.roomCode || room?.title || 'Ảnh phòng trọ'} />
            ) : (
              <div className="room-detail__placeholder">Không có ảnh</div>
            )}

            {galleryImages.length > 1 ? (
              <>
                <button type="button" className="room-detail__gallery-arrow room-detail__gallery-arrow--left" onClick={goToPreviousImage} aria-label="Ảnh trước">
                  <ChevronLeftIcon />
                </button>
                <button type="button" className="room-detail__gallery-arrow room-detail__gallery-arrow--right" onClick={goToNextImage} aria-label="Ảnh tiếp theo">
                  <ChevronRightIcon />
                </button>
              </>
            ) : null}
          </div>

          {galleryImages.length > 1 ? (
            <div className="room-detail__gallery-thumbs" aria-label="Danh sách ảnh phòng">
              {galleryImages.map((imageUrl, index) => (
                <button
                  key={`${imageUrl}-${index}`}
                  type="button"
                  className={`room-detail__thumb ${index === currentImageIndex ? 'is-active' : ''}`}
                  onClick={() => setCurrentImageIndex(index)}
                  aria-label={`Xem ảnh ${index + 1}`}
                >
                  <img src={imageUrl} alt={`Ảnh phòng ${index + 1}`} />
                </button>
              ))}
            </div>
          ) : null}
        </section>

        <section className="room-detail__content">
          <div className="room-detail__left">
            <div className="room-detail__hero card-panel">
              <div className="room-detail__hero-top">
                <div>
                  <span className="room-detail__label">Tin nhà trọ</span>
                  <h1>{room?.title || room?.roomCode || 'Phòng trọ'}</h1>
                </div>
                <div className="room-detail__price-box">
                  <span>Giá thuê</span>
                  <strong>{formatCurrency(room?.price)}</strong>
                  <small>/tháng</small>
                </div>
              </div>

              <div className="room-detail__meta">
                <span><AreaIcon />{formatArea(room?.area)}</span>
                <span><BedIcon />{room?.bedroom ? `${room?.bedroom} PN` : '1 PN'}</span>
                <span><CalendarIcon />{room?.availableDate ? formatDate(room?.availableDate) : 'Trống ngay'}</span>
              </div>

              <div className="room-detail__badges">
                <span className="room-detail__badge room-detail__badge--primary">{roomTypeName}</span>
                <span className="room-detail__badge">{districtName}</span>
                {!room?.locked ? <span className="room-detail__badge room-detail__badge--success">Còn trống</span> : null}
              </div>

              <div className="room-detail__actions">
                <button type="button" className="room-detail__action-btn room-detail__action-btn--outline" onClick={handleViewLocation}>
                  <MapPinIcon />
                  Xem vị trí
                </button>
                <button type="button" className="room-detail__action-btn room-detail__action-btn--primary" onClick={handleConsult}>
                  <PhoneIcon />
                  Tư vấn
                </button>
              </div>
            </div>

            <div className="card-panel">
              <div className="room-detail__section-heading">
                <HomeIcon />
                <h2>Thông tin phòng</h2>
              </div>

              <div className="room-detail__info-grid">
                <div className="info-row">
                  <span>Mã phòng</span>
                  <strong>{room?.roomCode || '—'}</strong>
                </div>
                <div className="info-row">
                  <span>Loại phòng</span>
                  <strong>{roomTypeName}</strong>
                </div>
                <div className="info-row">
                  <span>Diện tích</span>
                  <strong>{formatArea(room?.area)}</strong>
                </div>
                <div className="info-row">
                  <span>Số phòng ngủ</span>
                  <strong>{room?.bedroom || 1}</strong>
                </div>
                <div className="info-row">
                  <span>Số người tối đa</span>
                  <strong>{room?.personLimit || '—'}</strong>
                </div>
                <div className="info-row">
                  <span>Ngày trống</span>
                  <strong>{room?.availableDate ? formatDate(room?.availableDate) : 'Trống ngay'}</strong>
                </div>
              </div>
            </div>

            <div className="card-panel">
              <div className="room-detail__section-heading">
                <MapPinIcon />
                <h2>Thông tin tòa nhà</h2>
              </div>

              <div className="room-detail__info-grid">
                <div className="info-row info-row--full">
                  <span>Địa chỉ</span>
                  <strong>{buildingAddress}</strong>
                </div>
                <div className="info-row">
                  <span>Quận / huyện</span>
                  <strong>{districtName}</strong>
                </div>
                <div className="info-row">
                  <span>Ghi chú</span>
                  <strong>{buildingNote}</strong>
                </div>
              </div>
            </div>

            <div className="card-panel">
              <div className="room-detail__section-heading">
                <HomeIcon />
                <h2>Chi phí sinh hoạt</h2>
              </div>

              <div className="room-detail__info-grid">
                <div className="info-row">
                  <span>Điện</span>
                  <strong>{room?.buildingFee?.electricityPrice ? formatCurrency(room.buildingFee.electricityPrice) : 'Đang cập nhật'}</strong>
                </div>
                <div className="info-row">
                  <span>Nước</span>
                  <strong>{room?.buildingFee?.waterPrice ? formatCurrency(room.buildingFee.waterPrice) : 'Đang cập nhật'}</strong>
                </div>
                <div className="info-row">
                  <span>Phí dịch vụ</span>
                  <strong>{room?.buildingFee?.serviceFee ? formatCurrency(room.buildingFee.serviceFee) : 'Đang cập nhật'}</strong>
                </div>
                <div className="info-row">
                  <span>Xe máy / ô tô</span>
                  <strong>{room?.buildingFee?.parkingFee ? formatCurrency(room.buildingFee.parkingFee) : 'Đang cập nhật'}</strong>
                </div>
                <div className="info-row">
                  <span>Phí khác</span>
                  <strong>{room?.buildingFee?.otherFee ? formatCurrency(room.buildingFee.otherFee) : 'Đang cập nhật'}</strong>
                </div>
                <div className="info-row">
                  <span>Miễn phí gửi xe</span>
                  <strong>{room?.buildingFee?.freeParking ?? 'Đang cập nhật'}</strong>
                </div>
              </div>
            </div>
          </div>

          <aside className="room-detail__right">
            <div className="card-panel room-detail__sticky">
              <div className="room-detail__section-heading room-detail__section-heading--compact">
                <HomeIcon />
                <h2>Tiện ích</h2>
              </div>

              {room?.roomAmenities && room.roomAmenities.length > 0 ? (
                <div className="room-detail__amenities">
                  {room.roomAmenities.map((amenity) => (
                    <span key={`${amenity?.amenityId || amenity?.name}-${amenity?.name}`} className="room-detail__amenity">
                      {amenity?.name || 'Tiện ích'}
                    </span>
                  ))}
                </div>
              ) : (
                <p className="room-detail__empty">Chưa có thông tin tiện ích.</p>
              )}

              <div className="room-detail__section-heading room-detail__section-heading--compact">
                <HomeIcon />
                <h2>Hợp đồng</h2>
              </div>

              <div className="room-detail__contract">
                <div className="contract-row">
                  <span>Thời hạn hợp đồng</span>
                  <strong>{commissionMonth ? `${commissionMonth} tháng` : 'Đang cập nhật'}</strong>
                </div>
                <div className="contract-row">
                  <span>Tiền cọc</span>
                  <strong>{commissionDeposit ? `${commissionDeposit} tháng` : 'Đang cập nhật'}</strong>
                </div>
              </div>

              <div className="room-detail__note">
                <h3>Mô tả phòng</h3>
                <p>{room?.note || 'Chưa có mô tả chi tiết cho phòng này.'}</p>
              </div>
            </div>
          </aside>
        </section>
      </div>

      {isConsultOpen ? (
        <div className="consult-modal__overlay" role="dialog" aria-modal="true" onClick={() => setIsConsultOpen(false)}>
          <div className="consult-modal" onClick={(event) => event.stopPropagation()}>
            <button type="button" className="consult-modal__close" onClick={() => setIsConsultOpen(false)} aria-label="Đóng">
              <CloseIcon />
            </button>

            <h3>Liên hệ tư vấn</h3>
            <p>
              Bạn quan tâm đến phòng <strong>{room?.title || room?.roomCode}</strong>? Gọi ngay hoặc nhắn Zalo, chúng tôi sẽ hỗ trợ trong thời gian sớm nhất.
            </p>

            <div className="consult-modal__actions">
              <a className="room-detail__action-btn room-detail__action-btn--primary" href={`tel:${CONTACT_PHONE}`}>
                <PhoneIcon />
                Gọi {CONTACT_PHONE}
              </a>
              <a className="room-detail__action-btn room-detail__action-btn--outline" href={CONTACT_ZALO_URL} target="_blank" rel="noopener noreferrer">
                Nhắn Zalo
              </a>
            </div>
          </div>
        </div>
      ) : null}
    </main>
  );
}

export default RoomDetail;