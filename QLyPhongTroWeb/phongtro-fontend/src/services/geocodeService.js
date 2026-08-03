import { loadGoogleMaps } from '../utils/googleMaps.js';

let geocoderInstance = null;

async function getGeocoder() {
  const maps = await loadGoogleMaps();

  if (!geocoderInstance) {
    geocoderInstance = new maps.Geocoder();
  }

  return geocoderInstance;
}

export async function geocodeAddress(address) {
  const geocoder = await getGeocoder();

  return new Promise((resolve, reject) => {
    geocoder.geocode({ address, region: 'vn' }, (results, status) => {
      if (status === 'OK' && results?.[0]) {
        const location = results[0].geometry.location;
        resolve({ lat: location.lat(), lng: location.lng() });
      } else {
        reject(new Error(`Không tìm thấy tọa độ cho địa chỉ: ${address}`));
      }
    });
  });
}

export async function reverseGeocode(lat, lng) {
  const geocoder = await getGeocoder();

  return new Promise((resolve, reject) => {
    geocoder.geocode({ location: { lat, lng } }, (results, status) => {
      if (status === 'OK' && results?.[0]) {
        resolve(results[0].formatted_address);
      } else {
        reject(new Error('Không thể xác định địa chỉ từ vị trí hiện tại'));
      }
    });
  });
}

export function getDistanceKm(lat1, lng1, lat2, lng2) {
  const toRad = (value) => (value * Math.PI) / 180;
  const earthRadiusKm = 6371;

  const dLat = toRad(lat2 - lat1);
  const dLng = toRad(lng2 - lng1);

  const a =
    Math.sin(dLat / 2) ** 2 +
    Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * Math.sin(dLng / 2) ** 2;

  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  return earthRadiusKm * c;
}

export function getCurrentPosition() {
  return new Promise((resolve, reject) => {
    if (!navigator.geolocation) {
      reject(new Error('Trình duyệt không hỗ trợ định vị vị trí.'));
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        resolve({
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        });
      },
      (error) => {
        if (error.code === error.PERMISSION_DENIED) {
          reject(
            new Error(
              'Bạn chưa cấp quyền truy cập vị trí. Vui lòng cho phép truy cập vị trí trong trình duyệt.',
            ),
          );
        } else {
          reject(new Error('Không thể xác định vị trí hiện tại. Vui lòng thử lại.'));
        }
      },
      { enableHighAccuracy: true, timeout: 10000 },
    );
  });
}