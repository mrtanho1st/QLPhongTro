package com.minhtan.qlptclient.gui;

import com.minhtan.qlptclient.entity.Landmark;
import com.minhtan.qlptclient.entity.LandmarkType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LandmarkGUI extends BorderPane {

    private final Label statusLabel = new Label("Sẵn sàng kết nối API backend");
    private final ObservableList<Landmark> landmarkItems = FXCollections.observableArrayList();
    private final TableView<Landmark> landmarkList = new TableView<>(landmarkItems);

    // Cache tên LandmarkType theo id để hiển thị cột "LandmarkType" trong bảng
    // khi đối tượng Landmark trả về không kèm sẵn landmarkType lồng bên trong.
    private final Map<Integer, String> landmarkTypeNames = new HashMap<>();

    private final TextField landmarkIdField = new TextField();
    private final TextField landmarkNameField = new TextField();
    private final ComboBox<LandmarkType> landmarkTypeComboBox = new ComboBox<>(FXCollections.observableArrayList());
    private final TextField addressField = new TextField();
    private final TextField latitudeField = new TextField();
    private final TextField longitudeField = new TextField();
    private final TextArea descriptionField = new TextArea();
    private final ComboBox<Boolean> isActiveComboBox = new ComboBox<>(
            FXCollections.observableArrayList(Boolean.TRUE, Boolean.FALSE));

    private final ComboBox<String> searchModeBox = new ComboBox<>(FXCollections.observableArrayList(
            "All",
            "LandmarkName",
            "LandmarkTypesId",
            "Address",
            "IsActive"));
    private final TextField searchField = new TextField();
    private final Button refreshButton = new Button("Tải lại");
    private final Button searchButton = new Button("Tìm");
    private final Button createButton = new Button("Thêm");
    private final Button updateButton = new Button("Cập nhật");
    private final Button deleteButton = new Button("Xóa");
    private final Button clearButton = new Button("Làm mới");
    private final TextArea detailArea = new TextArea();

    public LandmarkGUI() {
        buildUi();
    }

    private void buildUi() {
        statusLabel.setStyle("-fx-text-fill: #4b5563;");

        configureLandmarkTable();
        landmarkList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        landmarkIdField.setEditable(false);
        landmarkIdField.setPromptText("Tự động");
        landmarkNameField.setPromptText("Tên địa danh");
        addressField.setPromptText("Địa chỉ");
        latitudeField.setPromptText("Vĩ độ (vd: 10.762622)");
        longitudeField.setPromptText("Kinh độ (vd: 106.660172)");
        descriptionField.setPromptText("Mô tả");
        descriptionField.setPrefRowCount(3);

        configureLandmarkTypeComboBox();
        configureIsActiveComboBox();

        searchModeBox.getSelectionModel().selectFirst();
        searchField.setPromptText("Nhập giá trị tìm kiếm");

        detailArea.setEditable(false);
        detailArea.setPromptText("Chi tiết dữ liệu sẽ hiển thị ở đây");
        detailArea.setPrefRowCount(8);

        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        HBox searchControls = new HBox(10, searchModeBox, searchField, searchButton, refreshButton);
        searchControls.setAlignment(Pos.CENTER_LEFT);

        VBox searchBar = new VBox(8, sectionLabel("Danh sách địa danh"), searchControls);
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
        form.addRow(0, fieldLabel("Landmark ID"), landmarkIdField);
        form.addRow(1, fieldLabel("Name"), landmarkNameField);
        form.addRow(2, fieldLabel("Landmark Type"), landmarkTypeComboBox);
        form.addRow(3, fieldLabel("Address"), addressField);
        form.addRow(4, fieldLabel("Latitude"), latitudeField);
        form.addRow(5, fieldLabel("Longitude"), longitudeField);
        form.addRow(6, fieldLabel("Description"), descriptionField);
        form.addRow(7, fieldLabel("Is Active"), isActiveComboBox);

        form.getChildren().stream()
                .filter(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setMaxWidth(Double.MAX_VALUE));
        landmarkTypeComboBox.setMaxWidth(Double.MAX_VALUE);
        isActiveComboBox.setMaxWidth(Double.MAX_VALUE);

        HBox actionBar = new HBox(10, createButton, updateButton, deleteButton, clearButton);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(4, 0, 0, 0));

        VBox leftPane = new VBox(12, searchBar, landmarkList);
        leftPane.setMinWidth(430);
        VBox.setVgrow(landmarkList, Priority.ALWAYS);

        VBox rightPane = new VBox(14, sectionLabel("Thông tin địa danh"), form, actionBar, new Separator(), detailArea);
        rightPane.setMinWidth(360);
        rightPane.setPadding(new Insets(2, 12, 12, 2));
        detailArea.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(detailArea, Priority.ALWAYS);

        ScrollPane rightScrollPane = new ScrollPane(rightPane);
        rightScrollPane.setFitToWidth(true);
        rightScrollPane.setMinWidth(360);
        rightScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        rightScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rightScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        SplitPane splitPane = new SplitPane(leftPane, rightScrollPane);
        splitPane.setDividerPositions(0.58);
        splitPane.setStyle("-fx-background-color: transparent;");

        setPadding(new Insets(0));
        setTop(statusLabel);
        BorderPane.setMargin(splitPane, new Insets(12, 0, 0, 0));
        setCenter(splitPane);
        setStyle("-fx-background-color: #f3f6fb;");
        styleControls();
    }

    private void configureLandmarkTypeComboBox() {
        landmarkTypeComboBox.setPromptText("Chọn loại địa danh");
        landmarkTypeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(LandmarkType type) {
                if (type == null) {
                    return "";
                }
                return type.getLandmarkTypeId() + " - " + textOrEmpty(type.getLandmarkTypeName());
            }

            @Override
            public LandmarkType fromString(String string) {
                return landmarkTypeComboBox.getItems().stream()
                        .filter(type -> toString(type).equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void configureIsActiveComboBox() {
        isActiveComboBox.setPromptText("Chọn trạng thái");
        isActiveComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Boolean active) {
                if (active == null) {
                    return "";
                }
                return active ? "Hoạt động" : "Ngưng hoạt động";
            }

            @Override
            public Boolean fromString(String string) {
                return "Hoạt động".equals(string);
            }
        });
    }

    private void configureLandmarkTable() {
        TableColumn<Landmark, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLandmarkId()));
        idColumn.setPrefWidth(60);
        idColumn.setMinWidth(50);

        TableColumn<Landmark, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(textOrEmpty(cellData.getValue().getLandmarkName())));
        nameColumn.setPrefWidth(180);

        TableColumn<Landmark, String> typeColumn = new TableColumn<>("LandmarkType");
        typeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(resolveTypeName(cellData.getValue())));
        typeColumn.setPrefWidth(140);

        TableColumn<Landmark, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(textOrEmpty(cellData.getValue().getAddress())));
        addressColumn.setPrefWidth(220);

        TableColumn<Landmark, String> activeColumn = new TableColumn<>("Active");
        activeColumn.setCellValueFactory(cellData -> {
            Boolean active = cellData.getValue().getIsActive();
            return new ReadOnlyStringWrapper(active == null ? "" : (active ? "Hoạt động" : "Ngưng hoạt động"));
        });
        activeColumn.setPrefWidth(110);

        landmarkList.getColumns().setAll(List.of(idColumn, nameColumn, typeColumn, addressColumn, activeColumn));
        landmarkList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        landmarkList.setPlaceholder(new Label("Chưa có dữ liệu địa danh"));
        landmarkList.setStyle(
                "-fx-background-color: white; -fx-border-color: #d8dee9; -fx-border-radius: 8; -fx-background-radius: 8;");
    }

    private String resolveTypeName(Landmark landmark) {
        if (landmark.getLandmarkType() != null && landmark.getLandmarkType().getLandmarkTypeName() != null) {
            return landmark.getLandmarkType().getLandmarkTypeName();
        }
        if (landmark.getLandmarkTypesId() != null) {
            return landmarkTypeNames.getOrDefault(landmark.getLandmarkTypesId(), "");
        }
        return "";
    }

    private String textOrEmpty(String text) {
        return text == null ? "" : text;
    }

    private Label fieldLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: black;");
        return label;
    }

    private Label sectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");
        return label;
    }

    private void styleControls() {
        String fieldStyle = "-fx-background-color: white; -fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-background-radius: 6; -fx-text-fill: black;";
        String comboBoxStyle = fieldStyle + " -fx-mark-color: black;";
        landmarkIdField.setStyle(fieldStyle);
        landmarkNameField.setStyle(fieldStyle);
        addressField.setStyle(fieldStyle);
        latitudeField.setStyle(fieldStyle);
        longitudeField.setStyle(fieldStyle);
        descriptionField.setStyle(fieldStyle);
        landmarkTypeComboBox.setStyle(comboBoxStyle);
        isActiveComboBox.setStyle(comboBoxStyle);
        searchField.setStyle(fieldStyle);
        searchModeBox.setStyle(comboBoxStyle);
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

    public TableView<Landmark> getLandmarkList() {
        return landmarkList;
    }

    public TextField getLandmarkIdField() {
        return landmarkIdField;
    }

    public TextField getLandmarkNameField() {
        return landmarkNameField;
    }

    public ComboBox<LandmarkType> getLandmarkTypeComboBox() {
        return landmarkTypeComboBox;
    }

    public TextField getAddressField() {
        return addressField;
    }

    public TextField getLatitudeField() {
        return latitudeField;
    }

    public TextField getLongitudeField() {
        return longitudeField;
    }

    public TextArea getDescriptionField() {
        return descriptionField;
    }

    public ComboBox<Boolean> getIsActiveComboBox() {
        return isActiveComboBox;
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

    public ObservableList<Landmark> getLandmarkItems() {
        return landmarkItems;
    }

    /**
     * Đổ danh sách LandmarkType (lấy từ ApiClient.getInstance().getLandmarkTypes())
     * vào combo box của form và cập nhật cache tên hiển thị cho cột "LandmarkType"
     * trong bảng.
     */
    public void setLandmarkTypes(List<LandmarkType> types) {
        landmarkTypeComboBox.getItems().setAll(types);
        landmarkTypeNames.clear();
        for (LandmarkType type : types) {
            landmarkTypeNames.put(type.getLandmarkTypeId(), type.getLandmarkTypeName());
        }
        landmarkList.refresh();
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    public void setDetails(String text) {
        detailArea.setText(text);
    }

    public void clearForm() {
        landmarkIdField.clear();
        landmarkNameField.clear();
        landmarkTypeComboBox.getSelectionModel().clearSelection();
        addressField.clear();
        latitudeField.clear();
        longitudeField.clear();
        descriptionField.clear();
        isActiveComboBox.getSelectionModel().clearSelection();
        detailArea.clear();
        landmarkList.getSelectionModel().clearSelection();
    }
}