# challenge-literalura  LiterAlura

**Requisitos del sistema:**

Java JDK: versión: 17 en adelante<br>
Download the Latest Java LTS Free<br>
Maven: versión 4 en adelante<br>
Spring: versión 3.2.3 - https://start.spring.io/<br>
Postgres: versión 16 en adelante - PostgreSQL: Downloads<br>
IDE (Entorno de desenvolvimento integrado) IntelliJ IDEA- opcional<br>
Descargar IntelliJ IDEA: el IDE líder para Java y Kotlin<br>



---

# Challenge Literatura - Documentación para el Usuario

## Requisitos del Sistema
- **Java 17**: Asegúrate de tener instalado Java 17 en tu sistema.
- **IntelliJ IDEA**: Este proyecto está desarrollado en IntelliJ IDEA. Aunque puedes usar otras IDE, se recomienda usar IntelliJ para una mejor experiencia de desarrollo.
- **PostgreSQL**: La base de datos utilizada en este proyecto es PostgreSQL. Asegúrate de tener PostgreSQL instalado y configurado en tu sistema.

## Instrucciones de Configuración
1. **Clonar el Repositorio**: Clona este repositorio en tu sistema local.
2. **Importar el Proyecto**: Abre IntelliJ IDEA y selecciona la opción para importar un proyecto. Navega hasta la ubicación del repositorio clonado y selecciona el archivo `pom.xml` para importar el proyecto.
3. **Configurar la Base de Datos**: Abre el archivo `application.properties` en la carpeta `src/main/resources` y configura los detalles de tu base de datos PostgreSQL, como el URL, el nombre de usuario y la contraseña.
4. **Ejecutar la Aplicación**: Ejecuta la clase `Principal.java` que se encuentra en el paquete `com.aluracursos.challengeliteralura.principal`. Esto iniciará la aplicación y mostrará el menú principal.

## Uso de la Aplicación
- **Menú Principal**: Una vez que la aplicación esté en funcionamiento, verás un menú con varias opciones. Puedes seleccionar una opción ingresando el número correspondiente y presionando Enter.
- **Buscar Libro por Título**: Esta opción te permite buscar un libro por su título. Simplemente ingresa el título del libro cuando se te solicite.
- **Listar Libros Registrados**: Esta opción muestra todos los libros que están registrados en la base de datos.
- **Listar Autores Registrados**: Muestra una lista de todos los autores que tienen libros registrados en la base de datos.
- **Listar Autores Vivos en un Año**: Te permite ver una lista de autores que estaban vivos en un año específico que ingresas.
- **Listar Libros por Idioma**: Puedes listar todos los libros disponibles en un idioma específico.

## Contribuir
Si quieres contribuir a este proyecto, siéntete libre de enviar un pull request o abrir un issue con tus sugerencias o correcciones.

