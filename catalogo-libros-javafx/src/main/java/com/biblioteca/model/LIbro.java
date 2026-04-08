package com.biblioteca.model;

public class LIbro {
    private String ISBN;
    private  String titulo;
    private  String autor;
    private  int anio;
    private String genero;
    private boolean disponible;

    public LIbro(String ISBN, String titulo, String autor, int anio, String genero) {

        this.ISBN = ISBN;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.genero = genero;
        this.disponible = disponible;


    }
    public LIbro(){}

    public String getISBN(){
        return ISBN;
    }
    public void setISBN(String ISBN){
        this.ISBN = ISBN;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString(){
        return String.format("Libro {ISBN'', titulo '', autor '', anio '', genero '', disponible ''}", ISBN, titulo, autor, anio, genero);
}
}
