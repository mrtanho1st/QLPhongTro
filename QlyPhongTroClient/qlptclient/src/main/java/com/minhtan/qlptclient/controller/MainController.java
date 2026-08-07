package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.gui.MainView;

public class MainController {

    private final MainView view;

    private final SearchRoomController searchRoomController;

    public MainController(MainView view) {

        this.view = view;

        // =====================================
        // CONTROLLERS
        // =====================================

        this.searchRoomController = new SearchRoomController(
                view.getSearchRoomGUI());

        new BuildingController(
                view.getBuildingGUI());

        new RoomController(
                view.getRoomGUI());

        new AmenityController(
                view.getAmenityGUI());

        new CommissionController(
                view.getCommissionGUI());

        new HomeDashboardController(
                view.getHomeDashboardPane());

        new DistrictController(
                view.getDistrictGUI());

        new TypeRoomController(
                view.getTypeRoomGUI());

        new LandmarkController(
                view.getLandmarkGUI());

        new SaleOffController(
                view.getSaleOffGUI());

        new RoomSaleOffController(
                view.getRoomSaleOffGUI());

        new LandmarkTypeController(
                view.getLandmarkTypeGUI());

        new SaleOffController(
                view.getSaleOffGUI());

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

        view.setPage(
                "Trang chủ",
                view.getHomeDashboardPane(),
                view.getHomeButton());
    }

    // =====================================================
    // QUẢN LÝ PHÒNG TRỌ
    // =====================================================

    private void showBuilding() {

        view.setPage(
                "Quản lý tòa nhà",
                view.getBuildingGUI(),
                view.getBuildingButton());
    }

    private void showRoom() {

        view.setPage(
                "Quản lý phòng",
                view.getRoomGUI(),
                view.getRoomButton());
    }

    private void showTypeRoom() {

        view.setPage(
                "Quản lý loại phòng",
                view.getTypeRoomGUI(),
                view.getTypeRoomButton());
    }

    private void showAmenity() {

        view.setPage(
                "Quản lý tiện ích",
                view.getAmenityGUI(),
                view.getAmenityButton());
    }

    private void showDistrict() {

        view.setPage(
                "Quản lý khu vực",
                view.getDistrictGUI(),
                view.getDistrictButton());
    }

    // =====================================================
    // TÌM KIẾM
    // =====================================================

    private void showSearchRoom() {

        view.setPage(
                "Tìm phòng trọ",
                view.getSearchRoomGUI(),
                view.getSearchButton());
    }

    // =====================================================
    // KINH DOANH
    // =====================================================

    private void showCommission() {

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

        view.setPage(
                "Quản lý địa danh",
                view.getLandmarkGUI(),
                view.getLandmarkButton());
    }

    private void showLandmarkType() {

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