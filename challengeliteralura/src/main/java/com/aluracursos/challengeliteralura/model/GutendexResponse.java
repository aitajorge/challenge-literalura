package com.aluracursos.challengeliteralura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexResponse(
        @JsonProperty("results") Result[] results
) {
}
