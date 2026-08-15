package com.minhtan.qlptclient.controller;

import com.minhtan.qlptclient.entity.Building;
import com.minhtan.qlptclient.entity.Commission;
import com.minhtan.qlptclient.gui.CommissionGUI;
import com.minhtan.qlptclient.service.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommissionController {

    private final CommissionGUI view;
    private List<Commission> allCommissions = new ArrayList<>();
    private List<Building> allBuildings = new ArrayList<>();

    public CommissionController(CommissionGUI view) {
        this.view = view;
        wireEvents();
    }

    public void refreshData() {
        loadInitialData();
    }

    private void wireEvents() {
        view.getCreateButton().setOnAction(event -> handleCreate());
        view.getUpdateButton().setOnAction(event -> handleUpdate());
        view.getDeleteButton().setOnAction(event -> handleDelete());
        view.getClearButton().setOnAction(event -> handleClear());
        view.getSearchButton().setOnAction(event -> handleSearch());
        view.getRefreshButton().setOnAction(event -> loadInitialData());
        view.getCommissionList().getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> onSelectionChanged(newVal));
    }

    private void loadInitialData() {
        view.setStatus("Đang tải dữ liệu...");
        loadBuildings();
    }

    private void loadBuildings() {
        Task<List<Building>> task = new Task<>() {
            @Override
            protected List<Building> call() throws Exception {
                return ApiClient.getInstance().getBuildings();
            }
        };

        task.setOnSucceeded(event -> {
            allBuildings = task.getValue();
            Platform.runLater(() -> {
                view.getBuildingComboBox().getItems().setAll(allBuildings);
                loadCommissions();
            });
        });

        task.setOnFailed(event -> {
            showAlert("Lỗi", "Không tải được danh sách tòa nhà: " + buildFailureMessage(task));
            view.setStatus("Lỗi tải dữ liệu");
        });

        startTask(task);
    }

    private void loadCommissions() {
        Task<List<Commission>> task = new Task<>() {
            @Override
            protected List<Commission> call() throws Exception {
                return ApiClient.getInstance().getCommissions();
            }
        };

        task.setOnSucceeded(event -> {
            allCommissions = task.getValue();
            Platform.runLater(() -> {
                view.getCommissionItems().setAll(allCommissions);
                view.setStatus("Tải xong " + allCommissions.size() + " bản ghi");
            });
        });

        task.setOnFailed(event -> {
            showAlert("Lỗi", "Không tải được danh sách hoa hồng: " + buildFailureMessage(task));
            view.setStatus("Lỗi tải dữ liệu");
        });

        startTask(task);
    }

    private void handleCreate() {
        Commission commission = new Commission();

        Building selectedBuilding = view.getBuildingComboBox().getSelectionModel().getSelectedItem();
        if (selectedBuilding == null) {
            showAlert("Cảnh báo", "Vui lòng chọn tòa nhà");
            return;
        }

        String contractMonthText = view.getContractMonthField().getText().trim();
        String commissionPercentText = view.getCommissionPercentField().getText().trim();
        String depositText = view.getDepositField().getText().trim();

        if (contractMonthText.isEmpty() || commissionPercentText.isEmpty() || depositText.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng điền đầy đủ thông tin");
            return;
        }

        try {
            commission.setBuildingId(selectedBuilding.getBuildingId());
            commission.setBuilding(selectedBuilding);
            commission.setContractMonth(Integer.parseInt(contractMonthText));
            commission.setCommissionPercent(new BigDecimal(commissionPercentText));
            commission.setDeposit(new BigDecimal(depositText));

            Task<Commission> task = new Task<Commission>() {
                @Override
                protected Commission call() throws Exception {
                    return ApiClient.getInstance().createCommission(commission);
                }
            };

            task.setOnSucceeded(event -> {
                Commission created = task.getValue();
                allCommissions.add(created);
                Platform.runLater(() -> {
                    view.getCommissionItems().setAll(allCommissions);
                    view.clearForm();
                    view.setStatus("Tạo thành công");
                });
            });

            task.setOnFailed(event -> {
                showAlert("Lỗi", "Lỗi khi tạo: " + buildFailureMessage(task));
                view.setStatus("Lỗi khi tạo");
            });

            startTask(task);
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Giá trị số không hợp lệ: " + e.getMessage());
        }
    }

    private void handleUpdate() {
        Commission selected = view.getCommissionList().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Cảnh báo", "Vui lòng chọn bản ghi để cập nhật");
            return;
        }

        Building selectedBuilding = view.getBuildingComboBox().getSelectionModel().getSelectedItem();
        if (selectedBuilding == null) {
            showAlert("Cảnh báo", "Vui lòng chọn tòa nhà");
            return;
        }

        String contractMonthText = view.getContractMonthField().getText().trim();
        String commissionPercentText = view.getCommissionPercentField().getText().trim();
        String depositText = view.getDepositField().getText().trim();

        if (contractMonthText.isEmpty() || commissionPercentText.isEmpty() || depositText.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng điền đầy đủ thông tin");
            return;
        }

        try {
            selected.setBuildingId(selectedBuilding.getBuildingId());
            selected.setBuilding(selectedBuilding);
            selected.setContractMonth(Integer.parseInt(contractMonthText));
            selected.setCommissionPercent(new BigDecimal(commissionPercentText));
            selected.setDeposit(new BigDecimal(depositText));

            Task<Commission> task = new Task<Commission>() {
                @Override
                protected Commission call() throws Exception {
                    return ApiClient.getInstance().updateCommission(selected.getCommissionId(), selected);
                }
            };

            task.setOnSucceeded(event -> {
                Platform.runLater(() -> {
                    view.getCommissionList().refresh();
                    view.clearForm();
                    view.setStatus("Cập nhật thành công");
                });
            });

            task.setOnFailed(event -> {
                showAlert("Lỗi", "Lỗi khi cập nhật: " + buildFailureMessage(task));
                view.setStatus("Lỗi khi cập nhật");
            });

            startTask(task);
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Giá trị số không hợp lệ: " + e.getMessage());
        }
    }

    private void handleDelete() {
        Commission selected = view.getCommissionList().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Cảnh báo", "Vui lòng chọn bản ghi để xóa");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận");
        confirm.setHeaderText("Xóa hoa hồng");
        confirm.setContentText("Bạn có chắc chắn muốn xóa bản ghi này không?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    ApiClient.getInstance().deleteCommission(selected.getCommissionId());
                    return null;
                }
            };

            task.setOnSucceeded(event -> {
                allCommissions.remove(selected);
                Platform.runLater(() -> {
                    view.getCommissionItems().setAll(allCommissions);
                    view.clearForm();
                    view.setStatus("Xóa thành công");
                });
            });

            task.setOnFailed(event -> {
                showAlert("Lỗi", "Lỗi khi xóa: " + buildFailureMessage(task));
                view.setStatus("Lỗi khi xóa");
            });

            startTask(task);
        }
    }

    private void handleClear() {
        view.clearForm();
        view.setStatus("Đã làm mới");
    }

    private void handleSearch() {
        String searchMode = view.getSearchModeBox().getSelectionModel().getSelectedItem();
        String searchText = view.getSearchField().getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            view.getCommissionItems().setAll(allCommissions);
            view.setStatus("Đã xóa bộ lọc tìm kiếm");
            return;
        }

        List<Commission> filtered = new ArrayList<>();
        for (Commission commission : allCommissions) {
            boolean matches = false;

            if ("All".equals(searchMode) || "Building".equals(searchMode)) {
                Building building = commission.getBuilding();
                String buildingAddress = building == null || building.getTrueAddress() == null ? ""
                        : building.getTrueAddress();
                if (buildingAddress.toLowerCase().contains(searchText)) {
                    matches = true;
                }
            }

            if ("All".equals(searchMode) || "ContractMonth".equals(searchMode)) {
                Integer month = commission.getContractMonth();
                if (month != null && month.toString().contains(searchText)) {
                    matches = true;
                }
            }

            if (matches) {
                filtered.add(commission);
            }
        }

        view.getCommissionItems().setAll(filtered);
        view.setStatus("Tìm được " + filtered.size() + " kết quả");
    }

    private void onSelectionChanged(Commission selected) {
        if (selected == null) {
            view.clearForm();
            return;
        }

        view.getCommissionIdField().setText(String.valueOf(selected.getCommissionId()));

        Building building = selected.getBuilding();
        if (building != null) {
            view.getBuildingComboBox().getSelectionModel().select(building);
        }

        if (selected.getContractMonth() != null) {
            view.getContractMonthField().setText(selected.getContractMonth().toString());
        }
        if (selected.getCommissionPercent() != null) {
            view.getCommissionPercentField().setText(selected.getCommissionPercent().toPlainString());
        }
        if (selected.getDeposit() != null) {
            view.getDepositField().setText(selected.getDeposit().toPlainString());
        }

        StringBuilder details = new StringBuilder();
        details.append("ID: ").append(selected.getCommissionId()).append("\n");
        details.append("Tòa nhà: ").append(building == null ? "" : building.getTrueAddress()).append("\n");
        details.append("Tháng: ").append(selected.getContractMonth()).append("\n");
        details.append("% HH: ").append(selected.getCommissionPercent()).append("\n");
        details.append("Cọc: ").append(selected.getDeposit()).append("\n");
        view.setDetails(details.toString());
    }

    private void startTask(Task<?> task) {
        Thread thread = new Thread(task, "commission-task");
        thread.setDaemon(true);
        thread.start();
    }

    private String buildFailureMessage(Task<?> task) {
        Throwable error = task.getException();
        return error == null ? "Lỗi không xác định" : error.getMessage();
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
