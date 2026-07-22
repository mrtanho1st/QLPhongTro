module com.minhtan.qlptclient {

    requires javafx.controls;
    requires javafx.fxml;

    requires java.net.http;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.minhtan.qlptclient to javafx.fxml;
    opens com.minhtan.qlptclient.gui to javafx.fxml;
    opens com.minhtan.qlptclient.controller to javafx.fxml;
    opens com.minhtan.qlptclient.entity to com.fasterxml.jackson.databind;

    exports com.minhtan.qlptclient;
    exports com.minhtan.qlptclient.gui;
    exports com.minhtan.qlptclient.controller;

}