package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.Building;
import com.minhtan.qlptclient.entity.BuildingFee;
import com.minhtan.qlptclient.gui.BuildingGUI;
import com.minhtan.qlptclient.service.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.util.List;

public class BuildingController {

    private final ApiClient apiClient = new ApiClient("http://localhost:8080");
    private final BuildingGUI view;

    public BuildingController(BuildingGUI view) {
        this.view = view;
        wireEvents();
        loadBuildings();
    }

    private void wireEvents() {
        view.getBuildingList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            view.getBuildingIdField().setText(valueOrEmpty(selected.getBuildingId()));
            view.getTrueAddressField().setText(valueOrEmpty(selected.getTrueAddress()));
            view.getFakeAddressField().setText(valueOrEmpty(selected.getFakeAddress()));
            view.getNoteField().setText(valueOrEmpty(selected.getNote()));
            view.getOwnerPhoneField().setText(valueOrEmpty(selected.getOwnerPhone()));
            view.getFeeBuildingIdField().setText(valueOrEmpty(selected.getBuildingId()));
            loadBuildingFee(selected.getBuildingId());
        });

        view.getRefreshButton().setOnAction(event -> loadBuildings());
        view.getSearchButton().setOnAction(event -> searchBuildings());
        view.getCreateButton().setOnAction(event -> createBuilding());
        view.getUpdateButton().setOnAction(event -> updateBuilding());
        view.getDeleteButton().setOnAction(event -> deleteBuilding());
        view.getClearButton().setOnAction(event -> view.clearForm());
        view.getCreateFeeButton().setOnAction(event -> createBuildingFee());
        view.getUpdateFeeButton().setOnAction(event -> updateBuildingFee());
        view.getDeleteFeeButton().setOnAction(event -> deleteBuildingFee());
        view.getClearFeeButton().setOnAction(event -> clearFeeFormForSelectedBuilding());
    }

    private void loadBuildings() {
        runLoadTask("Đang tải /api/buildings...", apiClient::getBuildings);
    }

    private void searchBuildings() {
        String keyword = view.getSearchField().getText() == null ? "" : view.getSearchField().getText().trim();
        String mode = view.getSearchModeBox().getValue();

        if ("All".equals(mode)) {
            loadBuildings();
            return;
        }

        if (keyword.isBlank()) {
            showAlert("Nhập từ khóa", "Hãy nhập giá trị tìm kiếm trước khi tìm.");
            return;
        }

        Task<List<Building>> task = new Task<>() {
            @Override
            protected List<Building> call() throws Exception {
                return switch (mode) {
                    case "True Address" -> apiClient.searchBuildingsByTrueAddress(keyword);
                    case "Fake Address" -> apiClient.searchBuildingsByFakeAddress(keyword);
                    case "Note" -> apiClient.searchBuildingsByNote(keyword);
                    case "Owner Phone" -> apiClient.searchBuildingsByOwnerPhone(keyword);
                    default -> apiClient.getBuildings();
                };
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            List<Building> buildings = task.getValue();
            view.getBuildingItems().setAll(buildings);
            view.setStatus("Tìm thấy " + buildings.size() + " building(s)");
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tìm dữ liệu"));

        view.setStatus("Đang tìm kiếm...");
        startTask(task);
    }

    private void createBuilding() {
        runMutationTask("Đang tạo building mới...", () -> {
            Building building = readForm();
            building.setBuildingId(null);
            apiClient.createBuilding(building);
        });
    }

    private void updateBuilding() {
        if (view.getBuildingIdField().getText().isBlank()) {
            showAlert("Chưa chọn building", "Hãy chọn một dòng trong danh sách trước khi cập nhật.");
            return;
        }

        runMutationTask("Đang cập nhật building...", () ->
                apiClient.updateBuilding(Integer.valueOf(view.getBuildingIdField().getText()), readForm()));
    }

    private void deleteBuilding() {
        if (view.getBuildingIdField().getText().isBlank()) {
            showAlert("Chưa chọn building", "Hãy chọn một dòng trong danh sách trước khi xóa.");
            return;
        }

        runMutationTask("Đang xóa building...", () ->
                apiClient.deleteBuilding(Integer.valueOf(view.getBuildingIdField().getText())));
    }

    private void loadBuildingFee(Integer buildingId) {
        if (buildingId == null) {
            view.clearFeeForm();
            return;
        }

        Task<List<BuildingFee>> task = new Task<>() {
            @Override
            protected List<BuildingFee> call() throws Exception {
                return apiClient.searchBuildingFeesByBuildingId(buildingId);
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            List<BuildingFee> fees = task.getValue();
            if (fees.isEmpty()) {
                view.clearFeeForm();
                view.getFeeBuildingIdField().setText(String.valueOf(buildingId));
                view.setStatus("Chưa có phí dịch vụ cho building " + buildingId);
                return;
            }
            fillFeeForm(fees.get(0));
            view.setStatus("Đã tải phí dịch vụ cho building " + buildingId);
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tải phí dịch vụ"));

        view.setStatus("Đang tải phí dịch vụ...");
        startTask(task);
    }

    private void createBuildingFee() {
        runMutationTask("Đang tạo phí dịch vụ...", () -> {
            BuildingFee buildingFee = readFeeForm();
            buildingFee.setFeeId(null);
            apiClient.createBuildingFee(buildingFee);
        });
    }

    private void updateBuildingFee() {
        if (view.getFeeIdField().getText().isBlank()) {
            showAlert("Chưa chọn phí dịch vụ", "Hãy chọn tòa nhà có phí dịch vụ trước khi cập nhật.");
            return;
        }

        runMutationTask("Đang cập nhật phí dịch vụ...", () ->
                apiClient.updateBuildingFee(Integer.valueOf(view.getFeeIdField().getText()), readFeeForm()));
    }

    private void deleteBuildingFee() {
        if (view.getFeeIdField().getText().isBlank()) {
            showAlert("Chưa chọn phí dịch vụ", "Hãy chọn tòa nhà có phí dịch vụ trước khi xóa.");
            return;
        }

        Integer buildingId = integerOrNull(view.getFeeBuildingIdField().getText(), "Building ID");
        runMutationTask("Đang xóa phí dịch vụ...", () ->
                apiClient.deleteBuildingFee(Integer.valueOf(view.getFeeIdField().getText())));
        if (buildingId != null) {
            view.getFeeBuildingIdField().setText(String.valueOf(buildingId));
        }
    }

    private void clearFeeFormForSelectedBuilding() {
        String buildingId = view.getBuildingIdField().getText();
        view.clearFeeForm();
        view.getFeeBuildingIdField().setText(buildingId == null ? "" : buildingId.trim());
    }

    private void runLoadTask(String statusText, Loader loader) {
        Task<List<Building>> task = new Task<>() {
            @Override
            protected List<Building> call() throws Exception {
                return loader.load();
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            List<Building> buildings = task.getValue();
            view.getBuildingItems().setAll(buildings);
            view.setStatus("Tải thành công " + buildings.size() + " building(s)");
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tải dữ liệu"));

        view.setStatus(statusText);
        startTask(task);
    }

    private void runMutationTask(String statusText, Mutation mutation) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                mutation.run();
                return null;
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            view.setStatus("Thực hiện thành công");
            loadBuildings();
            if (!view.getBuildingIdField().getText().isBlank()) {
                loadBuildingFee(Integer.valueOf(view.getBuildingIdField().getText()));
            }
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, statusText));

        view.setStatus(statusText);
        startTask(task);
    }

    private void handleTaskFailure(Task<?> task, String fallbackMessage) {
        Throwable error = task.getException();
        String message = error == null ? fallbackMessage : error.getMessage();
        Platform.runLater(() -> {
            view.setStatus(fallbackMessage + ": " + message);
            showAlert("Lỗi kết nối backend", message);
        });
    }

    private void startTask(Task<?> task) {
        Thread thread = new Thread(task, "api-client-task");
        thread.setDaemon(true);
        thread.start();
    }

    private Building readForm() {
        Building building = new Building();
        String buildingIdText = view.getBuildingIdField().getText() == null ? "" : view.getBuildingIdField().getText().trim();
        if (!buildingIdText.isBlank()) {
            building.setBuildingId(Integer.valueOf(buildingIdText));
        }
        building.setTrueAddress(textOf(view.getTrueAddressField().getText()));
        building.setFakeAddress(textOf(view.getFakeAddressField().getText()));
        building.setNote(textOf(view.getNoteField().getText()));
        building.setOwnerPhone(textOf(view.getOwnerPhoneField().getText()));
        return building;
    }

    private BuildingFee readFeeForm() {
        Integer buildingId = integerOrNull(view.getFeeBuildingIdField().getText(), "Building ID");
        if (buildingId == null) {
            showAlert("Lỗi", "Hãy chọn tòa nhà trước khi tạo phí dịch vụ.");
            throw new IllegalArgumentException("Hãy chọn tòa nhà trước khi tạo phí dịch vụ.");
        }

        BuildingFee buildingFee = new BuildingFee();
        Integer feeId = integerOrNull(view.getFeeIdField().getText(), "Fee ID");
        if (feeId != null) {
            buildingFee.setFeeId(feeId);
        }
        buildingFee.setBuildingId(buildingId);
        buildingFee.setBuilding(null);
        buildingFee.setElectricityPrice(decimalOrNull(view.getElectricityPriceField().getText(), "Electricity Price"));
        buildingFee.setWaterPrice(decimalOrNull(view.getWaterPriceField().getText(), "Water Price"));
        buildingFee.setServiceFee(decimalOrNull(view.getServiceFeeField().getText(), "Service Fee"));
        buildingFee.setParkingFee(decimalOrNull(view.getParkingFeeField().getText(), "Parking Fee"));
        buildingFee.setOtherFee(decimalOrNull(view.getOtherFeeField().getText(), "Other Fee"));
        buildingFee.setFreeParking(integerOrNull(view.getFreeParkingField().getText(), "Free Parking"));
        return buildingFee;
    }

    private void fillFeeForm(BuildingFee buildingFee) {
        view.getFeeIdField().setText(valueOrEmpty(buildingFee.getFeeId()));
        view.getFeeBuildingIdField().setText(valueOrEmpty(buildingFee.getBuildingId()));
        view.getElectricityPriceField().setText(valueOrEmpty(buildingFee.getElectricityPrice()));
        view.getWaterPriceField().setText(valueOrEmpty(buildingFee.getWaterPrice()));
        view.getServiceFeeField().setText(valueOrEmpty(buildingFee.getServiceFee()));
        view.getParkingFeeField().setText(valueOrEmpty(buildingFee.getParkingFee()));
        view.getOtherFeeField().setText(valueOrEmpty(buildingFee.getOtherFee()));
        view.getFreeParkingField().setText(valueOrEmpty(buildingFee.getFreeParking()));
    }

    private static String textOf(String value) {
        return value == null ? "" : value.trim();
    }

    private static Integer integerOrNull(String value, String fieldName) {
        String text = textOf(value);
        if (text.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " phải là số nguyên.");
        }
    }

    private static BigDecimal decimalOrNull(String value, String fieldName) {
        String text = textOf(value);
        if (text.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " phải là số.");
        }
    }

    private static String valueOrEmpty(Integer value) {
        return value == null ? "" : String.valueOf(value);
    }

    private static String valueOrEmpty(BigDecimal value) {
        return value == null ? "" : value.toPlainString();
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @FunctionalInterface
    private interface Loader {
        List<Building> load() throws Exception;
    }

    @FunctionalInterface
    private interface Mutation {
        void run() throws Exception;
    }
}
