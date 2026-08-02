import { useEffect, useMemo, useState } from 'react';
import Navbar from '../../components/Navbar/Navbar.jsx';
import SidebarFilter from '../../components/SidebarFilter/SidebarFilter.jsx';
import RoomCard from '../../components/RoomCard/RoomCard.jsx';
import Pagination from '../../components/Pagination/Pagination.jsx';
import Footer from '../../components/Footer/Footer.jsx';
import MobileBottomNav from '../../components/Common/MobileBottomNav.jsx';
import { AREA_BANDS, PRICE_BANDS } from '../../components/Navbar/Navbar.jsx';
import {
  getAmenities,
  getBuildings,
  getDistricts,
  getRoomAmenities,
  getRoomMedia,
  getRooms,
  getTypeRooms,
} from '../../services/roomService.js';
import { toText } from '../../utils/format.js';
import './Home.css';

const PAGE_SIZE = 12;

const initialFilters = {
  districtId: 'all',
  typeRoomId: 'all',
  priceBand: 'all',
  areaBand: 'all',
  amenityIds: [],
  onlyAvailable: false,
};

function Home() {
  const [rooms, setRooms] = useState([]);
  const [roomMedia, setRoomMedia] = useState([]);
  const [buildings, setBuildings] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [typeRooms, setTypeRooms] = useState([]);
  const [amenities, setAmenities] = useState([]);
  const [roomAmenities, setRoomAmenities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchValue, setSearchValue] = useState('');
  const [filters, setFilters] = useState(initialFilters);
  const [currentPage, setCurrentPage] = useState(1);
  const [addressOpen, setAddressOpen] = useState(false);
  const [filterOpen, setFilterOpen] = useState(false);
  useEffect(() => {
    let isMounted = true;

    async function loadData() {
      setLoading(true);
      setError('');

      try {
        const [roomData, mediaData, buildingData, districtData, typeRoomData, amenityData, roomAmenityData] =
          await Promise.all([
            getRooms(),
            getRoomMedia(),
            getBuildings(),
            getDistricts(),
            getTypeRooms(),
            getAmenities(),
            getRoomAmenities(),
          ]);

        if (!isMounted) {
          return;
        }

        setRooms(roomData || []);
        setRoomMedia(mediaData || []);
        setBuildings(buildingData || []);
        setDistricts(districtData || []);
        setTypeRooms(typeRoomData || []);
        setAmenities(amenityData || []);
        setRoomAmenities(roomAmenityData || []);
      } catch (loadError) {
        if (isMounted) {
          setError(loadError.message || 'Không thể tải dữ liệu phòng trọ.');
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    }

    loadData();

    return () => {
      isMounted = false;
    };
  }, []);

  useEffect(() => {
    const handleEscape = (event) => {
      if (event.key === 'Escape') {
        setAddressOpen(false);
        setFilterOpen(false);
      }
    };

    window.addEventListener('keydown', handleEscape);
    return () => window.removeEventListener('keydown', handleEscape);
  }, []);

  const buildingMap = useMemo(
    () => new Map(buildings.map((building) => [String(building.buildingId), building])),
    [buildings],
  );

  const districtMap = useMemo(
    () => new Map(districts.map((district) => [String(district.districtId), district])),
    [districts],
  );

  const typeRoomMap = useMemo(
    () => new Map(typeRooms.map((typeRoom) => [String(typeRoom.typeRoomId), typeRoom])),
    [typeRooms],
  );

  const amenityMap = useMemo(
    () => new Map(amenities.map((amenity) => [String(amenity.amenityId), amenity])),
    [amenities],
  );

  const roomAmenityMap = useMemo(() => {
    const nextMap = new Map();

    roomAmenities.forEach((roomAmenity) => {
      const roomKey = String(roomAmenity.roomId);
      const amenityKey = String(roomAmenity.amenityId);
      const currentAmenityList = nextMap.get(roomKey) || [];

      currentAmenityList.push(amenityKey);
      nextMap.set(roomKey, currentAmenityList);
    });

    return nextMap;
  }, [roomAmenities]);

  const normalizedRooms = useMemo(() => {
    return rooms.map((room) => {
      const building = buildingMap.get(String(room.buildingId)) || room.building || null;
      const typeRoom = typeRoomMap.get(String(room.typeRoomId)) || room.typeRoom || null;
      const district = building ? districtMap.get(String(building.districtId)) || building.district || null : null;
      const amenityIds = roomAmenityMap.get(String(room.roomId)) || [];
      const amenityNames = amenityIds
        .map((amenityId) => amenityMap.get(String(amenityId))?.name)
        .filter(Boolean);
      const mediaList = roomMedia
        .filter((media) => String(media.roomId) === String(room.roomId))
        .sort((left, right) => Number(left.sortOrder || 0) - Number(right.sortOrder || 0));
      const preferredMedia = mediaList.find((media) => Number(media.mediaType) === 1) || mediaList[0] || null;

      const addressLabel = [
        building?.trueAddress || building?.fakeAddress || room.note,
        district?.districtName,
      ]
        .filter(Boolean)
        .join(' • ');

      return {
        ...room,
        building,
        district,
        typeRoom,
        districtId: building?.districtId,
        districtName: district?.districtName || '',
        typeRoomName: typeRoom?.typeRoomName || '',
        title: room.roomCode || typeRoom?.typeRoomName || 'Phòng trọ',
        addressLabel: addressLabel || `BĐS #${room.buildingId}`,
        imageUrl: preferredMedia?.url || '',
        amenityNames,
      };
    });
  }, [amenityMap, buildingMap, districtMap, roomMedia, rooms, typeRoomMap, roomAmenityMap]);

  const filteredRooms = useMemo(() => {
    const keyword = searchValue.trim().toLowerCase();

    return normalizedRooms
      .filter((room) => {
        const matchesKeyword =
          keyword.length === 0 ||
          [
            room.roomCode,
            room.addressLabel,
            room.note,
            room.typeRoomName,
            room.districtName,
            room.amenityNames.join(' '),
          ]
            .filter(Boolean)
            .some((value) => toText(value).toLowerCase().includes(keyword));

        const matchesDistrict =
          filters.districtId === 'all' || String(room.districtId) === String(filters.districtId);

        const matchesTypeRoom =
          filters.typeRoomId === 'all' || String(room.typeRoomId) === String(filters.typeRoomId);

        const matchesPrice = matchesBand(Number(room.price || 0), filters.priceBand);
        const matchesArea = matchesBand(Number(room.area || 0), filters.areaBand);
        const matchesAmenity =
          filters.amenityIds.length === 0 ||
          filters.amenityIds.every((amenityId) => roomAmenityMap.get(String(room.roomId))?.includes(amenityId));
        const matchesAvailability = !filters.onlyAvailable || !room.locked;

        return (
          matchesKeyword &&
          matchesDistrict &&
          matchesTypeRoom &&
          matchesPrice &&
          matchesArea &&
          matchesAmenity &&
          matchesAvailability
        );
      })
      .sort((left, right) => Number(left.price || 0) - Number(right.price || 0));
  }, [filters, normalizedRooms, roomAmenityMap, searchValue]);

  const totalPages = Math.max(1, Math.ceil(filteredRooms.length / PAGE_SIZE));
  const safeCurrentPage = Math.min(currentPage, totalPages);
  const paginatedRooms = filteredRooms.slice((safeCurrentPage - 1) * PAGE_SIZE, safeCurrentPage * PAGE_SIZE);

  const handleFiltersChange = (nextValues) => {
    setFilters((currentFilters) => ({
      ...currentFilters,
      ...nextValues,
    }));
  };

  const handleFiltersReset = (keys = null) => {
    if (!keys) {
      setFilters(initialFilters);
      return;
    }

    setFilters((currentFilters) => {
      const nextFilters = { ...currentFilters };
      keys.forEach((key) => {
        nextFilters[key] = initialFilters[key];
      });
      return nextFilters;
    });
  };

  const handleRoomOpen = () => {
    const contactElement = document.getElementById('contact');

    if (contactElement) {
      contactElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  };

  const handleSearchSubmit = (event) => {
    event.preventDefault();
    const roomListElement = document.getElementById('room-list');

    if (roomListElement) {
      roomListElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  };

  return (
    <div className="app-shell">
      <div className="app-shell__glow" />
      <div className="app-shell__glow app-shell__glow--right" />

      <Navbar
        searchValue={searchValue}
        onSearchChange={setSearchValue}
        onSearchSubmit={handleSearchSubmit}
        districts={districts}
        typeRooms={typeRooms}
        amenities={amenities}
        filters={filters}
        onFiltersChange={handleFiltersChange}
        onFiltersReset={handleFiltersReset}
        addressOpen={addressOpen}
        filterOpen={filterOpen}
        onToggleAddress={() => {
          setAddressOpen((currentValue) => !currentValue);
          setFilterOpen(false);
        }}
        onToggleFilter={() => {
          setFilterOpen((currentValue) => !currentValue);
          setAddressOpen(false);
        }}
        onClosePanels={() => {
          setAddressOpen(false);
          setFilterOpen(false);
        }}
        onSearchFocus={() => document.getElementById('navbar-search-input')?.focus()}
      />

      <main className="home" id="top">
        <section className="home__intro" aria-label="Tổng quan kết quả tìm kiếm">
          <p>
            Tìm thấy <strong>{filteredRooms.length}</strong> phòng trọ phù hợp
          </p>
        </section>

        <section className="home__content" id="room-list">
          <div className="home__main-column">
            {loading ? (
              <div className="home__grid">
                {Array.from({ length: 6 }).map((_, index) => (
                  <article key={index} className="room-card room-card--skeleton">
                    <div className="room-card__media room-card__media--skeleton" />
                    <div className="room-card__body">
                      <div className="home__skeleton-line home__skeleton-line--wide" />
                      <div className="home__skeleton-line" />
                      <div className="home__skeleton-line home__skeleton-line--short" />
                    </div>
                  </article>
                ))}
              </div>
            ) : error ? (
              <div className="home__state home__state--error">
                <h3>Không tải được dữ liệu từ backend</h3>
                <p>{error}</p>
                <button type="button" onClick={() => window.location.reload()}>
                  Thử lại
                </button>
              </div>
            ) : paginatedRooms.length > 0 ? (
              <>
                <div className="home__grid">
                  {paginatedRooms.map((room) => (
                    <RoomCard key={room.roomId} room={room} onOpen={handleRoomOpen} />
                  ))}
                </div>
                <Pagination
                  currentPage={safeCurrentPage}
                  totalPages={totalPages}
                  onPageChange={setCurrentPage}
                />
              </>
            ) : (
              <div className="home__state">
                <h3>Không có phòng nào khớp bộ lọc hiện tại</h3>
                <p>Hãy nới bộ lọc hoặc xóa vài điều kiện để xem thêm kết quả.</p>
                <button type="button" onClick={() => handleFiltersReset()}>
                  Xóa bộ lọc
                </button>
              </div>
            )}
          </div>

          <div className="home__side-column">
            <SidebarFilter
              filters={filters}
              onChange={handleFiltersChange}
              onReset={() => handleFiltersReset()}
              typeRooms={typeRooms}
              amenities={amenities}
              priceBands={PRICE_BANDS}
              areaBands={AREA_BANDS}
            />
          </div>
        </section>
      </main>

      <Footer />
      <MobileBottomNav
        onSearchFocus={() => document.getElementById('navbar-mobile-search-input')?.focus()}
        onScrollToContact={() => document.getElementById('contact')?.scrollIntoView({ behavior: 'smooth' })}
      />
    </div>
  );
}

function matchesBand(value, bandId) {
  if (bandId === 'all') {
    return true;
  }

  if (bandId === 'under-2m') {
    return value < 2_000_000;
  }

  if (bandId === '2m-4m') {
    return value >= 2_000_000 && value < 4_000_000;
  }

  if (bandId === '4m-6m') {
    return value >= 4_000_000 && value < 6_000_000;
  }

  if (bandId === '6m-8m') {
    return value >= 6_000_000 && value < 8_000_000;
  }

  if (bandId === 'over-8m') {
    return value >= 8_000_000;
  }

  if (bandId === 'under-20') {
    return value < 20;
  }

  if (bandId === '20-30') {
    return value >= 20 && value < 30;
  }

  if (bandId === '30-40') {
    return value >= 30 && value < 40;
  }

  if (bandId === 'over-40') {
    return value >= 40;
  }

  return true;
}

export default Home;