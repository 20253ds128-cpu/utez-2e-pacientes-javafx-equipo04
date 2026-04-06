package com.utez.catalogolibros.controller;

import com.utez.catalogolibros.model.Libro;
import com.utez.catalogolibros.repository.LibroRepository;

import com.utez.catalogolibros.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    private final LibroRepository repository = new LibroRepository();

    @FXML
    private TableView<Libro> tablaLibros;

    @FXML
    private TableColumn<Libro, String> colIsbn;

    @FXML
    private TableColumn<Libro, String> colTitulo;

    @FXML
    private TableColumn<Libro, String> colAutor;

    @FXML
    private TableColumn<Libro, String> colAnio;

    @FXML
    private TableColumn<Libro, String> colGenero;

    @FXML
    private TableColumn<Libro, String> colDisponible;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnDetalle;

    @FXML
    private Button btnExportar;

    @FXML
    public void initialize() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        cargarTabla();
    }

    @FXML
    private void onNuevo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LibroFormView.fxml"));
            Stage stage = new Stage();

            Scene scene = new Scene(loader.load(), 600, 400);
            stage.setScene(scene);
            stage.setTitle("Nuevo Libro");

            LibroFormController controller = loader.getController();
            controller.setOnGuardarCallback(this::cargarTabla);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onEditar() {
        Libro seleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            AlertUtil.error("Debes seleccionar un libro para editar");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LibroFormView.fxml"));
            Stage stage = new Stage();

            Scene scene = new Scene(loader.load(), 600, 400);
            stage.setScene(scene);
            stage.setTitle("Editar Libro");

            LibroFormController controller = loader.getController();

            controller.setLibro(seleccionado);

            controller.setOnGuardarCallback(this::cargarTabla);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cargarTabla() {
        var lista = javafx.collections.FXCollections.observableArrayList(
                repository.cargarLibros()
        );
        tablaLibros.setItems(lista);
    }


}
