package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.Building;
import com.minhtan.qlptclient.entity.Room;
import com.minhtan.qlptclient.entity.RoomMedia;
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
    private Building selectedBuilding;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SearchRoomController(SearchRoomGUI view) {
        this.view = view;
        wireEvents();
        loadInitialData();
    }

    private void wireEvents() {
        view.getSearchButton().setOnAction(event -> searchRooms());
        view.getResetButton().setOnAction(event -> resetFilters());
        view.getRefreshButton().setOnAction(event -> reloadData());
    }

    private void loadInitialData() {
        reloadData();
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

        return rooms.stream()
                .filter(room -> room != null)
                .filter(room -> Boolean.FALSE.equals(room.getLocked()))
                .filter(room -> filterByAddress(room, address))
                .filter(room -> filterByPrice(room, minPrice, maxPrice))
                .filter(room -> filterByAvailableDate(room, selectedDate))
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
        view.getDetailPane().getChildren().clear();
        if (room == null) {
            view.getDetailPane().getChildren().add(new Label("Chọn phòng để xem thông tin"));
            return;
        }

        Building building = findBuildingById(room.getBuildingId());
        Label title = new Label(textOf(room.getRoomCode()));
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        Label buildingName = new Label("Tòa nhà: " + (building == null ? "" : textOf(building.getTrueAddress())));
        Label fakeAddress = new Label("Địa chỉ ảo: " + (building == null ? "" : textOf(building.getFakeAddress())));
        Label price = new Label("Giá: " + formatPrice(room.getPrice()));
        Label area = new Label("Diện tích: " + (room.getArea() == null ? "" : room.getArea().toPlainString()));
        Label bedroom = new Label("Phòng ngủ: " + (room.getBedroom() == null ? "" : room.getBedroom()));
        Label people = new Label("Số người tối đa: " + (room.getPersonLimit() == null ? "" : room.getPersonLimit()));
        Label availableDate = new Label(
                "Ngày có sẵn: " + (room.getAvailableDate() == null ? "" : room.getAvailableDate().format(formatter)));
        Label note = new Label("Ghi chú: " + textOf(room.getNote()));
        Label description = new Label("Mô tả: " + textOf(room.getNote()));

        VBox infoBox = new VBox(6, title, buildingName, fakeAddress, price, area, bedroom, people, availableDate, note,
                description);
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
        stage.setScene(new Scene(roomView, 1400, 800));
        stage.show();
    }

    private void openMediaDialog(Room room) {
        if (room == null || room.getRoomId() == null) {
            return;
        }
        new RoomMediaDialog(room, apiClient).show();
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
}
