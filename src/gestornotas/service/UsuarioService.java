package gestornotas.service;

import gestornotas.model.Usuario;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;

// Servicio que gestiona las operaciones de usuarios (registro, login, validación)
public class UsuarioService {
    // Ruta del directorio principal de datos
    private static final Path DATA_DIR = Paths.get("data");
    // Archivo que almacena los usuarios registrados
    private static final Path USERS_FILE = DATA_DIR.resolve("users.txt");
    // Directorio que contiene las carpetas individuales de cada usuario
    private static final Path USUARIOS_DIR = DATA_DIR.resolve("usuarios");

    // Constructor que inicializa los directorios necesarios
    public UsuarioService() {
        initDirectories();
    }

    // Creo los directorios y archivos necesarios si no existen
    private void initDirectories() {
        try {
            // Creo el directorio de datos si no existe
            if (!Files.exists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
            // Creo el directorio de usuarios si no existe
            if (!Files.exists(USUARIOS_DIR)) {
                Files.createDirectories(USUARIOS_DIR);
            }
            // Creo el archivo de usuarios si no existe
            if (!Files.exists(USERS_FILE)) {
                Files.createFile(USERS_FILE);
            }
        } catch (IOException e) {
            System.out.println("Error al inicializar directorios: " + e.getMessage());
        }
    }

    // Registro un nuevo usuario en el sistema
    public boolean registrar(String email, String password) {
        // Valida que el usuario no exista
        if (usuarioExiste(email)) {
            return false;
        }

        // Valida que el email y contraseña no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            return false;
        }

        try {
            // Creo el nuevo usuario
            Usuario usuario = new Usuario(email, password);
            String linea = usuario.toString() + "\n";

            // Escribo el usuario en el archivo de usuarios
            BufferedWriter writer = Files.newBufferedWriter(USERS_FILE, StandardOpenOption.APPEND);
            try (writer) {
                writer.write(linea);
            }

            // Creo una carpeta individual para el usuario
            Path carpetaUsuario = USUARIOS_DIR.resolve(usuario.sanitizeEmail());
            if (!Files.exists(carpetaUsuario)) {
                Files.createDirectory(carpetaUsuario);
            }

            return true;
        } catch (IOException error) {
            System.out.println("Error al registrar: " + error.getMessage());
            return false;
        }
    }

    // Verifica si un usuario ya existe en el sistema
    public boolean usuarioExiste(String email) {
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(USERS_FILE);
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Ignora líneas vacías
                if (linea.trim().isEmpty()) {
                    continue;
                }
                
                // Divide la línea por el delimitador ";"
                String[] partes = linea.split(";");
                if (partes.length >= 1) {
                    String emailEnArchivo = partes[0];
                    // Compara el email
                    if (emailEnArchivo.equals(email)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
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
        return false;
    }

    // Valida las credenciales de un usuario y lo autentica
    public Usuario login(String email, String password) {
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(USERS_FILE);
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Ignora líneas vacías
                if (linea.trim().isEmpty()) {
                    continue;
                }
                
                // Divide la línea por el delimitador ";"
                String[] partes = linea.split(";");
                if (partes.length == 2) {
                    String emailEnArchivo = partes[0];
                    String passwordEnArchivo = partes[1];

                    // Valida que email y contraseña coincidan
                    if (emailEnArchivo.equals(email) && passwordEnArchivo.equals(password)) {
                        return new Usuario(email, password);
                    }
                }
            }
        } catch (IOException error) {
            System.out.println("Error al hacer login: " + error.getMessage());
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
        // Retorna null si las credenciales no son válidas
        return null;
    }

    // Obtiene la ruta de la carpeta de notas de un usuario
    public Path getCarpetaUsuario(String email) {
        Usuario usuario = new Usuario(email, "");
        return USUARIOS_DIR.resolve(usuario.sanitizeEmail());
    }
}
