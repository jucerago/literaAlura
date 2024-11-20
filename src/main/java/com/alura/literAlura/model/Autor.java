package com.alura.literAlura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autor")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String autor_name;
    private Integer birth_year;
    private Integer death_year;
    @ManyToMany(mappedBy = "autores")
    private List<Libro> libros;

    public Autor(){}

    public Autor(DatosAutor autores) {
        this.autor_name= autores.autor_name();
        this.birth_year=autores.birth_year();
        this.death_year=autores.death_year();
        libros = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder output_string= new StringBuilder("=".repeat(120)+"\n"+
                "nombre      : "+this.get_formated_autor_name()+"\n"+
                "birth year: "+birth_year+"\n"+
                "death year: "+death_year);
        return output_string.toString();
    }

    public String get_formated_autor_name(){
        var tmp=autor_name.split(", ");
        if(tmp.length>1)return tmp[1]+" "+tmp[0];
        else return tmp[0];
    }

    public Long get_id() {
        return id;
    }

    public void set_id(Long id) {
        this.id = id;
    }

    public String get_autor_name() {
        return autor_name;
    }

    public void set_autor_name(String autor_name) {
        this.autor_name = autor_name;
    }

    public Integer get_birth_year() {
        return birth_year;
    }

    public void set_birth_year(Integer birth_year) {
        this.birth_year = birth_year;
    }

    public Integer get_death_year() {
        return death_year;
    }

    public void set_death_year(Integer death_year) {
        this.death_year = death_year;
    }

    public List<Libro> get_libros() {
        return libros;
    }

    public void set_libros(Libro libro) {
        this.libros.add(libro);
    }
}
