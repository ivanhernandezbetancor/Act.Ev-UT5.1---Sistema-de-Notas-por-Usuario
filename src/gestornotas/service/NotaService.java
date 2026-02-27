package gestornotas.service;

import gestornotas.model.Nota;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotaService {
    private static final String NOTAS_FILENAME = "notas.txt";

    // Crear nueva nota
    public boolean crearNota(Path carpetaUsuario, String titulo, String contenido) {
        if (titulo.isEmpty() || contenido.isEmpty()) {
            return false;
        }

        try {
            Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

            Nota nota = new Nota(titulo, contenido);
            String linea = nota.toString() + "\n";

            try (var writer = Files.newBufferedWriter(notasFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(linea);
            }

            return true;
        } catch (IOException e) {
            System.out.println("No se pudo crear la nota: " + e.getMessage());
            return false;
        }
    }

    // Listar todas las notas
    public List<Nota> listarNotas(Path carpetaUsuario) {
        List<Nota> notas = new ArrayList<>();
        Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

        if (!Files.exists(notasFile)) {
            return notas;
        }

        try (var reader = Files.newBufferedReader(notasFile)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Nota nota = Nota.fromString(linea);
                    if (nota != null) {
                        notas.add(nota);
                    }
                }
            }
        } catch (IOException error) {
            System.out.println("No se pudieron listar las notas: " + error.getMessage());
        }

        return notas;
    }

    // Obtener nota por número => índice
    public Nota obtenerNota(Path carpetaUsuario, int numero) {
        List<Nota> notas = listarNotas(carpetaUsuario);
        if (numero > 0 && numero <= notas.size()) {
            return notas.get(numero - 1);
        }
        return null;
    }

    // Eliminar nota por número
    public boolean eliminarNota(Path carpetaUsuario, int numero) {
        List<Nota> notas = listarNotas(carpetaUsuario);

        if (numero < 1 || numero > notas.size()) {
            return false;
        }

        // Eliminar el elemento
        notas.remove(numero - 1);

        // Reescribir el archivo
        try {
            Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

            try (var writer = Files.newBufferedWriter(notasFile, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
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

    // Contar notas
    public int contarNotas(Path carpetaUsuario) {
        return listarNotas(carpetaUsuario).size();
    }
}