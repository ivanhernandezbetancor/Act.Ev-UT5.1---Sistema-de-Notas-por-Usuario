package gestornotas.service;

import gestornotas.model.Usuario;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;

public class UsuarioService {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.txt");
    private static final Path USUARIOS_DIR = DATA_DIR.resolve("usuarios");

    public UsuarioService() {
        initDirectories();
    }

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

    public boolean registrar(String email, String password) {
        if (usuarioExiste(email)) {
            return false;
        }

        if (email.isEmpty() || password.isEmpty()) {
            return false;
        }

        try {
            Usuario usuario = new Usuario(email, password);
            String linea = usuario.toString() + "\n";

            BufferedWriter writer = Files.newBufferedWriter(USERS_FILE, StandardOpenOption.APPEND);
            try (writer) {
                writer.write(linea);
            }

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

    public boolean usuarioExiste(String email) {
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(USERS_FILE);
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                
                String[] partes = linea.split(";");
                if (partes.length >= 1) {
                    String emailEnArchivo = partes[0];
                    if (emailEnArchivo.equals(email)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al verificar usuario: " + e.getMessage());
        } finally {
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

    public Usuario login(String email, String password) {
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(USERS_FILE);
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                
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
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar reader: " + e.getMessage());
                }
            }
        }
        return null;
    }

    public Path getCarpetaUsuario(String email) {
        Usuario usuario = new Usuario(email, "");
        return USUARIOS_DIR.resolve(usuario.sanitizeEmail());
    }
}
