package com.utez.catalogolibros.controller;

import com.utez.catalogolibros.model.Libro;
import com.utez.catalogolibros.repository.LibroRepository;
import com.utez.catalogolibros.util.AlertUtil;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import java.time.Year;
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

    private final LibroRepository repository = new LibroRepository();

    @FXML
    private void onGuardar() {
        try {
            String isbn = txtIsbn.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String anioStr = txtAnio.getText().trim();
            String genero = txtGenero.getText().trim();
            boolean disponible = chkDisponible.isSelected();

            // Validaciones

            if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty() || anioStr.isEmpty() || genero.isEmpty()) {
                AlertUtil.error("Todos los campos son obligatorios");
                return;
            }

            if (titulo.length() < 3) {
                AlertUtil.error("El título debe tener al menos 3 caracteres");
            }

            if (autor.length() < 3) {
                AlertUtil.error("El autor debe tener al menos 3 caracteres");
            }

            int anio;

            try {
                anio = Integer.parseInt(anioStr);
            } catch (NumberFormatException e) {
                AlertUtil.error("El año debe ser numérico");
                return;
            }

            int anioActual = Year.now().getValue();

            if (anio < 1500 || anio > anioActual) {
                AlertUtil.error("El año debe estar entre 1500 y " + anioActual);
                return;
            }

            var lista = repository.cargarLibros();

            boolean existe = lista.stream()
                    .anyMatch(l -> l.getIsbn().equalsIgnoreCase(isbn));

            if (existe) {
                AlertUtil.error("Ya existe un libro con ese ISBN");
                return;
            }

            Libro libro = new Libro(isbn, titulo, autor, anio, genero, disponible);

            lista.add(libro);
            repository.guardarLibros(lista);

            AlertUtil.info("Libro creado correctamente");

            // Cierra ventana
            Stage stage = (Stage) btnGuardar.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.error("Ocurrió un error al guardar el libro");
        }
    }
}