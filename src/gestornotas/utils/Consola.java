package gestornotas.utils;

import java.util.Scanner;

public class Consola {
    private static Scanner scanner = new Scanner(System.in);

    // Mostrar línea separadora
    public static void mostrarSeparador() {
        System.out.println("=====================");
    }

    // Mostrar menú principal
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

    // Mostrar menú de usuario
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

    // Leer entrada del usuario
    public static String leerLinea() {
        return scanner.nextLine().trim();
    }

    // Leer contraseña 
    public static String leerPassword() {
        return scanner.nextLine().trim();
    }

    // Mostrar mensaje de error
    public static void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    // Mostrar mensaje de éxito
    public static void mostrarExito(String mensaje) {
        System.out.println("[OK] " + mensaje);
    }

    // Mostrar mensaje informativo
    public static void mostrarInfo(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }

    // Mostrar pregunta
    public static void mostrarPregunta(String pregunta) {
        System.out.print(pregunta + ": ");
    }

    // Cerrar scanner
    public static void cerrar() {
        if (scanner != null) {
            scanner.close();
        }
    }
}