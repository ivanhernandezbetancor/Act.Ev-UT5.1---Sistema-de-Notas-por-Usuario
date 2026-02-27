## Act.Ev-UT5.1---Sistema-de-Notas-por-Usuario

## Descripción

- Un aplicación de consola en Java que permite registrar usuarios, iniciar sesión y gestionar notas personales. Los datos persisten en ficheros de texto.

## Cómo ejecutar

- Compila el proyecto desde la raíz main.java

## Estructura del Proyecto : 

## Paquete: gestornotas.app
- ArchivoDescripciónMain.javaPunto de entrada, menús principal y de usuario
## Paquete: gestornotas.model
- ArchivoDescripciónUsuario.javaModelo de usuario (email, contraseña)Nota.javaModelo de nota (título, contenido)
## Paquete: gestornotas.service
- ArchivoDescripciónUsuarioService.javaGestión de registro y loginNotaService.javaGestión de CRUD de notas
## Paquete: gestornotas.utils
- ArchivoDescripciónConsola.javaUtilidades de entrada/salidaValidador.javaValidaciones de datos
## Carpeta: data/ (Se crea automáticamente)
- ArchivoDescripciónusers.txtBase de datos de usuarios (auto-generado)usuarios/email_usuario/notas.txtNotas por usuario (auto-generado)
