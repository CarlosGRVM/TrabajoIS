package controlador;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.ModeloCandidato;
import modelo.ModeloCandidatoBD;
import vista.ListaCandidato;

public class ControladorListaCandidato implements ActionListener {

    private final ListaCandidato vista;
    private final ModeloCandidatoBD modelo;
    private boolean modoEdicion = false;
    private String noControlOriginal = "";

    public ControladorListaCandidato(ListaCandidato vista, ModeloCandidatoBD modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnMostrar.addActionListener(this);
        this.vista.btnModificar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnRegresar.addActionListener(this);

        this.vista.txtBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarEnTiempoReal();
            }
        });

        cargarTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == vista.btnBuscar) {
            buscarPorNoControl();
        } else if (src == vista.btnMostrar) {
            cargarTabla();
        } else if (src == vista.btnModificar) {
            if (!modoEdicion) {
                habilitarEdicion();
            } else {
                guardarCambios();
            }
        } else if (src == vista.btnEliminar) {
            eliminar();
        } else if (src == vista.btnRegresar) {
            controlador.GestorVistas.regresar(vista);
        }
    }

    private void habilitarEdicion() {
        int fila = vista.tblCandidato.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona una fila para modificar.");
            return;
        }

        noControlOriginal = vista.tblCandidato.getValueAt(fila, 0).toString();
        vista.tblCandidato.setEnabled(true);
        vista.btnModificar.setText("Guardar");
        modoEdicion = true;
    }

    private void guardarCambios() {
        int fila = vista.tblCandidato.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona una fila para guardar los cambios.");
            return;
        }

        String noControlNuevo = vista.tblCandidato.getValueAt(fila, 0).toString().trim();
        String nombre = vista.tblCandidato.getValueAt(fila, 1).toString().trim();
        String apePat = vista.tblCandidato.getValueAt(fila, 2).toString().trim();
        String apeMat = vista.tblCandidato.getValueAt(fila, 3).toString().trim();
        String telefono = vista.tblCandidato.getValueAt(fila, 4).toString().trim();
        String correo = vista.tblCandidato.getValueAt(fila, 5).toString().trim();

        if (noControlNuevo.isEmpty() || nombre.isEmpty() || apePat.isEmpty() || apeMat.isEmpty()
                || telefono.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos deben estar llenos.");
            return;
        }

        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(vista, "Correo inválido.");
            return;
        }

        if (!telefono.matches("\\d{10,13}")) {
            JOptionPane.showMessageDialog(vista, "Teléfono inválido. Debe tener entre 10 y 13 dígitos.");
            return;
        }

        if (!noControlNuevo.equals(noControlOriginal) && modelo.existeCandidato(noControlNuevo)) {
            JOptionPane.showMessageDialog(vista, "Ya existe un candidato con ese número de control.");
            return;
        }

        ModeloCandidato c = new ModeloCandidato(noControlNuevo, nombre, apePat, apeMat, telefono, correo);
        if (modelo.actualizar(c, noControlOriginal)) {
            JOptionPane.showMessageDialog(vista, "✅ Candidato actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(vista, "❌ Error al actualizar el candidato.");
        }

        cargarTabla();
        vista.tblCandidato.clearSelection();
        vista.btnModificar.setText("Modificar");
        modoEdicion = false;
    }

    private void cargarTabla() {
        List<ModeloCandidato> lista = modelo.obtenerTodos();
        DefaultTableModel modeloTabla = (DefaultTableModel) vista.tblCandidato.getModel();
        modeloTabla.setRowCount(0);

        for (ModeloCandidato c : lista) {
            modeloTabla.addRow(new Object[]{
                c.getNumControl(), c.getNombre(), c.getApeP(), c.getApeM(), c.getTelefono(), c.getCorreo()
            });
        }
    }

    private void buscarPorNoControl() {
        String noControl = vista.txtBusqueda.getText().trim();
        ModeloCandidato c = modelo.buscarPorNoControl(noControl);

        DefaultTableModel modeloTabla = (DefaultTableModel) vista.tblCandidato.getModel();
        modeloTabla.setRowCount(0);

        if (c != null) {
            modeloTabla.addRow(new Object[]{
                c.getNumControl(), c.getNombre(), c.getApeP(), c.getApeM(), c.getTelefono(), c.getCorreo()
            });
        } else {
            JOptionPane.showMessageDialog(vista, "Candidato no encontrado.");
        }
    }

    private void buscarEnTiempoReal() {
        String texto = vista.txtBusqueda.getText().trim().toLowerCase();
        List<ModeloCandidato> lista = modelo.listarTodos();
        DefaultTableModel modelo = (DefaultTableModel) vista.tblCandidato.getModel();
        modelo.setRowCount(0);

        for (ModeloCandidato c : lista) {
            if (c.getNumControl().toLowerCase().contains(texto)
                    || c.getNombre().toLowerCase().contains(texto)
                    || c.getApeP().toLowerCase().contains(texto)
                    || c.getApeM().toLowerCase().contains(texto)
                    || c.getCorreo().toLowerCase().contains(texto)
                    || c.getTelefono().toLowerCase().contains(texto)) {
                modelo.addRow(new Object[]{
                    c.getNumControl(), c.getNombre(), c.getApeP(), c.getApeM(), c.getTelefono(), c.getCorreo()
                });
            }
        }
    }

    private void eliminar() {
        int fila = vista.tblCandidato.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona un candidato.");
            return;
        }

        String noControl = vista.tblCandidato.getValueAt(fila, 0).toString();
        int confirmacion = JOptionPane.showConfirmDialog(
                vista, "¿Eliminar candidato " + noControl + "?", "Confirmar", JOptionPane.YES_NO_OPTION
        );

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
