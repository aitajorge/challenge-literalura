package com.aluracursos.challengeliteralura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonProperty("title") String titulo,
        @JsonProperty("authors") Author autor,
        @JsonProperty("languages")String[] lenguaje
) {

    public String getNombreAutor(){
        return autor.getNombre();
    }

    public int getAnioNacimiento(){
        return autor.getAnioNacimiento();
    }

    public int getAnioMuerte(){
        return autor.getAnioMuerte();
    }



}
