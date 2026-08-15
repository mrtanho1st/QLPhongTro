package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.gui.MainView;

public class MainController {

    private final MainView view;

    private final SearchRoomController searchRoomController;
    private final BuildingController buildingController;
    private final RoomController roomController;
    private final AmenityController amenityController;
    private final CommissionController commissionController;
    private final HomeDashboardController homeDashboardController;
    private final DistrictController districtController;
    private final TypeRoomController typeRoomController;
    private final LandmarkController landmarkController;
    private final LandmarkTypeController landmarkTypeController;

    public MainController(MainView view) {

        this.view = view;

        this.searchRoomController = new SearchRoomController(
                view.getSearchRoomGUI());

        this.buildingController = new BuildingController(
                view.getBuildingGUI());

        this.roomController = new RoomController(
                view.getRoomGUI());

        this.amenityController = new AmenityController(
                view.getAmenityGUI());

        this.commissionController = new CommissionController(
                view.getCommissionGUI());

        this.homeDashboardController = new HomeDashboardController(
                view.getHomeDashboardPane());

        this.districtController = new DistrictController(
                view.getDistrictGUI());

        this.typeRoomController = new TypeRoomController(
                view.getTypeRoomGUI());

        this.landmarkController = new LandmarkController(
                view.getLandmarkGUI());

        new SaleOffController(
                view.getSaleOffGUI());

        new RoomSaleOffController(
                view.getRoomSaleOffGUI());

        this.landmarkTypeController = new LandmarkTypeController(
                view.getLandmarkTypeGUI());

        wireEvents();

        showHome();
    }

    // =====================================================
    // WIRE EVENTS
    // =====================================================

    private void wireEvents() {

        // =====================================
        // TRANG CHỦ
        // =====================================

        view.getHomeButton()
                .setOnAction(
                        event -> showHome());

        // =====================================
        // QUẢN LÝ PHÒNG TRỌ
        // =====================================

        view.getBuildingButton()
                .setOnAction(
                        event -> showBuilding());

        view.getRoomButton()
                .setOnAction(
                        event -> showRoom());

        view.getTypeRoomButton()
                .setOnAction(
                        event -> showTypeRoom());

        view.getAmenityButton()
                .setOnAction(
                        event -> showAmenity());

        view.getDistrictButton()
                .setOnAction(
                        event -> showDistrict());

        // =====================================
        // TÌM KIẾM
        // =====================================

        view.getSearchButton()
                .setOnAction(
                        event -> showSearchRoom());

        // =====================================
        // KINH DOANH
        // =====================================

        view.getCommissionButton()
                .setOnAction(
                        event -> showCommission());

        view.getSaleOffButton()
                .setOnAction(
                        event -> showSaleOff());

        view.getRoomSaleOffButton()
                .setOnAction(
                        event -> showRoomSaleOff());

        // =====================================
        // ĐỊA ĐIỂM
        // =====================================

        view.getLandmarkButton()
                .setOnAction(
                        event -> showLandmark());

        view.getLandmarkTypeButton()
                .setOnAction(
                        event -> showLandmarkType());
    }

    // =====================================================
    // SHOW HOME
    // =====================================================

    private void showHome() {
        homeDashboardController.refreshData();
        view.setPage(
                "Trang chủ",
                view.getHomeDashboardPane(),
                view.getHomeButton());
    }

    // =====================================================
    // QUẢN LÝ PHÒNG TRỌ
    // =====================================================

    private void showBuilding() {
        buildingController.refreshData();
        view.setPage(
                "Quản lý tòa nhà",
                view.getBuildingGUI(),
                view.getBuildingButton());
    }

    private void showRoom() {
        roomController.refreshData();
        view.setPage(
                "Quản lý phòng",
                view.getRoomGUI(),
                view.getRoomButton());
    }

    private void showTypeRoom() {
        typeRoomController.refreshData();
        view.setPage(
                "Quản lý loại phòng",
                view.getTypeRoomGUI(),
                view.getTypeRoomButton());
    }

    private void showAmenity() {
        amenityController.refreshData();
        view.setPage(
                "Quản lý tiện ích",
                view.getAmenityGUI(),
                view.getAmenityButton());
    }

    private void showDistrict() {
        districtController.refreshData();
        view.setPage(
                "Quản lý khu vực",
                view.getDistrictGUI(),
                view.getDistrictButton());
    }

    // =====================================================
    // TÌM KIẾM
    // =====================================================

    private void showSearchRoom() {
        searchRoomController.refreshData();
        view.setPage(
                "Tìm phòng trọ",
                view.getSearchRoomGUI(),
                view.getSearchButton());
    }

    // =====================================================
    // KINH DOANH
    // =====================================================

    private void showCommission() {
        commissionController.refreshData();
        view.setPage(
                "Quản lý hoa hồng",
                view.getCommissionGUI(),
                view.getCommissionButton());
    }

    private void showSaleOff() {

        view.setPage(
                "Quản lý khuyến mãi",
                view.getSaleOffGUI(),
                view.getSaleOffButton());
    }

    private void showRoomSaleOff() {

        view.setPage(
                "Quản lý loại khuyến mãi",
                view.getSaleOffGUI(),
                view.getRoomSaleOffButton());
    }

    // =====================================================
    // ĐỊA ĐIỂM
    // =====================================================

    private void showLandmark() {
        landmarkController.refreshData();
        view.setPage(
                "Quản lý địa danh",
                view.getLandmarkGUI(),
                view.getLandmarkButton());
    }

    private void showLandmarkType() {
        landmarkTypeController.refreshData();
        view.setPage(
                "Quản lý loại địa danh",
                view.getLandmarkTypeGUI(),
                view.getLandmarkTypeButton());
    }

    // =====================================================
    // GETTER
    // =====================================================

    public SearchRoomController getSearchRoomController() {

        return this.searchRoomController;
    }
}