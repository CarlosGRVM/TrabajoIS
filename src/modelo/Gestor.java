/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;



public class Gestor {
    private UsuarioDAO usuarioDAO;
    private Correo correo;

    public Gestor() {
        this.usuarioDAO = new UsuarioDAO();
        this.correo = new Correo();
    }

    public boolean verificarCorreoExistente(String email) {
        return usuarioDAO.existeUsuarioPorCorreo(email);
    }

    public boolean enviarCodigoRecuperacion(String email) {
        String codigo = Codigo.generarCodigo();
        Codigo.setCodigoGenerado(codigo);
        Codigo.setCorreoAsociado(email);

        String asunto = "Código de recuperación de contraseña";
        String mensaje = "Hola,\n\nTu código de recuperación es: " + codigo + "\n\nIgnora este correo si no lo solicitaste.";

        return correo.enviarCorreo(email, asunto, mensaje);
    }

    public boolean verificarCodigo(String codigoIngresado) {
        return Codigo.getCodigoGenerado() != null && Codigo.getCodigoGenerado().equals(codigoIngresado);
    }

public boolean actualizarContrasena(String correo, String nuevaContrasena) {
    String sql = "UPDATE usuario SET contrasena = ? WHERE correo = ?";
String hash = encriptarSHA256(nuevaContrasena);  // <--- Encriptamos antes de guardar
try (Connection con = ConexionSQL.conectar();
     PreparedStatement ps = con.prepareStatement(sql)) {
     
    ps.setString(1, hash);  // <--- Guardamos el hash
    ps.setString(2, correo);

        int rows = ps.executeUpdate();
        return rows > 0;
    } catch (SQLException e) {
        System.err.println("❌ Error al actualizar contraseña: " + e.getMessage());
        return false;
    }
}

public static String encriptarSHA256(String input) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
        System.err.println("Error al encriptar: " + e.getMessage());
        return null;
    }
}

  


}