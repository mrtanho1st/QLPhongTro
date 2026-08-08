package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.LandmarkType;
import com.minhtan.qlptclient.entity.TypeRoom;
import com.minhtan.qlptclient.gui.LandmarkTypeGUI;
import com.minhtan.qlptclient.gui.TypeRoomGUI;
import com.minhtan.qlptclient.service.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;

public class LandmarkTypeController {

    private final LandmarkTypeGUI view;

    public LandmarkTypeController(LandmarkTypeGUI view) {
        this.view = view;
        wireEvents();
        loadLandmarkTypes();
    }

    private void wireEvents() {
        view.getLandmarkTypeList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            view.getLandmarkTypeIdField().setText(valueOrEmpty(selected.getLandmarkTypeId()));
            view.getNameField().setText(valueOrEmpty(selected.getLandmarkTypeName()));
            view.setDetails(selected.toString());
        });

        view.getRefreshButton().setOnAction(event -> loadLandmarkTypes());
        view.getSearchButton().setOnAction(event -> searchLandmarkTypes());
        view.getCreateButton().setOnAction(event -> createLandmarkType());
        view.getUpdateButton().setOnAction(event -> updateLandmarkType());
        view.getDeleteButton().setOnAction(event -> deleteLandmarkType());
        view.getClearButton().setOnAction(event -> view.clearForm());
    }

    private void loadLandmarkTypes() {
        runLoadTask("Đang tải /api/landmark-types...", ApiClient.getInstance()::getLandmarkTypes);
    }

    private void searchLandmarkTypes() {
        String keyword = view.getSearchField().getText() == null ? "" : view.getSearchField().getText().trim();
        String mode = view.getSearchModeBox().getValue();

        if ("All".equals(mode)) {
            loadLandmarkTypes();
            return;
        }

        if (keyword.isBlank()) {
            showAlert("Nhập từ khóa", "Hãy nhập giá trị tìm kiếm trước khi tìm.");
            return;
        }

        Task<List<LandmarkType>> task = new Task<>() {
            @Override
            protected List<LandmarkType> call() throws Exception {
                return switch (mode) {
                    case "Landmark Type Name" -> ApiClient.getInstance().searchLandmarkTypesByName(keyword);
                    default -> ApiClient.getInstance().getLandmarkTypes();
                };
            }
        };

        task.setOnSucceeded(workerStateEvent ->
                showLandmarkTypes(task.getValue(), "Tìm thấy " + task.getValue().size() + " landmark type(s)"));

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tìm dữ liệu"));

        view.setStatus("Đang tìm kiếm...");
        startTask(task);
    }

    private void createLandmarkType() {
        runMutationTask("Đang tạo landmark type mới...", () -> {
            LandmarkType landmarkType = readForm();
            landmarkType.setLandmarkTypeId(null);
            ApiClient.getInstance().createLandmarkType(landmarkType);
        });
    }

    private void updateLandmarkType() {
        if (view.getLandmarkTypeIdField().getText().isBlank()) {
            showAlert("Chưa chọn landmark type", "Hãy chọn một dòng trong danh sách trước khi cập nhật.");
            return;
        }

        runMutationTask("Đang cập nhật landmark type...", () ->
                ApiClient.getInstance().updateLandmarkType(Integer.valueOf(view.getLandmarkTypeIdField().getText()), readForm()));
    }

    private void deleteLandmarkType() {
        if (view.getLandmarkTypeIdField().getText().isBlank()) {
            showAlert("Chưa chọn landmark type", "Hãy chọn một dòng trong danh sách trước khi xóa.");
            return;
        }

        runMutationTask("Đang xóa landmark type...", () ->
                ApiClient.getInstance().deleteLandmarkType(Integer.valueOf(view.getLandmarkTypeIdField().getText())));
    }

    private void runLoadTask(String statusText, Loader loader) {
        Task<List<LandmarkType>> task = new Task<>() {
            @Override
            protected List<LandmarkType> call() throws Exception {
                return loader.load();
            }
        };

        task.setOnSucceeded(workerStateEvent ->
                showLandmarkTypes(task.getValue(), "Tải thành công " + task.getValue().size() + " landmark type(s)"));

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
            loadLandmarkTypes();
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, statusText));

        view.setStatus(statusText);
        startTask(task);
    }

    private void showLandmarkTypes(List<LandmarkType> landmarkTypes, String statusText) {
        view.getLandmarkTypeItems().setAll(landmarkTypes);
        view.setDetails(landmarkTypes.isEmpty()
                ? "Không có dữ liệu"
                : landmarkTypes.stream().map(LandmarkType::toString)
                        .reduce((left, right) -> left + System.lineSeparator() + right).orElse("Không có dữ liệu"));
        view.setStatus(statusText);
    }

    private LandmarkType readForm() {
        LandmarkType landmarkType = new LandmarkType();
        String landmarkTypeIdText = textOf(view.getLandmarkTypeIdField().getText());
        if (!landmarkTypeIdText.isBlank()) {
            landmarkType.setLandmarkTypeId(Integer.valueOf(landmarkTypeIdText));
        }
        landmarkType.setLandmarkTypeName(textOf(view.getNameField().getText()));
        return landmarkType;
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
        List<LandmarkType> load() throws Exception;
    }

    @FunctionalInterface
    private interface Mutation {
        void run() throws Exception;
    }
}
