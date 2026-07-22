package com.minhtan.qlptclient.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SearchRoomGUI extends BorderPane {

    private final TextField addressField = new TextField();
    private final TextField minPriceField = new TextField();
    private final TextField maxPriceField = new TextField();
    private final DatePicker availableDatePicker = new DatePicker();
    private final Button searchButton = new Button("Tìm kiếm");
    private final Button resetButton = new Button("Đặt lại");
    private final Button refreshButton = new Button("Tải lại");

    private final VBox buildingListPane = new VBox(12);
    private final FlowPane roomCardsPane = new FlowPane();
    private final VBox detailPane = new VBox(12);
    private final Label statusLabel = new Label("Sẵn sàng");

    public SearchRoomGUI() {
        buildUi();
    }

    private void buildUi() {
        setStyle("-fx-background-color:#f5f7fa;");

        Label titleLabel = new Label("Tìm phòng trọ");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        addressField.setPromptText("Nhập địa chỉ...");
        minPriceField.setPromptText("Giá tối thiểu");
        maxPriceField.setPromptText("Giá tối đa");
        availableDatePicker.setPromptText("Ngày có sẵn");
        availableDatePicker.setMaxWidth(Double.MAX_VALUE);

        searchButton.setStyle("-fx-background-color:#2563eb; -fx-text-fill:white; -fx-font-weight:bold;");
        resetButton.setStyle("-fx-background-color:#e2e8f0; -fx-text-fill:#0f172a; -fx-font-weight:bold;");
        refreshButton.setStyle("-fx-background-color:#0f766e; -fx-text-fill:white; -fx-font-weight:bold;");

        HBox searchBar = new HBox(12, addressField, minPriceField, maxPriceField, availableDatePicker, searchButton,
                resetButton, refreshButton);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(addressField, Priority.ALWAYS);
        HBox.setHgrow(minPriceField, Priority.SOMETIMES);
        HBox.setHgrow(maxPriceField, Priority.SOMETIMES);

        VBox topBar = new VBox(10, titleLabel, searchBar);
        topBar.setPadding(new Insets(18, 20, 12, 20));
        topBar.setStyle("-fx-background-color:white; -fx-border-color:#e2e8f0; -fx-border-width:0 0 1 0;");

        VBox leftPanel = buildPanel("Tòa nhà", buildingListPane);
        VBox middlePanel = buildPanel("Danh sách phòng", roomCardsPane);
        VBox rightPanel = buildPanel("Thông tin phòng", detailPane);

        ScrollPane leftScroll = new ScrollPane(leftPanel);
        leftScroll.setFitToWidth(true);
        leftScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        leftScroll.setStyle("-fx-background-color:transparent;");

        ScrollPane middleScroll = new ScrollPane(middlePanel);
        middleScroll.setFitToWidth(true);
        middleScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        middleScroll.setStyle("-fx-background-color:transparent;");

        ScrollPane rightScroll = new ScrollPane(rightPanel);
        rightScroll.setFitToWidth(true);
        rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rightScroll.setStyle("-fx-background-color:transparent;");

        SplitPane splitPane = new SplitPane(leftScroll, middleScroll, rightScroll);
        splitPane.setDividerPositions(0.25, 0.70);
        splitPane.setPadding(new Insets(12, 14, 14, 14));

        VBox content = new VBox(10, statusLabel, splitPane);
        content.setPadding(new Insets(0, 14, 14, 14));
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        setTop(topBar);
        setCenter(content);

        buildingListPane.setSpacing(12);
        roomCardsPane.setHgap(14);
        roomCardsPane.setVgap(14);
        roomCardsPane.setPrefWrapLength(800);
    }

    private VBox buildPanel(String title, VBox contentPane) {
        Label sectionTitle = new Label(title);
        sectionTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        VBox box = new VBox(10, sectionTitle, contentPane);
        box.setPadding(new Insets(12));
        box.setStyle(
                "-fx-background-color:white; -fx-background-radius:12; -fx-border-radius:12; -fx-border-color:#e2e8f0;");
        return box;
    }

    private VBox buildPanel(String title, FlowPane contentPane) {
        Label sectionTitle = new Label(title);
        sectionTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        VBox box = new VBox(10, sectionTitle, contentPane);
        box.setPadding(new Insets(12));
        box.setStyle(
                "-fx-background-color:white; -fx-background-radius:12; -fx-border-radius:12; -fx-border-color:#e2e8f0;");
        return box;
    }

    public TextField getAddressField() {
        return addressField;
    }

    public TextField getMinPriceField() {
        return minPriceField;
    }

    public TextField getMaxPriceField() {
        return maxPriceField;
    }

    public DatePicker getAvailableDatePicker() {
        return availableDatePicker;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public VBox getBuildingListPane() {
        return buildingListPane;
    }

    public FlowPane getRoomCardsPane() {
        return roomCardsPane;
    }

    public VBox getDetailPane() {
        return detailPane;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }
}
