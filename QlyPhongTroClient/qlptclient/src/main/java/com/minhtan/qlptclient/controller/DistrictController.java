package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.District;
import com.minhtan.qlptclient.gui.DistrictGUI;
import com.minhtan.qlptclient.service.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;

public class DistrictController {

    private final DistrictGUI view;

    public DistrictController(DistrictGUI view) {
        this.view = view;
        wireEvents();
    }

    public void refreshData() {
        loadDistricts();
    }

    private void wireEvents() {
        view.getDistrictList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            view.getDistrictIdField().setText(valueOrEmpty(selected.getDistrictId()));
            view.getNameField().setText(valueOrEmpty(selected.getDistrictName()));
            view.setDetails(selected.toString());
        });

        view.getRefreshButton().setOnAction(event -> loadDistricts());
        view.getSearchButton().setOnAction(event -> searchDistricts());
        view.getCreateButton().setOnAction(event -> createDistrict());
        view.getUpdateButton().setOnAction(event -> updateDistrict());
        view.getDeleteButton().setOnAction(event -> deleteDistrict());
        view.getClearButton().setOnAction(event -> view.clearForm());
    }

    private void loadDistricts() {
        runLoadTask("Đang tải /api/districts...", ApiClient.getInstance()::getDistricts);
    }

    private void searchDistricts() {
        String keyword = view.getSearchField().getText() == null ? "" : view.getSearchField().getText().trim();
        String mode = view.getSearchModeBox().getValue();

        if ("All".equals(mode)) {
            loadDistricts();
            return;
        }

        if (keyword.isBlank()) {
            showAlert("Nhập từ khóa", "Hãy nhập giá trị tìm kiếm trước khi tìm.");
            return;
        }

        Task<List<District>> task = new Task<>() {
            @Override
            protected List<District> call() throws Exception {
                return switch (mode) {
                    case "District Name" -> ApiClient.getInstance().searchDistrictsByName(keyword);
                    default -> ApiClient.getInstance().getDistricts();
                };
            }
        };

        task.setOnSucceeded(workerStateEvent ->
                showDistricts(task.getValue(), "Tìm thấy " + task.getValue().size() + " district(s)"));

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tìm dữ liệu"));

        view.setStatus("Đang tìm kiếm...");
        startTask(task);
    }

    private void createDistrict() {
        runMutationTask("Đang tạo district mới...", () -> {
            District district = readForm();
            district.setDistrictId(null);
            ApiClient.getInstance().createDistrict(district);
        });
    }

    private void updateDistrict() {
        if (view.getDistrictIdField().getText().isBlank()) {
            showAlert("Chưa chọn district", "Hãy chọn một dòng trong danh sách trước khi cập nhật.");
            return;
        }

        runMutationTask("Đang cập nhật district...", () ->
                ApiClient.getInstance().updateDistrict(Integer.valueOf(view.getDistrictIdField().getText()), readForm()));
    }

    private void deleteDistrict() {
        if (view.getDistrictIdField().getText().isBlank()) {
            showAlert("Chưa chọn district", "Hãy chọn một dòng trong danh sách trước khi xóa.");
            return;
        }

        runMutationTask("Đang xóa district...", () ->
                ApiClient.getInstance().deleteDistrict(Integer.valueOf(view.getDistrictIdField().getText())));
    }

    private void runLoadTask(String statusText, Loader loader) {
        Task<List<District>> task = new Task<>() {
            @Override
            protected List<District> call() throws Exception {
                return loader.load();
            }
        };

        task.setOnSucceeded(workerStateEvent ->
                showDistricts(task.getValue(), "Tải thành công " + task.getValue().size() + " district(s)"));

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
            view.setDetails("Đã cập nhật dữ liệu thành công");
            loadDistricts();
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, statusText));

        view.setStatus(statusText);
        startTask(task);
    }

    private void showDistricts(List<District> districts, String statusText) {
        view.getDistrictItems().setAll(districts);
        view.setDetails(districts.isEmpty()
                ? "Không có dữ liệu"
                : districts.stream().map(District::toString)
                        .reduce((left, right) -> left + System.lineSeparator() + right).orElse("Không có dữ liệu"));
        view.setStatus(statusText);
    }

    private District readForm() {
        District district = new District();
        String districtIdText = textOf(view.getDistrictIdField().getText());
        if (!districtIdText.isBlank()) {
            district.setDistrictId(Integer.valueOf(districtIdText));
        }
        district.setDistrictName(textOf(view.getNameField().getText()));
        return district;
    }

    private void handleTaskFailure(Task<?> task, String fallbackMessage) {
        Throwable error = task.getException();
        String message = error == null ? fallbackMessage : error.getMessage();
        Platform.runLater(() -> {
            view.setStatus(fallbackMessage);
            view.setDetails(message);
            showAlert("Lỗi kết nối backend", message);
        });
    }

    private void startTask(Task<?> task) {
        Thread thread = new Thread(task, "district-api-client-task");
        thread.setDaemon(true);
        thread.start();
    }

    private static String textOf(String value) {
        return value == null ? "" : value.trim();
    }

    private static String valueOrEmpty(Integer value) {
        return value == null ? "" : String.valueOf(value);
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
        List<District> load() throws Exception;
    }

    @FunctionalInterface
    private interface Mutation {
        void run() throws Exception;
    }
}
