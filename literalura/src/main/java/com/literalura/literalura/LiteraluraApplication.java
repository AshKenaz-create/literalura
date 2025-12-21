package com.literalura.literalura;

import com.literalura.literalura.model.Autor;
import com.literalura.literalura.model.Libro;
import com.literalura.literalura.service.CatalogoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private final CatalogoService catalogoService;

    public LiteraluraApplication(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        mostrarMenu();
    }

    private void mostrarMenu() {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("""
                    
                    ================================
                           LiterAlura - Menú
                    ================================
                    1- Buscar libro por título (y guardar el primero)
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    6- Estadística: cantidad de libros por idioma
                    0- Salir
                    """);

            System.out.print("Elige una opción: ");
            String entrada = sc.nextLine();

            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("❌ Error: Debes escribir un número. Intenta de nuevo.\n");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarYGuardarLibro(sc);
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosEnAnio(sc);
                case 5 -> listarLibrosPorIdioma(sc);

                // ✅ NUEVO: opción 6 (estadística)
                case 6 -> mostrarCantidadLibrosPorIdioma(sc);

                case 0 -> System.out.println("✅ Saliendo... ¡Hasta luego!");
                default -> System.out.println("⚠️ Opción inválida. Elige un número del menú.\n");
            }
        }
    }

    private void buscarYGuardarLibro(Scanner sc) {
        System.out.print("Escribe el título a buscar: ");
        String titulo = sc.nextLine().trim();

        if (titulo.isBlank()) {
            System.out.println("❌ Error: El título no puede estar vacío.\n");
            return;
        }

        try {
            Libro guardado = catalogoService.buscarYGuardarPrimerLibroPorTitulo(titulo);

            if (guardado == null) {
                System.out.println("🔎 No se encontraron resultados.\n");
                return;
            }

            System.out.println("\n✅ Libro guardado en el catálogo:");
            System.out.println(guardado);
            System.out.println();

        } catch (Exception e) {
            System.out.println("❌ Error procesando la búsqueda/guardado. Intenta de nuevo.");
            System.out.println("Detalle: " + e.getMessage() + "\n");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = catalogoService.listarLibrosRegistrados();

        if (libros.isEmpty()) {
            System.out.println("📭 Aún no hay libros registrados.\n");
            return;
        }

        System.out.println("\n📚 Libros registrados (" + libros.size() + "):\n");
        libros.forEach(l -> {
            System.out.println(l);
            System.out.println("--------------------------------------");
        });
        System.out.println();
    }

    // ✅ Método EXACTO que pediste (opción 3)
    private void listarAutoresRegistrados() {
        var autores = catalogoService.listarAutoresRegistrados();

        if (autores.isEmpty()) {
            System.out.println("📭 Aún no hay autores registrados.\n");
            return;
        }

        System.out.println("\n👤 Autores registrados (" + autores.size() + "):\n");
        autores.forEach(a -> {
            System.out.println(a);
            System.out.println("--------------------------------------");
        });
        System.out.println();
    }

    // ✅ Método EXACTO que pediste (opción 4)
    private void listarAutoresVivosEnAnio(Scanner sc) {
        System.out.print("Escribe el año para consultar autores vivos: ");
        String entrada = sc.nextLine().trim();

        Integer anio;
        try {
            anio = Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("❌ Debes escribir un año válido.\n");
            return;
        }

        var autores = catalogoService.listarAutoresVivosEnAnio(anio);

        if (autores.isEmpty()) {
            System.out.println("🔎 No se encontraron autores vivos en el año " + anio + ".\n");
            return;
        }

        System.out.println("\n🟢 Autores vivos en el año " + anio + " (" + autores.size() + "):\n");
        autores.forEach(a -> {
            System.out.println(a);
            System.out.println("--------------------------------------");
        });
        System.out.println();
    }

    // ✅ opción 5 (ya la tenías)
    private void listarLibrosPorIdioma(Scanner sc) {
        System.out.print("Escribe el idioma (ej: en, es, fr, pt): ");
        String idioma = sc.nextLine().trim();

        List<Libro> libros = catalogoService.listarLibrosPorIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("📭 No hay libros registrados en ese idioma.\n");
            return;
        }

        System.out.println("\n📚 Libros en idioma '" + idioma + "' (" + libros.size() + "):\n");
        libros.forEach(l -> {
            System.out.println(l);
            System.out.println("--------------------------------------");
        });
        System.out.println();
    }

    // ✅ NUEVO: opción 6 (estadística)
    private void mostrarCantidadLibrosPorIdioma(Scanner sc) {
        System.out.print("Escribe el idioma para contar libros (ej: en, es): ");
        String idioma = sc.nextLine().trim();

        if (idioma.isBlank()) {
            System.out.println("❌ El idioma no puede estar vacío.\n");
            return;
        }

        long cantidad = catalogoService.contarLibrosPorIdioma(idioma);

        System.out.println("\n📊 Cantidad de libros en idioma '" + idioma + "': " + cantidad + "\n");
    }
}
