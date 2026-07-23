package com.minhtan.qlptclient.gui;

import com.minhtan.qlptclient.entity.District;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class DistrictGUI extends BorderPane {

    private final Label statusLabel = new Label("Sẵn sàng kết nối API backend");
    private final ObservableList<District> districtItems = FXCollections.observableArrayList();
    private final TableView<District> districtList = new TableView<>(districtItems);
    private final TextField districtIdField = new TextField();
    private final TextField nameField = new TextField();
    private final ComboBox<String> searchModeBox = new ComboBox<>(FXCollections.observableArrayList(
            "All",
            "Name"));
    private final TextField searchField = new TextField();
    private final Button refreshButton = new Button("Tải lại");
    private final Button searchButton = new Button("Tìm");
    private final Button createButton = new Button("Thêm");
    private final Button updateButton = new Button("Cập nhật");
    private final Button deleteButton = new Button("Xóa");
    private final Button clearButton = new Button("Làm mới");
    private final TextArea detailArea = new TextArea();

    public DistrictGUI() {
        buildUi();
    }

    private void buildUi() {
        statusLabel.setStyle("-fx-text-fill: #4b5563;");

        configureDistrictTable();
        districtList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        districtIdField.setEditable(false);
        districtIdField.setPromptText("Tự động");
        nameField.setPromptText("Tên quận");
        searchModeBox.getSelectionModel().selectFirst();
        searchField.setPromptText("Nhập giá trị tìm kiếm");

        detailArea.setEditable(false);
        detailArea.setPromptText("Chi tiết dữ liệu sẽ hiển thị ở đây");
        detailArea.setPrefRowCount(8);

        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        HBox searchControls = new HBox(10, searchModeBox, searchField, searchButton, refreshButton);
        searchControls.setAlignment(Pos.CENTER_LEFT);

        VBox searchBar = new VBox(8, sectionLabel("Danh sách quận"), searchControls);
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
        form.addRow(0, new Label("District ID"), districtIdField);
        form.addRow(1, new Label("District Name"), nameField);
        form.getChildren().stream()
                .filter(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setMaxWidth(Double.MAX_VALUE));

        HBox actionBar = new HBox(10, createButton, updateButton, deleteButton, clearButton);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(4, 0, 0, 0));

        VBox leftPane = new VBox(12, searchBar, districtList);
        leftPane.setMinWidth(430);
        VBox.setVgrow(districtList, Priority.ALWAYS);

        VBox rightPane = new VBox(14, sectionLabel("Thông tin quận"), form, actionBar, new Separator(), detailArea);
        rightPane.setMinWidth(340);
        detailArea.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(detailArea, Priority.ALWAYS);

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

    private void configureDistrictTable() {
        TableColumn<District, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDistrictId()));
        idColumn.setPrefWidth(90);
        idColumn.setMinWidth(70);

        TableColumn<District, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(textOrEmpty(cellData.getValue().getDistrictName())));
        nameColumn.setPrefWidth(260);

        districtList.getColumns().setAll(List.of(idColumn, nameColumn));
        districtList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        districtList.setPlaceholder(new Label("Chưa có dữ liệu quận"));
        districtList.setStyle(
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

    private void styleControls() {
        String fieldStyle = "-fx-background-color: white; -fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-background-radius: 6;";
        districtIdField.setStyle(fieldStyle);
        nameField.setStyle(fieldStyle);
        searchField.setStyle(fieldStyle);
        searchModeBox.setStyle(fieldStyle);
        detailArea.setStyle(fieldStyle);

        createButton.setStyle(primaryButtonStyle("#2563eb"));
        updateButton.setStyle(primaryButtonStyle("#0f766e"));
        deleteButton.setStyle(primaryButtonStyle("#dc2626"));
        searchButton.setStyle(primaryButtonStyle("#334155"));
        refreshButton.setStyle(secondaryButtonStyle());
        clearButton.setStyle(secondaryButtonStyle());
    }

    private String primaryButtonStyle(String color) {
        return "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 6; -fx-padding: 8 14;";
    }

    private String secondaryButtonStyle() {
        return "-fx-background-color: white; -fx-text-fill: #1f2937; -fx-font-weight: bold; "
                + "-fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8 14;";
    }

    public TableView<District> getDistrictList() {
        return districtList;
    }

    public TextField getDistrictIdField() {
        return districtIdField;
    }

    public TextField getNameField() {
        return nameField;
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

    public ObservableList<District> getDistrictItems() {
        return districtItems;
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    public void setDetails(String text) {
        detailArea.setText(text);
    }

    public void clearForm() {
        districtIdField.clear();
        nameField.clear();
        detailArea.clear();
        districtList.getSelectionModel().clearSelection();
    }
}
