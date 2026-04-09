package com.biblioteca.controller;

import com.biblioteca.model.Libro;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class DetalleController {
    @FXML
    private Label lblIsbn;
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblAutor;
    @FXML
    private Label lblAnio;
    @FXML
    private Label lblGenero;
    @FXML
    private Label lblDisponible;
    @FXML
    private Button btnCancelar;

    public void  setLibro(Libro libro){
        lblISBN.setText(libro.getISBN());
        lblTitulo.setText(libro.getTitulo());
        lblAutor.setText(libro.getAutor());
        lblAnio.setAnio(String.valueOf(libro.getAnio()));
        lblGenero.setGenero(libro.getGenero());
        lblDisponible.setText(libro.isDisponible() ? "Disponible" : "No disponible");

        if ()
    }
}
