package gestornotas.app;

import gestornotas.model.Usuario;
import gestornotas.model.Nota;
import gestornotas.service.UsuarioService;
import gestornotas.service.NotaService;
import gestornotas.utils.Consola;
import gestornotas.utils.Validador;
import java.nio.file.Path;
import java.util.List;

// Clase principal que controla el flujo de la aplicación
public class Main {
    // Servicio para gestionar usuarios
    private static UsuarioService usuarioService;
    // Servicio para gestionar notas
    private static NotaService notaService;
    // Usuario actualmente autenticado en la aplicación
    private static Usuario usuarioActual;

    // Punto de entrada de la aplicación
    public static void main(String[] args) {
        // Inicializa los servicios
        usuarioService = new UsuarioService();
        notaService = new NotaService();

        boolean continuar = true;

        // Bucle principal de la aplicación
        while (continuar) {
            // Si no hay usuario autenticado, muestra menú principal
            if (usuarioActual == null) {
                continuar = menuPrincipal();
            } else {
                // Si hay usuario autenticado, muestra menú de usuario
                continuar = menuUsuario();
            }
        }

        // Mensaje de despedida
        Consola.mostrarInfo("¡ Hasta luego !");
        // Cierra los recursos
        Consola.cerrar();
    }

    private static boolean menuPrincipal() {
        Consola.mostrarMenuPrincipal();
        String opcion = Consola.leerLinea();

        // Procesa la opción seleccionada
        switch (opcion) {
            case "1":
                // Registra un nuevo usuario
                registrarse();
                break;
            case "2":
                // Inicia sesión de un usuario existente
                iniciarSesion();
                break;
            case "0":
                // Salir de la aplicación
                return false;
            default:
                // Opción no válida
                Consola.mostrarError(" Opción no válida ");
        }

        return true;
    }

    // Menú del usuario autenticado
    private static boolean menuUsuario() {
        // Muestra las opciones del menú de usuario
        Consola.mostrarMenuUsuario(usuarioActual.getEmail());
        String opcion = Consola.leerLinea();

        // Procesa la opción seleccionada
        switch (opcion) {
            case "1":
                // Crea una nueva nota
                crearNota();
                break;
            case "2":
                // Lista todas las notas del usuario
                listarNotas();
                break;
            case "3":
                // Ve una nota específica por número
                verNotaPorNumero();
                break;
            case "4":
                // Elimina una nota específica
                eliminarNota();
                break;
            case "0":
                // Cierra sesión
                usuarioActual = null;
                Consola.mostrarInfo(" Sesión cerrada ");
                break;
            default:
                // Opción no válida
                Consola.mostrarError(" Opción no válida ");
        }

        return true;
    }

    // Gestiona el registro de un nuevo usuario
    private static void registrarse() {
        // Muestra la sección de registro
        Consola.mostrarSeparador();
        System.out.println(" REGISTRO ");
        Consola.mostrarSeparador();

        // Se solicita el email
        Consola.mostrarPregunta(" Email ");
        String email = Consola.leerLinea();

        // Valida que el email tenga un formato válido
        if (!Validador.esEmailValido(email)) {
            Consola.mostrarError(" Email no válido ");
            return;
        }

        // Verifica que el email no esté ya registrado
        if (usuarioService.usuarioExiste(email)) {
            Consola.mostrarError(" El email ya está registrado ");
            return;
        }

        // Solicita la contraseña
        Consola.mostrarPregunta(" Contraseña ");
        String password = Consola.leerPassword();

        // Valida que la contraseña cumpla con los requisitos
        if (!Validador.esPasswordValida(password)) {
            Consola.mostrarError(" Contraseña no válida ");
            return;
        }

        // Intenta registrar el usuario
        if (usuarioService.registrar(email, password)) {
            Consola.mostrarExito(" Usuario registrado correctamente ");
        } else {
            Consola.mostrarError(" Error al registrar el usuario ");
        }
    }

    // Gestiona el inicio de sesión de un usuario
    private static void iniciarSesion() {
        // Muestra la sección de login
        Consola.mostrarSeparador();
        System.out.println(" INICIAR SESIÓN ");
        Consola.mostrarSeparador();

        // Se solicita el email
        Consola.mostrarPregunta(" Email ");
        String email = Consola.leerLinea();

        // Se solicita la contraseña
        Consola.mostrarPregunta(" Contraseña ");
        String password = Consola.leerPassword();

        // Intenta autenticar al usuario
        Usuario usuario = usuarioService.login(email, password);

        // Si la autenticación es exitosa, establece el usuario actual
        if (usuario != null) {
            usuarioActual = usuario;
            Consola.mostrarExito(" Sesión iniciada correctamente ");
        } else {
            // Credenciales inválidas
            Consola.mostrarError(" Email o contraseña incorrectos ");
        }
    }

    // Gestiona la creación de una nueva nota
    private static void crearNota() {
        // Muestra la sección de crear nota
        Consola.mostrarSeparador();
        System.out.println(" CREAR NOTA ");
        Consola.mostrarSeparador();

        // Se solicita el título
        Consola.mostrarPregunta(" Título ");
        String titulo = Consola.leerLinea();

        // Valida que el título sea válido
        if (!Validador.esTituloValido(titulo)) {
            Consola.mostrarError(" Título no válido ");
            return;
        }

        // Se solicita el contenido
        Consola.mostrarPregunta(" Contenido ");
        String contenido = Consola.leerLinea();

        // Valida que el contenido sea válido
        if (!Validador.esContenidoValido(contenido)) {
            Consola.mostrarError(" Contenido no válido ");
            return;
        }

        // Obtiene la carpeta del usuario actual
        Path carpetaUsuario = usuarioService.getCarpetaUsuario(usuarioActual.getEmail());

        // Intenta crear la nota
        if (notaService.crearNota(carpetaUsuario, titulo, contenido)) {
            Consola.mostrarExito(" Nota creada correctamente ");
        } else {
            Consola.mostrarError("Error al crear la nota");
        }
    }

    // Gestiona el listado de todas las notas del usuario
    private static void listarNotas() {
        // Muestra la sección de notas
        Consola.mostrarSeparador();
        System.out.println(" MIS NOTAS ");
        Consola.mostrarSeparador();

        // Obtiene la carpeta del usuario y lista sus notas
        Path carpetaUsuario = usuarioService.getCarpetaUsuario(usuarioActual.getEmail());
        List<Nota> notas = notaService.listarNotas(carpetaUsuario);

        // Si no hay notas, lo indica
        if (notas.isEmpty()) {
            Consola.mostrarInfo(" No tienes notas ");
        } else {
            // Muestra el título de cada nota numerada
            for (int i = 0; i < notas.size(); i++) {
                Nota nota = notas.get(i);
                System.out.println((i + 1) + ". " + nota.getTitulo());
            }
        }

        Consola.mostrarSeparador();
    }

    // Gestiono la visualización de una nota específica por número
    private static void verNotaPorNumero() {
        // Muestra la sección de ver nota
        Consola.mostrarSeparador();
        System.out.println(" VER NOTA ");
        Consola.mostrarSeparador();

        // Obtiene la carpeta del usuario
        Path carpetaUsuario = usuarioService.getCarpetaUsuario(usuarioActual.getEmail());
        // Cuenta cuántas notas tiene el usuario
        int cantidad = notaService.contarNotas(carpetaUsuario);

        // Si no hay notas, lo indica
        if (cantidad == 0) {
            Consola.mostrarInfo(" No tienes notas ");
            return;
        }

        // Solicita el número de nota a visualizar
        Consola.mostrarPregunta(" Número de nota (1-" + cantidad + ") ");
        String input = Consola.leerLinea();

        // Valido que el input sea un número válido
        if (!Validador.esNumeroValido(input)) {
            Consola.mostrarError(" Número no válido ");
            return;
        }

        // Convierte el input a número entero
        int numero = Integer.parseInt(input);
        // Obtiene la nota del servicio
        Nota nota = notaService.obtenerNota(carpetaUsuario, numero);

        // Si la nota existe, la muestra
        if (nota != null) {
            Consola.mostrarSeparador();
            System.out.println("Título: " + nota.getTitulo());
            System.out.println("Contenido: " + nota.getContenido());
            Consola.mostrarSeparador();
        } else {
            // Nota no encontrada
            Consola.mostrarError(" Nota no encontrada ");
        }
    }

    // Gestiona la eliminación de una nota específica
    private static void eliminarNota() {
        // Muestra la sección de eliminar nota
        Consola.mostrarSeparador();
        System.out.println(" ELIMINAR NOTA ");
        Consola.mostrarSeparador();

        // Obtiene la carpeta del usuario
        Path carpetaUsuario = usuarioService.getCarpetaUsuario(usuarioActual.getEmail());
        // Cuenta cuántas notas tiene el usuario
        int cantidad = notaService.contarNotas(carpetaUsuario);

        // Si no hay notas, lo indica
        if (cantidad == 0) {
            Consola.mostrarInfo(" No tienes notas intentalo de nuevo ");
            return;
        }

        // se solicita el número de nota a eliminar
        Consola.mostrarPregunta(" Número de nota a eliminar (1-" + cantidad + ") ");
        String input = Consola.leerLinea();

        // Esto valida que el input sea un número válido
        if (!Validador.esNumeroValido(input)) {
            Consola.mostrarError(" Número no válido ");
            return;
        }

        // Convierte el input a número entero
        int numero = Integer.parseInt(input);

        // Intenta eliminar la nota
        if (notaService.eliminarNota(carpetaUsuario, numero)) {
            Consola.mostrarExito( "Nota eliminada correctamente ");
        } else {
            Consola.mostrarError(" No se pudo eliminar la nota ");
        }
    }
}
