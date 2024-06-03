package com.aluracursos.challengeliteralura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Result(
        @JsonProperty("title") String titulo,
        @JsonProperty("authors") Author[] autores,
        @JsonProperty("languages") String[] lenguajes
) {
}
