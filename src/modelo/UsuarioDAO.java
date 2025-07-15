/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import modelo.ConexionSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UsuarioDAO {
    private ConexionSQL conexionBD;

    public UsuarioDAO() {
        conexionBD = new ConexionSQL();
    }

    public boolean existeUsuarioPorCorreo(String email) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;
        try {
            con = conexionBD.conectar();
            String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?"; // Asume una tabla 'usuarios' y columna 'correo'
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                existe = rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al verificar correo en BD: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close(); // Cierra la conexión aquí
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return existe;
    }

    public boolean actualizarContrasenaUsuario(String email, String nuevaContrasena) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean actualizado = false;
        try {
            con = conexionBD.conectar();
            // ¡IMPORTANTE!: Considera encriptar la contraseña antes de almacenarla
            String sql = "UPDATE usuario SET contrasena = ? WHERE correo = ?"; // Asume columna 'password'
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevaContrasena); // Aquí deberías pasar la contraseña encriptada
            ps.setString(2, email);
            int filasAfectadas = ps.executeUpdate();
            actualizado = filasAfectadas > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar contraseña en BD: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close(); // Cierra la conexión aquí
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return actualizado;
    }
}
