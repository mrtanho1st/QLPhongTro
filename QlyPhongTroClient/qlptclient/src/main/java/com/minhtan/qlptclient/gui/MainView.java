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

    // =========================
    // CONTENT
    // =========================

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
    private final LandmarkTypeGUI landmarkTypeGUI = new LandmarkTypeGUI();
    private final SaleOffGUI saleOffGUI = new SaleOffGUI();
    private final RoomSaleOffGUI roomSaleOffGUI = new RoomSaleOffGUI();

    // =========================
    // BUTTONS
    // =========================

    private final Button homeButton = navButton("Trang chủ");

    // Quản lý phòng trọ
    private final Button buildingButton = subMenuButton("Tòa nhà");

    private final Button roomButton = subMenuButton("Phòng");

    private final Button typeRoomButton = subMenuButton("Loại phòng");

    private final Button amenityButton = subMenuButton("Tiện ích");

    private final Button districtButton = subMenuButton("Khu vực");

    // Tìm kiếm
    private final Button searchButton = subMenuButton("Tìm kiếm phòng");

    // Kinh doanh
    private final Button commissionButton = subMenuButton("Hoa hồng");

    private final Button saleOffButton = subMenuButton("Loại khuyến mãi");

    private final Button roomSaleOffButton = subMenuButton("khuyến mãi");

    // Địa điểm
    private final Button landmarkButton = subMenuButton("Địa danh");

    private final Button landmarkTypeButton = subMenuButton("Loại địa danh");

    // =========================
    // CONSTRUCTOR
    // =========================

    public MainView() {
        buildUi();
    }

    // =========================
    // BUILD UI
    // =========================

    private void buildUi() {

        titleLabel.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #111827;");

        // =====================================
        // SIDEBAR
        // =====================================

        VBox sidebar = new VBox(8);

        sidebar.setPadding(new Insets(18));
        sidebar.setPrefWidth(240);

        sidebar.setStyle(
                "-fx-background-color: #111827;");

        // =====================================
        // MENU
        // =====================================

        VBox roomManagementMenu = createMenu(
                "Quản lý phòng trọ",
                buildingButton,
                roomButton,
                typeRoomButton,
                amenityButton,
                districtButton);

        VBox searchMenu = createMenu(
                "Tìm kiếm",
                searchButton);

        VBox businessMenu = createMenu(
                "Kinh doanh",
                commissionButton,
                saleOffButton,
                roomSaleOffButton);

        VBox locationMenu = createMenu(
                "Địa điểm",
                landmarkButton,
                landmarkTypeButton);

        // =====================================
        // ADD TO SIDEBAR
        // =====================================

        sidebar.getChildren().addAll(
                appTitle(),

                homeButton,

                roomManagementMenu,

                searchMenu,

                businessMenu,

                locationMenu);

        // =====================================
        // HEADER
        // =====================================

        VBox header = new VBox(4, titleLabel);

        header.setPadding(
                new Insets(18, 22, 8, 22));

        header.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: transparent transparent #e5e7eb transparent;");

        // =====================================
        // CONTENT
        // =====================================

        contentPane.setPadding(
                new Insets(18, 22, 22, 22));

        contentPane.setStyle(
                "-fx-background-color: #f3f6fb;");

        VBox mainArea = new VBox(
                header,
                contentPane);

        VBox.setVgrow(
                contentPane,
                Priority.ALWAYS);

        // =====================================
        // ROOT
        // =====================================

        setLeft(sidebar);
        setCenter(mainArea);
    }

    // =====================================================
    // CREATE GROUP MENU
    // =====================================================

    private VBox createMenu(
            String title,
            Button... children) {

        Label menuTitle = new Label(title);

        menuTitle.setMaxWidth(Double.MAX_VALUE);

        menuTitle.setAlignment(Pos.CENTER_LEFT);

        menuTitle.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #94a3b8;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 12 8 12;");

        VBox submenu = new VBox(3);

        submenu.setPadding(
                new Insets(0, 0, 5, 12));

        submenu.getChildren().addAll(children);

        VBox menu = new VBox(
                menuTitle,
                submenu);

        menu.setFillWidth(true);

        // =====================================
        // BAN ĐẦU ẨN SUBMENU
        // =====================================

        submenu.setVisible(false);
        submenu.setManaged(false);

        // =====================================
        // HOVER VÀO MENU
        // =====================================

        menu.setOnMouseEntered(event -> {

            submenu.setVisible(true);
            submenu.setManaged(true);

        });

        // =====================================
        // HOVER RA KHỎI MENU
        // =====================================

        menu.setOnMouseExited(event -> {

            submenu.setVisible(false);
            submenu.setManaged(false);

        });

        return menu;
    }

    // =====================================================
    // APP TITLE
    // =====================================================

    private Label appTitle() {

        Label label = new Label(
                "Quản lý Phòng Trọ");

        label.setStyle(
                "-fx-font-size: 19px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 0 0 14 0;");

        return label;
    }

    // =====================================================
    // PLACEHOLDER
    // =====================================================

    public StackPane createPlaceholder(
            String message) {

        Label messageLabel = new Label(message);

        messageLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: #475569;");

        StackPane placeholder = new StackPane(messageLabel);

        placeholder.setAlignment(
                Pos.CENTER);

        placeholder.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d8dee9;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;");

        return placeholder;
    }

    // =====================================================
    // SET PAGE
    // =====================================================

    public void setPage(
            String title,
            Node content,
            Button activeButton) {

        titleLabel.setText(title);

        contentPane
                .getChildren()
                .setAll(content);

        // Reset toàn bộ submenu button
        List<Button> buttons = List.of(
                homeButton,
                buildingButton,
                roomButton,
                typeRoomButton,
                amenityButton,
                districtButton,
                searchButton,
                commissionButton,
                saleOffButton,
                roomSaleOffButton,
                landmarkButton,
                landmarkTypeButton);

        buttons.forEach(button -> button.setStyle(
                navButtonStyle(
                        button == activeButton)));
    }

    // =====================================================
    // MAIN NAV BUTTON
    // =====================================================

    private Button navButton(
            String text) {

        Button button = new Button(text);

        button.setMaxWidth(
                Double.MAX_VALUE);

        button.setAlignment(
                Pos.CENTER_LEFT);

        button.setStyle(
                navButtonStyle(false));

        return button;
    }

    // =====================================================
    // SUB MENU BUTTON
    // =====================================================

    private Button subMenuButton(
            String text) {

        Button button = new Button(text);

        button.setMaxWidth(
                Double.MAX_VALUE);

        button.setAlignment(
                Pos.CENTER_LEFT);

        button.setStyle(
                navButtonStyle(false));

        return button;
    }

    // =====================================================
    // BUTTON STYLE
    // =====================================================

    private String navButtonStyle(
            boolean active) {

        if (active) {

            return "-fx-background-color: #2563eb;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 6;" +
                    "-fx-padding: 10 12;";
        }

        return "-fx-background-color: transparent;" +
                "-fx-text-fill: #cbd5e1;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 9 12;";
    }

    // =====================================================
    // GETTERS - GUI
    // =====================================================

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

    public LandmarkTypeGUI getLandmarkTypeGUI() {
        return landmarkTypeGUI;
    }

    public SaleOffGUI getSaleOffGUI() {
        return saleOffGUI;
    }

    public RoomSaleOffGUI getRoomSaleOffGUI() {
        return roomSaleOffGUI;
    }

    // =====================================================
    // GETTERS - BUTTON
    // =====================================================

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

    public Button getLandmarkTypeButton() {
        return landmarkTypeButton;
    }

    public Button getSaleOffButton() {
        return saleOffButton;
    }

    public Button getRoomSaleOffButton() {
        return roomSaleOffButton;
    }
}