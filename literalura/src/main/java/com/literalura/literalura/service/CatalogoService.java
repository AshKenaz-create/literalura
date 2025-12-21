package com.literalura.literalura.service;

import com.literalura.literalura.dto.AuthorDTO;
import com.literalura.literalura.dto.BookDTO;
import com.literalura.literalura.dto.GutendexResponseDTO;
import com.literalura.literalura.model.Autor;
import com.literalura.literalura.model.Libro;
import com.literalura.literalura.repository.AutorRepository;
import com.literalura.literalura.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CatalogoService {

    private final GutendexService gutendexService;
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public CatalogoService(GutendexService gutendexService,
                           LibroRepository libroRepository,
                           AutorRepository autorRepository) {
        this.gutendexService = gutendexService;
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    // ✅ Requisito: buscar por título y quedarse con el primer resultado
    public Libro buscarYGuardarPrimerLibroPorTitulo(String titulo) {
        GutendexResponseDTO respuesta = gutendexService.buscarLibrosPorTitulo(titulo);

        if (respuesta == null || respuesta.results() == null || respuesta.results().isEmpty()) {
            return null;
        }

        BookDTO primero = respuesta.results().get(0);
        return convertirYGuardar(primero);
    }

    // ✅ Requisito: listar todos los libros guardados
    // ✅ FIX LazyInitializationException: traer libros + autores + idiomas con FETCH JOIN
    public List<Libro> listarLibrosRegistrados() {
        return libroRepository.findAllWithAutoresAndIdiomas();
    }

    // ✅ Listar autores registrados
    public List<Autor> listarAutoresRegistrados() {
        return autorRepository.findAll();
    }

    // ✅ Autores vivos en un año (combina 2 derived queries y evita duplicados)
    public List<Autor> listarAutoresVivosEnAnio(Integer anio) {
        if (anio == null) return List.of();

        List<Autor> vivosSinFallecimiento =
                autorRepository.findByNacimientoLessThanEqualAndFallecimientoIsNull(anio);

        List<Autor> vivosConFallecimiento =
                autorRepository.findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqual(anio, anio);

        Set<Autor> unidos = new HashSet<>();
        unidos.addAll(vivosSinFallecimiento);
        unidos.addAll(vivosConFallecimiento);

        return unidos.stream().toList();
    }

    // ✅ Opción 5: listar libros por idioma (derived query)
    public List<Libro> listarLibrosPorIdioma(String idioma) {
        if (idioma == null || idioma.isBlank()) return List.of();
        return libroRepository.findByIdiomasContainingIgnoreCase(idioma.trim());
    }

    // ✅ Contar libros por idioma
    public long contarLibrosPorIdioma(String idioma) {
        if (idioma == null || idioma.isBlank()) return 0;
        return libroRepository.countByIdiomasContainingIgnoreCase(idioma.trim());
    }

    private Libro convertirYGuardar(BookDTO dto) {
        // 1) Evitar duplicado por gutendexId
        Libro libro = libroRepository.findByGutendexId(dto.id())
                .orElseGet(Libro::new);

        libro.setGutendexId(dto.id());
        libro.setTitulo(dto.title());

        // ✅ Idioma: el challenge pide SOLO el primer idioma (null-safe)
        Set<String> idiomas = new HashSet<>();
        if (dto.languages() != null && !dto.languages().isEmpty() && dto.languages().get(0) != null) {
            String primerIdioma = dto.languages().get(0).trim();
            if (!primerIdioma.isBlank()) {
                idiomas.add(primerIdioma);
            }
        }
        libro.setIdiomas(idiomas);

        // ✅ Descargas null-safe
        libro.setDescargas(dto.downloadCount() == null ? 0 : dto.downloadCount());

        // ✅ AUTOR: el challenge pide SOLO el primer autor (null-safe)
        Set<Autor> autoresFinales = new HashSet<>();

        if (dto.authors() != null && !dto.authors().isEmpty()) {
            AuthorDTO a = dto.authors().get(0);

            if (a != null && a.name() != null && !a.name().isBlank()) {
                String nombreAutor = a.name().trim();

                Autor autor = autorRepository.findByNombreIgnoreCase(nombreAutor)
                        .orElseGet(() -> autorRepository.save(
                                new Autor(nombreAutor, a.birthYear(), a.deathYear())
                        ));

                autoresFinales.add(autor);
            }
        }

        libro.setAutores(autoresFinales);

        // 3) Guardar libro
        return libroRepository.save(libro);
    }
}

