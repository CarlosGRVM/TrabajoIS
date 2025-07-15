/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import modelo.UsuarioDAO; // Importar la nueva clase DAO
import javax.swing.JOptionPane;

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

    public boolean actualizarContrasena(String email, String nuevaContrasena) {
        return usuarioDAO.actualizarContrasenaUsuario(email, nuevaContrasena);
    }
}