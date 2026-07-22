package com.minhtan.qlptclient.gui;

import com.minhtan.qlptclient.entity.Building;
import com.minhtan.qlptclient.entity.Commission;
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

public class CommissionGUI extends BorderPane {

    private final Label statusLabel = new Label("Sẵn sàng kết nối API backend");
    private final ObservableList<Commission> commissionItems = FXCollections.observableArrayList();
    private final TableView<Commission> commissionList = new TableView<>(commissionItems);
    private final TextField commissionIdField = new TextField();
    private final ComboBox<Building> buildingComboBox = new ComboBox<>();
    private final TextField contractMonthField = new TextField();
    private final TextField commissionPercentField = new TextField();
    private final TextField depositField = new TextField();

    private final ComboBox<String> searchModeBox = new ComboBox<>(FXCollections.observableArrayList(
            "All",
            "Building",
            "ContractMonth"));
    private final TextField searchField = new TextField();
    private final Button refreshButton = new Button("Tải lại");
    private final Button searchButton = new Button("Tìm");
    private final Button createButton = new Button("Thêm");
    private final Button updateButton = new Button("Cập nhật");
    private final Button deleteButton = new Button("Xóa");
    private final Button clearButton = new Button("Làm mới");
    private final TextArea detailArea = new TextArea();

    public CommissionGUI() {
        buildUi();
    }

    private void buildUi() {
        statusLabel.setStyle("-fx-text-fill: #4b5563;");

        configureCommissionTable();
        commissionList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        commissionIdField.setEditable(false);
        commissionIdField.setPromptText("Tự động");

        buildingComboBox.setPrefWidth(Double.MAX_VALUE);
        buildingComboBox.setPromptText("Chọn tòa nhà");
        buildingComboBox.setConverter(new javafx.util.StringConverter<Building>() {
            @Override
            public String toString(Building building) {
                return building == null ? "" : building.getTrueAddress();
            }

            @Override
            public Building fromString(String string) {
                return null;
            }
        });

        contractMonthField.setPromptText("Số tháng hợp đồng");
        commissionPercentField.setPromptText("Phần trăm hoa hồng");
        depositField.setPromptText("Tiền cọc");

        detailArea.setEditable(false);
        detailArea.setPromptText("Chi tiết dữ liệu sẽ hiển thị ở đây");
        detailArea.setPrefRowCount(8);

        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        HBox searchControls = new HBox(10, searchModeBox, searchField, searchButton, refreshButton);
        searchControls.setAlignment(Pos.CENTER_LEFT);

        VBox searchBar = new VBox(8, sectionLabel("Danh sách hoa hồng"), searchControls);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(0, 0, 4, 0));

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setMaxWidth(Double.MAX_VALUE);
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setMinWidth(140);
        ColumnConstraints fieldColumn = new ColumnConstraints();
        fieldColumn.setHgrow(Priority.ALWAYS);
        fieldColumn.setPrefWidth(200);
        form.getColumnConstraints().addAll(labelColumn, fieldColumn);
        form.addRow(0, new Label("Commission ID"), commissionIdField);
        form.addRow(1, new Label("Tòa nhà"), buildingComboBox);
        form.addRow(2, new Label("Tháng hợp đồng"), contractMonthField);
        form.addRow(3, new Label("Hoa hồng(%)"), commissionPercentField);
        form.addRow(4, new Label("Tiền cọc"), depositField);

        // Set max width for all input controls
        commissionIdField.setMaxWidth(Double.MAX_VALUE);
        buildingComboBox.setMaxWidth(Double.MAX_VALUE);
        contractMonthField.setMaxWidth(Double.MAX_VALUE);
        commissionPercentField.setMaxWidth(Double.MAX_VALUE);
        depositField.setMaxWidth(Double.MAX_VALUE);

        HBox actionBar = new HBox(10, createButton, updateButton, deleteButton, clearButton);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(4, 0, 0, 0));

        VBox leftPane = new VBox(12, searchBar, commissionList);
        leftPane.setMinWidth(430);
        VBox.setVgrow(commissionList, Priority.ALWAYS);

        VBox rightPane = new VBox(14, sectionLabel("Thông tin hoa hồng"), form, actionBar, new Separator(), detailArea);
        rightPane.setMinWidth(360);
        detailArea.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(detailArea, Priority.ALWAYS);

        SplitPane splitPane = new SplitPane(leftPane, rightPane);
        splitPane.setDividerPositions(0.55);
        splitPane.setStyle("-fx-background-color: transparent;");

        setPadding(new Insets(0));
        setTop(statusLabel);
        BorderPane.setMargin(splitPane, new Insets(12, 0, 0, 0));
        setCenter(splitPane);
        setStyle("-fx-background-color: #f3f6fb;");
        styleControls();
    }

    private void configureCommissionTable() {
        TableColumn<Commission, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCommissionId()));
        idColumn.setPrefWidth(70);
        idColumn.setMinWidth(50);

        TableColumn<Commission, String> buildingColumn = new TableColumn<>("Tòa nhà");
        buildingColumn.setCellValueFactory(cellData -> {
            Building building = cellData.getValue().getBuilding();
            String buildingName = building == null ? ""
                    : (building.getTrueAddress() == null ? "" : building.getTrueAddress());
            return new ReadOnlyStringWrapper(buildingName);
        });
        buildingColumn.setPrefWidth(180);

        TableColumn<Commission, Integer> contractMonthColumn = new TableColumn<>("Tháng");
        contractMonthColumn
                .setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getContractMonth()));
        contractMonthColumn.setPrefWidth(80);

        TableColumn<Commission, String> commissionPercentColumn = new TableColumn<>("% HH");
        commissionPercentColumn.setCellValueFactory(cellData -> {
            var percent = cellData.getValue().getCommissionPercent();
            String display = percent == null ? "" : percent.toPlainString();
            return new ReadOnlyStringWrapper(display);
        });
        commissionPercentColumn.setPrefWidth(80);

        TableColumn<Commission, String> depositColumn = new TableColumn<>("Cọc");
        depositColumn.setCellValueFactory(cellData -> {
            var deposit = cellData.getValue().getDeposit();
            String display = deposit == null ? "" : deposit.toPlainString();
            return new ReadOnlyStringWrapper(display);
        });
        depositColumn.setPrefWidth(100);

        commissionList.getColumns()
                .setAll(List.of(idColumn, buildingColumn, contractMonthColumn, commissionPercentColumn, depositColumn));
        commissionList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        commissionList.setPlaceholder(new Label("Chưa có dữ liệu hoa hồng"));
        commissionList.setStyle(
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
        commissionIdField.setStyle(fieldStyle);
        buildingComboBox.setStyle(fieldStyle);
        contractMonthField.setStyle(fieldStyle);
        commissionPercentField.setStyle(fieldStyle);
        depositField.setStyle(fieldStyle);
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

    public TableView<Commission> getCommissionList() {
        return commissionList;
    }

    public TextField getCommissionIdField() {
        return commissionIdField;
    }

    public ComboBox<Building> getBuildingComboBox() {
        return buildingComboBox;
    }

    public TextField getContractMonthField() {
        return contractMonthField;
    }

    public TextField getCommissionPercentField() {
        return commissionPercentField;
    }

    public TextField getDepositField() {
        return depositField;
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

    public ObservableList<Commission> getCommissionItems() {
        return commissionItems;
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    public void setDetails(String text) {
        detailArea.setText(text);
    }

    public void clearForm() {
        commissionIdField.clear();
        buildingComboBox.getSelectionModel().clearSelection();
        contractMonthField.clear();
        commissionPercentField.clear();
        depositField.clear();
        detailArea.clear();
        commissionList.getSelectionModel().clearSelection();
    }
}
