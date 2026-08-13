import { useEffect, useMemo, useState } from 'react';
import { MapPinIcon, ChevronDownIcon, CloseIcon, LocateIcon } from '../Common/Icons.jsx';
import './AddressDropdown.css';

function AddressDropdown({
  open,
  districts = [],
  landmarkTypes = [],
  landmarks = [],
  selectedDistrictId,
  onSelectDistrict,
  onClear,
  onClose,
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
  const [mode, setMode] = useState('choose');
  const [showLandmarkTypes, setShowLandmarkTypes] = useState(false);
  const [selectedLandmarkTypeId, setSelectedLandmarkTypeId] = useState('');
  const [selectedLandmarkId, setSelectedLandmarkId] = useState('');

  useEffect(() => {
    if (!open) {
      setMode('choose');
      setShowLandmarkTypes(false);
      setSelectedLandmarkTypeId('');
      setSelectedLandmarkId('');
    }
  }, [open]);

  const filteredLandmarks = useMemo(
    () =>
      landmarks.filter(
        (landmark) => String(landmark.landmarkTypesId ?? landmark.landmarkTypeId ?? '') === String(selectedLandmarkTypeId),
      ),
    [landmarks, selectedLandmarkTypeId],
  );

  const selectedLandmarkType = landmarkTypes.find(
    (item) => String(item.landmarkTypeId ?? item.landmarkTypesId) === String(selectedLandmarkTypeId),
  );

  const selectedLandmark = landmarks.find(
    (item) => String(item.landmarkId) === String(selectedLandmarkId),
  );

  const handleReset = () => {
    setMode('choose');
    setShowLandmarkTypes(false);
    setSelectedLandmarkTypeId('');
    setSelectedLandmarkId('');
    onClear?.();
  };

  const handleApply = () => {
    if (mode === 'district') {
      onApply?.({
        mode: 'district',
        address: address || '',
        radiusKm: Number(radius),
      });
      return;
    }

    if (mode === 'landmark') {
      onApply?.({
        mode: 'landmark',
        landmarkId: selectedLandmarkId,
        radiusKm: Number(radius),
      });
    }
  };

  if (!open) {
    return null;
  }

  return (
    <section className="address-dropdown" aria-label="Chọn địa chỉ">
      <div className="address-dropdown__header">
        <div>
          <h3 className="address-dropdown__title">
            {mode === 'choose' && 'Chọn cách tìm phòng'}
            {mode === 'district' && 'Tìm phòng theo khu vực'}
            {mode === 'landmark' && 'Tìm phòng quanh địa điểm'}
          </h3>
        </div>
        <button className="address-dropdown__close" type="button" onClick={onClose}>
          <CloseIcon />
        </button>
      </div>

      {mode === 'choose' ? (
        <>
          <div className="address-dropdown__mode-list">
            <button
              type="button"
              className="address-dropdown__mode-card"
              onClick={() => {
                setMode('landmark');
                setShowLandmarkTypes(false);
              }}
            >
              <MapPinIcon />
              <span>Tìm phòng quanh địa điểm</span>
            </button>

            <button
              type="button"
              className="address-dropdown__mode-card"
              onClick={() => {
                setMode('district');
                setShowLandmarkTypes(false);
              }}
            >
              <ChevronDownIcon />
              <span>Tìm phòng theo khu vực</span>
            </button>
          </div>
        </>
      ) : null}

      {mode === 'district' ? (
        <>
          <button className="address-dropdown__back" type="button" onClick={() => setMode('choose')}>
            ← Quay lại
          </button>

          {/* <div className="address-dropdown__fields">
            <label className="address-dropdown__field">
              <span className="address-dropdown__field-label">Địa chỉ tìm kiếm</span>
              <div className="address-dropdown__field-input">
                <MapPinIcon />
                <input
                  type="text"
                  placeholder="Nhập địa chỉ"
                  value={address}
                  onChange={(event) => onAddressChange?.(event.target.value)}
                />
              </div>
              <button
                className="address-dropdown__locate-btn"
                type="button"
                onClick={onUseCurrentLocation}
                disabled={locating}
              >
                <LocateIcon />
                <span>{locating ? 'Đang xác định vị trí…' : 'Dùng vị trí hiện tại'}</span>
              </button>
              {locationError ? <p className="address-dropdown__error">{locationError}</p> : null}
            </label>

            <label className="address-dropdown__field">
              <span className="address-dropdown__field-label">Bán kính tìm kiếm</span>
              <div className="address-dropdown__field-input address-dropdown__field-input--radius">
                <input
                  type="number"
                  min="0"
                  step="1"
                  placeholder="Nhập bán kính"
                  value={radius}
                  onChange={(event) => onRadiusChange?.(event.target.value)}
                />
                <span className="address-dropdown__unit">km</span>
              </div>
            </label>

            {applyError ? <p className="address-dropdown__error">{applyError}</p> : null}
          </div> */}

          <div className="address-dropdown__quick-list">
            <h3 className="address-dropdown__title">Danh sách khu vực</h3>
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
            <button className="address-dropdown__ghost" type="button" onClick={handleReset}>
              Xóa lọc
            </button>
            <button className="address-dropdown__primary" type="button" onClick={handleApply} disabled={applying}>
              {applying ? 'Đang áp dụng…' : 'Áp dụng'}
            </button>
          </div>
        </>
      ) : null}

      {mode === 'landmark' ? (
        <>
          <button className="address-dropdown__back" type="button" onClick={() => setMode('choose')}>
            ← Quay lại
          </button>

          <div className="address-dropdown__fields">
            <div className="address-dropdown__field">
              <span className="address-dropdown__field-label">Loại địa điểm</span>
              <button
                type="button"
                className={`address-dropdown__select-button ${selectedLandmarkType ? 'is-selected' : ''}`}
                onClick={() => setShowLandmarkTypes((currentValue) => !currentValue)}
              >
                <span>{selectedLandmarkType ? selectedLandmarkType.name : 'Chọn loại địa điểm'}</span>
                <ChevronDownIcon />
              </button>

              {showLandmarkTypes ? (
                <div className="address-dropdown__panel-list">
                  {landmarkTypes.map((typeItem) => (
                    <button
                      key={typeItem.landmarkTypeId ?? typeItem.landmarkTypesId}
                      type="button"
                      className={`address-dropdown__panel-item ${String(selectedLandmarkTypeId) === String(typeItem.landmarkTypeId ?? typeItem.landmarkTypesId) ? 'is-selected' : ''}`}
                      onClick={() => {
                        setSelectedLandmarkTypeId(typeItem.landmarkTypeId ?? typeItem.landmarkTypesId);
                        setSelectedLandmarkId('');
                        setShowLandmarkTypes(false);
                      }}
                    >
                      {typeItem.name}
                    </button>
                  ))}
                </div>
              ) : null}
            </div>

            {selectedLandmarkType ? (
              <div className="address-dropdown__field">
                <span className="address-dropdown__field-label">Địa điểm</span>
                <div className="address-dropdown__panel-list address-dropdown__panel-list--compact">
                  {filteredLandmarks.length > 0 ? (
                    filteredLandmarks.map((landmark) => (
                      <button
                        key={landmark.landmarkId}
                        type="button"
                        className={`address-dropdown__panel-item ${String(selectedLandmarkId) === String(landmark.landmarkId) ? 'is-selected' : ''}`}
                        onClick={() => setSelectedLandmarkId(landmark.landmarkId)}
                      >
                        {landmark.landmarkName || landmark.name || 'Địa điểm'}
                      </button>
                    ))
                  ) : (
                    <p className="address-dropdown__empty">Không có địa điểm nào trong loại đã chọn.</p>
                  )}
                </div>
              </div>
            ) : null}

            <label className="address-dropdown__field">
              <span className="address-dropdown__field-label">Bán kính tìm kiếm</span>
              <div className="address-dropdown__field-input address-dropdown__field-input--radius">
                <input
                  type="number"
                  min="0"
                  step="1"
                  placeholder="Nhập bán kính"
                  value={radius}
                  onChange={(event) => onRadiusChange?.(event.target.value)}
                />
                <span className="address-dropdown__unit">km</span>
              </div>
            </label>

            {applyError ? <p className="address-dropdown__error">{applyError}</p> : null}
          </div>

          <div className="address-dropdown__footer">
            <button className="address-dropdown__ghost" type="button" onClick={handleReset}>
              Xóa lọc
            </button>
            <button
              className="address-dropdown__primary"
              type="button"
              onClick={handleApply}
              disabled={applying || !selectedLandmarkId || !Number(radius) || Number(radius) <= 0}
            >
              {applying ? 'Đang áp dụng…' : 'Áp dụng'}
            </button>
          </div>
        </>
      ) : null}
    </section>
  );
}

export default AddressDropdown;