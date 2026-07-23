package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.TypeRoom;
import com.minhtan.qlptclient.gui.TypeRoomGUI;
import com.minhtan.qlptclient.service.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;

public class TypeRoomController {

    private final ApiClient apiClient = new ApiClient("http://localhost:8080");
    private final TypeRoomGUI view;

    public TypeRoomController(TypeRoomGUI view) {
        this.view = view;
        wireEvents();
        loadTypeRooms();
    }

    private void wireEvents() {
        view.getTypeRoomList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            view.getTypeRoomIdField().setText(valueOrEmpty(selected.getTypeRoomId()));
            view.getNameField().setText(valueOrEmpty(selected.getTypeRoomName()));
            view.setDetails(selected.toString());
        });

        view.getRefreshButton().setOnAction(event -> loadTypeRooms());
        view.getSearchButton().setOnAction(event -> searchTypeRooms());
        view.getCreateButton().setOnAction(event -> createTypeRoom());
        view.getUpdateButton().setOnAction(event -> updateTypeRoom());
        view.getDeleteButton().setOnAction(event -> deleteTypeRoom());
        view.getClearButton().setOnAction(event -> view.clearForm());
    }

    private void loadTypeRooms() {
        runLoadTask("Đang tải /api/type-rooms...", apiClient::getTypeRooms);
    }

    private void searchTypeRooms() {
        String keyword = view.getSearchField().getText() == null ? "" : view.getSearchField().getText().trim();
        String mode = view.getSearchModeBox().getValue();

        if ("All".equals(mode)) {
            loadTypeRooms();
            return;
        }

        if (keyword.isBlank()) {
            showAlert("Nhập từ khóa", "Hãy nhập giá trị tìm kiếm trước khi tìm.");
            return;
        }

        Task<List<TypeRoom>> task = new Task<>() {
            @Override
            protected List<TypeRoom> call() throws Exception {
                return switch (mode) {
                    case "Type Room Name" -> apiClient.searchTypeRoomsByName(keyword);
                    default -> apiClient.getTypeRooms();
                };
            }
        };

        task.setOnSucceeded(workerStateEvent ->
                showTypeRooms(task.getValue(), "Tìm thấy " + task.getValue().size() + " type room(s)"));

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, "Thất bại khi tìm dữ liệu"));

        view.setStatus("Đang tìm kiếm...");
        startTask(task);
    }

    private void createTypeRoom() {
        runMutationTask("Đang tạo type room mới...", () -> {
            TypeRoom typeRoom = readForm();
            typeRoom.setTypeRoomId(null);
            apiClient.createTypeRoom(typeRoom);
        });
    }

    private void updateTypeRoom() {
        if (view.getTypeRoomIdField().getText().isBlank()) {
            showAlert("Chưa chọn type room", "Hãy chọn một dòng trong danh sách trước khi cập nhật.");
            return;
        }

        runMutationTask("Đang cập nhật type room...", () ->
                apiClient.updateTypeRoom(Integer.valueOf(view.getTypeRoomIdField().getText()), readForm()));
    }

    private void deleteTypeRoom() {
        if (view.getTypeRoomIdField().getText().isBlank()) {
            showAlert("Chưa chọn type room", "Hãy chọn một dòng trong danh sách trước khi xóa.");
            return;
        }

        runMutationTask("Đang xóa type room...", () ->
                apiClient.deleteTypeRoom(Integer.valueOf(view.getTypeRoomIdField().getText())));
    }

    private void runLoadTask(String statusText, Loader loader) {
        Task<List<TypeRoom>> task = new Task<>() {
            @Override
            protected List<TypeRoom> call() throws Exception {
                return loader.load();
            }
        };

        task.setOnSucceeded(workerStateEvent ->
                showTypeRooms(task.getValue(), "Tải thành công " + task.getValue().size() + " type room(s)"));

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
            loadTypeRooms();
        });

        task.setOnFailed(workerStateEvent -> handleTaskFailure(task, statusText));

        view.setStatus(statusText);
        startTask(task);
    }

    private void showTypeRooms(List<TypeRoom> typeRooms, String statusText) {
        view.getTypeRoomItems().setAll(typeRooms);
        view.setDetails(typeRooms.isEmpty()
                ? "Không có dữ liệu"
                : typeRooms.stream().map(TypeRoom::toString)
                        .reduce((left, right) -> left + System.lineSeparator() + right).orElse("Không có dữ liệu"));
        view.setStatus(statusText);
    }

    private TypeRoom readForm() {
        TypeRoom typeRoom = new TypeRoom();
        String typeRoomIdText = textOf(view.getTypeRoomIdField().getText());
        if (!typeRoomIdText.isBlank()) {
            typeRoom.setTypeRoomId(Integer.valueOf(typeRoomIdText));
        }
        typeRoom.setTypeRoomName(textOf(view.getNameField().getText()));
        return typeRoom;
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
        List<TypeRoom> load() throws Exception;
    }

    @FunctionalInterface
    private interface Mutation {
        void run() throws Exception;
    }
}
