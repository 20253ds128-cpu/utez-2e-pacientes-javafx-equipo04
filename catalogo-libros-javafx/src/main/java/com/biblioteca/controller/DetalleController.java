package com.biblioteca.controller;

import javafx.fxml.FXML;

import java.awt.*;
import java.lang.classfile.Label;


public class DetalleController {

    @FXML
    private Label lblISBN;
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblAutor;
    @FXML
    private Label lblAnio;
    @FXML
    private  Label lblGenero;
    @FXML
    private  Label lblDisponibilidad;
    @FXML
    private Button btnRegresar;

    public void setLibro(Libro libro){
        lblISBN.setText(libro.getISBN);
    }

}
