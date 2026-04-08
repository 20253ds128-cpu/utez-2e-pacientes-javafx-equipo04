package com.biblioteca.controller;

import com.biblioteca.MainApp;
import com.biblioteca.model.Libro;
import com.biblioteca.service.LibroService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador principal de la vista
 * aquí se maneja la tabla y las acciones de los botones
 */
public class MainController implements Initializable {

    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colAutor;
    @FXML private TableColumn<Libro, Integer> colAnio;
    @FXML private TableColumn<Libro, String> colGenero;
    @FXML private TableColumn<Libro, Boolean> colDisponible;

    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnDetalle;
    @FXML private Button btnExportar;
    @FXML private Label lblEstado;

    private LibroService servicio;
    private ObservableList<Libro> datos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicio = MainApp.getLibroService();
        datos = FXCollections.observableArrayList();

        configurarColumnas();
        cargarDatos();
        configurarBotones();
    }

    // configura las columnas de la tabla
    private void configurarColumnas() {
        colIsbn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIsbn()));
        colTitulo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitulo()));
        colAutor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAutor()));
        colAnio.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getAnio()).asObject());
        colGenero.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGenero()));
        colDisponible.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isDisponible()));

        colDisponible.setCellFactory(CheckBoxTableCell.forTableColumn(colDisponible));

        tablaLibros.setItems(datos);
        tablaLibros.setPlaceholder(new Label("El catálogo está vacío. Haga clic en \"Nuevo\" para agregar un libro."));
    }

    // carga o actualiza los datos en la tabla
    public void cargarDatos() {
        datos.setAll(servicio.obtenerTodos());
        actualizarEstado();
    }

    // actualiza el texto de estado abajo
    private void actualizarEstado() {
        int total = datos.size();
        long disponibles = datos.stream().filter(Libro::isDisponible).count();
        lblEstado.setText("Total: " + total + " libro(s)  |  Disponibles: " + disponibles);
    }

    // activa o desactiva botones según la selección
    private void configurarBotones() {
        btnEditar.disableProperty().bind(tablaLibros.getSelectionModel().selectedItemProperty().isNull());
        btnEliminar.disableProperty().bind(tablaLibros.getSelectionModel().selectedItemProperty().isNull());
        btnDetalle.disableProperty().bind(tablaLibros.getSelectionModel().selectedItemProperty().isNull());

        tablaLibros.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && tablaLibros.getSelectionModel().getSelectedItem() != null) {
                abrirDetalle();
            }
        });
    }

    @FXML
    private void onNuevo() {
        abrirFormulario(null);
    }

    @FXML
    private void onEditar() {
        Libro seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario(seleccionado);
        }
    }

    @FXML
    private void onEliminar() {
        Libro seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar el libro seleccionado?");
        confirmacion.setContentText(
                "ISBN: " + seleccionado.getIsbn() + "\n" +
                        "Título: " + seleccionado.getTitulo() + "\n\n" +
                        "Esta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                servicio.eliminar(seleccionado.getIsbn());
                cargarDatos();
                mostrarInfo("Libro eliminado correctamente");
            } catch (Exception ex) {
                mostrarError("Error al eliminar", ex.getMessage());
            }
        }
    }

    @FXML
    private void onDetalle() {
        abrirDetalle();
    }

    @FXML
    private void onExportar() {
        try {
            String ruta = servicio.exportarReporte();

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Reporte exportado");
            info.setHeaderText("Reporte generado exitosamente");
            info.setContentText("Archivo guardado en:\n" + ruta);
            info.showAndWait();

        } catch (Exception ex) {
            mostrarError("Error al exportar", ex.getMessage());
        }
    }

    /**
     * abre el formulario, si recibe null es nuevo, si no es edición
     */
    private void abrirFormulario(Libro libro) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/biblioteca/views/FormView.fxml"));

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(libro == null ? "Nuevo libro" : "Editar libro");

            Scene scene = new Scene(loader.load(), 480, 420);
            scene.getStylesheets().add(
                    getClass().getResource("/com/biblioteca/views/styles.css").toExternalForm());
            stage.setScene(scene);

            FormController formCtrl = loader.getController();
            formCtrl.inicializar(libro, servicio, this);

            stage.showAndWait();

        } catch (IOException ex) {
            mostrarError("Error al abrir formulario", ex.getMessage());
        }
    }

    /**
     * abre la ventana de detalle del libro seleccionado
     */
    private void abrirDetalle() {
        Libro seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/biblioteca/views/DetalleView.fxml"));

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Detalle del libro");

            Scene scene = new Scene(loader.load(), 420, 360);
            scene.getStylesheets().add(
                    getClass().getResource("/com/biblioteca/views/styles.css").toExternalForm());
            stage.setScene(scene);

            DetalleController detalleCtrl = loader.getController();
            detalleCtrl.setLibro(seleccionado);

            stage.showAndWait();

        } catch (IOException ex) {
            mostrarError("Error al abrir detalle", ex.getMessage());
        }
    }

    private void mostrarInfo(String mensaje) {
        lblEstado.setText(mensaje);
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}