package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.Building;
import com.minhtan.qlptclient.entity.Room;
import com.minhtan.qlptclient.gui.HomeDashboardPane;
import com.minhtan.qlptclient.service.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;

public class HomeDashboardController {

    private final HomeDashboardPane view;

    public HomeDashboardController(HomeDashboardPane view) {
        this.view = view;
        wireEvents();
        loadStats();
    }

    private void wireEvents() {
        view.getRefreshButton().setOnAction(event -> loadStats());
    }

    private void loadStats() {
        Task<DashboardStats> task = new Task<>() {
            @Override
            protected DashboardStats call() throws Exception {
                List<Building> buildings = ApiClient.getInstance().getBuildings();
                List<Room> rooms = ApiClient.getInstance().getRooms();
                long lockedRooms = rooms.stream().filter(room -> Boolean.TRUE.equals(room.getLocked())).count();
                long availableRooms = rooms.stream().filter(room -> !Boolean.TRUE.equals(room.getLocked())).count();
                return new DashboardStats(buildings.size(), availableRooms, lockedRooms);
            }
        };

        task.setOnSucceeded(event -> {
            DashboardStats stats = task.getValue();
            view.setBuildingCount(stats.buildingCount());
            view.setAvailableRoomCount(stats.availableRoomCount());
            view.setLockedRoomCount(stats.lockedRoomCount());
            view.setStatus("Thống kê đã được cập nhật");
        });

        task.setOnFailed(event -> {
            Throwable error = task.getException();
            String message = error == null ? "Không thể tải thống kê" : error.getMessage();
            Platform.runLater(() -> view.setStatus("Lỗi tải thống kê: " + message));
        });

        view.setStatus("Đang tải thống kê từ backend...");
        Thread thread = new Thread(task, "dashboard-stats-task");
        thread.setDaemon(true);
        thread.start();
    }

    private record DashboardStats(int buildingCount, long availableRoomCount, long lockedRoomCount) {
    }
}
