// Campos del formulario
package com.biblioteca.controller;

import com.biblioteca.model.Libro;
import com.biblioteca.services.LibroServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

    public class FormController {

        @FXML private Label lblTituloPantalla;
        @FXML private TextField txtIsbn;
        @FXML private TextField txtTitulo;
        @FXML private TextField txtAutor;
        @FXML private TextField txtAnio;
        @FXML private TextField txtGenero;
        @FXML private CheckBox chkDisponible;
        @FXML private Button btnGuardar;
        @FXML private Button btnCancelar;
        @FXML private Label lblError;

        private LibroServices servicio;
        private MainController mainCtrl;
        private Libro libroEditando;
        private String isbnOriginal;

        // inicializa el formulario
        public void inicializar(Libro libro, LibroServices servicio, MainController mainCtrl) {
            this.servicio = servicio;
            this.mainCtrl = mainCtrl;
            this.libroEditando = libro;

            if (libro != null) {
                lblTituloPantalla.setText("Editar libro");
                isbnOriginal = libro.getIsbn();
                txtIsbn.setText(libro.getIsbn());
                txtTitulo.setText(libro.getTitulo());
                txtAutor.setText(libro.getAutor());
                txtAnio.setText(String.valueOf(libro.getAnio()));
                txtGenero.setText(libro.getGenero());
                chkDisponible.setSelected(libro.isDisponible());
            } else {
                lblTituloPantalla.setText("Registrar libro");
                chkDisponible.setSelected(true);
            }

            lblError.setText("");
        }

        @FXML
        private void onGuardar() {
            lblError.setText("");

            Libro libro = construirLibroDesdeFormulario();
            if (libro == null) return;

            try {
                if (libroEditando == null) {
                    servicio.agregar(libro);
                } else {
                    servicio.actualizar(isbnOriginal, libro);
                }

                mainCtrl.cargarDatos();
                cerrarVentana();

            } catch (IllegalArgumentException ex) {
                lblError.setText(ex.getMessage());
            } catch (Exception ex) {
                lblError.setText("error: " + ex.getMessage());
            }
        }

        @FXML
        private void onCancelar() {
            cerrarVentana();
        }

        // crea objeto libro desde el form
        private Libro construirLibroDesdeFormulario() {
            String isbn = txtIsbn.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String anioTxt = txtAnio.getText().trim();
            String genero = txtGenero.getText().trim();

            int anio;
            try {
                anio = Integer.parseInt(anioTxt);
            } catch (NumberFormatException e) {
                lblError.setText("el año debe ser numero");
                txtAnio.requestFocus();
                return null;
            }

            return new Libro(isbn, titulo, autor, anio, genero, chkDisponible.isSelected());
        }

        private void cerrarVentana() {
            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        }
    }
