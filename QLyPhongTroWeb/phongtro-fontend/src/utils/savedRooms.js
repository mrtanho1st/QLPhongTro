const SAVED_ROOMS_KEY = 'savedRooms';

export function readSavedRooms() {
  if (typeof window === 'undefined') {
    return [];
  }

  try {
    const rawValue = window.localStorage.getItem(SAVED_ROOMS_KEY);
    if (!rawValue) {
      return [];
    }

    const parsedValue = JSON.parse(rawValue);
    return Array.isArray(parsedValue) ? parsedValue : [];
  } catch {
    return [];
  }
}

export function writeSavedRooms(nextRooms) {
  if (typeof window === 'undefined') {
    return;
  }

  window.localStorage.setItem(SAVED_ROOMS_KEY, JSON.stringify(nextRooms));
}

export function getRoomStorageKey(room) {
  return String(room?.roomId ?? room?.id ?? room?.roomCode ?? '');
}

export function isRoomSaved(room) {
  const roomKey = getRoomStorageKey(room);
  if (!roomKey) {
    return false;
  }

  return readSavedRooms().some((savedRoom) => getRoomStorageKey(savedRoom) === roomKey);
}

export function toggleSavedRoom(room) {
  const roomKey = getRoomStorageKey(room);
  const savedRooms = readSavedRooms();

  if (!roomKey) {
    return false;
  }

  const isSaved = savedRooms.some((savedRoom) => getRoomStorageKey(savedRoom) === roomKey);

  const nextSavedRooms = isSaved
    ? savedRooms.filter((savedRoom) => getRoomStorageKey(savedRoom) !== roomKey)
    : [...savedRooms, room];

  writeSavedRooms(nextSavedRooms);
  return !isSaved;
}
