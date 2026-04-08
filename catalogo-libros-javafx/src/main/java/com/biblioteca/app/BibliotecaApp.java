package com.biblioteca.app;

import com.biblioteca.service.LibroService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal que arranca la aplicación de la biblioteca.
 */
public class BibliotecaApp extends Application {

    private static LibroService libroService;

    /**
     * Inicializa la interfaz principal y carga el archivo FXML.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        libroService = new LibroService();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/biblioteca/views/MainView.fxml"));

        Scene scene = new Scene(loader.load(), 900, 600);

    }

    public static LibroService getLibroService() {
        return libroService;
    }

    public static void main(String[] args) {
        launch(args);
    }
}