package com.utez.catalogolibros.repository;

import com.utez.catalogolibros.model.Libro;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class LibroRepository {

    private final String FILE_PATH = "data/libros.csv";

    public List<Libro> cargarLibros() {
        List<Libro> lista = new ArrayList<>();

        try {
            Path path = Paths.get(FILE_PATH);

            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                return lista;
            }

            List<String> lineas = Files.readAllLines(path);

            for (String linea : lineas) {
                if (linea.isBlank()) continue;

                String[] datos = linea.split(",");

                Libro libro = new Libro(
                  datos[0],
                  datos[1],
                  datos[2],
                  Integer.parseInt(datos[3]),
                  datos[4],
                  Boolean.parseBoolean(datos[5])
                );

                lista.add(libro);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void guardarLibros(List<Libro> libros) {

        try {
            Path path = Paths.get(FILE_PATH);

            List<String> lineas = new ArrayList<>();

            for (Libro libro : libros) {
                String linea = String.join(",",
                        libro.getIsbn(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        String.valueOf(libro.getAnio()),
                        libro.getGenero(),
                        String.valueOf(libro.isDisponible())
                );

                lineas.add(linea);
            }

            Files.write(path, lineas);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
