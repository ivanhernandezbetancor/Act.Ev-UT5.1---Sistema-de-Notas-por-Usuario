package gestornotas.service;

import gestornotas.model.Nota;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

// Servicio que gestiona las operaciones CRUD de notas
public class NotaService {
    // Nombre del archivo donde se almacenan las notas de cada usuario
    private static final String NOTAS_FILENAME = "notas.txt";

    // Crea el archivo de notas si no existe
    private void inicializarNotasFile(Path carpetaUsuario) {
        try {
            Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);
            if (!Files.exists(notasFile)) {
                Files.createFile(notasFile);
            }
        } catch (IOException e) {
            System.out.println("Error al inicializar archivo de notas: " + e.getMessage());
        }
    }

    // Crea una nueva nota y la guarda en el archivo de notas del usuario
    public boolean crearNota(Path carpetaUsuario, String titulo, String contenido) {
        // Valida que título y contenido no estén vacíos
        if (titulo.isEmpty() || contenido.isEmpty()) {
            return false;
        }

        try {
            // Inicializa el archivo de notas si es necesario
            inicializarNotasFile(carpetaUsuario);
            
            Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

            // Crea una nueva nota con los datos proporcionados
            Nota nota = new Nota(titulo, contenido);
            String linea = nota.toString() + "\n";

            // Escribe la nota en el archivo 
            BufferedWriter writer = Files.newBufferedWriter(notasFile, StandardOpenOption.APPEND);
            try (writer) {
                writer.write(linea);
            }

            return true;
        } catch (IOException e) {
            System.out.println("No se pudo crear la nota: " + e.getMessage());
            return false;
        }
    }

    // Obtiene todas las notas del usuario
    public List<Nota> listarNotas(Path carpetaUsuario) {
        List<Nota> notas = new ArrayList<>();
        Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

        // Si el archivo no existe, lo crea y retorna lista vacía
        if (!Files.exists(notasFile)) {
            inicializarNotasFile(carpetaUsuario);
            return notas;
        }

        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(notasFile);
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Ignora líneas vacías
                if (!linea.trim().isEmpty()) {
                    // Convierte la línea a objeto Nota
                    Nota nota = Nota.fromString(linea);
                    if (nota != null) {
                        notas.add(nota);
                    }
                }
            }
        } catch (IOException error) {
            System.out.println("No se pudieron listar las notas: " + error.getMessage());
        } finally {
            // Cierra el reader correctamente
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar reader: " + e.getMessage());
                }
            }
        }

        return notas;
    }

    // Obtiene una nota específica por su número (posición en la lista)
    public Nota obtenerNota(Path carpetaUsuario, int numero) {
        List<Nota> notas = listarNotas(carpetaUsuario);
        // Valida que el número esté en rango 
        if (numero > 0 && numero <= notas.size()) {
            return notas.get(numero - 1);
        }
        return null;
    }

    // Elimina una nota específica por su número
    public boolean eliminarNota(Path carpetaUsuario, int numero) {
        List<Nota> notas = listarNotas(carpetaUsuario);

        // Valida que el número esté en rango válido
        if (numero < 1 || numero > notas.size()) {
            return false;
        }

        // Elimina la nota de la lista
        notas.remove(numero - 1);

        try {
            Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

            // Reescribe el archivo sin la nota eliminada
            BufferedWriter writer = Files.newBufferedWriter(notasFile, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            try (writer) {
                for (Nota nota : notas) {
                    writer.write(nota.toString() + "\n");
                }
            }

            return true;
        } catch (IOException e) {
            System.out.println("No se pudo eliminar la nota: " + e.getMessage());
            return false;
        }
    }

    // Retorna la cantidad total de notas del usuario
    public int contarNotas(Path carpetaUsuario) {
        return listarNotas(carpetaUsuario).size();
    }

    // Busca notas que contengan el término en título o contenido (case-insensitive)
    public List<Nota> buscarNotas(Path carpetaUsuario, String termino) {
        List<Nota> todasLasNotas = listarNotas(carpetaUsuario);
        List<Nota> resultados = new ArrayList<>();

        // Convierte el término a minúsculas para búsqueda case-insensitive
        String terminoLower = termino.toLowerCase();

        // Filtra las notas que contengan el término
        for (Nota nota : todasLasNotas) {
            if (nota.getTitulo().toLowerCase().contains(terminoLower) ||
                nota.getContenido().toLowerCase().contains(terminoLower)) {
                resultados.add(nota);
            }
        }

        return resultados;
    }

    // Esto edita el título y contenido de una nota específica
    public boolean editarNota(Path carpetaUsuario, int numero, String nuevoTitulo, String nuevoContenido) {
        List<Nota> notas = listarNotas(carpetaUsuario);

        // Valido que el número esté en rango válido
        if (numero < 1 || numero > notas.size()) {
            return false;
        }

        // Reemplaza la nota antigua con la nueva
        notas.set(numero - 1, new Nota(nuevoTitulo, nuevoContenido));

        try {
            Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

            // Reescribe el archivo con la nota actualizada
            BufferedWriter writer = Files.newBufferedWriter(notasFile, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            try (writer) {
                for (Nota nota : notas) {
                    writer.write(nota.toString() + "\n");
                }
            }

            return true;
        } catch (IOException e) {
            System.out.println("No se pudo editar la nota: " + e.getMessage());
            return false;
        }
    }
}
