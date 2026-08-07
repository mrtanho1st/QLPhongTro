package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.gui.MainView;

public class MainController {

    private final MainView view;
    private final SearchRoomController searchRoomController;

    public MainController(MainView view) {
        this.view = view;
        this.searchRoomController = new SearchRoomController(view.getSearchRoomGUI());
        
        new BuildingController(view.getBuildingGUI());
        new RoomController(view.getRoomGUI());
        new AmenityController(view.getAmenityGUI());
        new CommissionController(view.getCommissionGUI());
        new HomeDashboardController(view.getHomeDashboardPane());
        new DistrictController(view.getDistrictGUI());
        new TypeRoomController(view.getTypeRoomGUI());
        new LandmarkController(view.getLandmarkGUI());
        wireEvents();
        showHome();
    }

    private void wireEvents() {
        view.getHomeButton().setOnAction(event -> showHome());
        view.getBuildingButton().setOnAction(event -> showBuilding());
        view.getRoomButton().setOnAction(event -> showRoom());
        view.getAmenityButton().setOnAction(event -> showAmenity());
        view.getCommissionButton().setOnAction(event -> showCommission());
        view.getSearchButton().setOnAction(event -> showSearchRoom());
        view.getDistrictButton().setOnAction(event -> showDistrict());
        view.getTypeRoomButton().setOnAction(event -> showTypeRoom());
        view.getLandmarkButton().setOnAction(event -> showLandmark());
    }

    private void showHome() {
        view.setPage("Trang chủ", view.getHomeDashboardPane(), view.getHomeButton());
    }

    private void showBuilding() {
        view.setPage("Quản lý tòa nhà", view.getBuildingGUI(), view.getBuildingButton());
    }

    private void showRoom() {
        view.setPage("Quản lý phòng", view.getRoomGUI(), view.getRoomButton());
    }

    private void showAmenity() {
        view.setPage("Quản lý tiện ích", view.getAmenityGUI(), view.getAmenityButton());
    }

    private void showCommission() {
        view.setPage("Quản lý hoa hồng", view.getCommissionGUI(), view.getCommissionButton());
    }

    private void showSearchRoom() {
        view.setPage("Tìm phòng trọ", view.getSearchRoomGUI(), view.getSearchButton());
    }

    private void showDistrict() {
        view.setPage("Quản lý quận", view.getDistrictGUI(), view.getDistrictButton());
    }

    private void showTypeRoom() {
        view.setPage("Quản lý loại phòng", view.getTypeRoomGUI(), view.getTypeRoomButton());
    }

    private void showLandmark() {
        view.setPage("Quản lý cột mốc", view.getLandmarkGUI(), view.getLandmarkButton());
    }
    
    public SearchRoomController getSearchRoomController() {
        return this.searchRoomController;
    }
}
