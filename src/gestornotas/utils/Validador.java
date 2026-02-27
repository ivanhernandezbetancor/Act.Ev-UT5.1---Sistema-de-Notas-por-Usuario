package gestornotas.utils;

public class Validador {

    // Validar que el email tenga un formato básico
    public static boolean esEmailValido(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }

    // Validar que la contraseña no esté vacía
    public static boolean esPasswordValida(String password) {
        return password != null && !password.isEmpty();
    }

    // Validar que el título no esté vacío
    public static boolean esTituloValido(String titulo) {
        return titulo != null && !titulo.isEmpty();
    }

    // Validar que el contenido no esté vacío
    public static boolean esContenidoValido(String contenido) {
        return contenido != null && !contenido.isEmpty();
    }

    // Validar que un número sea válido
    public static boolean esNumeroValido(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException error) {
            return false;
        }
    }
}
