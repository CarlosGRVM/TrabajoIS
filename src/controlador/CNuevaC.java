/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Gestor;
import modelo.Codigo; // Para obtener el correo asociado
import vista.NuevaC;
// Si tienes una vista de login, impórtala para redirigir al usuario
// import vistas.Login; 

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class CNuevaC implements ActionListener {

    private NuevaC vistaNuevaC;
    private Gestor modeloGestor;

    public CNuevaC(NuevaC vistaNuevaC) {
        this.vistaNuevaC = vistaNuevaC;
        this.modeloGestor = new Gestor();
        
        // Asociar el ActionListener al botón de la vista
        this.vistaNuevaC.btnAceptar.addActionListener(this);
    }

    public void iniciar() {
        vistaNuevaC.setTitle("Establecer Nueva Contraseña");
        vistaNuevaC.setLocationRelativeTo(null); // Centrar la ventana
        vistaNuevaC.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaNuevaC.btnAceptar) {
            String nuevaContrasena = new String(vistaNuevaC.txtNueva.getPassword());
            String correoAsociado = Codigo.getCorreoAsociado(); // Obtenemos el correo del modelo Codigo

            if (nuevaContrasena.isEmpty()) {
                JOptionPane.showMessageDialog(vistaNuevaC, "Por favor, ingresa tu nueva contraseña.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Aquí puedes agregar validaciones de complejidad de contraseña (longitud, caracteres, etc.)
            // if (nuevaContrasena.length() < 6) {
            //    JOptionPane.showMessageDialog(vistaNuevaC, "La contraseña debe tener al menos 6 caracteres.", "Contraseña Débil", JOptionPane.WARNING_MESSAGE);
            //    return;
            // }

            if (correoAsociado != null && modeloGestor.actualizarContrasena(correoAsociado, nuevaContrasena)) {
                JOptionPane.showMessageDialog(vistaNuevaC, "Contraseña actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vistaNuevaC.dispose(); // Cerrar la ventana actual
                // Opcional: Redirigir al usuario a la pantalla de login
                // Login vistaLogin = new Login();
                // CLogin controladorLogin = new CLogin(vistaLogin); // Asumiendo que tienes un controlador de Login
                // controladorLogin.iniciar(); 
            } else {
                JOptionPane.showMessageDialog(vistaNuevaC, "Error al actualizar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}