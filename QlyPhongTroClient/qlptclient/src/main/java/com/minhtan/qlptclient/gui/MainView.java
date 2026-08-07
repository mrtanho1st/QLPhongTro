package com.minhtan.qlptclient.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainView extends BorderPane {

    private final Label titleLabel = new Label();
    private final StackPane contentPane = new StackPane();
    private final HomeDashboardPane homeDashboardPane = new HomeDashboardPane();
    private final BuildingGUI buildingGUI = new BuildingGUI();
    private final RoomGUI roomGUI = new RoomGUI();
    private final AmenityGUI amenityGUI = new AmenityGUI();
    private final CommissionGUI commissionGUI = new CommissionGUI();
    private final SearchRoomGUI searchRoomGUI = new SearchRoomGUI();
    private final DistrictGUI districtGUI = new DistrictGUI();
    private final TypeRoomGUI typeRoomGUI = new TypeRoomGUI();
    private final LandmarkGUI landmarkGUI = new LandmarkGUI();
    private final Button homeButton = navButton("Trang chủ");
    private final Button buildingButton = navButton("Tòa nhà");
    private final Button roomButton = navButton("Phòng");
    private final Button amenityButton = navButton("Tiện ích");
    private final Button commissionButton = navButton("Hoa hồng");
    private final Button districtButton = navButton("Khu vực");
    private final Button typeRoomButton = navButton("Loại phòng");
    private final Button searchButton = navButton("Tìm kiếm");
    private final Button landmarkButton = navButton("Cột mốc");
    private final List<Button> navButtons = List.of(homeButton, buildingButton, roomButton, amenityButton,
            commissionButton, districtButton, typeRoomButton, searchButton, landmarkButton);

    public MainView() {
        buildUi();
    }

    private void buildUi() {
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        VBox sidebar = new VBox(10,
                appTitle(),
                homeButton,
                buildingButton,
                roomButton,
                amenityButton,
                commissionButton,
                districtButton,
                typeRoomButton,
                searchButton,
                landmarkButton);
        sidebar.setPadding(new Insets(18));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #111827;");

        VBox header = new VBox(4, titleLabel);
        header.setPadding(new Insets(18, 22, 8, 22));
        header.setStyle("-fx-background-color: white; -fx-border-color: transparent transparent #e5e7eb transparent;");

        contentPane.setPadding(new Insets(18, 22, 22, 22));
        contentPane.setStyle("-fx-background-color: #f3f6fb;");

        VBox mainArea = new VBox(header, contentPane);
        VBox.setVgrow(contentPane, Priority.ALWAYS);

        setLeft(sidebar);
        setCenter(mainArea);
    }

    private Label appTitle() {
        Label label = new Label("Quản lý Phòng Trọ");
        label.setStyle("-fx-font-size: 19px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 0 0 14 0;");
        return label;
    }

    public StackPane createPlaceholder(String message) {
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #475569;");

        StackPane placeholder = new StackPane(messageLabel);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setStyle("-fx-background-color: white; -fx-border-color: #d8dee9; "
                + "-fx-border-radius: 8; -fx-background-radius: 8;");
        return placeholder;
    }

    public void setPage(String title, Node content, Button activeButton) {
        titleLabel.setText(title);
        contentPane.getChildren().setAll(content);
        navButtons.forEach(button -> button.setStyle(navButtonStyle(button == activeButton)));
    }

    private Button navButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle(navButtonStyle(false));
        return button;
    }

    private String navButtonStyle(boolean active) {
        if (active) {
            return "-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; "
                    + "-fx-background-radius: 6; -fx-padding: 10 12;";
        }
        return "-fx-background-color: transparent; -fx-text-fill: #cbd5e1; -fx-font-weight: bold; "
                + "-fx-background-radius: 6; -fx-padding: 10 12;";
    }

    public HomeDashboardPane getHomeDashboardPane() {
        return homeDashboardPane;
    }

    public BuildingGUI getBuildingGUI() {
        return buildingGUI;
    }

    public RoomGUI getRoomGUI() {
        return roomGUI;
    }

    public AmenityGUI getAmenityGUI() {
        return amenityGUI;
    }

    public CommissionGUI getCommissionGUI() {
        return commissionGUI;
    }

    public SearchRoomGUI getSearchRoomGUI() {
        return searchRoomGUI;
    }

    public DistrictGUI getDistrictGUI() {
        return districtGUI;
    }

    public TypeRoomGUI getTypeRoomGUI() {
        return typeRoomGUI;
    }

    public LandmarkGUI getLandmarkGUI() {
        return landmarkGUI;
    }

    public Button getHomeButton() {
        return homeButton;
    }

    public Button getBuildingButton() {
        return buildingButton;
    }

    public Button getRoomButton() {
        return roomButton;
    }

    public Button getAmenityButton() {
        return amenityButton;
    }

    public Button getCommissionButton() {
        return commissionButton;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getDistrictButton() {
        return districtButton;
    }

    public Button getTypeRoomButton() {
        return typeRoomButton;
    }

    public Button getLandmarkButton() {
        return landmarkButton;
    }

}
