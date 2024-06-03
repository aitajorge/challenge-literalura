package com.aluracursos.challengeliteralura.model;

import jakarta.persistence.*;
import java.util.Arrays;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String titulo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author autor;

    @ElementCollection
    private String[] lenguajes;

    public Libro() {}

    public Libro(String titulo, Author autor, String[] lenguajes) {
        this.titulo = titulo;
        this.autor = autor;
        this.lenguajes = lenguajes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Author getAutor() {
        return autor;
    }

    public void setAutor(Author autor) {
        this.autor = autor;
    }

    public String[] getLenguajes() {
        return lenguajes;
    }

    public void setLenguajes(String[] lenguajes) {
        this.lenguajes = lenguajes;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", lenguajes=" + Arrays.toString(lenguajes) +
                '}';
    }
}

