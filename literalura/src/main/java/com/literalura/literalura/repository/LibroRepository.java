package com.literalura.literalura.repository;

import com.literalura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByGutendexId(Integer gutendexId);

    // ya lo tienes/lo usas para listar
    List<Libro> findByIdiomasContainingIgnoreCase(String idioma);

    // ✅ contar libros por idioma (para un idioma específico)
    long countByIdiomasContainingIgnoreCase(String idioma);

    // ✅ BONUS: traer estadísticas por idioma (sin que el usuario escriba nada)
    // Devuelve filas tipo: [ "en", 12 ], [ "es", 4 ], etc.
    @Query("""
           SELECT i as idioma, COUNT(l) as cantidad
           FROM Libro l
           JOIN l.idiomas i
           GROUP BY i
           ORDER BY COUNT(l) DESC
           """)
    List<Object[]> contarLibrosAgrupadosPorIdioma();

    // ✅ NUEVO: traer libros + autores en la misma consulta (evita LazyInitializationException)
    @Query("""
           SELECT DISTINCT l
           FROM Libro l
           LEFT JOIN FETCH l.autores
           """)
    List<Libro> findAllWithAutores();

    // ✅ NUEVO (imagen): traer libros + autores + idiomas en la misma consulta
    @Query("""
           SELECT DISTINCT l
           FROM Libro l
           LEFT JOIN FETCH l.autores
           LEFT JOIN FETCH l.idiomas
           """)
    List<Libro> findAllWithAutoresAndIdiomas();
}
