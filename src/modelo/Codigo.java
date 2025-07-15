/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Codigo {
    private static String codigoGenerado;
    private static String correoAsociado;

    public static String getCodigoGenerado() {
        return codigoGenerado;
    }

    public static void setCodigoGenerado(String codigoGenerado) {
        Codigo.codigoGenerado = codigoGenerado;
    }

    public static String getCorreoAsociado() {
        return correoAsociado;
    }

    public static void setCorreoAsociado(String correoAsociado) {
        Codigo.correoAsociado = correoAsociado;
    }
    
    // Método para generar un código de 4 dígitos (puedes personalizarlo)
    public static String generarCodigo() {
        int codigo = (int) (Math.random() * 9000) + 1000; // Números entre 1000 y 9999
        return String.valueOf(codigo);
    }
}