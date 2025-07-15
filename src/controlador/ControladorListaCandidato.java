/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.ModeloCandidato;
import modelo.ModeloCandidatoBD;
import vista.ListaCandidato;

/**
 *
 * @author ubuntu
 */
public class ControladorListaCandidato implements ActionListener {

    private ListaCandidato vista;
    private ModeloCandidatoBD modelo;

    public ControladorListaCandidato(ListaCandidato vista, ModeloCandidatoBD modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnMostrar.addActionListener(this);
        this.vista.btnModificar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);

        cargarTabla(); // Cargar candidatos al iniciar
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnBuscar) {
            buscar();
        } else if (e.getSource() == vista.btnMostrar) {
            cargarTabla();
        } else if (e.getSource() == vista.btnModificar) {
            modificar();
        } else if (e.getSource() == vista.btnEliminar) {
            eliminar();
        }
    }
        public void cargarTabla() {
        List<ModeloCandidato> lista = modelo.obtenerTodos();
        DefaultTableModel modeloTabla = (DefaultTableModel) vista.tblCandidato.getModel();
        modeloTabla.setRowCount(0); // Limpiar

        for (ModeloCandidato c : lista) {
            modeloTabla.addRow(new Object[]{
                c.getNumControl(),
                c.getNombre(),
                c.getApeP(),
                c.getApeM(),
                c.getTelefono(),
                c.getCorreo()
            });
        }
    }
        public void modificar() {
    int fila = vista.tblCandidato.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(vista, "Selecciona una fila a modificar.");
        return;
    }

    // Obtener datos modificados
    String noControl = vista.tblCandidato.getValueAt(fila, 0).toString();
    String nombre = vista.tblCandidato.getValueAt(fila, 1).toString();
    String apePat = vista.tblCandidato.getValueAt(fila, 2).toString();
    String apeMat = vista.tblCandidato.getValueAt(fila, 3).toString();
    String telefono = vista.tblCandidato.getValueAt(fila, 4).toString();
    String correo = vista.tblCandidato.getValueAt(fila, 5).toString();



    // Validaciones básicas
    if (nombre.isEmpty() || apePat.isEmpty() || apeMat.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
        JOptionPane.showMessageDialog(vista, "Todos los campos deben estar llenos.");
        return;
    }

    if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
    JOptionPane.showMessageDialog(vista, "Correo inválido (usa un formato como usuario@dominio.com).");
    return;
}



    if (!telefono.matches("\\d{10}")) {
        JOptionPane.showMessageDialog(vista, "Teléfono debe tener 10 dígitos.");
        return;
    }

    // Crear objeto candidato
    ModeloCandidato candidato = new ModeloCandidato();
    candidato.setNumControl(noControl);
    candidato.setNombre(nombre);
    candidato.setApeP(apePat);
    candidato.setApeM(apeMat);
    candidato.setTelefono(telefono);
    candidato.setCorreo(correo);
    

    if (modelo.actualizar(candidato)) {
        JOptionPane.showMessageDialog(vista, "Candidato actualizado correctamente.");
        cargarTabla();
    } else {
        JOptionPane.showMessageDialog(vista, "Error al actualizar el candidato.");
    }
}

        public void buscar() {
        String noControl = vista.txtBusqueda.getText().trim();
        ModeloCandidato c = modelo.buscarPorNoControl(noControl);

        DefaultTableModel modeloTabla = (DefaultTableModel) vista.tblCandidato.getModel();
        modeloTabla.setRowCount(0); // Limpiar tabla

        if (c != null) {
            modeloTabla.addRow(new Object[]{
                c.getNumControl(),
                c.getNombre(),
                c.getApeP(),
                c.getApeM(),
                c.getTelefono(),
                c.getCorreo()
            });
        } else {
            JOptionPane.showMessageDialog(vista, "Candidato no encontrado.");
        }
    }
        public void eliminar() {
        int fila = vista.tblCandidato.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona un candidato.");
            return;
        }

        String noControl = vista.tblCandidato.getValueAt(fila, 0).toString();
        int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Eliminar candidato " + noControl + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (modelo.eliminar(noControl)) {
                cargarTabla();
                JOptionPane.showMessageDialog(vista, "Candidato eliminado.");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar.");
            }
        }
    }


}