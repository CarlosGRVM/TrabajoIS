package controlador;

import modelo.Gestor;
import modelo.Codigo; // Para obtener el correo asociado
import vista.NuevaC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class CNuevaC implements ActionListener {

    private NuevaC vistaNuevaC;
    private Gestor modeloGestor;

    public CNuevaC(NuevaC vistaNuevaC) {
        this.vistaNuevaC = vistaNuevaC;
        this.modeloGestor = new Gestor();

        // Asociar el ActionListener al botón de aceptar
        this.vistaNuevaC.btnAceptar.addActionListener(this);

        // Mostrar contraseña (ícono de ojo abierto)
        this.vistaNuevaC.mostrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vistaNuevaC.txtNueva.setEchoChar((char) 0); // Muestra texto plano
                vistaNuevaC.mostrar.setVisible(false);
                vistaNuevaC.ocultar.setVisible(true);
            }
        });

        // Ocultar contraseña (ícono de ojo cerrado)
        this.vistaNuevaC.ocultar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vistaNuevaC.txtNueva.setEchoChar('•'); // Oculta contraseña
                vistaNuevaC.mostrar.setVisible(true);
                vistaNuevaC.ocultar.setVisible(false);
            }
        });

        // Configurar estado inicial
        this.vistaNuevaC.mostrar.setVisible(true);
        this.vistaNuevaC.ocultar.setVisible(false);
        this.vistaNuevaC.txtNueva.setEchoChar('•'); // Asegurar oculto por defecto
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

            if (correoAsociado != null && modeloGestor.actualizarContrasena(correoAsociado, nuevaContrasena)) {
                JOptionPane.showMessageDialog(vistaNuevaC, "Contraseña actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vistaNuevaC.dispose(); // Cerrar la ventana actual

                // Redirigir al login
                vista.inicioSesion login = new vista.inicioSesion();
                controlador.CInicioSesion.reiniciarEstado(); // Reiniciar intentos de login
                controlador.CInicioSesion controladorLogin = new controlador.CInicioSesion(login);
                login.setVisible(true);
                login.setLocationRelativeTo(null);
            } else {
                JOptionPane.showMessageDialog(vistaNuevaC, "Error al actualizar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}