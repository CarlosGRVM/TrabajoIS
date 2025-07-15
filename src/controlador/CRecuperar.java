/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Gestor;
import vista.Recuperar;
import vista.Verificar; // Necesitamos esta vista para abrirla
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class CRecuperar implements ActionListener {

    private Recuperar vistaRecuperar;
    private Gestor modeloGestor;

    public CRecuperar(Recuperar vistaRecuperar) {
        this.vistaRecuperar = vistaRecuperar;
        this.modeloGestor = new Gestor();
        
        // Asociar el ActionListener al botón de la vista
        this.vistaRecuperar.btnEnviar.addActionListener(this);
    }

    public void iniciar() {
        vistaRecuperar.setTitle("Recuperar Contraseña");
        vistaRecuperar.setLocationRelativeTo(null); // Centrar la ventana
        vistaRecuperar.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaRecuperar.btnEnviar) {
            String correo = vistaRecuperar.txtCorreo.getText();

            if (correo.isEmpty()) {
                JOptionPane.showMessageDialog(vistaRecuperar, "Por favor, ingresa tu correo electrónico.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (modeloGestor.verificarCorreoExistente(correo)) {
                if (modeloGestor.enviarCodigoRecuperacion(correo)) {
                    JOptionPane.showMessageDialog(vistaRecuperar, "Código de recuperación enviado a tu correo.", "Correo Enviado", JOptionPane.INFORMATION_MESSAGE);
                    // Abrir la ventana de verificación
                    Verificar vistaVerificar = new Verificar();
                    CVerificar controladorVerificar = new CVerificar(vistaVerificar);
                    controladorVerificar.iniciar();
                    vistaRecuperar.dispose(); // Cerrar la ventana actual
                } else {
                    JOptionPane.showMessageDialog(vistaRecuperar, "Error al enviar el código. Intenta de nuevo más tarde.", "Error de Envío", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(vistaRecuperar, "El correo electrónico no está registrado.", "Correo No Encontrado", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}