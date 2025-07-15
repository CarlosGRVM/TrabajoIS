/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author carlo
 */
public class ConexionSQL {
    
    private static final String URL = "jdbc:mysql://localhost:3306/SistemaResidencia"; // Cambia si tu puerto/base cambian
    private static final String USER = "root"; // Tu usuario
    private static final String PASSWORD = "abd1234"; // Tu contraseña de base de datos
    private static Connection conexion = null;

    // Constructor privado para evitar instancias externas
    public ConexionSQL() {
    }

    // Método público para obtener la conexión
    public static Connection conectar() {
        if (conexion == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar driver de MySQL
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexion exitosa a la base de datos.");
            } catch (ClassNotFoundException e) {
                System.err.println("Error: Driver de base de datos no encontrado. " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            }
        }
        return conexion;
    }

    // Método opcional para cerrar conexión
    public static void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexión cerrada exitosamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
}
