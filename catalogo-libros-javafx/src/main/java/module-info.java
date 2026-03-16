module com.utez.catalogolibros.catalogolibrosjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.utez.catalogolibros.catalogolibrosjavafx to javafx.fxml;
    exports com.utez.catalogolibros.catalogolibrosjavafx;
}