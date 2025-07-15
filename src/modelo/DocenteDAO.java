/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;



import modelo.ConexionSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;

public class DocenteDAO {

    public boolean existeDocente(String numeroEmpleado) {
        String sql = "SELECT COUNT(*) FROM docentes WHERE no_empleado = ?";
        try (Connection con = new ConexionSQL().conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, numeroEmpleado);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar número de empleado: " + e.getMessage());
        }
        return false;
    }

    public void guardarDocente(Docente docente) {
        if (existeDocente(docente.getNumeroEmpleado())) {
            JOptionPane.showMessageDialog(null, "La credencial ya existe. No se puede guardar.");
            return;
        }

        String insertarSQL = "INSERT INTO docentes (no_empleado, nombre, apellidoPaterno, apellidoMaterno, correo) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = new ConexionSQL().conectar();
             PreparedStatement pst = con.prepareStatement(insertarSQL)) {

            pst.setString(1, docente.getNumeroEmpleado());
            pst.setString(2, docente.getNombre());
            pst.setString(3, docente.getApellidoPaterno());
            pst.setString(4, docente.getApellidoMaterno());
            pst.setString(5, docente.getCorreo());
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Docente registrado correctamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar docente: " + e.getMessage());
        }
    }
}
