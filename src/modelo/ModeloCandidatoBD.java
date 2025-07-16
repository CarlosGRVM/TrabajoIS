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
        String sql = "SELECT * FROM candidato WHERE estatus = 'activo'";
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
    String sql = "SELECT * FROM candidato WHERE estatus = 'activo'";

    try (Connection conn = ConexionSQL.conectar();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            ModeloCandidato c = new ModeloCandidato();
            c.setNumControl(rs.getString("no_control"));
            c.setNombre(rs.getString("nombre"));
            c.setApeP(rs.getString("apellido_p"));
            c.setApeM(rs.getString("apellido_m"));
            c.setTelefono(rs.getString("telefono"));
            c.setCorreo(rs.getString("correo"));
            lista.add(c);
        }

    } catch (SQLException e) {
        System.out.println("❌ Error al obtener candidatos: " + e.getMessage());
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
public boolean actualizar(ModeloCandidato c, String noControlOriginal) {
    String sql = "UPDATE candidato SET no_control = ?, Nombre = ?, Apellido_p = ?, Apellido_m = ?, Telefono = ?, Correo = ? WHERE no_control = ?";

    try (Connection conn = ConexionSQL.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, c.getNumControl()); // Nuevo no_control
        ps.setString(2, c.getNombre());
        ps.setString(3, c.getApeP());
        ps.setString(4, c.getApeM());
        ps.setString(5, c.getTelefono());
        ps.setString(6, c.getCorreo());
        ps.setString(7, noControlOriginal); // Clave original en WHERE

        int filas = ps.executeUpdate();
        System.out.println("🔄 Filas actualizadas: " + filas);
        return filas > 0;

    } catch (SQLException e) {
        System.out.println("❌ Error al actualizar candidato: " + e.getMessage());
        return false;
    }
}
public boolean existeCandidato(String noControl) {
    String sql = "SELECT 1 FROM candidato WHERE no_control = ?";
    try (Connection conn = ConexionSQL.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, noControl);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // true si hay resultado
    } catch (SQLException e) {
        System.out.println("❌ Error al verificar existencia: " + e.getMessage());
        return true; // por seguridad, asumimos que sí existe
    }
}



public boolean eliminar(String noControl) {
    String sql = "UPDATE candidato SET estatus = 'inactivo' WHERE no_control = ?";
    try (Connection conn = ConexionSQL.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, noControl);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("❌ Error en eliminación lógica: " + e.getMessage());
        return false;
    }
}



}

