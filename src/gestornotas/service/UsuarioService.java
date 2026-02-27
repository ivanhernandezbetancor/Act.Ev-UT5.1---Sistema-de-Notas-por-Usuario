package gestornotas.service;

import gestornotas.model.Usuario;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;

public class UsuarioService {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.txt");
    private static final Path USUARIOS_DIR = DATA_DIR.resolve("usuarios");

    public UsuarioService() {
        initDirectories();
    }

    // Inicializar directorios necesarios
    private void initDirectories() {
        try {
            if (!Files.exists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
            if (!Files.exists(USUARIOS_DIR)) {
                Files.createDirectories(USUARIOS_DIR);
            }
            if (!Files.exists(USERS_FILE)) {
                Files.createFile(USERS_FILE);
            }
        } catch (IOException e) {
            System.out.println("Error al inicializar directorios: " + e.getMessage());
        }
    }

    // Registrar nuevo usuario
    public boolean registrar(String email, String password) {
        // Validar que no exista
        if (usuarioExiste(email)) {
            return false;
        }

        // Validar que no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            return false;
        }

        try {
            // Guardar en users.txt
            Usuario usuario = new Usuario(email, password);
            String linea = usuario.toString() + "\n";

            try (var writer = Files.newBufferedWriter(USERS_FILE, StandardOpenOption.APPEND)) {
                writer.write(linea);
            }

            // Crear carpeta del usuario
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

    // Verificar si el usuario existe
    public boolean usuarioExiste(String email) {
        try (var reader = Files.newBufferedReader(USERS_FILE)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String emailEnArchivo = linea.split(";")[0];
                if (emailEnArchivo.equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
        }
        return false;
    }

    // Login: verificar credenciales
    public Usuario login(String email, String password) {
        try (var reader = Files.newBufferedReader(USERS_FILE)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 2) {
                    String emailEnArchivo = partes[0];
                    String passwordEnArchivo = partes[1];

                    if (emailEnArchivo.equals(email) && passwordEnArchivo.equals(password)) {
                        return new Usuario(email, password);
                    }
                }
            }
        } catch (IOException error) {
            System.out.println("Error al hacer login: " + error.getMessage());
        }
        return null;
    }

    // Obtener carpeta del usuario
    public Path getCarpetaUsuario(String email) {
        Usuario usuario = new Usuario(email, "");
        return USUARIOS_DIR.resolve(usuario.sanitizeEmail());
    }
}