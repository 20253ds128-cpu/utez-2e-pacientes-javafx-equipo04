package com.biblioteca;

import com.biblioteca.services.LibroServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class MainApp extends Application {

    private static LibroServices libroServices;

    @Override
    public void start(Stage primaryStage) throws IOException{

        libroServices = new LibroServices();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/biblioteca/views/MainView.fxml"));

        Scene scene = new Scene(loader.load(), 900,600);

        scene.getStylesheets().add(
                getClass().getResource("/com/biblioteca/views/styles.css").toExternalForm()
        );

        primaryStage.setTitle("Biblioteca Escolar - Catalogo de libros");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMaxHeight(500);
        primaryStage.show();
    }

    public static LibroServices getLibroServices(){
        return libroServices;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
