package com.minhtan.qlptclient.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.minhtan.qlptclient.entity.Amenity;
import com.minhtan.qlptclient.entity.Building;
import com.minhtan.qlptclient.entity.BuildingFee;
import com.minhtan.qlptclient.entity.Commission;
import com.minhtan.qlptclient.entity.District;
import com.minhtan.qlptclient.entity.Landmark;
import com.minhtan.qlptclient.entity.LandmarkType;
import com.minhtan.qlptclient.entity.Room;
import com.minhtan.qlptclient.entity.RoomAmenity;
import com.minhtan.qlptclient.entity.RoomMedia;
import com.minhtan.qlptclient.entity.TypeRoom;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class ApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final static ApiClient INSTANCE = new ApiClient("http://192.168.1.243:8080");

    private ApiClient(String baseUrl) {
        this.baseUrl = stripTrailingSlash(baseUrl);
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    public List<Building> getBuildings() throws IOException, InterruptedException {
        return getList("/api/buildings", new TypeReference<List<Building>>() {
        });
    }

    public List<TypeRoom> getTypeRooms() throws IOException, InterruptedException {
        return getList("/api/type-rooms", new TypeReference<List<TypeRoom>>() {
        });
    }

    public List<District> getDistricts() throws IOException, InterruptedException {
        return getList("/api/districts", new TypeReference<List<District>>() {
        });
    }

    public List<Room> getRooms() throws IOException, InterruptedException {
        return getList("/api/rooms", new TypeReference<List<Room>>() {
        });
    }

    public List<Amenity> getAmenities() throws IOException, InterruptedException {
        return getList("/api/amenities", new TypeReference<List<Amenity>>() {
        });
    }

    public List<Commission> getCommissions() throws IOException, InterruptedException {
        return getList("/api/commissions", new TypeReference<List<Commission>>() {
        });
    }

    public List<RoomAmenity> getRoomAmenities() throws IOException, InterruptedException {
        return getList("/api/room-amenities", new TypeReference<List<RoomAmenity>>() {
        });
    }

    public List<RoomAmenity> searchRoomAmenitiesByRoomId(Integer roomId)
            throws IOException, InterruptedException {
        return getList("/api/room-amenities/search/room-id/" + roomId, new TypeReference<List<RoomAmenity>>() {
        });
    }

    public List<RoomAmenity> searchRoomAmenitiesByAmenityId(Integer amenityId)
            throws IOException, InterruptedException {
        return getList("/api/room-amenities/search/amenity-id/" + amenityId, new TypeReference<List<RoomAmenity>>() {
        });
    }

    public List<RoomMedia> searchRoomMediaByRoomId(Integer roomId)
            throws IOException, InterruptedException {
        return getList("/api/room-media/search/room-id/" + roomId, new TypeReference<List<RoomMedia>>() {
        });
    }

    public RoomMedia createRoomMedia(RoomMedia roomMedia) throws IOException, InterruptedException {
        return sendJson("/api/room-media", "POST", roomMedia, RoomMedia.class);
    }

    public RoomMedia updateRoomMedia(Integer mediaId, RoomMedia roomMedia) throws IOException, InterruptedException {
        return sendJson("/api/room-media/" + mediaId, "PUT", roomMedia, RoomMedia.class);
    }

    public void deleteRoomMedia(Integer mediaId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/room-media/" + mediaId))
                .timeout(Duration.ofSeconds(20))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
    }

    public List<BuildingFee> getBuildingFees() throws IOException, InterruptedException {
        return getList("/api/building-fees", new TypeReference<List<BuildingFee>>() {
        });
    }

    public List<BuildingFee> searchBuildingFeesByBuildingId(Integer buildingId)
            throws IOException, InterruptedException {
        return getList("/api/building-fees/search/building-id/" + buildingId,
                new TypeReference<List<BuildingFee>>() {
                });
    }

    public List<Room> searchRoomsByBuildingId(Integer buildingId) throws IOException, InterruptedException {
        return getList("/api/rooms/search/building-id/" + buildingId, new TypeReference<List<Room>>() {
        });
    }

    public List<Room> searchRoomsByRoomCode(String roomCode) throws IOException, InterruptedException {
        return getList("/api/rooms/search/room-code?roomCode=" + encode(roomCode),
                new TypeReference<List<Room>>() {
                });
    }

    public List<Room> searchRoomsByPrice(String price) throws IOException, InterruptedException {
        return getList("/api/rooms/search/price?price=" + encode(price),
                new TypeReference<List<Room>>() {
                });
    }

    public List<Room> searchRoomsByBedroom(Integer bedroom) throws IOException, InterruptedException {
        return getList("/api/rooms/search/bedroom/" + bedroom, new TypeReference<List<Room>>() {
        });
    }

    public List<Room> searchRoomsByTypeRoomName(String typeRoomName) throws IOException, InterruptedException {
        return getList("/api/rooms/search/type-room-name?typeRoomName=" + encode(typeRoomName),
                new TypeReference<List<Room>>() {
                });
    }

    public List<Room> searchRoomsByPersonLimit(Integer personLimit) throws IOException, InterruptedException {
        return getList("/api/rooms/search/person-limit/" + personLimit, new TypeReference<List<Room>>() {
        });
    }

    public List<Room> searchRoomsByPersonLimitGreaterThanEqual(Integer personLimit)
            throws IOException, InterruptedException {
        return getList("/api/rooms/search/person-limit-ge/" + personLimit, new TypeReference<List<Room>>() {
        });
    }

    public List<Room> searchRoomsByArea(String area) throws IOException, InterruptedException {
        return getList("/api/rooms/search/area?area=" + encode(area),
                new TypeReference<List<Room>>() {
                });
    }

    public List<Room> searchRoomsByAreaGreaterThanEqual(String area) throws IOException, InterruptedException {
        return getList("/api/rooms/search/area-ge?area=" + encode(area),
                new TypeReference<List<Room>>() {
                });
    }

    public List<Room> searchRoomsByLocked(Boolean locked) throws IOException, InterruptedException {
        return getList("/api/rooms/search/locked/" + locked, new TypeReference<List<Room>>() {
        });
    }

    public List<Room> searchRoomsByAvailableDate(String availableDate) throws IOException, InterruptedException {
        return getList("/api/rooms/search/available-date/" + encode(availableDate), new TypeReference<List<Room>>() {
        });
    }

    public List<Room> searchRoomsByNote(String note) throws IOException, InterruptedException {
        return getList("/api/rooms/search/note?note=" + encode(note),
                new TypeReference<List<Room>>() {
                });
    }

    public List<Building> searchBuildingsByTrueAddress(String trueAddress) throws IOException, InterruptedException {
        return getList("/api/buildings/search/true-address?trueAddress=" + encode(trueAddress),
                new TypeReference<List<Building>>() {
                });
    }

    public List<Building> searchBuildingsByDistrictName(String districtName) throws IOException, InterruptedException {
        return getList("/api/buildings/search/district-name?districtName=" + encode(districtName),
                new TypeReference<List<Building>>() {
                });
    }

    public List<Building> searchBuildingsByFakeAddress(String fakeAddress) throws IOException, InterruptedException {
        return getList("/api/buildings/search/fake-address?fakeAddress=" + encode(fakeAddress),
                new TypeReference<List<Building>>() {
                });
    }

    public List<Building> searchBuildingsByNote(String note) throws IOException, InterruptedException {
        return getList("/api/buildings/search/note?note=" + encode(note),
                new TypeReference<List<Building>>() {
                });
    }

    public List<Building> searchBuildingsByOwnerPhone(String ownerPhone) throws IOException, InterruptedException {
        return getList("/api/buildings/search/owner-phone?ownerPhone=" + encode(ownerPhone),
                new TypeReference<List<Building>>() {
                });
    }

    public List<Amenity> searchAmenitiesByName(String name) throws IOException, InterruptedException {
        return getList("/api/amenities/search/name?name=" + encode(name),
                new TypeReference<List<Amenity>>() {
                });
    }

    public Building createBuilding(Building building) throws IOException, InterruptedException {
        return sendJson("/api/buildings", "POST", building, Building.class);
    }

    public Amenity createAmenity(Amenity amenity) throws IOException, InterruptedException {
        return sendJson("/api/amenities", "POST", amenity, Amenity.class);
    }

    public Commission createCommission(Commission commission) throws IOException, InterruptedException {
        return sendJson("/api/commissions", "POST", commission, Commission.class);
    }

    public Room createRoom(Room room) throws IOException, InterruptedException {
        return sendJson("/api/rooms", "POST", room, Room.class);
    }

    public RoomAmenity createRoomAmenity(RoomAmenity roomAmenity) throws IOException, InterruptedException {
        return sendJson("/api/room-amenities", "POST", roomAmenity, RoomAmenity.class);
    }

    public BuildingFee createBuildingFee(BuildingFee buildingFee) throws IOException, InterruptedException {
        return sendJson("/api/building-fees", "POST", buildingFee, BuildingFee.class);
    }

    public Building updateBuilding(Integer buildingId, Building building) throws IOException, InterruptedException {
        return sendJson("/api/buildings/" + buildingId, "PUT", building, Building.class);
    }

    public Amenity updateAmenity(Integer amenityId, Amenity amenity) throws IOException, InterruptedException {
        return sendJson("/api/amenities/" + amenityId, "PUT", amenity, Amenity.class);
    }

    public Commission updateCommission(Integer commissionId, Commission commission)
            throws IOException, InterruptedException {
        return sendJson("/api/commissions/" + commissionId, "PUT", commission, Commission.class);
    }

    public Room updateRoom(Integer roomId, Room room) throws IOException, InterruptedException {
        return sendJson("/api/rooms/" + roomId, "PUT", room, Room.class);
    }

    public BuildingFee updateBuildingFee(Integer feeId, BuildingFee buildingFee)
            throws IOException, InterruptedException {
        return sendJson("/api/building-fees/" + feeId, "PUT", buildingFee, BuildingFee.class);
    }

    public void deleteBuilding(Integer buildingId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/buildings/" + buildingId))
                .timeout(Duration.ofSeconds(20))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
    }

    public void deleteRoom(Integer roomId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/rooms/" + roomId))
                .timeout(Duration.ofSeconds(20))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
    }

    public void deleteRoomAmenity(Integer roomId, Integer amenityId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/room-amenities/" + roomId + "/" + amenityId))
                .timeout(Duration.ofSeconds(20))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
    }

    public void deleteAmenity(Integer amenityId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/amenities/" + amenityId))
                .timeout(Duration.ofSeconds(20))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
    }

    public void deleteCommission(Integer commissionId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/commissions/" + commissionId))
                .timeout(Duration.ofSeconds(20))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
    }

    public void deleteBuildingFee(Integer feeId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/building-fees/" + feeId))
                .timeout(Duration.ofSeconds(20))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
    }

    // District api
    public District createDistrict(District district) throws IOException, InterruptedException {
        return sendJson("/api/districts", "POST", district, District.class);
    }

    public District updateDistrict(Integer districtId, District district) throws IOException, InterruptedException {
        return sendJson("/api/districts/" + districtId, "PUT", district, District.class);
    }

    public void deleteDistrict(Integer districtId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/districts/" + districtId))
                .timeout(Duration.ofSeconds(20))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
    }

    public List<District> searchDistrictsByName(String districtName) throws IOException, InterruptedException {
        return getList("/api/districts/search/district-name?districtName=" + encode(districtName),
                new TypeReference<List<District>>() {
                });
    }

    // TypeRoom api
    public TypeRoom createTypeRoom(TypeRoom typeRoom) throws IOException, InterruptedException {
        return sendJson("/api/type-rooms", "POST", typeRoom, TypeRoom.class);
    }

    public TypeRoom updateTypeRoom(Integer typeRoomId, TypeRoom typeRoom) throws IOException, InterruptedException {
        return sendJson("/api/type-rooms/" + typeRoomId, "PUT", typeRoom, TypeRoom.class);
    }

    public void deleteTypeRoom(Integer typeRoomId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/type-rooms/" + typeRoomId))
                .timeout(Duration.ofSeconds(20))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
    }

    public List<TypeRoom> searchTypeRoomsByName(String typeRoomName) throws IOException, InterruptedException {
        return getList("/api/type-rooms/search/name?name=" + encode(typeRoomName),
                new TypeReference<List<TypeRoom>>() {
                });
    }

    // LandmarkType api
    public List<LandmarkType> getLandmarkTypes() throws IOException, InterruptedException {
        return getList("/api/landmark-types", new TypeReference<List<LandmarkType>>() {
        });
    }

    // Landmark api

    public List<Landmark> getLandmarks() throws IOException, InterruptedException {
        return getList("/api/landmarks", new TypeReference<List<Landmark>>() {
        });
    }

    public List<Landmark> searchLandmarksByName(String keyword) throws IOException, InterruptedException {
        return getList("/api/landmarks/search/name?keyword=" + encode(keyword),
                new TypeReference<List<Landmark>>() {
                });
    }

    public List<Landmark> searchLandmarksByTypeId(Integer typeId) throws IOException, InterruptedException {
        return getList("/api/landmarks/type/" + typeId,
                new TypeReference<List<Landmark>>() {
                });
    }

    public List<Landmark> searchLandmarksByAddress(String keyword) throws IOException, InterruptedException {
        return getList("/api/landmarks/search/address?keyword=" + encode(keyword),
                new TypeReference<List<Landmark>>() {
                });
    }

    public List<Landmark> getLandmarksActive() throws IOException, InterruptedException {
        return getList("/api/landmarks/active",
                new TypeReference<List<Landmark>>() {
                });
    }

    public List<Landmark> searchLandmarksByActive(Boolean isActive) throws IOException, InterruptedException {
        return getList("/api/landmarks/search/is-active?keyword=" + isActive,
                new TypeReference<List<Landmark>>() {
                });
    }

    public Landmark createLandmark(Landmark landmark) throws IOException, InterruptedException {
        return sendJson("/api/landmarks", "POST", landmark, Landmark.class);
    }

    public Landmark updateLandmark(Integer landmarkId, Landmark landmark) throws IOException, InterruptedException {
        return sendJson("/api/landmarks/" + landmarkId, "PUT", landmark, Landmark.class);
    }

    public void deleteLandmark(Integer landmarkId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/landmarks/" + landmarkId))
                .timeout(Duration.ofSeconds(20))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
    }

    // Ensures the HTTP response is successful
    private static void ensureSuccess(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new IllegalStateException("HTTP " + statusCode + ": " + response.body());
        }
    }

    private <T> T sendJson(String path, String method, Object body, Class<T> responseType)
            throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json")
                .method(method, HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
        return objectMapper.readValue(response.body(), responseType);
    }

    private <T> T getList(String path, TypeReference<T> typeReference) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response);
        return objectMapper.readValue(response.body(), typeReference);
    }

    private static String encode(String value) {
        return java.net.URLEncoder.encode(value == null ? "" : value, java.nio.charset.StandardCharsets.UTF_8);
    }

    private static String stripTrailingSlash(String value) {
        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }

    public static ApiClient getInstance() {
        return INSTANCE;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}