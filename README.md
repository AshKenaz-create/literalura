# LiterAlura – Catálogo de Libros (Java + Spring + JPA)

> Desafío Alura: construye tu propio catálogo de libros consumiendo la API pública **Gutendex**. Guarda los datos en BD y ofrece consultas por consola.

## ✨ Funcionalidades (MVP)
1. Buscar libro por título (consume API y guarda si no existe).
2. Listar libros registrados.
3. Listar autores registrados.
4. Listar autores vivos en un año dado.
5. Listar libros por idioma (ej: `es`, `en`, `fr`).

**Próximas mejoras:** filtros por descargas, exportar CSV/JSON, pruebas unitarias.

---

## 🧱 Stack
- Java 17, Spring Boot 3, Spring Data JPA  
- BD: PostgreSQL (H2 en dev opcional)  
- Cliente HTTP: `RestClient` (Spring 6+)  
- API: `https://gutendex.com/books/?search=<texto>`

---

## 🖥️ Demo (capturas)
> (subir en `docs/img/` cuando estén)
- Menú en consola  
  ![Menú](docs/img/menu.png)
- Búsqueda/guardado  
  ![Búsqueda](docs/img/search-save.png)

---

## ⚙️ Requisitos
- Java 17+
- Maven 3.9+
- PostgreSQL 14+ (o H2)

---

## 🚀 Cómo correr (cuando esté el código)
```bash
# src/main/resources/application.properties
# spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
# spring.datasource.username=postgres
# spring.datasource.password=tu_password
# spring.jpa.hibernate.ddl-auto=update

mvn spring-boot:run
