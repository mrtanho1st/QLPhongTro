import { useEffect, useMemo, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import RoomCard from '../../components/RoomCard/RoomCard.jsx';
import RoomDetail from '../RoomDetail/RoomDetail.jsx';
import { readSavedRooms, toggleSavedRoom } from '../../utils/savedRooms.js';
import './SavedRoom.css';

function SavedRoom() {
  const navigate = useNavigate();
  const { roomId: activeRoomId } = useParams();
  const [savedRooms, setSavedRooms] = useState([]);

  useEffect(() => {
    setSavedRooms(readSavedRooms());
  }, []);

  useEffect(() => {
    const handleStorageChange = () => setSavedRooms(readSavedRooms());
    window.addEventListener('storage', handleStorageChange);
    return () => window.removeEventListener('storage', handleStorageChange);
  }, []);

  const selectedRoomDetail = useMemo(() => {
    if (!activeRoomId) {
      return null;
    }

    return savedRooms.find((room) => String(room.roomId) === String(activeRoomId)) || null;
  }, [activeRoomId, savedRooms]);

  const handleOpenSavedRoom = (room) => {
    navigate(`/saved-rooms/${room.roomId}`);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleToggleSaved = (room, nextSavedState) => {
    const result = toggleSavedRoom(room);
    setSavedRooms(readSavedRooms());
    if (!nextSavedState && String(activeRoomId) === String(room.roomId)) {
      navigate('/saved-rooms');
    }
    return result;
  };

  const handleCloseDetail = () => {
    navigate('/saved-rooms');
  };

  if (selectedRoomDetail) {
    return (
      <RoomDetail
        room={selectedRoomDetail}
        onBack={handleCloseDetail}
        onViewLocation={(room) => {
          const lat = room?.building?.latitude;
          const lng = room?.building?.longitude;
          const query = lat && lng ? `${lat},${lng}` : room?.addressLabel || room?.building?.fakeAddress;
          window.open(`https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(query)}`, '_blank', 'noopener,noreferrer');
        }}
        onConsult={() => {
          window.location.href = 'tel:0349099412';
        }}
      />
    );
  }

  return (
    <main className="saved-room">
      <div className="saved-room__shell">
        <div className="saved-room__header">
          <div>
            <p className="saved-room__eyebrow">Danh mục của bạn</p>
            <h1>Phòng đã lưu</h1>
          </div>
          <button type="button" className="saved-room__back" onClick={() => navigate('/')}>
            Về trang chủ
          </button>
        </div>

        {savedRooms.length === 0 ? (
          <div className="saved-room__empty">
            <h2>Chưa có phòng nào được lưu</h2>
            <p>Nhấn nút tim trên bất kỳ phòng nào để lưu và xem lại ở đây.</p>
            <button type="button" onClick={() => navigate('/')}>
              Tìm phòng ngay
            </button>
          </div>
        ) : (
          <div className="saved-room__grid">
            {savedRooms.map((room) => (
              <RoomCard
                key={room.roomId}
                room={room}
                onOpen={handleOpenSavedRoom}
                isSaved
                onToggleSaved={handleToggleSaved}
              />
            ))}
          </div>
        )}
      </div>
    </main>
  );
}

export default SavedRoom;
