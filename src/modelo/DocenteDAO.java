package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocenteDAO {

    private String normalizarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty() || !telefono.matches("\\d{10,}")) {
            return "000000000"; // Valor por defecto
        }
        return telefono;
    }

    public boolean existeDocente(String numeroEmpleado) {
        String sql = "SELECT COUNT(*) FROM docente WHERE no_empleado = ?";
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, numeroEmpleado);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("Error al verificar número de empleado: " + e.getMessage());
        }
        return false;
    }

    public void guardarDocente(Docente docente) {
        if (existeDocente(docente.getNumeroEmpleado())) {
            JOptionPane.showMessageDialog(null, "El número de empleado ya existe. No se puede registrar.");
            return;
        }

        String sql = "INSERT INTO docente (no_empleado, Nombre, Apellido_p, Apellido_m, Telefono, Correo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, Integer.parseInt(docente.getNumeroEmpleado()));
            pst.setString(2, docente.getNombre());
            pst.setString(3, docente.getApellidoPaterno());
            pst.setString(4, docente.getApellidoMaterno());
            pst.setString(5, normalizarTelefono(docente.getTelefono()));
            pst.setString(6, docente.getCorreo());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Docente registrado correctamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar docente: " + e.getMessage());
        }
    }

    public boolean actualizarDocente(Docente docente) {
        String sql = "UPDATE docente SET Nombre = ?, Apellido_p = ?, Apellido_m = ?, Telefono = ?, Correo = ? WHERE no_empleado = ?";
        try (Connection con = ConexionSQL.conectar();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, docente.getNombre());
            pst.setString(2, docente.getApellidoPaterno());
            pst.setString(3, docente.getApellidoMaterno());
            pst.setString(4, normalizarTelefono(docente.getTelefono()));
            pst.setString(5, docente.getCorreo());
            pst.setInt(6, Integer.parseInt(docente.getNumeroEmpleado()));

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar docente: " + e.getMessage());
            return false;
        }
    }

    public List<Docente> obtenerTodosLosDocentes() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT no_empleado, Nombre, Apellido_p, Apellido_m, Telefono, Correo FROM docente";

        try (Connection con = ConexionSQL.conectar();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Docente d = new Docente(
                    rs.getString("no_empleado"),
                    rs.getString("Nombre"),
                    rs.getString("Apellido_p"),
                    rs.getString("Apellido_m"),
                    rs.getString("Telefono"),
                    rs.getString("Correo")
                );
                lista.add(d);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener docentes: " + e.getMessage());
        }

        return lista;
    }
}
