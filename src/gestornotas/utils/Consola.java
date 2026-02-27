package gestornotas.utils;

import java.util.Scanner;

// Clase utilidad que maneja toda la interacción con la consola
public class Consola {
    
    private static Scanner scanner = new Scanner(System.in);

    // Muestro una línea divisoria en la consola
    public static void mostrarSeparador() {
        System.out.println("=====================");
    }

    // Muestro el menú principal de la aplicación (antes de autenticarse)
    public static void mostrarMenuPrincipal() {
        System.out.println("\n");
        mostrarSeparador();
        System.out.println(" SISTEMA DE NOTAS");
        mostrarSeparador();
        System.out.println("  1. Registrarse");
        System.out.println("  2. Iniciar sesion");
        System.out.println("  0. Salir");
        mostrarSeparador();
        System.out.print("Selecciona una opcion: ");
    }

    // Muestro el menú del usuario autenticado con su email
    public static void mostrarMenuUsuario(String email) {
        System.out.println("\n");
        mostrarSeparador();
        System.out.println(" MENU DE USUARIO");
        mostrarSeparador();
        System.out.println(" Conectado: " + email);
        mostrarSeparador();
        System.out.println(" 1. Crear nota");
        System.out.println(" 2. Listar notas");
        System.out.println(" 3. Ver nota por numero");
        System.out.println(" 4. Eliminar nota");
        System.out.println(" 0. Cerrar sesion");
        mostrarSeparador();
        System.out.print("Selecciona una opcion: ");
    }

    // Lee una línea de entrada del usuario (sin caracteres de espacio al inicio/final)
    public static String leerLinea() {
        return scanner.nextLine().trim();
    }

    // Lee la contraseña del usuario (actualmente igual a leerLinea, pero permite cambios futuros)
    public static String leerPassword() {
        return scanner.nextLine().trim();
    }

    // Muestro un mensaje de error en la consola
    public static void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    // Muestro un mensaje de éxito en la consola
    public static void mostrarExito(String mensaje) {
        System.out.println("[OK] " + mensaje);
    }

    // Muestro un mensaje informativo en la consola
    public static void mostrarInfo(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }

    // Muestro una pregunta en la consola (sin salto de línea al final)
    public static void mostrarPregunta(String pregunta) {
        System.out.print(pregunta + ": ");
    }

    public static void cerrar() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
