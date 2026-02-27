package gestornotas.app;

import gestornotas.model.Usuario;
import gestornotas.model.Nota;
import gestornotas.service.UsuarioService;
import gestornotas.service.NotaService;
import gestornotas.utils.Consola;
import gestornotas.utils.Validador;
import java.nio.file.Path;
import java.util.List;

public class Main {
    private static UsuarioService usuarioService;
    private static NotaService notaService;
    private static Usuario usuarioActual;

    public static void main(String[] args) {
        usuarioService = new UsuarioService();
        notaService = new NotaService();

        boolean continuar = true;

        while (continuar) {
            if (usuarioActual == null) {
                continuar = menuPrincipal();
            } else {
                continuar = menuUsuario();
            }
        }

        Consola.mostrarInfo("¡ Hasta luego !");
        Consola.cerrar();
    }

    private static boolean menuPrincipal() {
        Consola.mostrarMenuPrincipal();
        String opcion = Consola.leerLinea();

        switch (opcion) {
            case "1":
                registrarse();
                break;
            case "2":
                iniciarSesion();
                break;
            case "0":
                return false;
            default:
                Consola.mostrarError(" Opción no válida ");
        }

        return true;
    }

    private static boolean menuUsuario() {
        Consola.mostrarMenuUsuario(usuarioActual.getEmail());
        String opcion = Consola.leerLinea();

        switch (opcion) {
            case "1":
                crearNota();
                break;
            case "2":
                listarNotas();
                break;
            case "3":
                verNotaPorNumero();
                break;
            case "4":
                eliminarNota();
                break;
            case "0":
                usuarioActual = null;
                Consola.mostrarInfo(" Sesión cerrada ");
                break;
            default:
                Consola.mostrarError(" Opción no válida ");
        }

        return true;
    }

    private static void registrarse() {
        Consola.mostrarSeparador();
        System.out.println(" REGISTRO ");
        Consola.mostrarSeparador();

        Consola.mostrarPregunta(" Email ");
        String email = Consola.leerLinea();

        if (!Validador.esEmailValido(email)) {
            Consola.mostrarError(" Email no válido ");
            return;
        }

        if (usuarioService.usuarioExiste(email)) {
            Consola.mostrarError(" El email ya está registrado ");
            return;
        }

        Consola.mostrarPregunta(" Contraseña ");
        String password = Consola.leerPassword();

        if (!Validador.esPasswordValida(password)) {
            Consola.mostrarError(" Contraseña no válida ");
            return;
        }

        if (usuarioService.registrar(email, password)) {
            Consola.mostrarExito(" Usuario registrado correctamente ");
        } else {
            Consola.mostrarError(" Error al registrar el usuario ");
        }
    }

    private static void iniciarSesion() {
        Consola.mostrarSeparador();
        System.out.println(" INICIAR SESIÓN ");
        Consola.mostrarSeparador();

        Consola.mostrarPregunta(" Email ");
        String email = Consola.leerLinea();

        Consola.mostrarPregunta(" Contraseña ");
        String password = Consola.leerPassword();

        Usuario usuario = usuarioService.login(email, password);

        if (usuario != null) {
            usuarioActual = usuario;
            Consola.mostrarExito(" Sesión iniciada correctamente ");
        } else {
            Consola.mostrarError(" Email o contraseña incorrectos ");
        }
    }

    private static void crearNota() {
        Consola.mostrarSeparador();
        System.out.println(" CREAR NOTA ");
        Consola.mostrarSeparador();

        Consola.mostrarPregunta(" Título ");
        String titulo = Consola.leerLinea();

        if (!Validador.esTituloValido(titulo)) {
            Consola.mostrarError(" Título no válido ");
            return;
        }

        Consola.mostrarPregunta(" Contenido ");
        String contenido = Consola.leerLinea();

        if (!Validador.esContenidoValido(contenido)) {
            Consola.mostrarError(" Contenido no válido ");
            return;
        }

        Path carpetaUsuario = usuarioService.getCarpetaUsuario(usuarioActual.getEmail());

        if (notaService.crearNota(carpetaUsuario, titulo, contenido)) {
            Consola.mostrarExito(" Nota creada correctamente ");
        } else {
            Consola.mostrarError("Error al crear la nota");
        }
    }

    private static void listarNotas() {
        Consola.mostrarSeparador();
        System.out.println(" MIS NOTAS ");
        Consola.mostrarSeparador();

        Path carpetaUsuario = usuarioService.getCarpetaUsuario(usuarioActual.getEmail());
        List<Nota> notas = notaService.listarNotas(carpetaUsuario);

        if (notas.isEmpty()) {
            Consola.mostrarInfo(" No tienes notas ");
        } else {
            for (int i = 0; i < notas.size(); i++) {
                Nota nota = notas.get(i);
                System.out.println((i + 1) + ". " + nota.getTitulo());
            }
        }

        Consola.mostrarSeparador();
    }

    private static void verNotaPorNumero() {
        Consola.mostrarSeparador();
        System.out.println(" VER NOTA ");
        Consola.mostrarSeparador();

        Path carpetaUsuario = usuarioService.getCarpetaUsuario(usuarioActual.getEmail());
        int cantidad = notaService.contarNotas(carpetaUsuario);

        if (cantidad == 0) {
            Consola.mostrarInfo(" No tienes notas ");
            return;
        }

        Consola.mostrarPregunta(" Número de nota (1-" + cantidad + ") ");
        String input = Consola.leerLinea();

        if (!Validador.esNumeroValido(input)) {
            Consola.mostrarError(" Número no válido ");
            return;
        }

        int numero = Integer.parseInt(input);
        Nota nota = notaService.obtenerNota(carpetaUsuario, numero);

        if (nota != null) {
            Consola.mostrarSeparador();
            System.out.println("Título: " + nota.getTitulo());
            System.out.println("Contenido: " + nota.getContenido());
            Consola.mostrarSeparador();
        } else {
            Consola.mostrarError(" Nota no encontrada ");
        }
    }

    private static void eliminarNota() {
        Consola.mostrarSeparador();
        System.out.println(" ELIMINAR NOTA ");
        Consola.mostrarSeparador();

        Path carpetaUsuario = usuarioService.getCarpetaUsuario(usuarioActual.getEmail());
        int cantidad = notaService.contarNotas(carpetaUsuario);

        if (cantidad == 0) {
            Consola.mostrarInfo(" No tienes notas intentalo de nuevo ");
            return;
        }

        Consola.mostrarPregunta(" Número de nota a eliminar (1-" + cantidad + ") ");
        String input = Consola.leerLinea();

        if (!Validador.esNumeroValido(input)) {
            Consola.mostrarError(" Número no válido ");
            return;
        }

        int numero = Integer.parseInt(input);

        if (notaService.eliminarNota(carpetaUsuario, numero)) {
            Consola.mostrarExito( "Nota eliminada correctamente ");
        } else {
            Consola.mostrarError(" No se pudo eliminar la nota ");
        }
    }
}
