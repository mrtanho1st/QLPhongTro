package com.minhtan.qlptclient;

import com.minhtan.qlptclient.controller.MainController;
import com.minhtan.qlptclient.gui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        MainView view = new MainView();
        MainController mainController = new MainController(view);
        mainController.getSearchRoomController().setHostServices(getHostServices());
        Scene scene = new Scene(view, 1200, 600);

        stage.setTitle("Quản Lý Phòng Trọ");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
