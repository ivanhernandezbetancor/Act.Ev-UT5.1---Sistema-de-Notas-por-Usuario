package gestornotas.model;

public class Usuario {
    private String email;
    private String password;

    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return email + ";" + password;
    }

    // Método para convertir email en nombre válido de carpeta
    public String sanitizeEmail() {
        return email.replace("@", "_").replace(".", "_");
    }
}
