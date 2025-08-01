package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {
    private String correo;
    private String contrasena;
    private String nombre; // ← se llena al iniciar sesión correctamente
    public Usuario(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }
    /**
     * Verifica si existe un registro en la tabla 'login' con las credenciales proporcionadas. (Este método parece estar en desuso si estás usando la tabla 'usuario')
     */
    public boolean validarRegistro() {
        try (Connection connection = ConexionSQL.conectar(); PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM login WHERE usuario = ? AND contrasena = ?")) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("❌ Error en validarRegistro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inicia sesión validando en la tabla 'usuario' con SHA2 para la contraseña. Si es exitoso, también guarda el nombre del usuario.
     */
  public boolean iniciarSesion() {
    String sql = "SELECT nombre FROM usuario WHERE correo = ? AND contrasena = ?";

    try (Connection conexion = ConexionSQL.conectar(); PreparedStatement stmt = conexion.prepareStatement(sql)) {

        if (conexion == null) {
            System.err.println("❌ No hay conexión a la base de datos.");
            return false;
        }

        String hashJava = encriptarSHA256(contrasena); // usa la función Java
        stmt.setString(1, correo);
        stmt.setString(2, hashJava);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            nombre = rs.getString("nombre"); // Guarda el nombre
            return true;
        }

    } catch (SQLException e) {
        System.err.println("⚠ Error al iniciar sesión: " + e.getMessage());
    }

    return false;
}


    public static String encriptarSHA256(String input) {
    try {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasena() {
        return contrasena;
    }


}