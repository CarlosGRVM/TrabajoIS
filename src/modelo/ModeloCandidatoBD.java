/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author marthy
 */

public class ModeloCandidatoBD {

public boolean insertar(ModeloCandidato c) {
    String sql = "INSERT INTO candidato (no_control, Nombre, Apellido_p, Apellido_m, Telefono, Correo) VALUES (?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = ConexionSQL.conectar()) {

        if (conn == null || conn.isClosed()) {
            System.out.println("❌ La conexión es nula o está cerrada.");
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (c.getNumControl().isEmpty() || c.getNombre().isEmpty() || 
                c.getApeP().isEmpty() || c.getApeM().isEmpty() || 
                c.getTelefono().isEmpty() || c.getCorreo().isEmpty()) {
                System.out.println("❌ Campos vacíos. Inserción cancelada.");
                return false;
            }

            ps.setString(1, c.getNumControl());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getApeP());
            ps.setString(4, c.getApeM());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getCorreo());

            ps.executeUpdate();
            return true;
        }

    } catch (SQLException e) {
        System.out.println("❌ Error al insertar: " + e.getMessage());
        return false;
    }
}
    public boolean existeNumeroControl(String numControl) {
        String sql = "SELECT COUNT(*) FROM candidato WHERE no_control = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numControl);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al verificar número de control: " + e.getMessage());
        }
        return false;
    }



    public ArrayList<ModeloCandidato> listarTodos() {
        ArrayList<ModeloCandidato> lista = new ArrayList<>();
        String sql = "SELECT * FROM candidato";
        try (Connection conn = ConexionSQL.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new ModeloCandidato(
                    rs.getString("no_control"),
                    rs.getString("Nombre"),
                    rs.getString("Apellido_p"),
                    rs.getString("Apellido_m"),
                    rs.getString("Telefono"),
                    rs.getString("Correo")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<ModeloCandidato> buscar(String filtro) {
        ArrayList<ModeloCandidato> lista = new ArrayList<>();
        String sql = "SELECT * FROM candidato WHERE Nombre LIKE ? OR no_control LIKE ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + filtro + "%");
            ps.setString(2, "%" + filtro + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new ModeloCandidato(
                    rs.getString("no_control"),
                    rs.getString("Nombre"),
                    rs.getString("Apellido_p"),
                    rs.getString("Apellido_m"),
                    rs.getString("Telefono"),
                    rs.getString("Correo")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }

        return lista;
    }
    public List<ModeloCandidato> obtenerTodos() {
    List<ModeloCandidato> lista = new ArrayList<>();
    String sql = "SELECT * FROM candidato";

    try (Connection conn = ConexionSQL.conectar();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            lista.add(new ModeloCandidato(
                rs.getString("no_control"),
                rs.getString("Nombre"),
                rs.getString("Apellido_p"),
                rs.getString("Apellido_m"),
                rs.getString("Telefono"),
                rs.getString("Correo")
            ));
        }

    } catch (SQLException e) {
        System.out.println("❌ Error al listar: " + e.getMessage());
    }

    return lista;
}
public ModeloCandidato buscarPorNoControl(String noControl) {
    String sql = "SELECT * FROM candidato WHERE no_control = ?";

    try (Connection conn = ConexionSQL.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, noControl);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new ModeloCandidato(
                rs.getString("no_control"),
                rs.getString("Nombre"),
                rs.getString("Apellido_p"),
                rs.getString("Apellido_m"),
                rs.getString("Telefono"),
                rs.getString("Correo")
            );
        }

    } catch (SQLException e) {
        System.out.println("❌ Error al buscar por número de control: " + e.getMessage());
    }

    return null;
}
public boolean actualizar(ModeloCandidato c) {
    String sql = "UPDATE candidato SET Nombre = ?, Apellido_p = ?, Apellido_m = ?, Telefono = ?, Correo = ? WHERE no_control = ?";

    try (Connection conn = ConexionSQL.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        // Mostrar los datos que se van a actualizar
        System.out.println("➡️ Actualizando candidato con:");
        System.out.println("Nombre: " + c.getNombre());
        System.out.println("Apellido P: " + c.getApeP());
        System.out.println("Apellido M: " + c.getApeM());
        System.out.println("Teléfono: " + c.getTelefono());
        System.out.println("Correo: " + c.getCorreo());
        System.out.println("No Control: " + c.getNumControl());

        // Asignar valores
        ps.setString(1, c.getNombre());
        ps.setString(2, c.getApeP());
        ps.setString(3, c.getApeM());
        ps.setString(4, c.getTelefono());
        ps.setString(5, c.getCorreo());
        ps.setString(6, c.getNumControl());

        // Ejecutar y verificar filas modificadas
        int filasAfectadas = ps.executeUpdate();
        System.out.println("✅ Filas modificadas: " + filasAfectadas);

        return filasAfectadas > 0;

    } catch (SQLException e) {
        System.out.println("❌ Error al actualizar candidato: " + e.getMessage());
        return false;
    }
}

public boolean eliminar(String noControl) {
    String sql = "DELETE FROM candidato WHERE no_control = ?";

    try (Connection conn = ConexionSQL.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, noControl);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("❌ Error al eliminar candidato: " + e.getMessage());
        return false;
    }
}


}

