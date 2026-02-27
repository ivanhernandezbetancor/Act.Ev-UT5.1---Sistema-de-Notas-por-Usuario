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

public class NotaService {
    private static final String NOTAS_FILENAME = "notas.txt";

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

    public boolean crearNota(Path carpetaUsuario, String titulo, String contenido) {
        if (titulo.isEmpty() || contenido.isEmpty()) {
            return false;
        }

        try {
            inicializarNotasFile(carpetaUsuario);
            
            Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

            Nota nota = new Nota(titulo, contenido);
            String linea = nota.toString() + "\n";

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

    public List<Nota> listarNotas(Path carpetaUsuario) {
        List<Nota> notas = new ArrayList<>();
        Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

        if (!Files.exists(notasFile)) {
            inicializarNotasFile(carpetaUsuario);
            return notas;
        }

        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(notasFile);
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
        } finally {
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

    public Nota obtenerNota(Path carpetaUsuario, int numero) {
        List<Nota> notas = listarNotas(carpetaUsuario);
        if (numero > 0 && numero <= notas.size()) {
            return notas.get(numero - 1);
        }
        return null;
    }

    public boolean eliminarNota(Path carpetaUsuario, int numero) {
        List<Nota> notas = listarNotas(carpetaUsuario);

        if (numero < 1 || numero > notas.size()) {
            return false;
        }

        notas.remove(numero - 1);

        try {
            Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

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

    public int contarNotas(Path carpetaUsuario) {
        return listarNotas(carpetaUsuario).size();
    }

    public List<Nota> buscarNotas(Path carpetaUsuario, String termino) {
        List<Nota> todasLasNotas = listarNotas(carpetaUsuario);
        List<Nota> resultados = new ArrayList<>();

        String terminoLower = termino.toLowerCase();

        for (Nota nota : todasLasNotas) {
            if (nota.getTitulo().toLowerCase().contains(terminoLower) ||
                nota.getContenido().toLowerCase().contains(terminoLower)) {
                resultados.add(nota);
            }
        }

        return resultados;
    }

    public boolean editarNota(Path carpetaUsuario, int numero, String nuevoTitulo, String nuevoContenido) {
        List<Nota> notas = listarNotas(carpetaUsuario);

        if (numero < 1 || numero > notas.size()) {
            return false;
        }

        notas.set(numero - 1, new Nota(nuevoTitulo, nuevoContenido));

        try {
            Path notasFile = carpetaUsuario.resolve(NOTAS_FILENAME);

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
