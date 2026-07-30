package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.Amenity;
import com.minhtan.qlptclient.entity.Building;
import com.minhtan.qlptclient.entity.Commission;
import com.minhtan.qlptclient.entity.District;
import com.minhtan.qlptclient.entity.Room;
import com.minhtan.qlptclient.entity.RoomAmenity;
import com.minhtan.qlptclient.entity.RoomMedia;
import com.minhtan.qlptclient.entity.TypeRoom;
import com.minhtan.qlptclient.gui.RoomGUI;
import com.minhtan.qlptclient.gui.RoomMediaDialog;
import com.minhtan.qlptclient.gui.SearchRoomGUI;
import com.minhtan.qlptclient.service.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.HostServices;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchRoomController {

    private final ApiClient apiClient = new ApiClient("http://localhost:8080");
    private final SearchRoomGUI view;
    private List<Building> allBuildings = new ArrayList<>();
    private List<Room> allRooms = new ArrayList<>();
    private List<TypeRoom> allTypeRooms = new ArrayList<>();
    private List<District> allDistricts = new ArrayList<>();
    private List<Commission> allCommissions = new ArrayList<>();
    private List<Amenity> allAmenities = new ArrayList<>();
    private List<RoomAmenity> allRoomAmenities = new ArrayList<>();
    private Building selectedBuilding;
    private Room currentRoom;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private HostServices hostServices;

    public SearchRoomController(SearchRoomGUI view) {
        this.view = view;
        wireEvents();
        loadInitialData();
    }

    private void wireEvents() {
        view.getSearchButton().setOnAction(event -> searchRooms());
        view.getResetButton().setOnAction(event -> resetFilters());
        view.getRefreshButton().setOnAction(event -> loadInitialData());
        view.getLocationButton().setOnAction(event -> openLocationOnMap());
    }

    private void loadInitialData() {
        reloadData();
        loadTypeRoomValues();
        loadDistrictValues();
        loadCommissionValues();
        loadAmenityValues();
    }

    private void reloadData() {
        allBuildings = new ArrayList<>();
        allRooms = new ArrayList<>();
        selectedBuilding = null;
        view.getBuildingListPane().getChildren().clear();
        view.getRoomCardsPane().getChildren().clear();
        view.getDetailPane().getChildren().clear();
        updateStatus("Đang tải lại dữ liệu...");
        loadBuildings();
    }

    private void loadBuildings() {
        Task<List<Building>> buildingsTask = new Task<>() {
            @Override
            protected List<Building> call() throws Exception {
                return apiClient.getBuildings();
            }
        };

        buildingsTask.setOnSucceeded(event -> {
            allBuildings = buildingsTask.getValue();
            renderBuildings(allBuildings);
            loadRooms();
        });

        buildingsTask
                .setOnFailed(event -> showAlert("Lỗi", buildFailureMessage(buildingsTask, "Không tải được tòa nhà")));

        startTask(buildingsTask);
    }

    private void loadRooms() {
        Task<List<Room>> roomsTask = new Task<>() {
            @Override
            protected List<Room> call() throws Exception {
                return apiClient.getRooms();
            }
        };

        roomsTask.setOnSucceeded(event -> {
            allRooms = roomsTask.getValue();
            if (selectedBuilding != null) {
                selectBuilding(selectedBuilding);
            } else if (!allRooms.isEmpty()) {
                selectBuilding(allBuildings.isEmpty() ? null : allBuildings.get(0));
            } else {
                renderRooms(List.of());
                view.getDetailPane().getChildren().clear();
                view.getDetailPane().getChildren().add(new Label("Không có phòng nào để hiển thị"));
                updateStatus("Đã tải lại dữ liệu");
            }
        });

        roomsTask.setOnFailed(event -> showAlert("Lỗi", buildFailureMessage(roomsTask, "Không tải được phòng")));

        startTask(roomsTask);
    }

    private void searchRooms() {
        renderRooms(filterRooms(allRooms));
        updateStatus("Đã áp dụng bộ lọc");
    }

    private void resetFilters() {
        view.getAddressField().clear();
        view.getMinPriceField().clear();
        view.getMaxPriceField().clear();
        view.getAvailableDatePicker().setValue(null);
        view.getTypeRoomComboBox().getSelectionModel().clearSelection();
        view.getDistrictComboBox().getSelectionModel().clearSelection();
        view.getCommissionComboBox().getSelectionModel().clearSelection();
        view.getAmenityComboBox().getSelectionModel().clearSelection();
        renderRooms(filterRooms(allRooms));
        updateStatus("Đã đặt lại bộ lọc");
    }

    private List<Room> filterRooms(List<Room> rooms) {
        String address = textOf(view.getAddressField().getText());
        String minPriceText = textOf(view.getMinPriceField().getText());
        String maxPriceText = textOf(view.getMaxPriceField().getText());
        LocalDate selectedDate = view.getAvailableDatePicker().getValue();

        BigDecimal minPrice = parsePrice(minPriceText);
        BigDecimal maxPrice = parsePrice(maxPriceText);

        String selectedTypeRoom = view.getTypeRoomComboBox().getValue();
        String selectedDistrict = view.getDistrictComboBox().getValue();
        String selectedCommission = view.getCommissionComboBox().getValue();
        String selectedAmenity = view.getAmenityComboBox().getValue();

        return rooms.stream()
                .filter(room -> room != null)
                .filter(room -> Boolean.FALSE.equals(room.getLocked()))
                .filter(room -> filterByAddress(room, address))
                .filter(room -> filterByPrice(room, minPrice, maxPrice))
                .filter(room -> filterByAvailableDate(room, selectedDate))
                .filter(room -> filterByTypeRoom(room, selectedTypeRoom))
                .filter(room -> filterByDistrict(room, selectedDistrict))
                .filter(room -> filterByCommission(room, selectedCommission))
                .filter(room -> filterByAmenity(room, selectedAmenity))
                .toList();
    }

    private boolean filterByAddress(Room room, String address) {
        if (address.isBlank()) {
            return true;
        }
        Building building = findBuildingById(room.getBuildingId());
        String buildingAddress = building == null ? "" : building.getTrueAddress();
        return buildingAddress != null && buildingAddress.toLowerCase().contains(address.toLowerCase());
    }

    private boolean filterByPrice(Room room, BigDecimal minPrice, BigDecimal maxPrice) {
        if (room.getPrice() == null) {
            return true;
        }
        boolean aboveMin = minPrice == null || room.getPrice().compareTo(minPrice) >= 0;
        boolean belowMax = maxPrice == null || room.getPrice().compareTo(maxPrice) <= 0;
        return aboveMin && belowMax;
    }

    private boolean filterByAvailableDate(Room room, LocalDate selectedDate) {
        if (selectedDate == null) {
            return true;
        }
        return room.getAvailableDate() == null || !room.getAvailableDate().isAfter(selectedDate);
    }

    private boolean filterByTypeRoom(Room room, String value) {
        if (value == null) {
            return true;
        }
        return room.getTypeRoom() != null && value.equals(room.getTypeRoom().getTypeRoomName());
    }

    private boolean filterByDistrict(Room room, String value) {
        if (value == null) {
            return true;
        }
        Building building = findBuildingById(room.getBuildingId());
        if (building == null || building.getDistrict() == null) {
            return false;
        }
        return value.equals(building.getDistrict().getDistrictName());
    }

    private boolean filterByCommission(Room room, String value) {
        if (value == null) {
            return true;
        }
        return allCommissions.stream()
                .filter(commission -> value.equals(formatCommission(commission)))
                .anyMatch(commission -> Objects.equals(commission.getBuildingId(), room.getBuildingId()));
    }

    private boolean filterByAmenity(Room room, String value) {
        if (value == null) {
            return true;
        }
        Integer amenityId = allAmenities.stream()
                .filter(amenity -> value.equals(amenity.getName()))
                .map(Amenity::getAmenityId)
                .findFirst()
                .orElse(null);
        if (amenityId == null) {
            return false;
        }
        // LƯU Ý: giả định RoomAmenity có getRoomId() và getAmenityId(); chỉnh lại tên getter nếu khác.
        return allRoomAmenities.stream()
                .anyMatch(roomAmenity -> Objects.equals(roomAmenity.getAmenityId(), amenityId)
                        && Objects.equals(roomAmenity.getRoomId(), room.getRoomId()));
    }

    private void loadTypeRoomValues() {
        Task<List<TypeRoom>> task = new Task<>() {
            @Override
            protected List<TypeRoom> call() throws Exception {
                return apiClient.getTypeRooms();
            }
        };

        task.setOnSucceeded(event -> {
            allTypeRooms = task.getValue();
            List<String> values = allTypeRooms.stream()
                    .filter(Objects::nonNull)
                    .map(TypeRoom::getTypeRoomName)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            Platform.runLater(() -> view.getTypeRoomComboBox().getItems().setAll(values));
        });

        task.setOnFailed(event -> showAlert("Lỗi", buildFailureMessage(task, "Không tải được loại phòng")));

        startTask(task);
    }

    private void loadDistrictValues() {
        Task<List<District>> task = new Task<>() {
            @Override
            protected List<District> call() throws Exception {
                return apiClient.getDistricts();
            }
        };

        task.setOnSucceeded(event -> {
            allDistricts = task.getValue();
            List<String> values = allDistricts.stream()
                    .filter(Objects::nonNull)
                    .map(District::getDistrictName)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            Platform.runLater(() -> view.getDistrictComboBox().getItems().setAll(values));
        });

        task.setOnFailed(event -> showAlert("Lỗi", buildFailureMessage(task, "Không tải được quận")));

        startTask(task);
    }

    private void loadCommissionValues() {
        Task<List<Commission>> task = new Task<>() {
            @Override
            protected List<Commission> call() throws Exception {
                return apiClient.getCommissions();
            }
        };

        task.setOnSucceeded(event -> {
            allCommissions = task.getValue();
            List<String> values = allCommissions.stream()
                    .filter(Objects::nonNull)
                    .map(this::formatCommission)
                    .distinct()
                    .toList();
            Platform.runLater(() -> view.getCommissionComboBox().getItems().setAll(values));
        });

        task.setOnFailed(event -> showAlert("Lỗi", buildFailureMessage(task, "Không tải được hoa hồng")));

        startTask(task);
    }

    private String formatCommission(Commission commission) {
        String contractMonth = commission.getContractMonth() == null ? "" : commission.getContractMonth().toString();
        String deposit = commission.getDeposit() == null ? "" : commission.getDeposit().stripTrailingZeros().toPlainString();
        String commissionPercent = commission.getCommissionPercent() == null ? ""
                : commission.getCommissionPercent().stripTrailingZeros().toPlainString() + "%";
        return contractMonth + " - " + deposit + " - " + commissionPercent;
    }

    private void loadAmenityValues() {
        Task<List<Amenity>> task = new Task<>() {
            @Override
            protected List<Amenity> call() throws Exception {
                return apiClient.getAmenities();
            }
        };

        task.setOnSucceeded(event -> {
            allAmenities = task.getValue();
            List<String> values = allAmenities.stream()
                    .filter(Objects::nonNull)
                    .map(Amenity::getName)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            Platform.runLater(() -> view.getAmenityComboBox().getItems().setAll(values));
            loadRoomAmenities();
        });

        task.setOnFailed(event -> showAlert("Lỗi", buildFailureMessage(task, "Không tải được tiện ích")));

        startTask(task);
    }

    private void loadRoomAmenities() {
        Task<List<RoomAmenity>> task = new Task<>() {
            @Override
            protected List<RoomAmenity> call() throws Exception {
                return apiClient.getRoomAmenities();
            }
        };

        task.setOnSucceeded(event -> allRoomAmenities = task.getValue());

        task.setOnFailed(event -> showAlert("Lỗi", buildFailureMessage(task, "Không tải được tiện ích của phòng")));

        startTask(task);
    }

    private void renderBuildings(List<Building> buildings) {
        view.getBuildingListPane().getChildren().clear();
        for (Building building : buildings) {
            VBox card = buildBuildingCard(building);
            view.getBuildingListPane().getChildren().add(card);
        }
    }

    private VBox buildBuildingCard(Building building) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(12));
        card.setStyle(
                "-fx-background-color:white; -fx-background-radius:10; -fx-border-radius:10; -fx-border-color:#e2e8f0; -fx-cursor:hand;");
        card.setOnMouseEntered(event -> card.setStyle(
                "-fx-background-color:#f8fafc; -fx-background-radius:10; -fx-border-radius:10; -fx-border-color:#94a3b8; -fx-cursor:hand;"));
        card.setOnMouseExited(event -> card.setStyle(
                "-fx-background-color:white; -fx-background-radius:10; -fx-border-radius:10; -fx-border-color:#e2e8f0; -fx-cursor:hand;"));
        card.setOnMouseClicked(event -> selectBuilding(building));

        Label title = new Label("🏢 " + textOf(building.getTrueAddress()));
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        int count = (int) allRooms.stream()
                .filter(room -> room != null && building.getBuildingId() != null
                        && building.getBuildingId().equals(room.getBuildingId())
                        && Boolean.FALSE.equals(room.getLocked()))
                .count();
        Label subtitle = new Label("Có " + count + " phòng");
        subtitle.setStyle("-fx-text-fill:#64748b;");

        card.getChildren().addAll(title, subtitle);
        return card;
    }

    private void renderRooms(List<Room> rooms) {
        view.getRoomCardsPane().getChildren().clear();
        for (Room room : rooms) {
            VBox card = buildRoomCard(room);
            view.getRoomCardsPane().getChildren().add(card);
        }
    }

    private VBox buildRoomCard(Room room) {
        VBox card = new VBox(8);
        card.setPrefWidth(300);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-background-color:white; -fx-background-radius:12; -fx-border-radius:12; -fx-border-color:#e2e8f0; -fx-cursor:hand;");
        card.setOnMouseEntered(event -> card.setStyle(
                "-fx-background-color:#f8fafc; -fx-background-radius:12; -fx-border-radius:12; -fx-border-color:#94a3b8; -fx-cursor:hand;"));
        card.setOnMouseExited(event -> card.setStyle(
                "-fx-background-color:white; -fx-background-radius:12; -fx-border-radius:12; -fx-border-color:#e2e8f0; -fx-cursor:hand;"));
        card.setOnMouseClicked(event -> {
            showRoomDetail(room);
            if (event.getClickCount() == 2) {
                openRoomDetail(room);
            }
        });

        ImageView imageView = new ImageView();
        imageView.setFitWidth(280);
        imageView.setFitHeight(152);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setStyle("-fx-background-color:#e2e8f0;");

        String imageUrl = firstImageUrl(room);
        if (imageUrl != null) {
            try {
                imageView.setImage(new Image(imageUrl));
            } catch (Exception ignored) {
            }
        }

        Label roomName = new Label(textOf(room.getRoomCode()));
        roomName.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        Label price = new Label(formatPrice(room.getPrice()));
        price.setStyle("-fx-text-fill:#2563eb; -fx-font-weight:bold;");

        VBox content = new VBox(4, roomName, price);
        content.setPadding(new Insets(4, 0, 0, 0));
        card.getChildren().addAll(imageView, content);
        return card;
    }

    private String firstImageUrl(Room room) {
        try {
            List<RoomMedia> media = apiClient.searchRoomMediaByRoomId(room.getRoomId());
            return media.stream()
                    .filter(item -> item != null && item.getMediaType() != null && item.getMediaType() == 1)
                    .filter(item -> item.getSortOrder() != null && item.getSortOrder() == 1)
                    .map(RoomMedia::getUrl)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        } catch (Exception ignored) {
            return null;
        }
    }

    private void showRoomDetail(Room room) {
        currentRoom = room;
        view.getDetailPane().getChildren().clear();
        if (room == null) {
            view.getDetailPane().getChildren().add(new Label("Chọn phòng để xem thông tin"));
            return;
        }

        Building building = findBuildingById(room.getBuildingId());
        Label title = new Label(textOf(room.getRoomCode()));
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        String str_trueAddress = "";
        String str_fakeAddress = "";
        String str_districtName = "";
        if (building != null) {
            str_trueAddress = textOf(building.getTrueAddress());
            str_fakeAddress = textOf(building.getFakeAddress());
            str_districtName = building.getDistrict() == null ? "" : textOf(building.getDistrict().getDistrictName());
        }

        Label buildingName = new Label("Tòa nhà: " + str_trueAddress);
        Label fakeAddress = new Label("Địa chỉ ảo: " + str_fakeAddress);
        Label districtName = new Label("Khu vực: " + str_districtName);
        Label typeRoom = new Label("Loại phòng: " + textOf(room.getTypeRoom() == null ? "" : room.getTypeRoom().getTypeRoomName()));
        Label price = new Label("Giá: " + formatPrice(room.getPrice()));
        Label area = new Label("Diện tích: " + (room.getArea() == null ? "" : room.getArea().toPlainString()));
        Label bedroom = new Label("Phòng ngủ: " + (room.getBedroom() == null ? "" : room.getBedroom()));
        Label people = new Label("Số người tối đa: " + (room.getPersonLimit() == null ? "" : room.getPersonLimit()));
        Label availableDate = new Label(
                "Ngày có sẵn: " + (room.getAvailableDate() == null ? "" : room.getAvailableDate().format(formatter)));
        Label note = new Label("Ghi chú: " + textOf(room.getNote()));

        VBox infoBox = new VBox(6, title, buildingName, fakeAddress, districtName, typeRoom, price, area, bedroom, people, availableDate, note);
        infoBox.setPadding(new Insets(12));
        infoBox.setStyle(
                "-fx-background-color:#f8fafc; -fx-background-radius:10; -fx-border-radius:10; -fx-border-color:#e2e8f0;");

        Button detailButton = new Button("Xem chi tiết");
        detailButton.setStyle("-fx-background-color:#2563eb; -fx-text-fill:white; -fx-font-weight:bold;");
        detailButton.setOnAction(event -> openRoomDetail(room));

        Button mediaButton = new Button("Xem ảnh/video");
        mediaButton.setStyle("-fx-background-color:#0ea5e9; -fx-text-fill:white; -fx-font-weight:bold;");
        mediaButton.setOnAction(event -> openMediaDialog(room));

        HBox actions = new HBox(10, detailButton, mediaButton);

        view.getDetailPane().getChildren().addAll(infoBox, actions);
    }

    private void selectBuilding(Building building) {
        selectedBuilding = building;
        view.getBuildingListPane().getChildren().clear();
        for (Building item : allBuildings) {
            VBox card = buildBuildingCard(item);
            if (building != null && Objects.equals(item.getBuildingId(), building.getBuildingId())) {
                card.setStyle(
                        "-fx-background-color:#eff6ff; -fx-background-radius:10; -fx-border-radius:10; -fx-border-color:#60a5fa; -fx-cursor:hand;");
            }
            view.getBuildingListPane().getChildren().add(card);
        }

        List<Room> filteredRooms = allRooms.stream()
                .filter(room -> room != null && Boolean.FALSE.equals(room.getLocked()))
                .filter(room -> building == null || building.getBuildingId() == null
                        || building.getBuildingId().equals(room.getBuildingId()))
                .filter(room -> filterRooms(allRooms).contains(room))
                .toList();

        renderRooms(filteredRooms);
        if (!filteredRooms.isEmpty()) {
            showRoomDetail(filteredRooms.get(0));
        }
    }

    private void openRoomDetail(Room room) {
        if (room == null || room.getRoomId() == null) {
            return;
        }
        RoomGUI roomView = new RoomGUI();
        new RoomController(roomView);
        roomView.getRoomItems().setAll(List.of(room));
        roomView.getRoomList().getSelectionModel().select(room);

        Stage stage = new Stage();
        stage.setTitle("Chi tiết phòng " + textOf(room.getRoomCode()));
        stage.setScene(new Scene(roomView, 900, 600));
        stage.show();
    }

    private void openMediaDialog(Room room) {
        if (room == null || room.getRoomId() == null) {
            return;
        }
        new RoomMediaDialog(room, apiClient).show();
    }

    private void openLocationOnMap() {
        if (currentRoom == null) {
            showAlert("Thông báo", "Vui lòng chọn một phòng để xem vị trí");
            return;
        }

        Building building = findBuildingById(currentRoom.getBuildingId());

        String fakeAddress = building == null
                ? null
                : building.getFakeAddress();

        if (fakeAddress == null || fakeAddress.isBlank()) {
            showAlert("Thông báo", "Phòng này chưa có địa chỉ để xem vị trí");
            return;
        }

        if (hostServices == null) {
            showAlert("Lỗi", "HostServices chưa được khởi tạo");
            return;
        }

        try {
            String query = URLEncoder.encode(
                    fakeAddress,
                    StandardCharsets.UTF_8
            );

            String mapUrl =
                    "https://www.google.com/maps/search/?api=1&query="
                    + query;

            hostServices.showDocument(mapUrl);

        } catch (Exception exception) {
            showAlert(
                    "Lỗi",
                    "Không thể mở Google Maps: "
                    + exception.getMessage()
            );
        }
    }

    private Building findBuildingById(Integer buildingId) {
        return allBuildings.stream()
                .filter(building -> building != null && Objects.equals(buildingId, building.getBuildingId()))
                .findFirst()
                .orElse(null);
    }

    private String textOf(String value) {
        return value == null ? "" : value;
    }

    private BigDecimal parsePrice(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String formatPrice(BigDecimal amount) {
        if (amount == null) {
            return "Liên hệ";
        }
        return amount.toPlainString() + " VNĐ";
    }

    private void updateStatus(String text) {
        Platform.runLater(() -> view.getStatusLabel().setText(text));
    }

    private void startTask(Task<?> task) {
        Thread thread = new Thread(task, "search-room-task");
        thread.setDaemon(true);
        thread.start();
    }

    private String buildFailureMessage(Task<?> task, String fallback) {
        Throwable error = task.getException();
        return error == null ? fallback : error.getMessage();
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

}