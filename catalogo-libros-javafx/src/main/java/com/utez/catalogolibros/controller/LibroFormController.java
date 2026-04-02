package com.utez.catalogolibros.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LibroFormController {

    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAutor;
    @FXML
    private TextField txtAnio;
    @FXML
    private TextField txtGenero;
    @FXML
    private CheckBox chkDisponible;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
}
