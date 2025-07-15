/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Gestor;
import vista.Verificar;
import vista.NuevaC; // Necesitamos esta vista para abrirla

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class CVerificar implements ActionListener {

    private Verificar vistaVerificar;
    private Gestor modeloGestor;

    public CVerificar(Verificar vistaVerificar) {
        this.vistaVerificar = vistaVerificar;
        this.modeloGestor = new Gestor();
        
        // Asociar el ActionListener al botón de la vista
        this.vistaVerificar.btnEnter.addActionListener(this);
    }

    public void iniciar() {
        vistaVerificar.setTitle("Verificar Código");
        vistaVerificar.setLocationRelativeTo(null); // Centrar la ventana
        vistaVerificar.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaVerificar.btnEnter) {
            String codigoIngresado = vistaVerificar.txt1.getText() + 
                                     vistaVerificar.txt2.getText() +
                                     vistaVerificar.txt3.getText() +
                                     vistaVerificar.txt4.getText();

            if (codigoIngresado.isEmpty() || codigoIngresado.length() != 4) {
                JOptionPane.showMessageDialog(vistaVerificar, "Por favor, ingresa los 4 dígitos del código.", "Código Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (modeloGestor.verificarCodigo(codigoIngresado)) {
                JOptionPane.showMessageDialog(vistaVerificar, "Código verificado exitosamente.", "Verificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                // Abrir la ventana para nueva contraseña
                NuevaC vistaNuevaC = new NuevaC();
                CNuevaC controladorNuevaC = new CNuevaC(vistaNuevaC);
                controladorNuevaC.iniciar();
                vistaVerificar.dispose(); // Cerrar la ventana actual
            } else {
                JOptionPane.showMessageDialog(vistaVerificar, "Código incorrecto. Intenta de nuevo.", "Código Inválido", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}