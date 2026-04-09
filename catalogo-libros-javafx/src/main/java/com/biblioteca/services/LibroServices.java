package com.biblioteca.services;

import com.biblioteca.model.Libro;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibroServices {

    private static final String ARCHIVO_DATOS = "data/catalogo.csv";
    private static final String ARCHIVO_REPORTE = "data/reporte_catalogo.csv";
    private static final String SEPARADOR = ":";
    private static final int ANIO_MIN = 1500;

    private final List<Libro> catalogo = new ArrayList<>();

    public LibroServices(){
        cargarDesdeArchivo();
    }

    public void agregar(Libro libro) {
        validar(libro);
        if (existeIsbn(libro.getIsbn())) {
            throw new IllegalArgumentException("ya existe un libro con el ISBN:");

        }
        catalogo.add(libro);
        guardarEnArchivo();
    }

    public List<Libro> obetenerTodos(){
        return new ArrayList<>(catalogo);
    }

    public Optional<Libro> busquedaPorIsbn(String  isbn){
        return catalogo.stream().filter(lib-> lib.getIsbn().equalsIgnoreCase(isbn)).findFirst();

    }
    public void actualizar(String isbnOriginal, Libro actualizacion){
        validar(actualizacion);
        if (!isbnOriginal.equalsIgnoreCase(actualizacion.getIsbn()) && existeIsbn(actualizacion.getIsbn())){
            throw new IllegalArgumentException("ya existe un libro con el ISBN:" + actualizacion.getIsbn());

        }
        for (int i =0; i< catalogo.size();i++){
            if (catalogo.get(i).getIsbn().equalsIgnoreCase(isbnOriginal)){
                catalogo.set(i, actualizacion);
                guardarEnArchivo();
                return;
            }
        }
        throw new IllegalArgumentException("No se encontro el libro con ISBN:"+ isbnOriginal);

    }

public void eliminar (String isbn){
        boolean eliminado = catalogo.removeIf(lib-> lib.getIsbn().equalsIgnoreCase(isbn));
        if (!eliminado){
            throw new IllegalArgumentException("No se encontro el libro con ISBN:" + isbn);

        }
        guardarEnArchivo();
}

public void cargarDesdeArchivo(){
        catalogo.clear();
        File archivo = new File (ARCHIVO_DATOS);

        if (!archivo.exists()){
            System.out.println("[LibroServices] Archivo de datos no encontrado. Se iniciara con catalogo vacio");
            return;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(archivo))){
            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null){
                numeroLinea++;
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] partes = linea.split(SEPARADOR, -1);
                if (partes.length != 6){
                    System.out.printf("Linea " + numeroLinea + "mal formada , se omite" + linea);
                    continue;
                }
                try {
                    String isbn      = partes[0].trim();
                    String titulo    = partes[1].trim();
                    String autor     = partes[2].trim();
                    int    anio      = Integer.parseInt(partes[3].trim());
                    String genero    = partes[4].trim();
                    boolean disp     = partes[5].trim().equalsIgnoreCase("true");

                    catalogo.add(new Libro(isbn, titulo, autor, anio, genero, disp));
                } catch (NumberFormatException e) {
                    System.err.println("[LibroService] Error de formato en línea " + numeroLinea + ": " + e.getMessage());
                }
            }
            System.out.println("[LibroService] Catálogo cargado: " + catalogo.size() + " libro(s).");

        } catch (IOException e) {
            System.err.println("[LibroService] Error al leer el archivo: " + e.getMessage());
        }
}
public void guardarEnArchivo(){
        try {
            Files.createDirectories(Paths.get("data"));

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_REPORTE))) {
                bw.write("# Catálogo Biblioteca Escolar - formato: isbn;titulo;autor;anio;genero;disponible");
                bw.newLine();

                for (Libro l : catalogo) {
                    String linea = String.join(SEPARADOR,
                            l.getIsbn(),
                            l.getTitulo(),
                            l.getAutor(),
                            String.valueOf(l.getAnio()),
                            l.getGenero(),
                            String.valueOf(l.getDisponible()));
                    bw.write(linea);
                    bw.newLine();
            }
            }
            System.out.println("[LibroService] Catálogo guardado (" + catalogo.size() + " libro(s)).");

        } catch (IOException e) {
            System.err.println("[LibroService] Error al guardar el archivo: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar el catálogo: " + e.getMessage(), e);
        }
}

    public String exportarReporte() {
        try {
            Files.createDirectories(Paths.get("data"));

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_REPORTE))) {

                bw.write("REPORTE DEL CATÁLOGO - BIBLIOTECA ESCOLAR");
                bw.newLine();
                bw.write("Fecha de generación: " + LocalDate.now());
                bw.newLine();
                bw.write("Total de libros: " + catalogo.size());
                bw.newLine();
                bw.write("─".repeat(80));
                bw.newLine();


                bw.write("ISBN;Título;Autor;Año;Género;Disponible");
                bw.newLine();

                for (Libro l : catalogo) {
                    String linea = String.join(";",
                            l.getIsbn(),
                            l.getTitulo(),
                            l.getAutor(),
                            String.valueOf(l.getAnio()),
                            l.getGenero(),
                            l.getDisponible() ? "Sí" : "No");
                    bw.write(linea);
                    bw.newLine();
                }
            }

            String rutaAbsoluta = new File(ARCHIVO_REPORTE).getAbsolutePath();
            System.out.println("[LibroService] Reporte exportado en: " + rutaAbsoluta);
            return rutaAbsoluta;

        } catch (IOException e) {
            throw new RuntimeException("No se pudo exportar el reporte: " + e.getMessage(), e);
        }
    }

    public void validar(Libro libro) {
        if (libro == null) throw new IllegalArgumentException("El libro no puede ser nulo.");

        if (isBlank(libro.getIsbn()))   throw new IllegalArgumentException("El ISBN no puede estar vacío.");
        if (isBlank(libro.getTitulo())) throw new IllegalArgumentException("El título no puede estar vacío.");
        if (isBlank(libro.getAutor()))  throw new IllegalArgumentException("El autor no puede estar vacío.");
        if (isBlank(libro.getGenero())) throw new IllegalArgumentException("El género no puede estar vacío.");

        if (libro.getTitulo().trim().length() < 3)
            throw new IllegalArgumentException("El título debe tener al menos 3 caracteres.");
        if (libro.getAutor().trim().length() < 3)
            throw new IllegalArgumentException("El autor debe tener al menos 3 caracteres.");

        int anioActual = LocalDate.now().getYear();
        if (libro.getAnio() < ANIO_MIN || libro.getAnio() > anioActual)
            throw new IllegalArgumentException(
                    "El año debe estar entre " + ANIO_MIN + " y " + anioActual + ".");
    }


    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean existeIsbn(String isbn) {
        return catalogo.stream()
                .anyMatch(l -> l.getIsbn().equalsIgnoreCase(isbn));
    }


}





