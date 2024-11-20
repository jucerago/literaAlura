package com.alura.literAlura.repository;

import com.alura.literAlura.model.Idioma;
import com.alura.literAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro,Long> {
    @Query("select distinct l from Libro b join b.idiomas l")
    List<Idioma> find_idiomas();
    @Query("select count(l) from Libro b join b.idiomas l where l=:idioma")
    Object find_count_by_idioma(Idioma idioma);
    @Query("select b from Libro b order by b.descargas desc limit 10")
    List<Libro> find_top10();
    @Query("select count(b) > 0 from Libro b where b.titulo=:titulo")
    Boolean is_book_present(String titulo);
}
