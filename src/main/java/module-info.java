module gtp.ems {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    //opens gtp.ems to javafx.fxml;
    opens gtp.ems.ui.controller to javafx.fxml;

    exports gtp.ems.ui.controller;

    exports gtp.ems.model;
    exports gtp.ems.service;

}