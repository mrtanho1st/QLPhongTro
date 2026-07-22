package com.minhtan.qlptclient.gui;

import com.minhtan.qlptclient.controller.RoomMediaDialogController;
import com.minhtan.qlptclient.entity.Room;
import com.minhtan.qlptclient.entity.RoomMedia;
import com.minhtan.qlptclient.service.ApiClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class RoomMediaDialog {

    private final Stage stage;
    private final ObservableList<RoomMedia> mediaItems = FXCollections.observableArrayList();
    private final TableView<RoomMedia> mediaTable = new TableView<>(mediaItems);
    private final TextField urlField = new TextField();
    private final ComboBox<String> typeBox = new ComboBox<>(FXCollections.observableArrayList("Image", "Video"));
    private final TextField sortOrderField = new TextField();
    private final ImageView previewImage = new ImageView();
    private final Label previewLabel = new Label("Chưa có ảnh/video");
    private final Button addButton = new Button("Thêm");
    private final Button deleteButton = new Button("Xóa");
    private final Button saveButton = new Button("Lưu");
    private final Button downloadButton = new Button("Tải về");
    private final RoomMediaDialogController controller;

    public RoomMediaDialog(Room room, ApiClient apiClient) {
        this(room, apiClient, null);
    }

    public RoomMediaDialog(Room room, ApiClient apiClient, String initialType) {
        this.stage = new Stage();
        this.stage.initModality(Modality.APPLICATION_MODAL);
        this.stage.setTitle("Media phòng " + (room != null ? room.getRoomId() : ""));
        this.controller = new RoomMediaDialogController(this, apiClient, room);
        buildUi();
        controller.initialize();
        controller.loadExistingMedia();
        if (initialType != null) {
            controller.addMediaFromDevice(initialType);
        }
    }

    public void show() {
        stage.show();
    }

    private void buildUi() {

        // --------------------- TABLE ---------------------

        Label tableTitle = new Label("Danh sách Media");
        tableTitle.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        mediaTable.setPlaceholder(new Label("Chưa có media"));
        mediaTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        VBox.setVgrow(mediaTable, Priority.ALWAYS);

        VBox tableCard = new VBox(10, tableTitle, mediaTable);
        tableCard.setPadding(new Insets(15));
        tableCard.setStyle("""
                -fx-background-color:white;
                -fx-background-radius:10;
                -fx-border-radius:10;
                -fx-border-color:#d1d5db;
                """);

        // --------------------- PREVIEW ---------------------

        Label previewTitle = new Label("Xem trước");
        previewTitle.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        previewImage.setPreserveRatio(true);
        previewImage.setSmooth(true);
        previewImage.setFitWidth(450);
        previewImage.setFitHeight(350);

        StackPane previewContainer = new StackPane(previewImage);
        previewContainer.setPadding(new Insets(10));
        previewContainer.setMinHeight(300);

        previewContainer.setStyle("""
                -fx-background-color:#fafafa;
                -fx-background-radius:8;
                -fx-border-radius:8;
                -fx-border-color:#d1d5db;
                """);

        VBox.setVgrow(previewContainer, Priority.ALWAYS);

        previewLabel.setWrapText(true);

        VBox previewCard = new VBox(
                10,
                previewTitle,
                previewContainer,
                previewLabel);

        previewCard.setPadding(new Insets(15));

        previewCard.setStyle("""
                -fx-background-color:white;
                -fx-background-radius:10;
                -fx-border-radius:10;
                -fx-border-color:#d1d5db;
                """);

        // --------------------- SPLIT ---------------------

        SplitPane splitPane = new SplitPane();

        splitPane.getItems().addAll(tableCard, previewCard);
        splitPane.setDividerPositions(0.6);

        // --------------------- FORM ---------------------

        GridPane form = new GridPane();

        form.setHgap(15);
        form.setVgap(15);

        form.add(new Label("Loại"), 0, 0);
        form.add(typeBox, 1, 0);

        form.add(new Label("Thứ tự"), 0, 1);
        form.add(sortOrderField, 1, 1);

        form.add(new Label("Đường dẫn"), 0, 2);
        form.add(urlField, 1, 2);

        GridPane.setHgrow(urlField, Priority.ALWAYS);

        typeBox.setMaxWidth(Double.MAX_VALUE);
        sortOrderField.setMaxWidth(Double.MAX_VALUE);
        urlField.setMaxWidth(Double.MAX_VALUE);

        form.setPadding(new Insets(15));

        form.setStyle("""
                -fx-background-color:white;
                -fx-background-radius:10;
                -fx-border-radius:10;
                -fx-border-color:#d1d5db;
                """);

        // --------------------- BUTTON ---------------------

        addButton.setPrefWidth(100);
        deleteButton.setPrefWidth(100);
        saveButton.setPrefWidth(100);
        downloadButton.setPrefWidth(100);

        addButton.setStyle("""
                -fx-background-color:#16a34a;
                -fx-text-fill:white;
                -fx-font-weight:bold;
                """);

        deleteButton.setStyle("""
                -fx-background-color:#dc2626;
                -fx-text-fill:white;
                -fx-font-weight:bold;
                """);

        saveButton.setStyle("""
                -fx-background-color:#2563eb;
                -fx-text-fill:white;
                -fx-font-weight:bold;
                """);

        downloadButton.setStyle("""
                -fx-background-color:#0ea5e9;
                -fx-text-fill:white;
                -fx-font-weight:bold;
                """);


        HBox buttonBar = new HBox(
                15,
                addButton,
                deleteButton,
                saveButton,
                downloadButton);

        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(15));

        buttonBar.setStyle("""
                -fx-background-color:white;
                -fx-background-radius:10;
                -fx-border-radius:10;
                -fx-border-color:#d1d5db;
                """);

        // --------------------- BOTTOM ---------------------

        VBox bottomPane = new VBox(15, form, buttonBar);

        // --------------------- ROOT ---------------------

        BorderPane root = new BorderPane();

        root.setCenter(splitPane);
        root.setBottom(bottomPane);

        BorderPane.setMargin(splitPane, new Insets(15, 15, 10, 15));
        BorderPane.setMargin(bottomPane, new Insets(0, 15, 15, 15));

        root.setStyle("""
                -fx-background-color:#f3f4f6;
                """);

        // --------------------- WINDOW ---------------------

        stage.setMinWidth(1100);
        stage.setMinHeight(700);

        stage.setScene(new Scene(root, 1300, 800));
    }

    public Stage getStage() {
        return stage;
    }

    public ObservableList<RoomMedia> getMediaItems() {
        return mediaItems;
    }

    public TableView<RoomMedia> getMediaTable() {
        return mediaTable;
    }

    public TextField getUrlField() {
        return urlField;
    }

    public ComboBox<String> getTypeBox() {
        return typeBox;
    }

    public TextField getSortOrderField() {
        return sortOrderField;
    }

    public ImageView getPreviewImage() {
        return previewImage;
    }

    public Label getPreviewLabel() {
        return previewLabel;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getDownloadButton() {
        return downloadButton;
    }

    public RoomMediaDialogController getController() {
        return controller;
    }

}
