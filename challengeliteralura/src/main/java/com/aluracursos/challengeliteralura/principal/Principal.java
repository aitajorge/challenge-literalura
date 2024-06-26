package com.aluracursos.challengeliteralura.principal;

import com.aluracursos.challengeliteralura.model.*;
import com.aluracursos.challengeliteralura.repository.LibroRepository;
import com.aluracursos.challengeliteralura.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final ConvierteDatos conversor = new ConvierteDatos();

    private final LibroRepository libroRepository;

    @Autowired
    public Principal(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void muestraMenu() {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = obtenerOpcionMenu();


            switch (opcion) {
                case 1 -> {
                    try {
                        buscarLibroPorTitulo();
                    } catch (RuntimeException e) {
                        System.err.println("Error al buscar el libro: " + e.getMessage());
                    }
                }

                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosEnAnio();
                case 5 -> listarLibrosPorIdioma();
                case 0 -> System.out.println("Fin de la aplicación.");
                default -> System.out.println("Opción inválida, intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void mostrarMenuPrincipal() {
        System.out.println("""
                Menú Principal:
                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma
                0 - Salir
                """);
    }

    private int obtenerOpcionMenu() {
        while (true) {
            try {
                System.out.print("Seleccione una opción: ");
                return Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Por favor, ingrese un número válido.");
            }
        }
    }


    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAllWithLenguajes();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            System.out.println("Libros registrados:");
            Set<String> titulosRegistrados = new HashSet<>();
            libros.forEach(libro -> {
                if (titulosRegistrados.add(libro.getTitulo())) {
                    System.out.println("--------------------------");
                    System.out.println("Título: " + libro.getTitulo());
                    System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
                    System.out.println("Lenguajes: " + (libro.getLenguajes() != null ? String.join(", ", libro.getLenguajes()) : "Desconocido"));
                    System.out.println();
                }
            });
            System.out.println("--------------------------");

        }
    }

    public void buscarLibroPorTitulo() {
        System.out.println("Título del libro a buscar: ");
        String tituloLibro = teclado.nextLine();

        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));

        if (json == null || json.trim().isEmpty()) {
            System.err.println("La respuesta de la API es vacía o nula");
            return;
        }

        try {
            GutendexResponse response = conversor.obtenerDatos(json, GutendexResponse.class);
            List<DatosLibro> datosLibros = convertirResponseALibros(response);

            // Obtener los libros existentes en la base de datos
            List<Libro> librosExistentes = libroRepository.findAllWithLenguajes();

            // Filtrar los libros que no están en la base de datos
            List<DatosLibro> librosNoDuplicados = datosLibros.stream()
                    .filter(libro -> !libroExisteEnBaseDeDatos(libro, librosExistentes))
                    .collect(Collectors.toList());

            if (!librosNoDuplicados.isEmpty()) {
                guardarLibrosEnBaseDeDatos(librosNoDuplicados);
                mostrarDetallesLibros(librosNoDuplicados);
            } else {
                System.out.println("El libro ya está registrado en la base de datos o no se encontró.");
            }
        } catch (RuntimeException e) {
            System.err.println("Error al buscar el libro: " + e.getMessage());
        }
    }

    private List<DatosLibro> convertirResponseALibros(GutendexResponse response) {
        return Arrays.stream(response.results())

                .map(result -> new DatosLibro(
                        result.titulo(),
                        result.autores() != null && result.autores().length > 0 ? result.autores()[0] : new Author("Desconocido", null, null),
                        result.lenguajes() != null && result.lenguajes().length > 0 ? result.lenguajes() : new String[]{"Desconocido"}
                ))
                .collect(Collectors.toList());
    }

    private void guardarLibrosEnBaseDeDatos(List<DatosLibro> libros) {
        List<Libro> librosParaGuardar = libros.stream()
                .map(libro -> {
                    Author autor = libro.autor() != null ? libro.autor() : new Author("Desconocido", null, null);
                    Integer anioNacimiento = autor.getAnioNacimiento() != null ? autor.getAnioNacimiento() : 0;
                    String[] lenguajes = libro.lenguaje() != null && libro.lenguaje().length > 0 ? libro.lenguaje() : new String[]{"Desconocido"};
                    return new Libro(libro.titulo(), autor, lenguajes);
                })
                .collect(Collectors.toList());

        libroRepository.saveAll(librosParaGuardar);
    }

    private void mostrarDetallesLibros(List<DatosLibro> libros) {
        System.out.println("Detalles de los libros:");
        libros.forEach(libro -> {
            System.out.println("Título: " + libro.titulo());
            System.out.println("Autor: " + libro.getNombreAutor());
            System.out.println("Año de nacimiento del autor: " + (libro.getAnioNacimiento() != 0 ? libro.getAnioNacimiento() : "Desconocido"));
            System.out.println("Año de muerte del autor: " + (libro.getAnioMuerte() != 0 ? libro.getAnioMuerte() : "Desconocido"));
            System.out.println("Lenguajes: " + String.join(", ", libro.lenguaje()));
            System.out.println();
        });
    }

    private boolean libroExisteEnBaseDeDatos(DatosLibro libro, List<Libro> librosExistentes) {
        return librosExistentes.stream()
                .anyMatch(libroExistente ->
                        libroExistente.getTitulo().equalsIgnoreCase(libro.titulo()) &&
                                libroExistente.getAutor().getNombre().equalsIgnoreCase(libro.getNombreAutor()) &&
                                Arrays.equals(libroExistente.getLenguajes(), libro.lenguaje())
                );
    }

    private void listarAutoresRegistrados() {
        List<Libro> libros = libroRepository.findAllWithLenguajes();
        Set<String> autoresRegistrados = new HashSet<>();
        libros.forEach(libro -> {
            if (libro.getAutor() != null && autoresRegistrados.add(libro.getAutor().getNombre())) {
                System.out.println("Autor: " + libro.getAutor().getNombre());
                System.out.println("--------------------------");
            }
        });
    }

    private void listarAutoresVivosEnAnio() {
        while (true) {
            System.out.print("Ingrese el año: ");
            String input = teclado.nextLine();

            try {
                int anio = Integer.parseInt(input);

                List<Libro> libros = libroRepository.findAllWithLenguajes();
                Set<String> autoresVivos = new HashSet<>();

                libros.forEach(libro -> {
                    Author autor = libro.getAutor();
                    if (autor != null && autor.getAnioNacimiento() != null) {
                        if (autor.getAnioNacimiento() <= anio && (autor.getAnioMuerte() == null || autor.getAnioMuerte() > anio)) {
                            autoresVivos.add(autor.getNombre());
                        }
                    }
                });

                if (autoresVivos.isEmpty()) {
                    System.out.println("No se encontraron autores vivos en el año " + anio);
                } else {
                    System.out.println("--------------------------");
                    System.out.println("Autores vivos en el año " + anio + ":");
                    autoresVivos.forEach(autor -> System.out.println("- " + autor));
                    System.out.println("--------------------------");
                }
                break; // Salir del bucle si se ingresó un año válido
            } catch (NumberFormatException e) {
                // **Mostrar el mensaje de error en rojo sin afectar el color general:**
                System.err.println("\u001B[37mIngreso inválido; el año debe ser un número entero.\u001B[0m");
            }
        }
    }


    private void listarLibrosPorIdioma() {
        // Se crea un conjunto con los idiomas válidos
        Set<String> idiomasValidos = new HashSet<>(Arrays.asList("es", "en", "fr", "pt"));

        boolean idiomaValido = false;
        String idioma = null;

        do {
            System.out.print("Ingrese el idioma (abreviatura en minúsculas): \n");
            System.out.println("es - español");
            System.out.println("en - inglés");
            System.out.println("fr - francés");
            System.out.println("pt - portugués");

            idioma = teclado.nextLine().toLowerCase();

            if (idiomasValidos.contains(idioma)) {
                idiomaValido = true;
            } else {
                System.out.println("Idioma no válido. Ingrese una abreviatura válida.");
            }
        } while (!idiomaValido);

        // Capturar una copia final de la variable 'idioma'
        final String idiomaElegido = idioma;

        List<Libro> libros = libroRepository.findAllWithLenguajes();
        List<Libro> librosEnIdioma = libros.stream()
                .filter(libro -> Arrays.asList(libro.getLenguajes()).contains(idiomaElegido))
                .collect(Collectors.toList());

        if (librosEnIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma " + idioma);
        } else {
            System.out.println("-------------------");
            System.out.println("Libros en el idioma " + idioma + ":");
            librosEnIdioma.forEach(libro -> {
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
                System.out.println();
                System.out.println("-------------------");
            });
        }
    }
}
