package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Empresa {

    private int id_empresa;
    private String rfc;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private boolean activo;
    private Connection conexion;

    public Empresa(int id_empresa, String rfc, String nombre, String direccion, String telefono, String correo) {
        this.id_empresa = id_empresa;
        this.rfc = rfc;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.conexion = ConexionSQL.conectar();
    }

    public Empresa() {
        this.conexion = ConexionSQL.conectar();
    }

    // Getters y Setters
    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Empresa{" + "id_empresa=" + id_empresa + ", rfc=" + rfc + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono + ", correo=" + correo + '}';
    }

    private void asegurarConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = ConexionSQL.conectar();
        }
    }

    public boolean InsertarEmpresa(Empresa empresa) {
        String sql = "INSERT INTO empresa (id_empresa, rfc, nombre, direccion, telefono, correo)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try {
            asegurarConexion();
            int nuevoId = generarSiguienteId();
            if (nuevoId == -1) {
                return false;
            }

            empresa.setId_empresa(nuevoId);
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, empresa.getId_empresa());
            stmt.setString(2, empresa.getRfc());
            stmt.setString(3, empresa.getNombre());
            stmt.setString(4, empresa.getDireccion());
            stmt.setString(5, empresa.getTelefono());
            stmt.setString(6, empresa.getCorreo());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar empresa: " + e.getMessage());
            return false;
        }
    }

    private int generarSiguienteId() {
        int id = 1;
        try {
            asegurarConexion();
            String sql = "SELECT COUNT(*) FROM empresa WHERE id_empresa = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);

            while (true) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    return id;
                }
                id++;
            }
        } catch (SQLException e) {
            System.err.println("Error al generar ID: " + e.getMessage());
        }
        return -1;
    }

    public boolean existeEmpresa(String rfc) {
        try {
            asegurarConexion();
            String sql = "SELECT COUNT(*) FROM empresa WHERE rfc = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, rfc);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar empresa: " + e.getMessage());
        }
        return false;
    }

    public boolean existeNombre(String nombre) {
        try {
            asegurarConexion();
            String sql = "SELECT COUNT(*) FROM empresa WHERE nombre = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar nombre: " + e.getMessage());
        }
        return false;
    }

    public boolean existeTelefono(String telefono) {
        try {
            asegurarConexion();
            String sql = "SELECT COUNT(*) FROM empresa WHERE telefono = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, telefono);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar teléfono: " + e.getMessage());
        }
        return false;
    }

    public boolean existeCorreo(String correo) {
        try {
            asegurarConexion();
            String sql = "SELECT COUNT(*) FROM empresa WHERE correo = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminarEmpresa(int idEmpresa) {
        try {
            asegurarConexion();
            String sql = "DELETE FROM empresa WHERE id_empresa = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idEmpresa);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar empresa: " + e.getMessage());
            return false;
        }
    }

    public List<Empresa> buscar(String tipo, String valor) {
        List<Empresa> resultados = new ArrayList<>();
        try {
            asegurarConexion();
            String sql = "SELECT * FROM empresa WHERE " + tipo + " LIKE ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, "%" + valor + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Empresa e = new Empresa();
                e.setId_empresa(rs.getInt("id_empresa"));
                e.setRfc(rs.getString("rfc"));
                e.setNombre(rs.getString("nombre"));
                e.setDireccion(rs.getString("direccion"));
                e.setTelefono(rs.getString("telefono"));
                e.setCorreo(rs.getString("correo"));
                resultados.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
        }
        return resultados;
    }

    public List<Empresa> ordenarPor(String campo, boolean ascendente) {
        List<Empresa> empresas = new ArrayList<>();
        try {
            asegurarConexion();
            String sql = "SELECT * FROM empresa ORDER BY " + campo + (ascendente ? " ASC" : " DESC");
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Empresa e = new Empresa();
                e.setId_empresa(rs.getInt("id_empresa"));
                e.setRfc(rs.getString("rfc"));
                e.setNombre(rs.getString("nombre"));
                e.setDireccion(rs.getString("direccion"));
                e.setTelefono(rs.getString("telefono"));
                e.setCorreo(rs.getString("correo"));
                empresas.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error en ordenamiento: " + e.getMessage());
        }
        return empresas;
    }

    public List<Empresa> obtenerTodo() {
        List<Empresa> empresas = new ArrayList<>();
        try {
            asegurarConexion();
            String sql = "SELECT id_empresa, rfc, nombre, direccion, telefono, correo FROM empresa WHERE activo = TRUE";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Empresa empresa = new Empresa();
                empresa.setId_empresa(rs.getInt("id_empresa"));
                empresa.setRfc(rs.getString("rfc"));
                empresa.setNombre(rs.getString("nombre"));
                empresa.setDireccion(rs.getString("direccion"));
                empresa.setTelefono(rs.getString("telefono"));
                empresa.setCorreo(rs.getString("correo"));
                empresas.add(empresa);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener empresas: " + e.getMessage());
        }

        return empresas;
    }

    public List<Empresa> buscarMulticampo(String valor) {
        List<Empresa> resultados = new ArrayList<>();
        try {
            asegurarConexion();
            String sql = """
            SELECT * FROM empresa
            WHERE rfc LIKE ? OR nombre LIKE ? OR correo LIKE ? OR telefono LIKE ?
        """;

            PreparedStatement stmt = conexion.prepareStatement(sql);
            String pattern = "%" + valor + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            stmt.setString(4, pattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Empresa e = new Empresa();
                e.setId_empresa(rs.getInt("id_empresa"));
                e.setRfc(rs.getString("rfc"));
                e.setNombre(rs.getString("nombre"));
                e.setDireccion(rs.getString("direccion"));
                e.setTelefono(rs.getString("telefono"));
                e.setCorreo(rs.getString("correo"));
                resultados.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Error en búsqueda multicampo: " + e.getMessage());
        }
        return resultados;
    }

    public boolean tieneProyectos(int idEmpresa) {
        try {
            asegurarConexion(); // Asegura que la conexión esté activa

            String sql = "SELECT COUNT(*) FROM proyecto WHERE id_empresa = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idEmpresa);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int cantidad = rs.getInt(1);
                return cantidad > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar proyectos asociados: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarEmpresa(Empresa empresa) {
        try {
            asegurarConexion();
            String sql = """
            UPDATE empresa SET rfc = ?, nombre = ?, direccion = ?, telefono = ?, correo = ?
            WHERE id_empresa = ?
        """;
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, empresa.getRfc());
            stmt.setString(2, empresa.getNombre());
            stmt.setString(3, empresa.getDireccion());
            stmt.setString(4, empresa.getTelefono());
            stmt.setString(5, empresa.getCorreo());
            stmt.setInt(6, empresa.getId_empresa());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar empresa: " + e.getMessage());
            return false;
        }
    }

    public boolean desactivarEmpresa(int id) {
        try (Connection conn = ConexionSQL.conectar()) {
            String sql = "UPDATE empresa SET activo = FALSE WHERE id_empresa = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
