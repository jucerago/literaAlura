package com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name") String autor_name,
        @JsonAlias("birth_year") Integer birth_year,
        @JsonAlias("death_year") Integer death_year
) {
}
