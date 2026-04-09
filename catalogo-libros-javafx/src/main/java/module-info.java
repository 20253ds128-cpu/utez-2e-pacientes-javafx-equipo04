module com.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.biblioteca            to javafx.fxml;
    opens com.biblioteca.controller to javafx.fxml;
    opens com.biblioteca.model      to javafx.base;
    opens com.biblioteca.services   to javafx.base;

    exports com.biblioteca;
    exports com.biblioteca.controller;
    exports com.biblioteca.model;
    exports com.biblioteca.services;
}
