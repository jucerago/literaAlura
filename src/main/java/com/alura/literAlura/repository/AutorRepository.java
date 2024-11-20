package com.alura.literAlura.repository;

import com.alura.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor,Long> {
    @Query("select a from Autor a where a.autor_name=:autor_name")
    Autor find_by_name(String autor_name);
    @Query("select a from Autor a where  a.birth_year<=:year and a.death_year>=:year")
    List<Autor> find_by_date(Integer year);
    @Query("select a from Autor a where a.autor_name ilike %:autor_name%")
    List<Autor> find_autores_by_name(String autor_name);
    @Query("select a from Autor a where a.birth_year=:year")
    List<Autor> find_autors_by_birth_year(Integer year);
    @Query("select a from Autor a where a.death_year=:year")
    List<Autor> find_autores_by_death_year(Integer year);
}
