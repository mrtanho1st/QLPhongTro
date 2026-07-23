package com.minhtan.qlptclient.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

import com.minhtan.qlptclient.entity.Building;
import com.minhtan.qlptclient.entity.District;

public class BuildingGUI extends BorderPane {

    private final Label statusLabel = new Label("Sẵn sàng kết nối API backend");
    private final ObservableList<Building> buildingItems = FXCollections.observableArrayList();
    private final ObservableList<District> districtItems = FXCollections.observableArrayList();
    private final TableView<Building> buildingList = new TableView<>(buildingItems);
    private final ComboBox<District> districtBox = new ComboBox<>(districtItems);
    private final TextField buildingIdField = new TextField();
    private final TextField trueAddressField = new TextField();
    private final TextField fakeAddressField = new TextField();
    private final TextField noteField = new TextField();
    private final TextField ownerPhoneField = new TextField();
    private final ComboBox<String> searchModeBox = new ComboBox<>(FXCollections.observableArrayList(
            "All",
            "District Name",
            "True Address",
            "Fake Address",
            "Note",
            "Owner Phone"));
    private final TextField searchField = new TextField();
    private final Button refreshButton = new Button("Tải lại");
    private final Button searchButton = new Button("Tìm");
    private final Button createButton = new Button("Thêm");
    private final Button updateButton = new Button("Cập nhật");
    private final Button deleteButton = new Button("Xoá");
    private final Button clearButton = new Button("Làm mới");
    private final TextField feeIdField = new TextField();
    private final TextField feeBuildingIdField = new TextField();
    private final TextField electricityPriceField = new TextField();
    private final TextField waterPriceField = new TextField();
    private final TextField serviceFeeField = new TextField();
    private final TextField parkingFeeField = new TextField();
    private final TextField otherFeeField = new TextField();
    private final TextField freeParkingField = new TextField();
    private final Button createFeeButton = new Button("Thêm");
    private final Button updateFeeButton = new Button("Sửa");
    private final Button deleteFeeButton = new Button("Xoá");
    private final Button clearFeeButton = new Button("Làm mới");

    public BuildingGUI() {
        buildUi();
    }

    private void buildUi() {
        statusLabel.setStyle("-fx-text-fill: #4b5563;");

        configureBuildingTable();
        configureDistrictComboBox();

        buildingList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        buildingIdField.setEditable(false);
        buildingIdField.setPromptText("Tự động");
        districtBox.setPromptText("Chọn quận");
        trueAddressField.setPromptText("Địa chỉ thật");
        fakeAddressField.setPromptText("Địa chỉ hiển thị");
        noteField.setPromptText("Ghi chú");
        ownerPhoneField.setPromptText("Số điện thoại chủ nhà");
        searchModeBox.getSelectionModel().selectFirst();
        searchField.setPromptText("Nhập giá trị tìm kiếm");

        feeIdField.setEditable(false);
        feeBuildingIdField.setEditable(false);
        feeIdField.setPromptText("Tự động");
        feeBuildingIdField.setPromptText("Chọn tòa nhà");
        electricityPriceField.setPromptText("Giá điện");
        waterPriceField.setPromptText("Giá nước");
        serviceFeeField.setPromptText("Phí dịch vụ");
        parkingFeeField.setPromptText("Phí gửi xe");
        otherFeeField.setPromptText("Phí khác");
        freeParkingField.setPromptText("Số xe miễn phí");

        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        HBox searchControls = new HBox(10, searchModeBox, searchField, searchButton, refreshButton);
        searchControls.setAlignment(Pos.CENTER_LEFT);

        VBox searchBar = new VBox(8, sectionLabel("Danh sách tòa nhà"), searchControls);
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
        form.addRow(0, textFieldLabel("Building ID"), buildingIdField);
        form.addRow(1, textFieldLabel("District"), districtBox);
        form.addRow(2, textFieldLabel("True Address"), trueAddressField);
        form.addRow(3, textFieldLabel("Fake Address"), fakeAddressField);
        form.addRow(4, textFieldLabel("Note"), noteField);
        form.addRow(5, textFieldLabel("Owner Phone"), ownerPhoneField);
        form.getChildren().stream()
                .filter(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setMaxWidth(Double.MAX_VALUE));

        HBox actionBar = new HBox(10, createButton, updateButton, deleteButton, clearButton);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(4, 0, 0, 0));

        GridPane feeForm = new GridPane();
        feeForm.setHgap(12);
        feeForm.setVgap(12);
        ColumnConstraints feeLabelColumn = new ColumnConstraints();
        feeLabelColumn.setMinWidth(112);
        ColumnConstraints feeFieldColumn = new ColumnConstraints();
        feeFieldColumn.setHgrow(Priority.ALWAYS);
        feeForm.getColumnConstraints().addAll(feeLabelColumn, feeFieldColumn);
        feeForm.addRow(0, textFieldLabel("Fee ID"), feeIdField);
        feeForm.addRow(1, textFieldLabel("Building ID"), feeBuildingIdField);
        feeForm.addRow(2, textFieldLabel("Electricity Price"), electricityPriceField);
        feeForm.addRow(3, textFieldLabel("Water Price"), waterPriceField);
        feeForm.addRow(4, textFieldLabel("Service Fee"), serviceFeeField);
        feeForm.addRow(5, textFieldLabel("Parking Fee"), parkingFeeField);
        feeForm.addRow(6, textFieldLabel("Other Fee"), otherFeeField);
        feeForm.addRow(7, textFieldLabel("Free Parking"), freeParkingField);
        feeForm.getChildren().stream()
                .filter(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setMaxWidth(Double.MAX_VALUE));

        HBox feeActionBar = new HBox(10, createFeeButton, updateFeeButton, deleteFeeButton, clearFeeButton);
        feeActionBar.setAlignment(Pos.CENTER_LEFT);
        feeActionBar.setPadding(new Insets(4, 0, 0, 0));

        VBox leftPane = new VBox(12, searchBar, buildingList);
        leftPane.setMinWidth(430);
        districtBox.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(buildingList, Priority.ALWAYS);

        VBox rightContent = new VBox(14,
                sectionLabel("Thông tin tòa nhà"),
                form,
                actionBar,
                new Separator(),
                sectionLabel("Phí dịch vụ"),
                feeForm,
                feeActionBar);
        rightContent.setPadding(new Insets(0, 10, 0, 0));

        ScrollPane rightPane = new ScrollPane(rightContent);
        rightPane.setFitToWidth(true);
        rightPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rightPane.setMinWidth(340);
        rightPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        SplitPane splitPane = new SplitPane(leftPane, rightPane);
        splitPane.setDividerPositions(0.62);
        splitPane.setStyle("-fx-background-color: transparent;");

        setPadding(new Insets(0));
        setTop(statusLabel);
        BorderPane.setMargin(splitPane, new Insets(12, 0, 0, 0));
        setCenter(splitPane);
        setStyle("-fx-background-color: #f3f6fb;");
        styleControls();
    }
    private void configureDistrictComboBox(){
        districtBox.setCellFactory(comboBox -> new DistrictListCell());
        districtBox.setButtonCell(new DistrictListCell());
    }
    private void configureBuildingTable() {
        TableColumn<Building, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBuildingId()));
        idColumn.setPrefWidth(70);
        idColumn.setMinWidth(64);

        TableColumn<Building, String> trueAddressColumn = new TableColumn<>("True Address");
        trueAddressColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(textOrEmpty(cellData.getValue().getTrueAddress())));
        trueAddressColumn.setPrefWidth(210);

        TableColumn<Building, String> fakeAddressColumn = new TableColumn<>("Fake Address");
        fakeAddressColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(textOrEmpty(cellData.getValue().getFakeAddress())));
        fakeAddressColumn.setPrefWidth(210);

        TableColumn<Building, String> noteColumn = new TableColumn<>("Note");
        noteColumn
                .setCellValueFactory(cellData -> new ReadOnlyStringWrapper(textOrEmpty(cellData.getValue().getNote())));
        noteColumn.setPrefWidth(180);

        TableColumn<Building, String> ownerPhoneColumn = new TableColumn<>("Owner Phone");
        ownerPhoneColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(textOrEmpty(cellData.getValue().getOwnerPhone())));
        ownerPhoneColumn.setPrefWidth(140);

        buildingList.getColumns().setAll(List.of(idColumn, trueAddressColumn, fakeAddressColumn, noteColumn,
                ownerPhoneColumn));
        buildingList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        buildingList.setPlaceholder(new Label("Chưa có dữ liệu tòa nhà"));
        buildingList.setStyle(
                "-fx-background-color: white; -fx-border-color: #d8dee9; -fx-border-radius: 8; -fx-background-radius: 8;");
    }

    private String textOrEmpty(String text) {
        return text == null ? "" : text;
    }

    private Label sectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");
        return label;
    }

    private Label textFieldLabel(String text) {
        Label label = new Label(" " + text);
        label.setStyle("-fx-font-size: 12px; -fx-text-fill: #1f2937;");
        return label;
    }

    private void styleControls() {
        String fieldStyle = "-fx-background-color: white; -fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-background-radius: 6;";
        buildingIdField.setStyle(fieldStyle);
        districtBox.setStyle(fieldStyle);
        trueAddressField.setStyle(fieldStyle);
        fakeAddressField.setStyle(fieldStyle);
        noteField.setStyle(fieldStyle);
        ownerPhoneField.setStyle(fieldStyle);
        feeIdField.setStyle(fieldStyle);
        feeBuildingIdField.setStyle(fieldStyle);
        electricityPriceField.setStyle(fieldStyle);
        waterPriceField.setStyle(fieldStyle);
        serviceFeeField.setStyle(fieldStyle);
        parkingFeeField.setStyle(fieldStyle);
        otherFeeField.setStyle(fieldStyle);
        freeParkingField.setStyle(fieldStyle);
        searchField.setStyle(fieldStyle);
        searchModeBox.setStyle(fieldStyle);

        createButton.setStyle(primaryButtonStyle("#2563eb"));
        updateButton.setStyle(primaryButtonStyle("#0f766e"));
        deleteButton.setStyle(primaryButtonStyle("#dc2626"));
        createFeeButton.setStyle(primaryButtonStyle("#2563eb"));
        updateFeeButton.setStyle(primaryButtonStyle("#0f766e"));
        deleteFeeButton.setStyle(primaryButtonStyle("#dc2626"));
        searchButton.setStyle(primaryButtonStyle("#334155"));
        refreshButton.setStyle(secondaryButtonStyle());
        clearButton.setStyle(secondaryButtonStyle());
        clearFeeButton.setStyle(secondaryButtonStyle());
    }

    private String primaryButtonStyle(String color) {
        return "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 6; -fx-padding: 8 14;";
    }

    private String secondaryButtonStyle() {
        return "-fx-background-color: white; -fx-text-fill: #1f2937; -fx-font-weight: bold; "
                + "-fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8 14;";
    }

    private class DistrictListCell extends ListCell<District> {
        @Override
        protected void updateItem(District district, boolean empty) {
            super.updateItem(district, empty);
            setText(empty || district == null ? null : districtText(district));
        }
    }

    private String districtText(District district) {
        if (district == null) {
            return "";
        }
        String name = textOrEmpty(district.getDistrictName());
        String id = district.getDistrictId() == null ? "" : String.valueOf(district.getDistrictId());
        return id.isBlank() ? name : id + " - " + name;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public TableView<Building> getBuildingList() {
        return buildingList;
    }

    public TextField getBuildingIdField() {
        return buildingIdField;
    }

    public TextField getTrueAddressField() {
        return trueAddressField;
    }

    public TextField getFakeAddressField() {
        return fakeAddressField;
    }

    public TextField getNoteField() {
        return noteField;
    }

    public TextField getOwnerPhoneField() {
        return ownerPhoneField;
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

    public ObservableList<Building> getBuildingItems() {
        return buildingItems;
    }

    public TextField getFeeIdField() {
        return feeIdField;
    }

    public TextField getFeeBuildingIdField() {
        return feeBuildingIdField;
    }

    public TextField getElectricityPriceField() {
        return electricityPriceField;
    }

    public TextField getWaterPriceField() {
        return waterPriceField;
    }

    public TextField getServiceFeeField() {
        return serviceFeeField;
    }

    public TextField getParkingFeeField() {
        return parkingFeeField;
    }

    public TextField getOtherFeeField() {
        return otherFeeField;
    }

    public TextField getFreeParkingField() {
        return freeParkingField;
    }

    public Button getCreateFeeButton() {
        return createFeeButton;
    }

    public Button getUpdateFeeButton() {
        return updateFeeButton;
    }

    public Button getDeleteFeeButton() {
        return deleteFeeButton;
    }

    public Button getClearFeeButton() {
        return clearFeeButton;
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }
    
    public ComboBox<District> getDistrictBox() {
        return districtBox;
    }

    public ObservableList<District> getDistrictItems() {
        return districtItems;
    }

    public void clearForm() {
        buildingIdField.clear();
        trueAddressField.clear();
        fakeAddressField.clear();
        noteField.clear();
        ownerPhoneField.clear();
        districtBox.getSelectionModel().clearSelection();
        buildingList.getSelectionModel().clearSelection();
        clearFeeForm();
    }

    public void clearFeeForm() {
        feeIdField.clear();
        feeBuildingIdField.clear();
        electricityPriceField.clear();
        waterPriceField.clear();
        serviceFeeField.clear();
        parkingFeeField.clear();
        otherFeeField.clear();
        freeParkingField.clear();
    }
}
