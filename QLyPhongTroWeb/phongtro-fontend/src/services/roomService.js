import { requestJson } from './api.js';

export const getRooms = () => requestJson('/rooms');
export const getRoomMedia = () => requestJson('/room-media');
export const getBuildings = () => requestJson('/buildings');
export const getDistricts = () => requestJson('/districts');
export const getTypeRooms = () => requestJson('/type-rooms');
export const getAmenities = () => requestJson('/amenities');
export const getRoomAmenities = () => requestJson('/room-amenities');
export const getBuildingFees = () => requestJson('/building-fees');
export const getCommissions = () => requestJson('/commissions');