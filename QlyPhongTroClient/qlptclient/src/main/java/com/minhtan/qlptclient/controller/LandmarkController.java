package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.Landmark;
import com.minhtan.qlptclient.entity.LandmarkType;
import com.minhtan.qlptclient.gui.LandmarkGUI;
import com.minhtan.qlptclient.service.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.util.List;

public class LandmarkController {

    private final LandmarkGUI view;

    public LandmarkController(LandmarkGUI view) {
        this.view = view;
        wireEvents();
        loadPage();
    }

    private void loadPage() {
        loadLandmarkTypes();
        loadLandmarks();
    }

    private void wireEvents() {
        view.getLandmarkList().getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, selected) -> {
                    if (selected == null) {
                        return;
                    }
                    populateForm(selected);
                });

        view.getRefreshButton().setOnAction(event -> loadPage());
        view.getSearchButton().setOnAction(event -> searchLandmarks());
        view.getCreateButton().setOnAction(event -> createLandmark());
        view.getUpdateButton().setOnAction(event -> updateLandmark());
        view.getDeleteButton().setOnAction(event -> deleteLandmark());
        view.getClearButton().setOnAction(event -> view.clearForm());
    }

    private void populateForm(Landmark selected) {
        String coordinatesText = "";
        if (selected.getLatitude() != null && selected.getLongitude() != null) {
            coordinatesText = selected.getLatitude().toString() + ", " + selected.getLongitude().toString();
        }
        else {
            coordinatesText = "";
        }
        view.getLandmarkIdField().setText(valueOrEmpty(selected.getLandmarkId()));
        view.getLandmarkNameField().setText(valueOrEmpty(selected.getLandmarkName()));
        view.getAddressField().setText(valueOrEmpty(selected.getAddress()));
        view.getCoordinatesField().setText(coordinatesText);
        view.getDescriptionField().setText(valueOrEmpty(selected.getDescription()));
        view.getIsActiveComboBox().getSelectionModel().select(selected.getIsActive());

        Integer typeId = selected.getLandmarkTypesId();
        if (typeId == null) {
            view.getLandmarkTypeComboBox().getSelectionModel().clearSelection();
        } else {
            view.getLandmarkTypeComboBox().getItems().stream()
                    .filter(type -> typeId.equals(type.getLandmarkTypeId()))
                    .findFirst()
                    .ifPresentOrElse(
                            type -> view.getLandmarkTypeComboBox().getSelectionModel().select(type),
                            () -> view.getLandmarkTypeComboBox().getSelectionModel().clearSelection());
        }

        view.setDetails(selected.toString());
    }

    /**
     * Tải danh sách LandmarkType để đổ vào combo box của form.
     * Cần: ApiClient.getInstance().getLandmarkTypes() -> List<LandmarkType>
     * (toàn bộ danh sách loại địa danh hiện có, không phân trang).
     */
    private void loadLandmarkTypes() {
        Task<List<LandmarkType>> task = new Task<>() {
            @Override
            protected List<LandmarkType> call() throws Exception {
                return ApiClient.getInstance().getLandmarkTypes();
            }
        };

        task.setOnSucceeded(event -> view.setLandmarkTypes(task.getValue()));
        task.setOnFailed(event -> handleTaskFailure(task, "Thất bại khi tải danh sách loại địa danh"));

        startTask(task);
    }

    /**
     * Cần: ApiClient.getInstance().getLandmarks() -> List<Landmark>
     * (toàn bộ danh sách địa danh).
     */
    private void loadLandmarks() {
        runLoadTask("Đang tải /api/landmarks...", ApiClient.getInstance()::getLandmarks);
    }

    private void searchLandmarks() {
        String keyword = view.getSearchField().getText() == null ? "" : view.getSearchField().getText().trim();
        String mode = view.getSearchModeBox().getValue();

        if ("All".equals(mode)) {
            loadLandmarks();
            return;
        }

        if (keyword.isBlank()) {
            showAlert("Nhập từ khóa", "Hãy nhập giá trị tìm kiếm trước khi tìm.");
            return;
        }

        Task<List<Landmark>> task = new Task<>() {
            @Override
            protected List<Landmark> call() throws Exception {
                return switch (mode) {
                    case "LandmarkName" -> ApiClient.getInstance().searchLandmarksByName(keyword);
                    case "LandmarkTypesId" ->
                        ApiClient.getInstance().searchLandmarksByTypeId(parseInteger(keyword, "LandmarkTypesId"));
                    case "Address" -> ApiClient.getInstance().searchLandmarksByAddress(keyword);
                    case "IsActive" -> ApiClient.getInstance().searchLandmarksByActive(parseBoolean(keyword));
                    default -> ApiClient.getInstance().getLandmarks();
                };
            }
        };

        task.setOnSucceeded(workerStateEvent -> showLandmarks(task.getValue(),
                "Tìm thấy " + task.getValue().size() + " landmark(s)"));

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tìm dữ liệu"));

        view.setStatus("Đang tìm kiếm...");
        startTask(task);
    }

    private void createLandmark() {
        Landmark landmark;
        try {
            landmark = readForm();
        } catch (IllegalArgumentException ex) {
            showAlert("Dữ liệu không hợp lệ", ex.getMessage());
            return;
        }

        Landmark toCreate = landmark;
        toCreate.setLandmarkId(null);
        // Cần: ApiClient.getInstance().createLandmark(Landmark landmark) -> void (hoặc
        // Landmark đã tạo)
        runMutationTask("Đang tạo landmark mới...", () -> ApiClient.getInstance().createLandmark(toCreate));
    }

    private void updateLandmark() {
        if (view.getLandmarkIdField().getText().isBlank()) {
            showAlert("Chưa chọn landmark", "Hãy chọn một dòng trong danh sách trước khi cập nhật.");
            return;
        }

        Landmark landmark;
        try {
            landmark = readForm();
        } catch (IllegalArgumentException ex) {
            showAlert("Dữ liệu không hợp lệ", ex.getMessage());
            return;
        }

        Integer id = Integer.valueOf(view.getLandmarkIdField().getText());
        Landmark toUpdate = landmark;
        // Cần: ApiClient.getInstance().updateLandmark(Integer id, Landmark landmark) ->
        // void (hoặc Landmark đã cập nhật)
        runMutationTask("Đang cập nhật landmark...", () -> ApiClient.getInstance().updateLandmark(id, toUpdate));
    }

    private void deleteLandmark() {
        if (view.getLandmarkIdField().getText().isBlank()) {
            showAlert("Chưa chọn landmark", "Hãy chọn một dòng trong danh sách trước khi xóa.");
            return;
        }

        Integer id = Integer.valueOf(view.getLandmarkIdField().getText());
        // Cần: ApiClient.getInstance().deleteLandmark(Integer id) -> void
        runMutationTask("Đang xóa landmark...", () -> ApiClient.getInstance().deleteLandmark(id));
    }

    private void runLoadTask(String statusText, Loader loader) {
        Task<List<Landmark>> task = new Task<>() {
            @Override
            protected List<Landmark> call() throws Exception {
                return loader.load();
            }
        };

        task.setOnSucceeded(workerStateEvent -> showLandmarks(task.getValue(),
                "Tải thành công " + task.getValue().size() + " landmark(s)"));

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
            loadLandmarks();
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, statusText));

        view.setStatus(statusText);
        startTask(task);
    }

    private void showLandmarks(List<Landmark> landmarks, String statusText) {
        view.getLandmarkItems().setAll(landmarks);
        view.setDetails(landmarks.isEmpty()
                ? "Không có dữ liệu"
                : landmarks.stream().map(Landmark::toString)
                        .reduce((left, right) -> left + System.lineSeparator() + right).orElse("Không có dữ liệu"));
        view.setStatus(statusText);
    }

    private String[] readCoordinates(String coordinatesText) {
        if (coordinatesText == null || coordinatesText.isBlank()) {
            return null;
        }
        String[] parts = coordinatesText.split(",");
        if (parts.length != 2) {
            return null;
        }
        return parts;
    }

    private Landmark readForm() {
        Landmark landmark = new Landmark();
        String landmarkIdText = textOf(view.getLandmarkIdField().getText());
        if (!landmarkIdText.isBlank()) {
            landmark.setLandmarkId(Integer.valueOf(landmarkIdText));
        }
        String[] coordinates = readCoordinates(view.getCoordinatesField().getText());
        if (coordinates != null) {
            landmark.setLatitude(parseBigDecimal(coordinates[0], "Latitude"));
            landmark.setLongitude(parseBigDecimal(coordinates[1], "Longitude"));
        } else {
            landmark.setLatitude(null);
            landmark.setLongitude(null);
        }
        landmark.setLandmarkName(textOf(view.getLandmarkNameField().getText()));
        landmark.setAddress(textOf(view.getAddressField().getText()));
        landmark.setDescription(textOf(view.getDescriptionField().getText()));
        landmark.setIsActive(view.getIsActiveComboBox().getValue());

        LandmarkType selectedType = view.getLandmarkTypeComboBox().getValue();
        if (selectedType == null) {
            throw new IllegalArgumentException("Hãy chọn Landmark Type.");
        }
        landmark.setLandmarkTypesId(selectedType.getLandmarkTypeId());
        landmark.setLandmarkType(selectedType);

        return landmark;
    }

    private BigDecimal parseBigDecimal(String text, String fieldName) {
        String value = textOf(text);
        if (value.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " phải là số hợp lệ.");
        }
    }

    private Integer parseInteger(String text, String fieldName) {
        try {
            return Integer.valueOf(text.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " phải là số nguyên hợp lệ.");
        }
    }

    private Boolean parseBoolean(String text) {
        String normalized = text.trim().toLowerCase();
        if (normalized.equals("true") || normalized.equals("hoạt động")) {
            return Boolean.TRUE;
        }
        if (normalized.equals("false") || normalized.equals("ngưng hoạt động")) {
            return Boolean.FALSE;
        }
        throw new IllegalArgumentException("IsActive phải là true/false.");
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
        Thread thread = new Thread(task, "landmark-api-client-task");
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
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @FunctionalInterface
    private interface Loader {
        List<Landmark> load() throws Exception;
    }

    @FunctionalInterface
    private interface Mutation {
        void run() throws Exception;
    }
}