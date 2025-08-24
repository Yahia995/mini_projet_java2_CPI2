module com.isimm.gestionstock {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    // Database connectivity
    requires java.sql;
    requires java.desktop;

    // Export packages for FXML access
    exports com.isimm.gestionstock;
    exports com.isimm.gestionstock.controller;
    exports com.isimm.gestionstock.model;
    exports com.isimm.gestionstock.dao;
    exports com.isimm.gestionstock.util;

    // Open packages for reflection (needed for FXML and database frameworks)
    opens com.isimm.gestionstock.controller to javafx.fxml;
    opens com.isimm.gestionstock.model to javafx.base;
}
