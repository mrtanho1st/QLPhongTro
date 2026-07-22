package com.minhtan.qlptclient.gui;

import com.minhtan.qlptclient.entity.Amenity;
import com.minhtan.qlptclient.entity.Building;
import com.minhtan.qlptclient.entity.Room;
import com.minhtan.qlptclient.entity.TypeRoom;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RoomGUI extends BorderPane {

    private final Label statusLabel = new Label("Sẵn sàng kết nối API backend");
    private final ObservableList<Room> roomItems = FXCollections.observableArrayList();
    private final ObservableList<Building> buildingItems = FXCollections.observableArrayList();
    private final ObservableList<TypeRoom> typeRoomItems = FXCollections.observableArrayList();
    private final ObservableList<Amenity> amenityItems = FXCollections.observableArrayList();
    private final ObservableList<Amenity> availableAmenityItems = FXCollections.observableArrayList();
    private final TableView<Room> roomList = new TableView<>(roomItems);
    private final TextField roomIdField = new TextField();
    private final ComboBox<Building> buildingBox = new ComboBox<>(buildingItems);
    private final TextField roomCodeField = new TextField();
    private final TextField priceField = new TextField();
    private final TextField bedroomField = new TextField();
    private final ComboBox<TypeRoom> typeRoomBox = new ComboBox<>(typeRoomItems);
    private final TextField personLimitField = new TextField();
    private final TextField areaField = new TextField();
    private final CheckBox lockedBox = new CheckBox("Đã khóa");
    private final DatePicker availableDatePicker = new DatePicker();
    private final TextField noteField = new TextField();
    private final ComboBox<String> searchModeBox = new ComboBox<>(FXCollections.observableArrayList(
            "All",
            "Building ID",
            "Type Room Name",
            "Room Code",
            "Price",
            "Bedroom",
            "Person Limit", // >= x
            "Area", // >= x
            "Locked", // 0:false or 1:true
            "Available Date",
            "Note"));
    private final TextField searchField = new TextField();
    private final Button refreshButton = new Button("Tải lại");
    private final Button searchButton = new Button("Tìm");
    private final Button createButton = new Button("Thêm");
    private final Button updateButton = new Button("Cập nhật");
    private final Button deleteButton = new Button("Xóa");
    private final Button clearButton = new Button("Làm mới");
    private final ListView<Amenity> amenityList = new ListView<>(amenityItems);
    private final ComboBox<Amenity> amenityBox = new ComboBox<>(availableAmenityItems);
    private final Button addAmenityButton = new Button("Thêm");
    private final Button removeAmenityButton = new Button("Xoá");
    private final Button uploadButton = new Button("Tải lên");

    public RoomGUI() {
        buildUi();
    }

    private void buildUi() {
        statusLabel.setStyle("-fx-text-fill: #4b5563;");

        configureRoomTable();
        configureBuildingComboBox();
        configureTypeRoomComboBox();

        roomList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        roomIdField.setEditable(false);
        roomIdField.setPromptText("Tự động");
        buildingBox.setPromptText("Chọn tòa nhà");
        typeRoomBox.setPromptText("Chọn loại phòng");
        roomCodeField.setPromptText("Mã phòng");
        priceField.setPromptText("Giá phòng");
        bedroomField.setPromptText("Số phòng ngủ");
        personLimitField.setPromptText("Số người tối đa");
        areaField.setPromptText("Diện tích");
        availableDatePicker.setPromptText("yyyy-MM-dd");
        noteField.setPromptText("Ghi chú");
        searchModeBox.getSelectionModel().selectFirst();
        searchField.setPromptText("Nhập giá trị tìm kiếm");

        amenityList.setPlaceholder(new Label("Chưa có tiện ích cho phòng này"));
        amenityList.setPrefHeight(180);
        amenityList.setCellFactory(listView -> new AmenityListCell());
        amenityList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        amenityBox.setPromptText("Chọn tiện ích");
        amenityBox.setCellFactory(comboBox -> new AmenityListCell());
        amenityBox.setButtonCell(new AmenityListCell());

        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        HBox searchControls = new HBox(10, searchModeBox, searchField, searchButton, refreshButton);
        searchControls.setAlignment(Pos.CENTER_LEFT);

        VBox searchBar = new VBox(8, sectionLabel("Danh sách phòng"), searchControls);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(0, 0, 4, 0));

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setMinWidth(112);
        ColumnConstraints fieldColumn = new ColumnConstraints();
        fieldColumn.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(labelColumn, fieldColumn);
        form.addRow(0, new Label("Room ID"), roomIdField);
        form.addRow(1, new Label("Building ID"), buildingBox);
        form.addRow(2, new Label("Room Code"), roomCodeField);
        form.addRow(3, new Label("Price"), priceField);
        form.addRow(4, new Label("Bedroom"), bedroomField);
        form.addRow(5, new Label("Type room"), typeRoomBox);
        form.addRow(6, new Label("Person Limit"), personLimitField);
        form.addRow(7, new Label("Area"), areaField);
        form.addRow(8, new Label("Locked"), lockedBox);
        form.addRow(9, new Label("Available Date"), availableDatePicker);
        form.addRow(10, new Label("Note"), noteField);
        form.getChildren().stream()
                .filter(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setMaxWidth(Double.MAX_VALUE));
        buildingBox.setMaxWidth(Double.MAX_VALUE);
        typeRoomBox.setMaxWidth(Double.MAX_VALUE);
        availableDatePicker.setMaxWidth(Double.MAX_VALUE);

        HBox actionBar = new HBox(10, createButton, updateButton, deleteButton, clearButton);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(4, 0, 0, 0));

        VBox leftPane = new VBox(12, searchBar, roomList);
        leftPane.setMinWidth(520);
        VBox.setVgrow(roomList, Priority.ALWAYS);

        HBox amenityActions = new HBox(10, amenityBox, addAmenityButton, removeAmenityButton, uploadButton);
        amenityActions.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(amenityBox, Priority.ALWAYS);

        VBox amenitySection = new VBox(8, sectionLabel("Tiện ích phòng"), amenityList, amenityActions);
        amenitySection.setMinHeight(220);
        VBox.setVgrow(amenityList, Priority.ALWAYS);

        VBox rightContent = new VBox(14, sectionLabel("Thông tin phòng"), form, actionBar, new Separator(),
                amenitySection);
        rightContent.setMinWidth(380);

        ScrollPane rightScroll = new ScrollPane(rightContent);
        rightScroll.setFitToWidth(true);
        rightScroll.setFitToHeight(true);
        rightScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        SplitPane splitPane = new SplitPane(leftPane, rightScroll);
        splitPane.setDividerPositions(0.62);
        splitPane.setStyle("-fx-background-color: transparent;");

        setPadding(new Insets(0));
        setTop(statusLabel);
        BorderPane.setMargin(splitPane, new Insets(12, 0, 0, 0));
        setCenter(splitPane);
        setStyle("-fx-background-color: #f3f6fb;");
        styleControls();
    }

    private void configureRoomTable() {
        TableColumn<Room, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getRoomId()));
        idColumn.setPrefWidth(64);

        TableColumn<Room, String> buildingColumn = new TableColumn<>("Building");
        buildingColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(buildingText(cellData.getValue())));
        buildingColumn.setPrefWidth(210);

        TableColumn<Room, String> codeColumn = new TableColumn<>("Room Code");
        codeColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(textOrEmpty(cellData.getValue().getRoomCode())));
        codeColumn.setPrefWidth(110);

        TableColumn<Room, BigDecimal> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrice()));
        priceColumn.setPrefWidth(110);

        TableColumn<Room, Integer> bedroomColumn = new TableColumn<>("Bedroom");
        bedroomColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBedroom()));
        bedroomColumn.setPrefWidth(90);

        TableColumn<Room, String> typeRoomColumn = new TableColumn<>("Type Room");
        typeRoomColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(typeRoomText(cellData.getValue())));
        typeRoomColumn.setPrefWidth(210);

        TableColumn<Room, Integer> personLimitColumn = new TableColumn<>("Person Limit");
        personLimitColumn
                .setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPersonLimit()));
        personLimitColumn.setPrefWidth(110);

        TableColumn<Room, BigDecimal> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getArea()));
        areaColumn.setPrefWidth(90);

        TableColumn<Room, String> lockedColumn = new TableColumn<>("Locked");
        lockedColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(booleanText(cellData.getValue().getLocked())));
        lockedColumn.setPrefWidth(90);

        TableColumn<Room, LocalDate> availableDateColumn = new TableColumn<>("Available Date");
        availableDateColumn
                .setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAvailableDate()));
        availableDateColumn.setPrefWidth(130);

        TableColumn<Room, String> noteColumn = new TableColumn<>("Note");
        noteColumn
                .setCellValueFactory(cellData -> new ReadOnlyStringWrapper(textOrEmpty(cellData.getValue().getNote())));
        noteColumn.setPrefWidth(180);

        roomList.getColumns().setAll(List.of(idColumn, buildingColumn, codeColumn, priceColumn, bedroomColumn,
                typeRoomColumn, personLimitColumn, areaColumn, lockedColumn, availableDateColumn, noteColumn));
        roomList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        roomList.setPlaceholder(new Label("Chưa có dữ liệu phòng"));
        roomList.setStyle(
                "-fx-background-color: white; -fx-border-color: #d8dee9; -fx-border-radius: 8; -fx-background-radius: 8;");
    }

    private void configureBuildingComboBox() {
        buildingBox.setCellFactory(comboBox -> new BuildingListCell());
        buildingBox.setButtonCell(new BuildingListCell());
    }

    private void configureTypeRoomComboBox() {
        typeRoomBox.setCellFactory(comboBox -> new TypeRoomListCell());
        typeRoomBox.setButtonCell(new TypeRoomListCell());
    }

    private String buildingText(Room room) {
        if (room == null) {
            return "";
        }
        Building building = room.getBuilding();
        if (building != null && building.getTrueAddress() != null && !building.getTrueAddress().isBlank()) {
            return building.getTrueAddress();
        }
        return room.getBuildingId() == null ? "" : String.valueOf(room.getBuildingId());
    }

    private String buildingText(Building building) {
        if (building == null) {
            return "";
        }
        String trueAddress = textOrEmpty(building.getTrueAddress());
        String id = building.getBuildingId() == null ? "" : String.valueOf(building.getBuildingId());
        if (trueAddress.isBlank()) {
            return id;
        }
        return id.isBlank() ? trueAddress : id + " - " + trueAddress;
    }

    private String typeRoomText(Room room) {
        if (room == null) {
            return "";
        }
        TypeRoom typeRoom = room.getTypeRoom();
        if (typeRoom != null && typeRoom.getTypeRoomName() != null && !typeRoom.getTypeRoomName().isBlank()) {
            return typeRoom.getTypeRoomName();
        }
        return room.getTypeRoomId() == null ? "" : String.valueOf(room.getTypeRoomId());
    }

    private String booleanText(Boolean value) {
        return Boolean.TRUE.equals(value) ? "True" : "False";
    }

    private String textOrEmpty(String text) {
        return text == null ? "" : text;
    }

    private Label sectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");
        return label;
    }

    private void styleControls() {
        String fieldStyle = "-fx-background-color: white; -fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-background-radius: 6;";
        roomIdField.setStyle(fieldStyle);
        buildingBox.setStyle(fieldStyle);
        typeRoomBox.setStyle(fieldStyle);
        roomCodeField.setStyle(fieldStyle);
        priceField.setStyle(fieldStyle);
        bedroomField.setStyle(fieldStyle);
        personLimitField.setStyle(fieldStyle);
        areaField.setStyle(fieldStyle);
        availableDatePicker.setStyle(fieldStyle);
        noteField.setStyle(fieldStyle);
        searchField.setStyle(fieldStyle);
        searchModeBox.setStyle(fieldStyle);
        amenityBox.setStyle(fieldStyle);
        amenityList.setStyle(
                "-fx-background-color: white; -fx-border-color: #cbd5e1; -fx-border-radius: 8; -fx-background-radius: 8;");

        createButton.setStyle(primaryButtonStyle("#2563eb"));
        updateButton.setStyle(primaryButtonStyle("#0f766e"));
        deleteButton.setStyle(primaryButtonStyle("#dc2626"));
        searchButton.setStyle(primaryButtonStyle("#334155"));
        refreshButton.setStyle(secondaryButtonStyle());
        clearButton.setStyle(secondaryButtonStyle());
        addAmenityButton.setStyle(primaryButtonStyle("#16a34a"));
        removeAmenityButton.setStyle(primaryButtonStyle("#dc2626"));
        uploadButton.setStyle(primaryButtonStyle("#2563eb"));
    }

    private String primaryButtonStyle(String color) {
        return "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 6; -fx-padding: 8 14;";
    }

    private String secondaryButtonStyle() {
        return "-fx-background-color: white; -fx-text-fill: #1f2937; -fx-font-weight: bold; "
                + "-fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8 14;";
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public TableView<Room> getRoomList() {
        return roomList;
    }

    public TextField getRoomIdField() {
        return roomIdField;
    }

    public ComboBox<Building> getBuildingBox() {
        return buildingBox;
    }

    public TextField getRoomCodeField() {
        return roomCodeField;
    }

    public TextField getPriceField() {
        return priceField;
    }

    public TextField getBedroomField() {
        return bedroomField;
    }

    public ComboBox<TypeRoom> getTypeRoomBox() {
        return typeRoomBox;
    }

    public TextField getPersonLimitField() {
        return personLimitField;
    }

    public TextField getAreaField() {
        return areaField;
    }

    public CheckBox getLockedBox() {
        return lockedBox;
    }

    public DatePicker getAvailableDatePicker() {
        return availableDatePicker;
    }

    public TextField getNoteField() {
        return noteField;
    }

    public ComboBox<String> getSearchModeBox() {
        return searchModeBox;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public ListView<Amenity> getAmenityList() {
        return amenityList;
    }

    public ComboBox<Amenity> getAmenityBox() {
        return amenityBox;
    }

    public Button getAddAmenityButton() {
        return addAmenityButton;
    }

    public Button getRemoveAmenityButton() {
        return removeAmenityButton;
    }

    public Button getUploadButton() {
        return uploadButton;
    }

    public ObservableList<Room> getRoomItems() {
        return roomItems;
    }

    public ObservableList<Building> getBuildingItems() {
        return buildingItems;
    }
    public ObservableList<TypeRoom> getTypeRoomItems() {
        return typeRoomItems;
    }
    public ObservableList<Amenity> getAmenityItems() {
        return amenityItems;
    }

    public ObservableList<Amenity> getAvailableAmenityItems() {
        return availableAmenityItems;
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    public void setDetails(String text) {
    }

    public void clearForm() {
        roomIdField.clear();
        buildingBox.getSelectionModel().clearSelection();
        roomCodeField.clear();
        priceField.clear();
        bedroomField.clear();
        typeRoomBox.getSelectionModel().clearSelection();
        personLimitField.clear();
        areaField.clear();
        lockedBox.setSelected(false);
        availableDatePicker.setValue(null);
        noteField.clear();
        amenityItems.clear();
        amenityBox.getSelectionModel().clearSelection();
        amenityList.getSelectionModel().clearSelection();
        roomList.getSelectionModel().clearSelection();
    }

    private class BuildingListCell extends ListCell<Building> {
        @Override
        protected void updateItem(Building building, boolean empty) {
            super.updateItem(building, empty);
            setText(empty || building == null ? null : buildingText(building));
        }
    }

    private class TypeRoomListCell extends ListCell<TypeRoom> {
        @Override
        protected void updateItem(TypeRoom typeRoom, boolean empty) {
            super.updateItem(typeRoom, empty);
            setText(empty || typeRoom == null ? null : typeRoomText(typeRoom));
        }
    }

    private class AmenityListCell extends ListCell<Amenity> {
        @Override
        protected void updateItem(Amenity amenity, boolean empty) {
            super.updateItem(amenity, empty);
            setText(empty || amenity == null ? null : amenityText(amenity));
        }
    }

    private String amenityText(Amenity amenity) {
        if (amenity == null) {
            return "";
        }
        String name = textOrEmpty(amenity.getName());
        String id = amenity.getAmenityId() == null ? "" : String.valueOf(amenity.getAmenityId());
        return id.isBlank() ? name : id + " - " + name;
    }

    private String typeRoomText(TypeRoom typeRoom) {
        if (typeRoom == null) {
            return "";
        }
        String name = textOrEmpty(typeRoom.getTypeRoomName());
        String id = typeRoom.getTypeRoomId() == null ? "" : String.valueOf(typeRoom.getTypeRoomId());
        return id.isBlank() ? name : id + " - " + name;
    }
}
