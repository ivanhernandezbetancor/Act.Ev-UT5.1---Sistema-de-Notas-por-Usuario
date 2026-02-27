package gestornotas.model;

public class Nota {
    private String titulo;
    private String contenido;

    public Nota(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @Override
    public String toString() {
        return titulo + ";" + contenido;
    }

    public static Nota fromString(String linea) {
        String[] partes = linea.split(";", 2);
        if (partes.length == 2) {
            return new Nota(partes[0], partes[1]);
        }
        return null;
    }
}
