package com.minhtan.qlptclient.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HomeDashboardPane extends VBox {

    private final Label buildingCountLabel = new Label("-");
    private final Label availableRoomCountLabel = new Label("-");
    private final Label lockedRoomCountLabel = new Label("-");
    private final Label statusLabel = new Label("Sẵn sàng tải thống kê");
    private final Button refreshButton = new Button("Tải lại");

    public HomeDashboardPane() {
        buildUi();
    }

    private void buildUi() {
        setSpacing(18);
        setPadding(new Insets(4));

        Label heading = new Label("Tổng quan hệ thống");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        statusLabel.setStyle("-fx-text-fill: #64748b;");
        refreshButton.setStyle(primaryButtonStyle("#2563eb"));

        HBox header = new HBox(12, heading, spacer(), refreshButton);
        header.setAlignment(Pos.CENTER_LEFT);

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(14);
        statsGrid.setVgap(14);
        statsGrid.add(statCard("Số lượng tòa nhà", buildingCountLabel, "#2563eb"), 0, 0);
        statsGrid.add(statCard("Phòng trống", availableRoomCountLabel, "#0f766e"), 1, 0);
        statsGrid.add(statCard("Phòng đã khoá", lockedRoomCountLabel, "#dc2626"), 2, 0);

        getChildren().addAll(header, statusLabel, statsGrid);
    }

    private VBox statCard(String title, Label valueLabel, String accentColor) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 13px; -fx-font-weight: bold;");

        valueLabel.setStyle("-fx-text-fill: " + accentColor + "; -fx-font-size: 34px; -fx-font-weight: bold;");

        VBox card = new VBox(10, titleLabel, valueLabel);
        card.setPadding(new Insets(18));
        card.setMinWidth(220);
        card.setStyle("-fx-background-color: white; -fx-border-color: #d8dee9; "
                + "-fx-border-radius: 8; -fx-background-radius: 8;");
        return card;
    }

    private HBox spacer() {
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private String primaryButtonStyle(String color) {
        return "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 6; -fx-padding: 8 14;";
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public void setBuildingCount(int buildingCount) {
        buildingCountLabel.setText(String.valueOf(buildingCount));
    }

    public void setAvailableRoomCount(long availableRoomCount) {
        availableRoomCountLabel.setText(String.valueOf(availableRoomCount));
    }

    public void setLockedRoomCount(long lockedRoomCount) {
        lockedRoomCountLabel.setText(String.valueOf(lockedRoomCount));
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }
}
