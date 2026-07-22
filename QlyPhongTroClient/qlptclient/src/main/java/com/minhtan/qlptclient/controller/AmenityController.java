package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.Amenity;
import com.minhtan.qlptclient.gui.AmenityGUI;
import com.minhtan.qlptclient.service.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;

public class AmenityController {

    private final ApiClient apiClient = new ApiClient("http://localhost:8080");
    private final AmenityGUI view;

    public AmenityController(AmenityGUI view) {
        this.view = view;
        wireEvents();
        loadAmenities();
    }

    private void wireEvents() {
        view.getAmenityList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            view.getAmenityIdField().setText(valueOrEmpty(selected.getAmenityId()));
            view.getNameField().setText(valueOrEmpty(selected.getName()));
            view.setDetails(selected.toString());
        });

        view.getRefreshButton().setOnAction(event -> loadAmenities());
        view.getSearchButton().setOnAction(event -> searchAmenities());
        view.getCreateButton().setOnAction(event -> createAmenity());
        view.getUpdateButton().setOnAction(event -> updateAmenity());
        view.getDeleteButton().setOnAction(event -> deleteAmenity());
        view.getClearButton().setOnAction(event -> view.clearForm());
    }

    private void loadAmenities() {
        runLoadTask("Đang tải /api/amenities...", apiClient::getAmenities);
    }

    private void searchAmenities() {
        String keyword = view.getSearchField().getText() == null ? "" : view.getSearchField().getText().trim();
        String mode = view.getSearchModeBox().getValue();

        if ("All".equals(mode)) {
            loadAmenities();
            return;
        }

        if (keyword.isBlank()) {
            showAlert("Nhập từ khóa", "Hãy nhập giá trị tìm kiếm trước khi tìm.");
            return;
        }

        Task<List<Amenity>> task = new Task<>() {
            @Override
            protected List<Amenity> call() throws Exception {
                return switch (mode) {
                    case "Name" -> apiClient.searchAmenitiesByName(keyword);
                    default -> apiClient.getAmenities();
                };
            }
        };

        task.setOnSucceeded(workerStateEvent ->
                showAmenities(task.getValue(), "Tìm thấy " + task.getValue().size() + " amenity(s)"));

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tìm dữ liệu"));

        view.setStatus("Đang tìm kiếm...");
        startTask(task);
    }

    private void createAmenity() {
        runMutationTask("Đang tạo amenity mới...", () -> {
            Amenity amenity = readForm();
            amenity.setAmenityId(null);
            apiClient.createAmenity(amenity);
        });
    }

    private void updateAmenity() {
        if (view.getAmenityIdField().getText().isBlank()) {
            showAlert("Chưa chọn amenity", "Hãy chọn một dòng trong danh sách trước khi cập nhật.");
            return;
        }

        runMutationTask("Đang cập nhật amenity...", () ->
                apiClient.updateAmenity(Integer.valueOf(view.getAmenityIdField().getText()), readForm()));
    }

    private void deleteAmenity() {
        if (view.getAmenityIdField().getText().isBlank()) {
            showAlert("Chưa chọn amenity", "Hãy chọn một dòng trong danh sách trước khi xóa.");
            return;
        }

        runMutationTask("Đang xóa amenity...", () ->
                apiClient.deleteAmenity(Integer.valueOf(view.getAmenityIdField().getText())));
    }

    private void runLoadTask(String statusText, Loader loader) {
        Task<List<Amenity>> task = new Task<>() {
            @Override
            protected List<Amenity> call() throws Exception {
                return loader.load();
            }
        };

        task.setOnSucceeded(workerStateEvent ->
                showAmenities(task.getValue(), "Tải thành công " + task.getValue().size() + " amenity(s)"));

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
            loadAmenities();
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, statusText));

        view.setStatus(statusText);
        startTask(task);
    }

    private void showAmenities(List<Amenity> amenities, String statusText) {
        view.getAmenityItems().setAll(amenities);
        view.setDetails(amenities.isEmpty()
                ? "Không có dữ liệu"
                : amenities.stream().map(Amenity::toString)
                        .reduce((left, right) -> left + System.lineSeparator() + right).orElse("Không có dữ liệu"));
        view.setStatus(statusText);
    }

    private Amenity readForm() {
        Amenity amenity = new Amenity();
        String amenityIdText = textOf(view.getAmenityIdField().getText());
        if (!amenityIdText.isBlank()) {
            amenity.setAmenityId(Integer.valueOf(amenityIdText));
        }
        amenity.setName(textOf(view.getNameField().getText()));
        return amenity;
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
        Thread thread = new Thread(task, "amenity-api-client-task");
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
        List<Amenity> load() throws Exception;
    }

    @FunctionalInterface
    private interface Mutation {
        void run() throws Exception;
    }
}
