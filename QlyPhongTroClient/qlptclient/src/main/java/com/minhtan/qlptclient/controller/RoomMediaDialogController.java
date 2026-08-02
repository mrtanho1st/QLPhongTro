package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.Room;
import com.minhtan.qlptclient.entity.RoomMedia;
import com.minhtan.qlptclient.gui.RoomMediaDialog;
import com.minhtan.qlptclient.service.ApiClient;
import com.minhtan.qlptclient.service.MethodAmenity;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class RoomMediaDialogController {

    private final RoomMediaDialog view;
    private final ApiClient apiClient;
    private final Room room;
    private final ObservableList<RoomMedia> mediaItems;
    private final List<RoomMedia> originalMedia = new ArrayList<>();

    public RoomMediaDialogController(RoomMediaDialog view, ApiClient apiClient, Room room) {
        this.view = view;
        this.apiClient = apiClient;
        this.room = room;
        this.mediaItems = view.getMediaItems();
    }

    public void initialize() {
        TableView<RoomMedia> mediaTable = view.getMediaTable();
        mediaTable.setPlaceholder(new Label("Chưa có media cho phòng này"));
        mediaTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mediaTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            fillForm(selected);
            showPreview(selected);
        });

        TableColumn<RoomMedia, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                cell -> new javafx.beans.property.ReadOnlyObjectWrapper<>(cell.getValue().getMediaId()));
        idColumn.setPrefWidth(70);

        TableColumn<RoomMedia, String> typeColumn = new TableColumn<>("Loại");
        typeColumn.setCellValueFactory(
                cell -> new javafx.beans.property.ReadOnlyStringWrapper(mediaTypeText(cell.getValue())));
        typeColumn.setPrefWidth(90);

        TableColumn<RoomMedia, String> urlColumn = new TableColumn<>("URL");
        urlColumn
                .setCellValueFactory(cell -> new javafx.beans.property.ReadOnlyStringWrapper(cell.getValue().getUrl()));
        urlColumn.setPrefWidth(260);

        TableColumn<RoomMedia, Integer> sortColumn = new TableColumn<>("Thứ tự");
        sortColumn.setCellValueFactory(
                cell -> new javafx.beans.property.ReadOnlyObjectWrapper<>(cell.getValue().getSortOrder()));
        sortColumn.setPrefWidth(80);

        mediaTable.getColumns().setAll(List.of(idColumn, typeColumn, urlColumn, sortColumn));

        view.getTypeBox().getSelectionModel().selectFirst();
        view.getAddButton().setOnAction(event -> addMediaFromDevice(null));
        view.getDeleteButton().setOnAction(event -> deleteSelectedMedia());
        view.getSaveButton().setOnAction(event -> saveMedia());
        view.getDownloadButton().setOnAction(event -> downloadSelectedMedia());
    }

    public void loadExistingMedia() {
        if (room == null || room.getRoomId() == null) {
            return;
        }

        Task<List<RoomMedia>> task = new Task<>() {
            @Override
            protected List<RoomMedia> call() throws Exception {
                return apiClient.searchRoomMediaByRoomId(room.getRoomId());
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            List<RoomMedia> loadedMedia = task.getValue();
            mediaItems.setAll(loadedMedia);
            originalMedia.clear();
            originalMedia.addAll(loadedMedia);
            if (!mediaItems.isEmpty()) {
                view.getMediaTable().getSelectionModel().selectFirst();
            }
        });

        task.setOnFailed(workerStateEvent -> {
            Throwable error = task.getException();
            showAlert(" lỗi", error == null ? "Không thể tải media" : error.getMessage());
        });

        Thread thread = new Thread(task, "room-media-load");
        thread.setDaemon(true);
        thread.start();
    }

    public void addMediaFromDevice(String initialType) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Chọn file media");
        if (initialType != null) {
            chooser.getExtensionFilters().addAll(
                    initialType.equalsIgnoreCase("Video")
                            ? new FileChooser.ExtensionFilter("Video MP4", "*.mp4", "*.m4v")
                            : new FileChooser.ExtensionFilter("Ảnh", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp",
                                    "*.webp"));
        } else {
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Ảnh", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp", "*.webp"),
                    new FileChooser.ExtensionFilter("Video MP4", "*.mp4", "*.m4v"));
        }

        List<java.io.File> selectedFiles = chooser.showOpenMultipleDialog(view.getStage());
        if (selectedFiles == null || selectedFiles.isEmpty()) {
            return;
        }

        RoomMedia lastAddedMedia = null;
        for (java.io.File selectedFile : selectedFiles) {
            String type = initialType != null ? initialType
                    : (selectedFile.getName().toLowerCase().endsWith(".mp4")
                            || selectedFile.getName().toLowerCase().endsWith(".m4v") ? "Video" : "Image");
            RoomMedia newMedia = new RoomMedia();
            newMedia.setRoomId(room == null ? null : room.getRoomId());
            newMedia.setMediaType(type.equalsIgnoreCase("Video") ? (byte) 2 : (byte) 1);
            newMedia.setUrl(selectedFile.getAbsolutePath());
            newMedia.setSortOrder(mediaItems.size() + 1);
            mediaItems.add(newMedia);
            lastAddedMedia = newMedia;
        }

        if (lastAddedMedia != null) {
            view.getMediaTable().getSelectionModel().select(lastAddedMedia);
            fillForm(lastAddedMedia);
            showPreview(lastAddedMedia);
        }
    }

    public void deleteSelectedMedia() {
        RoomMedia selected = view.getMediaTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        mediaItems.remove(selected);
        if (!mediaItems.isEmpty()) {
            view.getMediaTable().getSelectionModel().selectFirst();
        } else {
            clearForm();
        }
    }

    public void saveMedia() {
        if (room == null || room.getRoomId() == null) {
            showAlert("Chưa chọn phòng", "Hãy chọn phòng trước khi lưu media.");
            return;
        }

        for (RoomMedia media : mediaItems) {
            media.setRoomId(room.getRoomId());
            if (media.getMediaType() == null) {
                media.setMediaType((byte) 1);
            }
            if (media.getUrl() == null || media.getUrl().isBlank()) {
                continue;
            }
            try {
                if (media.getMediaId() == null) {
                    apiClient.createRoomMedia(media);
                } else {
                    apiClient.updateRoomMedia(media.getMediaId(), media);
                }
            } catch (Exception exception) {
                showAlert(" lỗi lưu media", exception.getMessage());
                return;
            }
        }

        for (RoomMedia media : originalMedia) {
            if (media.getMediaId() != null && !mediaItems.contains(media)) {
                try {
                    apiClient.deleteRoomMedia(media.getMediaId());
                } catch (Exception exception) {
                    showAlert(" lỗi xóa media", exception.getMessage());
                    return;
                }
            }
        }

        originalMedia.clear();
        originalMedia.addAll(new ArrayList<>(mediaItems));
        showAlert("Thành công", "Đã lưu media cho phòng.");
        view.getStage().close();
    }

    public void downloadSelectedMedia() {
        RoomMedia selected = view.getMediaTable().getSelectionModel().getSelectedItem();
        if (selected == null || selected.getUrl() == null || selected.getUrl().isBlank()) {
            showAlert("Chưa chọn media", "Hãy chọn một media trước khi tải về.");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Lưu file media");
        chooser.setInitialFileName(fileNameFromUrl(selected.getUrl()));
        java.io.File destinationFile = chooser.showSaveDialog(view.getStage());
        if (destinationFile == null) {
            return;
        }

        try {
            URI uri = URI.create(MethodAmenity.resolveMediaUrl(selected.getUrl()));
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                Files.copy(Path.of(uri), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                try (InputStream inputStream = uri.toURL().openStream()) {
                    Files.copy(inputStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            showAlert("Thành công", "Đã tải về file thành công.");
        } catch (IOException exception) {
            showAlert(" lỗi tải về", exception.getMessage());
        }
    }

    private void fillForm(RoomMedia selected) {
        if (selected == null) {
            clearForm();
            return;
        }
        view.getTypeBox().setValue(mediaTypeText(selected));
        view.getUrlField().setText(selected.getUrl());
        view.getSortOrderField()
                .setText(selected.getSortOrder() == null ? "" : String.valueOf(selected.getSortOrder()));
    }

    private void clearForm() {
        view.getTypeBox().getSelectionModel().clearSelection();
        view.getUrlField().clear();
        view.getSortOrderField().clear();
        view.getPreviewImage().setImage(null);
        view.getPreviewLabel().setText("Chưa có ảnh/video");
    }

    private void showPreview(RoomMedia selected) {
        if (selected == null || selected.getUrl() == null || selected.getUrl().isBlank()) {
            clearForm();
            return;
        }

        String url = selected.getUrl();
        if (isImageUrl(url)) {
            try {
                view.getPreviewImage().setImage(new Image(MethodAmenity.resolveMediaUrl(url)));
                view.getPreviewLabel().setText("Ảnh");
            } catch (Exception exception) {
                view.getPreviewLabel().setText("Không thể xem trước ảnh: " + exception.getMessage());
            }
        } else {
            view.getPreviewImage().setImage(null);
            view.getPreviewLabel().setText("Video: " + fileNameFromUrl(url));
        }
    }

    private boolean isImageUrl(String url) {
        String normalized = url.toLowerCase();
        return normalized.endsWith(".jpg") || normalized.endsWith(".jpeg") || normalized.endsWith(".png")
                || normalized.endsWith(".gif") || normalized.endsWith(".bmp") || normalized.endsWith(".webp");
    }

    private String fileNameFromUrl(String url) {
        try {
            URI uri = URI.create(url);
            String path = uri.getPath();
            if (path == null || path.isBlank()) {
                return "media-file";
            }
            int index = path.lastIndexOf('/');
            return index >= 0 && index < path.length() - 1 ? path.substring(index + 1) : path;
        } catch (Exception exception) {
            return "media-file";
        }
    }

    private String mediaTypeText(RoomMedia media) {
        if (media == null) {
            return "Image";
        }
        return media.getMediaType() != null && media.getMediaType() == 2 ? "Video" : "Image";
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
