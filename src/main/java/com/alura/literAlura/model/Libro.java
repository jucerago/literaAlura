package com.alura.literAlura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="autor_libro",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;
    @ElementCollection(targetClass = Idioma.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Idioma> idiomas;
    private Integer descargas;


    public Libro(){}

    public Libro(DatosLibros datosLibros) {

        this.titulo = datosLibros.titulo();
        this.autores = new ArrayList<>();
        for (DatosAutor datosAutor: datosLibros.autores()){
            this.autores.add(new Autor(datosAutor));
        }
        this.idiomas = new ArrayList<>();
        for (String idioma:datosLibros.idiomas()){
        this.idiomas.add(Idioma.fromString(idioma));
        }
        this.descargas = datosLibros.descargas();
    }

    @Override
    public String toString() {
        StringBuilder output_string= new StringBuilder("=".repeat(120)+"\n"+
                "titulo:     " + titulo + "\n" +
                "autores:   ");
        for(int i=0;i<autores.size();i++){
            output_string.append(autores.get(i).get_formated_autor_name()).append("\n");
            if(i!=autores.size()-1)output_string.append("           ");
        }
        output_string.append("idiomas: ");
        for(int i=0;i<idiomas.size();i++){
            output_string.append(idiomas.get(i)).append("\n");
            if(i!=idiomas.size()-1)output_string.append("           ");
        }
        output_string.append("descargas: ").append(descargas);
        return output_string.toString();
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

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public void update_autores(List<Autor> autores){
        List<Autor> final_autores = new ArrayList<>(autores);

        Map<String,Boolean> present_autores=final_autores.stream()
                .collect(Collectors.toMap(Autor::get_autor_name, obj->true));

        for(Autor autor:this.autores){
            if(!present_autores.get(autor.get_autor_name()))
                final_autores.add(autor);
        }

        this.autores=final_autores;
    }

    public List<Idioma> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public void add_autores(List<Autor> autores) {
        this.autores.addAll(autores);
    }

}
