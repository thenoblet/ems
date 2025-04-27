module gtp.ems {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.logging;

    //opens gtp.ems to javafx.fxml;
    opens gtp.ems.ui.controller to javafx.fxml;

    exports gtp.ems.ui.controller;

    exports gtp.ems.model;
    exports gtp.ems.service;
    exports gtp.ems.ui.util;
    opens gtp.ems.ui.util to javafx.fxml;

}