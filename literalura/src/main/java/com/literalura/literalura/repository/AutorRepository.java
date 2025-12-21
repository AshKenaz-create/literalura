package com.literalura.literalura.repository;

import com.literalura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreIgnoreCase(String nombre);

    // ✅ Derived queries correctas para "autores vivos en un año"
    // Regla: nacimiento <= año  Y (fallecimiento IS NULL  OR fallecimiento >= año)

    // 1) Nació <= año y falleció >= año
    List<Autor> findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqual(
            Integer anioNacimiento, Integer anioFallecimiento
    );

    // 2) Nació <= año y NO tiene año de fallecimiento (sigue vivo)
    List<Autor> findByNacimientoLessThanEqualAndFallecimientoIsNull(
            Integer anio
    );
}
