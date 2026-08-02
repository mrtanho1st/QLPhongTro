package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.Amenity;
import com.minhtan.qlptclient.entity.Building;
import com.minhtan.qlptclient.entity.Room;
import com.minhtan.qlptclient.entity.RoomAmenity;
import com.minhtan.qlptclient.entity.TypeRoom;
import com.minhtan.qlptclient.gui.RoomGUI;
import com.minhtan.qlptclient.gui.RoomMediaDialog;
import com.minhtan.qlptclient.service.ApiClient;
import com.minhtan.qlptclient.service.MethodAmenity;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RoomController {

    private final RoomGUI view;
    private final List<Amenity> allAmenities = new ArrayList<>();

    public RoomController(RoomGUI view) {
        this.view = view;
        wireEvents();
        loadInitialData();
    }

    private void wireEvents() {
        view.getRoomList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            view.getRoomIdField().setText(valueOrEmpty(selected.getRoomId()));
            selectBuilding(selected.getBuildingId());
            selectTypeRoom(selected.getTypeRoomId());
            view.getRoomCodeField().setText(valueOrEmpty(selected.getRoomCode()));
            view.getPriceField().setText(valueOrEmpty(selected.getPrice()));
            view.getBedroomField().setText(valueOrEmpty(selected.getBedroom()));
            view.getPersonLimitField().setText(valueOrEmpty(selected.getPersonLimit()));
            view.getAreaField().setText(valueOrEmpty(selected.getArea()));
            view.getLockedBox().setSelected(Boolean.TRUE.equals(selected.getLocked()));
            view.getAvailableDatePicker().setValue(selected.getAvailableDate());
            view.getNoteField().setText(valueOrEmpty(selected.getNote()));
            loadRoomAmenities(selected.getRoomId());
        });

        view.getRefreshButton().setOnAction(event -> loadInitialData());
        view.getSearchButton().setOnAction(event -> searchRooms());
        view.getCreateButton().setOnAction(event -> createRoom());
        view.getUpdateButton().setOnAction(event -> updateRoom());
        view.getDeleteButton().setOnAction(event -> deleteRoom());
        view.getClearButton().setOnAction(event -> view.clearForm());
        view.getAddAmenityButton().setOnAction(event -> addAmenityToSelectedRoom());
        view.getRemoveAmenityButton().setOnAction(event -> removeAmenityFromSelectedRoom());
        view.getUploadButton().setOnAction(event -> openMediaDialog());
        view.getRoomList().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                openMediaDialog();
            }
        });
    }

    private void loadInitialData() {
        Task<RoomPageData> task = new Task<>() {
            @Override
            protected RoomPageData call() throws Exception {
                List<Building> buildings = ApiClient.getInstance().getBuildings();
                List<TypeRoom> typeRooms = ApiClient.getInstance().getTypeRooms();
                List<Room> rooms = ApiClient.getInstance().getRooms();
                List<Amenity> amenities = ApiClient.getInstance().getAmenities();
                return new RoomPageData(rooms, buildings, amenities, typeRooms);
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            RoomPageData data = task.getValue();
            view.getBuildingItems().setAll(data.buildings());
            view.getTypeRoomItems().setAll(data.typeRooms());
            allAmenities.clear();
            allAmenities.addAll(data.amenities());
            view.getAvailableAmenityItems().setAll(data.amenities());
            showRooms(data.rooms(), "Tải thành công " + data.rooms().size() + " room(s)");
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tải dữ liệu"));

        view.setStatus("Đang tải /api/rooms và /api/buildings...");
        startTask(task);
    }

    private void searchRooms() {
        String keyword = view.getSearchField().getText() == null ? "" : view.getSearchField().getText().trim();
        String mode = view.getSearchModeBox().getValue();

        if ("All".equals(mode)) {
            loadInitialData();
            return;
        }

        if (keyword.isBlank()) {
            showAlert("Nhập từ khóa", "Hãy nhập giá trị tìm kiếm trước khi tìm.");
            return;
        }

        Task<List<Room>> task = new Task<>() {
            @Override
            protected List<Room> call() throws Exception {
                return switch (mode) {
                    case "Building ID" -> ApiClient.getInstance().searchRoomsByBuildingId(Integer.valueOf(keyword));
                    case "Type Room Name" -> ApiClient.getInstance().searchRoomsByTypeRoomName(keyword);
                    case "Room Code" -> ApiClient.getInstance().searchRoomsByRoomCode(keyword);
                    case "Price" -> ApiClient.getInstance().searchRoomsByPrice(keyword);
                    case "Bedroom" -> ApiClient.getInstance().searchRoomsByBedroom(Integer.valueOf(keyword));
                    case "Person Limit" ->
                        ApiClient.getInstance().searchRoomsByPersonLimitGreaterThanEqual(Integer.valueOf(keyword));
                    case "Area" -> ApiClient.getInstance().searchRoomsByAreaGreaterThanEqual(keyword);
                    case "Locked" -> ApiClient.getInstance().searchRoomsByLocked(Boolean.valueOf(keyword));
                    case "Available Date" -> ApiClient.getInstance().searchRoomsByAvailableDate(keyword);
                    case "Note" -> ApiClient.getInstance().searchRoomsByNote(keyword);
                    default -> ApiClient.getInstance().getRooms();
                };
            }
        };

        task.setOnSucceeded(
                workerStateEvent -> showRooms(task.getValue(), "Tìm thấy " + task.getValue().size() + " room(s)"));

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tìm dữ liệu"));

        view.setStatus("Đang tìm kiếm...");
        startTask(task);
    }

    private void createRoom() {
        runMutationTask("Đang tạo phòng mới...", () -> {
            Room room = readForm();
            room.setRoomId(null);
            ApiClient.getInstance().createRoom(room);
        });
    }

    private void updateRoom() {
        if (view.getRoomIdField().getText().isBlank()) {
            showAlert("Chưa chọn phòng", "Hãy chọn một dòng trong danh sách trước khi cập nhật.");
            return;
        }

        runMutationTask("Đang cập nhật phòng...",
                () -> ApiClient.getInstance().updateRoom(Integer.valueOf(view.getRoomIdField().getText()), readForm()));
    }

    private void deleteRoom() {
        if (view.getRoomIdField().getText().isBlank()) {
            showAlert("Chưa chọn phòng", "Hãy chọn một dòng trong danh sách trước khi xóa.");
            return;
        }

        runMutationTask("Đang xóa phòng...",
                () -> ApiClient.getInstance().deleteRoom(Integer.valueOf(view.getRoomIdField().getText())));
    }

    private void runMutationTask(String statusText, Mutation mutation) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                mutation.run();
                return null;
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            view.setStatus("Thực hiện thành công");
            view.setDetails("Đã cập nhật dữ liệu thành công");
            loadInitialData();
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, statusText));

        view.setStatus(statusText);
        startTask(task);
    }

    private void showRooms(List<Room> rooms, String statusText) {
        view.getRoomItems().setAll(rooms);
        view.setStatus(statusText);
    }

    private void loadRoomAmenities(Integer roomId) {
        if (roomId == null) {
            view.getAmenityItems().clear();
            view.getAvailableAmenityItems().setAll(allAmenities);
            return;
        }

        Task<List<RoomAmenity>> task = new Task<>() {
            @Override
            protected List<RoomAmenity> call() throws Exception {
                return ApiClient.getInstance().searchRoomAmenitiesByRoomId(roomId);
            }
        };

        task.setOnSucceeded(workerStateEvent -> showRoomAmenities(task.getValue()));
        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tải tiện ích phòng"));

        startTask(task);
    }

    private void showRoomAmenities(List<RoomAmenity> roomAmenities) {
        List<Amenity> assignedAmenities = roomAmenities == null ? List.of()
                : roomAmenities.stream()
                        .map(this::resolveAmenity)
                        .filter(Objects::nonNull)
                        .toList();

        view.getAmenityItems().setAll(assignedAmenities);
        Set<Integer> assignedIds = assignedAmenities.stream()
                .map(Amenity::getAmenityId)
                .filter(Objects::nonNull)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);

        List<Amenity> selectable = allAmenities.stream()
                .filter(amenity -> amenity.getAmenityId() == null || !assignedIds.contains(amenity.getAmenityId()))
                .toList();
        view.getAvailableAmenityItems().setAll(selectable);
    }

    private Amenity resolveAmenity(RoomAmenity roomAmenity) {
        if (roomAmenity == null) {
            return null;
        }
        if (roomAmenity.getAmenity() != null) {
            return roomAmenity.getAmenity();
        }
        if (roomAmenity.getAmenityId() == null) {
            return null;
        }
        return allAmenities.stream()
                .filter(amenity -> roomAmenity.getAmenityId().equals(amenity.getAmenityId()))
                .findFirst()
                .orElseGet(() -> {
                    Amenity amenity = new Amenity();
                    amenity.setAmenityId(roomAmenity.getAmenityId());
                    return amenity;
                });
    }

    private void addAmenityToSelectedRoom() {
        Room selectedRoom = view.getRoomList().getSelectionModel().getSelectedItem();
        if (selectedRoom == null || selectedRoom.getRoomId() == null) {
            showAlert("Chưa chọn phòng", "Hãy chọn một phòng trong danh sách trước khi thêm tiện ích.");
            return;
        }

        Amenity selectedAmenity = view.getAmenityBox().getValue();
        if (selectedAmenity == null || selectedAmenity.getAmenityId() == null) {
            showAlert("Chưa chọn tiện ích", "Hãy chọn một tiện ích trong danh sách trước khi thêm.");
            return;
        }

        runAmenityMutationTask("Đang thêm tiện ích...", () -> ApiClient.getInstance().createRoomAmenity(
                new RoomAmenity(selectedRoom.getRoomId(), selectedAmenity.getAmenityId(), null, null)));
    }

    private void removeAmenityFromSelectedRoom() {
        Room selectedRoom = view.getRoomList().getSelectionModel().getSelectedItem();
        if (selectedRoom == null || selectedRoom.getRoomId() == null) {
            showAlert("Chưa chọn phòng", "Hãy chọn một phòng trong danh sách trước khi xóa tiện ích.");
            return;
        }

        Amenity selectedAmenity = view.getAmenityList().getSelectionModel().getSelectedItem();
        if (selectedAmenity == null || selectedAmenity.getAmenityId() == null) {
            showAlert("Chưa chọn tiện ích", "Hãy chọn một tiện ích trong danh sách trước khi xóa.");
            return;
        }

        runAmenityMutationTask("Đang xóa tiện ích...",
                () -> ApiClient.getInstance().deleteRoomAmenity(selectedRoom.getRoomId(),
                        selectedAmenity.getAmenityId()));
    }

    private void openMediaDialog() {
        Room selectedRoom = view.getRoomList().getSelectionModel().getSelectedItem();
        if (selectedRoom == null || selectedRoom.getRoomId() == null) {
            showAlert("Chưa chọn phòng", "Hãy chọn một phòng trước khi xem media.");
            return;
        }

        RoomMediaDialog dialog = new RoomMediaDialog(selectedRoom, ApiClient.getInstance());
        dialog.show();
    }

    private void runAmenityMutationTask(String statusText, Mutation mutation) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                mutation.run();
                return null;
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            view.setStatus("Thực hiện thành công");
            Room selectedRoom = view.getRoomList().getSelectionModel().getSelectedItem();
            if (selectedRoom != null) {
                loadRoomAmenities(selectedRoom.getRoomId());
            }
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, statusText));

        view.setStatus(statusText);
        startTask(task);
    }

    private Room readForm() {
        Building selectedBuilding = view.getBuildingBox().getValue();
        if (selectedBuilding == null || selectedBuilding.getBuildingId() == null) {
            throw new IllegalArgumentException("Hãy chọn Building ID từ danh sách tòa nhà.");
        }

        TypeRoom selectedTypeRoom = view.getTypeRoomBox().getValue();
        if (selectedTypeRoom == null || selectedTypeRoom.getTypeRoomId() == null) {
            throw new IllegalArgumentException("Hãy chọn Type Room ID từ danh sách loại phòng.");
        }

        Room room = new Room();
        String roomIdText = textOf(view.getRoomIdField().getText());
        if (!roomIdText.isBlank()) {
            room.setRoomId(Integer.valueOf(roomIdText));
        }
        room.setBuildingId(selectedBuilding.getBuildingId());
        room.setBuilding(null);
        room.setTypeRoomId(selectedTypeRoom.getTypeRoomId());
        room.setTypeRoom(null);
        room.setRoomCode(textOf(view.getRoomCodeField().getText()));
        room.setPrice(
                MethodAmenity.convertToVnd(decimalOrNull(
                        view.getPriceField().getText(),
                        "Price")));
        room.setBedroom(integerOrNull(view.getBedroomField().getText(), "Bedroom"));
        room.setPersonLimit(integerOrNull(view.getPersonLimitField().getText(), "Person Limit"));
        room.setArea(decimalOrNull(view.getAreaField().getText(), "Area"));
        room.setLocked(view.getLockedBox().isSelected());
        room.setAvailableDate(view.getAvailableDatePicker().getValue());
        room.setNote(textOf(view.getNoteField().getText()));
        return room;
    }

    private void selectBuilding(Integer buildingId) {
        if (buildingId == null) {
            view.getBuildingBox().getSelectionModel().clearSelection();
            return;
        }

        view.getBuildingItems().stream()
                .filter(building -> buildingId.equals(building.getBuildingId()))
                .findFirst()
                .ifPresentOrElse(
                        building -> view.getBuildingBox().getSelectionModel().select(building),
                        () -> view.getBuildingBox().getSelectionModel().clearSelection());
    }

    private void selectTypeRoom(Integer typeRoomId) {
        if (typeRoomId == null) {
            view.getTypeRoomBox().getSelectionModel().clearSelection();
            return;
        }

        view.getTypeRoomItems().stream()
                .filter(typeRoom -> typeRoomId.equals(typeRoom.getTypeRoomId()))
                .findFirst()
                .ifPresentOrElse(
                        typeRoom -> view.getTypeRoomBox().getSelectionModel().select(typeRoom),
                        () -> view.getTypeRoomBox().getSelectionModel().clearSelection());
    }

    private void handleTaskFailure(Task<?> task, String fallbackMessage) {
        Throwable error = task.getException();
        String message = error == null ? fallbackMessage : error.getMessage();
        Platform.runLater(() -> {
            view.setStatus(fallbackMessage);
            view.setDetails(message);
            showAlert("Lỗi kết nối backend", message);
        });
    }

    private void startTask(Task<?> task) {
        Thread thread = new Thread(task, "room-api-client-task");
        thread.setDaemon(true);
        thread.start();
    }

    private static String textOf(String value) {
        return value == null ? "" : value.trim();
    }

    private static Integer integerOrNull(String value, String fieldName) {
        String text = textOf(value);
        if (text.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " phải là số nguyên.");
        }
    }

    private static BigDecimal decimalOrNull(String value, String fieldName) {
        String text = textOf(value);
        if (text.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " phải là số.");
        }
    }

    private static String valueOrEmpty(Integer value) {
        return value == null ? "" : String.valueOf(value);
    }

    private static String valueOrEmpty(BigDecimal value) {
        return value == null ? "" : value.toPlainString();
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private record RoomPageData(List<Room> rooms, List<Building> buildings, List<Amenity> amenities,
            List<TypeRoom> typeRooms) {
    }

    @FunctionalInterface
    private interface Mutation {
        void run() throws Exception;
    }
}
