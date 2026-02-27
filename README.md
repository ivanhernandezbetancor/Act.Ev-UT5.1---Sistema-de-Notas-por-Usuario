## Act.Ev-UT5.1---Sistema-de-Notas-por-Usuario

## Descripción

- Un aplicación de consola en Java que permite registrar usuarios, iniciar sesión y gestionar notas personales. Los datos persisten en ficheros de texto.

## Cómo ejecutar

- Compila el proyecto desde la raíz main.java

## Estructura del Proyecto

gestornotas/
├── app/
│   └── Main.java                    # Punto de entrada y lógica de menús
├── model/
│   ├── Usuario.java                 # Modelo de usuario
│   └── Nota.java                    # Modelo de nota
├── service/
│   ├── UsuarioService.java          # Lógica de usuarios
│   └── NotaService.java             # Lógica de notas
└── utils/
    ├── Consola.java                 # Utilidades de consola
    └── Validador.java               # Validaciones de datos

data/
├── users.txt                        # Base de datos de usuarios
└── usuarios/
    ├── email_usuario_1/
    │   └── notas.txt
    └── email_usuario_2/
        └── notas.txt
