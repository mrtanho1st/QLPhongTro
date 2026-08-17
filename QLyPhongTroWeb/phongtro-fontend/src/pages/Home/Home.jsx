import { useEffect, useMemo, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Navbar from '../../components/Navbar/Navbar.jsx';
import SidebarFilter from '../../components/SidebarFilter/SidebarFilter.jsx';
import RoomCard from '../../components/RoomCard/RoomCard.jsx';
import Pagination from '../../components/Pagination/Pagination.jsx';
import Footer from '../../components/Footer/Footer.jsx';
import MobileBottomNav from '../../components/Common/MobileBottomNav.jsx';
import RoomDetail from '../RoomDetail/RoomDetail.jsx';
import { AREA_BANDS, PRICE_BANDS, AMENITY_BANDS, AMENITY_BAND_MATCHES } from '../../components/Navbar/Navbar.jsx';
import { geocodeAddress, reverseGeocode, getCurrentPosition, getDistanceKm } from '../../services/geocodeService.js';
import {
  getAmenities,
  getBuildingFees,
  getBuildings,
  getCommissions,
  getDistricts,
  getLandmarkTypes,
  getLandmarks,
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
  priceMin: '',
  priceMax: '',
  areaBand: 'all',
  areaMin: '',
  areaMax: '',
  amenityBand: 'all',
  amenityIds: [],
  availabilityMode: '',
  availableFrom: '',
};

function Home({ homeDataCache, onHomeDataCache }) {
  const navigate = useNavigate();
  const { roomId: activeRoomId } = useParams(); // <-- nguồn duy nhất, bỏ selectedRoomId

  const [rooms, setRooms] = useState(homeDataCache?.rooms ?? []);
  const [roomMedia, setRoomMedia] = useState(homeDataCache?.roomMedia ?? []);
  const [buildings, setBuildings] = useState(homeDataCache?.buildings ?? []);
  const [districts, setDistricts] = useState(homeDataCache?.districts ?? []);
  const [landmarkTypes, setLandmarkTypes] = useState(homeDataCache?.landmarkTypes ?? []);
  const [landmarks, setLandmarks] = useState(homeDataCache?.landmarks ?? []);
  const [typeRooms, setTypeRooms] = useState(homeDataCache?.typeRooms ?? []);
  const [amenities, setAmenities] = useState(homeDataCache?.amenities ?? []);
  const [roomAmenities, setRoomAmenities] = useState(homeDataCache?.roomAmenities ?? []);
  const [buildingFees, setBuildingFees] = useState(homeDataCache?.buildingFees ?? []);
  const [commissions, setCommissions] = useState(homeDataCache?.commissions ?? []);
  const [loading, setLoading] = useState(!homeDataCache);
  const [error, setError] = useState('');
  const [searchValue, setSearchValue] = useState('');
  const [filters, setFilters] = useState(initialFilters);
  const [currentPage, setCurrentPage] = useState(1);
  const [addressOpen, setAddressOpen] = useState(false);
  const [filterOpen, setFilterOpen] = useState(false);
  const [address, setAddress] = useState('');
  const [radius, setRadius] = useState('');
  const [locating, setLocating] = useState(false);
  const [locationError, setLocationError] = useState('');
  const [applyingDistance, setApplyingDistance] = useState(false);
  const [distanceError, setDistanceError] = useState('');
  const [distanceFilter, setDistanceFilter] = useState(null);
  const buildingCoordsRef = useRef(new Map());

  useEffect(() => {
    if (homeDataCache) {
      setRooms(homeDataCache.rooms ?? []);
      setRoomMedia(homeDataCache.roomMedia ?? []);
      setBuildings(homeDataCache.buildings ?? []);
      setDistricts(homeDataCache.districts ?? []);
      setLandmarkTypes(homeDataCache.landmarkTypes ?? []);
      setLandmarks(homeDataCache.landmarks ?? []);
      setTypeRooms(homeDataCache.typeRooms ?? []);
      setAmenities(homeDataCache.amenities ?? []);
      setRoomAmenities(homeDataCache.roomAmenities ?? []);
      setBuildingFees(homeDataCache.buildingFees ?? []);
      setCommissions(homeDataCache.commissions ?? []);
      setLoading(false);
      setError('');
      return;
    }

    let isMounted = true;

    async function loadData() {
      setLoading(true);
      setError('');

      try {
        const [
          roomData,
          mediaData,
          buildingData,
          districtData,
          typeRoomData,
          amenityData,
          roomAmenityData,
          buildingFeeData,
          commissionData,
          landmarkTypeData,
          landmarkData,
        ] = await Promise.all([
          getRooms(),
          getRoomMedia(),
          getBuildings(),
          getDistricts(),
          getTypeRooms(),
          getAmenities(),
          getRoomAmenities(),
          getBuildingFees(),
          getCommissions(),
          getLandmarkTypes(),
          getLandmarks(),
        ]);

        if (!isMounted) {
          return;
        }

        const nextData = {
          rooms: roomData || [],
          roomMedia: mediaData || [],
          buildings: buildingData || [],
          districts: districtData || [],
          landmarkTypes: landmarkTypeData || [],
          landmarks: landmarkData || [],
          typeRooms: typeRoomData || [],
          amenities: amenityData || [],
          roomAmenities: roomAmenityData || [],
          buildingFees: buildingFeeData || [],
          commissions: commissionData || [],
        };

        setRooms(nextData.rooms);
        setRoomMedia(nextData.roomMedia);
        setBuildings(nextData.buildings);
        setDistricts(nextData.districts);
        setTypeRooms(nextData.typeRooms);
        setAmenities(nextData.amenities);
        setRoomAmenities(nextData.roomAmenities);
        setBuildingFees(nextData.buildingFees);
        setCommissions(nextData.commissions);
        setLandmarkTypes(nextData.landmarkTypes);
        setLandmarks(nextData.landmarks);

        if (typeof onHomeDataCache === 'function') {
          onHomeDataCache(nextData);
        }
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
  }, [homeDataCache, onHomeDataCache]);

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
    return rooms
      .filter((room) => room.locked === false)
      .map((room) => {
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
          building?.fakeAddress || building?.trueAddress || room.note
        ]

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

        const roomPrice = Number(room.price || 0);
        const roomArea = Number(room.area || 0);

        const priceMinValue = filters.priceMin === '' || filters.priceMin === null ? null : Number(filters.priceMin);
        const priceMaxValue = filters.priceMax === '' || filters.priceMax === null ? null : Number(filters.priceMax);
        const areaMinValue = filters.areaMin === '' || filters.areaMin === null ? null : Number(filters.areaMin);
        const areaMaxValue = filters.areaMax === '' || filters.areaMax === null ? null : Number(filters.areaMax);

        const matchesPrice =
          matchesBand(roomPrice, filters.priceBand) &&
          (priceMinValue === null || roomPrice >= priceMinValue) &&
          (priceMaxValue === null || roomPrice <= priceMaxValue);

        const matchesArea =
          matchesBand(roomArea, filters.areaBand) &&
          (areaMinValue === null || roomArea >= areaMinValue) &&
          (areaMaxValue === null || roomArea <= areaMaxValue);

        const matchesAmenity = matchesAmenityBand(room.amenityNames, filters.amenityBand);
        const matchesAmenityIds =
          filters.amenityIds.length === 0 ||
          filters.amenityIds.every((amenityId) => roomAmenityMap.get(String(room.roomId))?.includes(amenityId));

        let matchesAvailability = true;
        if (filters.availabilityMode === 'now') {
          // Phòng trống liền: availableDate trước ngày hôm nay
          const availableDateValue = room.availableDate ? new Date(room.availableDate) : null;
          const todayValue = new Date();
          todayValue.setHours(0, 0, 0, 0);
          matchesAvailability = !availableDateValue || availableDateValue <= todayValue;
        } 
        else if (filters.availabilityMode === 'date' && filters.availableFrom) {
          // Phòng trống từ ngày được chọn: availableDate trước ngày chọn
          const availableDateValue = room.availableDate ? new Date(room.availableDate) : null;
          const selectedDate = new Date(filters.availableFrom);
          selectedDate.setHours(0, 0, 0, 0);
          matchesAvailability = !availableDateValue || availableDateValue <= selectedDate;
        }

        const matchesDistance = (() => {
          if (!distanceFilter) {
            return true;
          }

          if (distanceFilter.type === 'landmark') {
            const landmark = distanceFilter.landmark;
            const buildingLat = Number(room.building?.latitude ?? room.latitude ?? 0);
            const buildingLng = Number(room.building?.longitude ?? room.longitude ?? 0);

            if (!landmark || !Number.isFinite(buildingLat) || !Number.isFinite(buildingLng)) {
              return false;
            }

            const distanceKm = getDistanceKm(
              Number(landmark.latitude),
              Number(landmark.longitude),
              buildingLat,
              buildingLng,
            );

            return distanceKm <= distanceFilter.radiusKm;
          }

          const coords = buildingCoordsRef.current.get(String(room.buildingId));

          if (!coords) {
            return false;
          }

          const distanceKm = getDistanceKm(
            distanceFilter.origin.lat,
            distanceFilter.origin.lng,
            coords.lat,
            coords.lng,
          );

          return distanceKm <= distanceFilter.radiusKm;
        })();

        return (
          matchesKeyword &&
          matchesDistrict &&
          matchesTypeRoom &&
          matchesPrice &&
          matchesArea &&
          matchesAmenity &&
          matchesAmenityIds &&
          matchesAvailability &&
          matchesDistance
        );
      })
      .sort((left, right) => Number(left.price || 0) - Number(right.price || 0));
  }, [distanceFilter, filters, normalizedRooms, roomAmenityMap, searchValue]);

  const totalPages = Math.max(1, Math.ceil(filteredRooms.length / PAGE_SIZE));
  const safeCurrentPage = Math.min(currentPage, totalPages);
  const paginatedRooms = filteredRooms.slice((safeCurrentPage - 1) * PAGE_SIZE, safeCurrentPage * PAGE_SIZE);

  const selectedRoomDetail = useMemo(() => {
    if (!activeRoomId) {
      return null;
    }

    const room = normalizedRooms.find((item) => String(item.roomId) === String(activeRoomId));

    if (!room) {
      return null;
    }

    const roomAmenityEntries = (roomAmenityMap.get(String(room.roomId)) || [])
      .map((amenityId) => amenityMap.get(String(amenityId)))
      .filter(Boolean);

    const buildingFee = buildingFees.find((fee) => String(fee.buildingId) === String(room.buildingId)) || null;
    const commission = commissions.find((item) => String(item.buildingId) === String(room.buildingId)) || null;
    const mediaList = roomMedia
      .filter((media) => String(media.roomId) === String(room.roomId))
      .sort((left, right) => Number(left.sortOrder || 0) - Number(right.sortOrder || 0));

    return {
      ...room,
      roomAmenities: roomAmenityEntries,
      buildingFee,
      commission,
      mediaList,
    };
  }, [activeRoomId, amenityMap, buildingFees, commissions, normalizedRooms, roomAmenityMap, roomMedia]);

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

  const handleUseCurrentLocation = async () => {
    setLocationError('');
    setLocating(true);

    try {
      const { lat, lng } = await getCurrentPosition();
      const formattedAddress = await reverseGeocode(lat, lng);
      setAddress(formattedAddress);
    } catch (locationErr) {
      setLocationError(locationErr.message || 'Không thể xác định vị trí hiện tại.');
    } finally {
      setLocating(false);
    }
  };

  const handleAddressChange = (value) => {
    setAddress(value);
    setLocationError('');

    if (filters.districtId !== 'all') {
      handleFiltersChange({ districtId: 'all' });
    }
  };

  const handleRadiusChange = (value) => {
    setRadius(value);

    if (filters.districtId !== 'all') {
      handleFiltersChange({ districtId: 'all' });
    }
  };

  const handleSelectDistrict = (districtId) => {
    handleFiltersChange({ districtId });
    setAddress('');
    setRadius('');
    setDistanceFilter(null);
    setDistanceError('');
  };

  const handleClearAddressFilter = () => {
    setAddress('');
    setRadius('');
    setDistanceFilter(null);
    setDistanceError('');
    handleFiltersReset(['districtId']);
  };

  const handleApplyAddressFilter = async (payload = {}) => {
    setDistanceError('');

    const mode = payload.mode || 'district';
    const radiusValue = Number(payload.radiusKm ?? radius);

    if (!radiusValue || radiusValue <= 0) {
      setDistanceFilter(null);
      setAddressOpen(false);
      return;
    }

    if (mode === 'landmark') {
      const landmark = landmarks.find((item) => String(item.landmarkId) === String(payload.landmarkId));

      if (!landmark) {
        setDistanceError('Vui lòng chọn địa điểm để tìm phòng quanh địa điểm.');
        return;
      }

      setDistanceFilter({ type: 'landmark', landmark, radiusKm: radiusValue });
      setAddressOpen(false);
      return;
    }

    const trimmedAddress = (payload.address ?? address).trim();

    if (!trimmedAddress) {
      setDistanceFilter(null);
      setAddressOpen(false);
      return;
    }

    setApplyingDistance(true);

    try {
      const origin = await geocodeAddress(trimmedAddress);

      const buildingsToGeocode = buildings.filter(
        (building) => !buildingCoordsRef.current.has(String(building.buildingId)),
      );

      for (const building of buildingsToGeocode) {
        const key = String(building.buildingId);
        const buildingAddress = building.trueAddress || building.fakeAddress;

        if (!buildingAddress) {
          buildingCoordsRef.current.set(key, null);
          continue;
        }

        try {
          const coords = await geocodeAddress(buildingAddress);
          buildingCoordsRef.current.set(key, coords);
        } catch {
          buildingCoordsRef.current.set(key, null);
        }
      }

      setDistanceFilter({ type: 'district', origin, radiusKm: radiusValue });
      setAddressOpen(false);
    } catch (applyError) {
      setDistanceError(applyError.message || 'Không thể xác định tọa độ. Vui lòng kiểm tra lại địa chỉ.');
    } finally {
      setApplyingDistance(false);
    }
  };

  const handleRoomOpen = (room) => {
    if (!room) {
      return;
    }

    navigate(`/room-detail/${room.roomId}`);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleRoomDetailClose = () => {
    navigate('/');
  };

  const handleSavedRoomsOpen = () => {
    navigate('/saved-rooms');
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
        landmarkTypes={landmarkTypes}
        landmarks={landmarks}
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
        onSavedRoomsClick={handleSavedRoomsOpen}
        onSelectDistrict={handleSelectDistrict}
        onClear={handleClearAddressFilter}
        address={address}
        onAddressChange={handleAddressChange}
        onUseCurrentLocation={handleUseCurrentLocation}
        locating={locating}
        locationError={locationError}
        radius={radius}
        onRadiusChange={handleRadiusChange}
        onApply={handleApplyAddressFilter}
        applying={applyingDistance}
        applyError={distanceError}
      />

      {selectedRoomDetail ? (
        <RoomDetail room={selectedRoomDetail} onBack={handleRoomDetailClose} />
      ) : (
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
                amenityBands={AMENITY_BANDS}
                priceBands={PRICE_BANDS}
                areaBands={AREA_BANDS}
              />
            </div>
          </section>
        </main>
      )}

      <Footer />
      <MobileBottomNav
        onSearchFocus={() => document.getElementById('navbar-mobile-search-input')?.focus()}
        onSavedRoomsClick={handleSavedRoomsOpen}
        onScrollToContact={() => document.getElementById('contact')?.scrollIntoView({ behavior: 'smooth' })}
      />
    </div>
  );
}

function matchesAmenityBand(roomAmenityNames, bandId) {
  const requiredNames = AMENITY_BAND_MATCHES[bandId] || [];

  if (requiredNames.length === 0) {
    return true;
  }

  return requiredNames.every((name) => roomAmenityNames.includes(name));
}

function matchesBand(value, bandId) {
  if (bandId === 'all') return true;
  if (bandId === 'under-2m') return value < 2_000_000;
  if (bandId === '2m-4m') return value >= 2_000_000 && value < 4_000_000;
  if (bandId === '4m-6m') return value >= 4_000_000 && value < 6_000_000;
  if (bandId === '6m-8m') return value >= 6_000_000 && value < 8_000_000;
  if (bandId === 'over-8m') return value >= 8_000_000;
  if (bandId === 'under-20') return value < 20;
  if (bandId === '20-30') return value >= 20 && value < 30;
  if (bandId === '30-40') return value >= 30 && value < 40;
  if (bandId === 'over-40') return value >= 40;
  return true;
}

export default Home;