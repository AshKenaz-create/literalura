package com.literalura.literalura.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private Integer nacimiento;
    private Integer fallecimiento;

    @ManyToMany(mappedBy = "autores")
    private Set<Libro> libros = new HashSet<>();

    public Autor() {}

    public Autor(String nombre, Integer nacimiento, Integer fallecimiento) {
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.fallecimiento = fallecimiento;
    }

    // getters & setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getNacimiento() { return nacimiento; }
    public Integer getFallecimiento() { return fallecimiento; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setNacimiento(Integer nacimiento) { this.nacimiento = nacimiento; }
    public void setFallecimiento(Integer fallecimiento) { this.fallecimiento = fallecimiento; }

    @Override
    public String toString() {
        return "Autor{" +
                "nombre='" + nombre + '\'' +
                ", nacimiento=" + nacimiento +
                ", fallecimiento=" + fallecimiento +
                '}';
    }
}