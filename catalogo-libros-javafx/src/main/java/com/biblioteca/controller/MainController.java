package com.biblioteca.controller;


import com.biblioteca.services.LibroServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController extends Application {

    private static LibroServices libroServices;

    @Override
    public void start (Stage primaryStage) throws IOException{
        libroServices = new LibroServices();

        FXMLLoader loder = new FXMLLoader(getClass().getResource("/fxml/libro.fxml"));

        Scene scene = new Scene(loder.load(), 900, 600);

        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());


        primaryStage.setTitle("Biblioteca Escolar - Catalogo de libros ");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    public static LibroServices getLibroServices(){
        return libroServices;
    }

    public static void main (String[] args){
        launch(args);
    }
}
