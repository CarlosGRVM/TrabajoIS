/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Aplicacion;

import controlador.CInicioSesion;
import vista.inicioSesion;

/**
 *
 * @author carlo
 */
public class Iniciar {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            inicioSesion vistaLogin = new inicioSesion();
            new CInicioSesion(vistaLogin); // Enlazamos controlador y vista
            vistaLogin.setVisible(true);
        });
    }
}
