package com.literalura.literalura.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Para no duplicar libros, guardamos el id de Gutendex
    @Column(unique = true)
    private Integer gutendexId;

    @Column(nullable = false)
    private String titulo;

    // ✅ FIX LazyInitializationException: cargar idiomas siempre (EAGER) + usar Set
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_idiomas", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "idioma")
    private Set<String> idiomas = new HashSet<>();

    private Integer descargas;

    @ManyToMany
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    public Libro() {}

    // getters & setters
    public Long getId() { return id; }
    public Integer getGutendexId() { return gutendexId; }
    public String getTitulo() { return titulo; }
    public Set<String> getIdiomas() { return idiomas; }
    public Integer getDescargas() { return descargas; }
    public Set<Autor> getAutores() { return autores; }

    public void setGutendexId(Integer gutendexId) { this.gutendexId = gutendexId; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setIdiomas(Set<String> idiomas) { this.idiomas = idiomas; }
    public void setDescargas(Integer descargas) { this.descargas = descargas; }
    public void setAutores(Set<Autor> autores) { this.autores = autores; }

    // ✅ SOLUCIÓN C: proteger toString() para que no explote con colecciones (y evitar NPE)
    // Nota: si 'autores' fuera LAZY y se imprime fuera de sesión, también podría fallar.
    // En tu caso, al menos 'idiomas' ya está EAGER, así que queda mucho más estable.
    @Override
    public String toString() {
        String autoresTxt;
        try {
            autoresTxt = (autores == null || autores.isEmpty())
                    ? "[]"
                    : autores.stream()
                    .map(Autor::getNombre)
                    .collect(Collectors.joining(", "));
        } catch (Exception e) {
            autoresTxt = "[no disponible]";
        }

        String idiomasTxt = (idiomas == null || idiomas.isEmpty())
                ? "[]"
                : idiomas.toString();

        return "Libro{" +
                "id=" + id +
                ", gutendexId=" + gutendexId +
                ", titulo='" + titulo + '\'' +
                ", descargas=" + descargas +
                ", idiomas=" + idiomasTxt +
                ", autores=" + autoresTxt +
                '}';
    }
}
