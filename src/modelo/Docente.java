/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Docente {

    private int noEmpleado;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private String correo;
    private Connection conexion;

    public Docente(int noEmpleado, String nombre, String apellidoPaterno, String apellidoMaterno, String telefono, String correo) {
        this.noEmpleado = noEmpleado;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.correo = correo;
        this.conexion = ConexionSQL.conectar();
    }

    public Docente() {
        this.conexion = ConexionSQL.conectar();
    }

    // Getters y Setters

    public int getNoEmpleado() {
        return noEmpleado;
    }

    public void setNoEmpleado(int noEmpleado) {
        this.noEmpleado = noEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
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

    private void asegurarConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = ConexionSQL.conectar();
        }
    }

    public boolean insertarDocente(Docente docentes) {
        String SQL = "INSERT INTO docente (no_empleado, Nombre, Apellido_p, Apellido_m, Telefono, Correo) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            asegurarConexion();

            PreparedStatement stmt = conexion.prepareStatement(SQL);
            stmt.setInt(1, docentes.getNoEmpleado());
            stmt.setString(2, docentes.getNombre());
            stmt.setString(3, docentes.getApellidoPaterno());
            stmt.setString(4, docentes.getApellidoMaterno());
            stmt.setString(5, docentes.getTelefono());
            stmt.setString(6, docentes.getCorreo());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("No se pudo agregar Docente: " + e.getMessage());
            return false;
        }
    }

    public List<Docente> obtenerTodosLosDocentes() {
        List<Docente> lista = new ArrayList<>();
        try {
            asegurarConexion();
            String SQL = "SELECT no_empleado, Nombre, Apellido_p, Apellido_m, Telefono, Correo FROM docente";
            PreparedStatement stmt = conexion.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Docente d = new Docente(
                    rs.getInt("no_empleado"),
                    rs.getString("Nombre"),
                    rs.getString("Apellido_p"),
                    rs.getString("Apellido_m"),
                    rs.getString("Telefono"),
                    rs.getString("Correo")
                );
                lista.add(d);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener docentes: " + e.getMessage());
        }
        return lista;
    }
}