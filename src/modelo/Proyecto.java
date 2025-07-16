package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Proyecto {

    private int id_proyecto;
    private String titulo;
    private String descripcion;
    private int espacios;
    private String disponible;
    private String tipo;
    private Empresa[] empresas;
    private Connection conexion;

    public Proyecto(int id_proyecto, String titulo, String descripcion, int espacios, String disponible, String tipo, Empresa[] empresas) {
        this.id_proyecto = id_proyecto;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.espacios = espacios;
        this.disponible = disponible;
        this.tipo = tipo;
        this.empresas = empresas;
        this.conexion = ConexionSQL.conectar();
    }

    public Proyecto() {
        this.conexion = ConexionSQL.conectar();
    }

    // Getters y Setters
    public int getId_proyecto() {
        return id_proyecto;
    }

    public void setId_proyecto(int id_proyecto) {
        this.id_proyecto = id_proyecto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEspacios() {
        return espacios;
    }

    public void setEspacios(int espacios) {
        this.espacios = espacios;
    }

    public String getDisponible() {
        return disponible;
    }

    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Empresa[] getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresa[] empresas) {
        this.empresas = empresas;
    }

    public Connection getConexion() {
        return conexion;
    }

    @Override
    public String toString() {
        return "Proyecto{" + "id_proyecto=" + id_proyecto + ", titulo=" + titulo + ", descripcion=" + descripcion + ", espacios=" + espacios + ", disponible=" + disponible + ", tipo=" + tipo + ", empresas=" + empresas + '}';
    }

    private void asegurarConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = ConexionSQL.conectar();
        }
    }

    public int generarSiguienteId() {
        int id = 1;
        try {
            asegurarConexion();
            String sql = "SELECT COUNT(*) FROM proyecto WHERE id_proyecto = ?";
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
            System.err.println("Error al generar ID proyecto: " + e.getMessage());
        }
        return -1;
    }

    private int obtenerIdEmpresaPorNombre(String nombreEmpresa) {
        try {
            asegurarConexion();
            String sql = "SELECT id_empresa FROM empresa WHERE nombre = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, nombreEmpresa);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_empresa");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener id_empresa: " + e.getMessage());
        }
        return -1;
    }

    public boolean insertarProyecto(int idEmpresa) throws SQLException {
        asegurarConexion();

        if (conexion == null) {
            throw new SQLException("❌ No se pudo establecer la conexión a la base de datos.");
        }

        // Verificar existencia de empresa
        String sqlVerificar = "SELECT COUNT(*) FROM empresa WHERE id_empresa = ?";
        PreparedStatement stmtVerificar = conexion.prepareStatement(sqlVerificar);
        stmtVerificar.setInt(1, idEmpresa);
        ResultSet rsVerificar = stmtVerificar.executeQuery();
        if (rsVerificar.next() && rsVerificar.getInt(1) == 0) {
            throw new SQLException("❌ No existe la empresa con ID: " + idEmpresa);
        }

        // Generar ID si no está definido
        if (this.id_proyecto <= 0) {
            this.id_proyecto = generarSiguienteId();
        }

        String sql = "INSERT INTO proyecto (id_proyecto, titulo, descripcion, espacios, disponible, tipo, id_empresa, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, TRUE)";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        stmt.setInt(1, this.id_proyecto);
        stmt.setString(2, titulo);
        stmt.setString(3, descripcion);
        stmt.setInt(4, espacios);
        stmt.setString(5, "Disponible");
        stmt.setString(6, "Anteproyecto");
        stmt.setInt(7, idEmpresa);

        int filas = stmt.executeUpdate();
        return filas > 0;
    }

    public List<Proyecto> obtenerTodos(Empresa[] empresas, String campoOrden, boolean asc) {
        List<Proyecto> lista = new ArrayList<>();
        try {
            asegurarConexion();
            String sql = "SELECT * FROM proyecto WHERE activo = TRUE AND disponible = 'Disponible' ORDER BY "
                    + campoOrden + (asc ? " ASC" : " DESC");
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Proyecto p = new Proyecto();
                p.setId_proyecto(rs.getInt("id_proyecto"));
                p.setTitulo(rs.getString("titulo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setEspacios(rs.getInt("espacios"));
                p.setDisponible(rs.getString("disponible"));
                p.setTipo(rs.getString("tipo"));

                int idEmpresa = rs.getInt("id_empresa");
                Empresa empresaMatch = null;
                for (Empresa e : empresas) {
                    if (e.getId_empresa() == idEmpresa) {
                        empresaMatch = e;
                        break;
                    }
                }
                p.setEmpresas(new Empresa[]{empresaMatch});
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener proyectos: " + e.getMessage());
        }

        return lista;
    }

    public boolean eliminarProyecto(int id) {
        try {
            asegurarConexion();
            String sql = "UPDATE proyecto SET activo = FALSE WHERE id_proyecto = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar proyecto (lógicamente): " + e.getMessage());
            return false;
        }
    }

    public boolean existeTitulo(String titulo) {
        try {
            asegurarConexion();
            String sql = "SELECT COUNT(*) FROM proyecto WHERE LOWER(titulo) = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, titulo.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar título: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarProyecto(Proyecto p) {
        try {
            asegurarConexion();
            String sql = "UPDATE proyecto SET titulo = ?, descripcion = ?, espacios = ?, id_empresa = ? WHERE id_proyecto = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, p.getTitulo());
            stmt.setString(2, p.getDescripcion());
            stmt.setInt(3, p.getEspacios());
            stmt.setInt(4, p.getEmpresas()[0].getId_empresa());
            stmt.setInt(5, p.getId_proyecto());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar proyecto: " + e.getMessage());
            return false;
        }
    }

    public Proyecto obtenerPorId(int id, Empresa[] empresas) {
        try {
            asegurarConexion();
            String sql = "SELECT titulo, descripcion, espacios, disponible, tipo, id_empresa FROM proyecto WHERE id_proyecto = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Proyecto p = new Proyecto();
                p.setId_proyecto(id);
                p.setTitulo(rs.getString("titulo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setEspacios(rs.getInt("espacios"));
                p.setDisponible(rs.getString("disponible"));
                p.setTipo(rs.getString("tipo"));

                int idEmp = rs.getInt("id_empresa");
                for (Empresa e : empresas) {
                    if (e.getId_empresa() == idEmp) {
                        p.setEmpresas(new Empresa[]{e});
                        break;
                    }
                }
                return p;
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener proyecto por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Proyecto> obtenerPorEmpresa(int idEmpresa, String campoOrden, boolean ascendente) {
        List<Proyecto> proyectos = new ArrayList<>();

        try {
            asegurarConexion();
            String sql = "SELECT * FROM proyecto WHERE id_empresa = ? AND activo = TRUE AND disponible = 'Disponible' ORDER BY "
                    + campoOrden + (ascendente ? " ASC" : " DESC");
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idEmpresa);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Proyecto p = new Proyecto();
                p.setId_proyecto(rs.getInt("id_proyecto"));
                p.setTitulo(rs.getString("titulo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setEspacios(rs.getInt("espacios"));
                p.setDisponible(rs.getString("disponible"));
                p.setTipo(rs.getString("tipo"));

                Empresa empresa = new Empresa();
                empresa.setId_empresa(idEmpresa);
                p.setEmpresas(new Empresa[]{empresa});

                proyectos.add(p);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener proyectos por empresa: " + e.getMessage());
        }

        return proyectos;
    }

}
