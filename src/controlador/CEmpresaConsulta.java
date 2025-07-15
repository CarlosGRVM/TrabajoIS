package controlador;

import modelo.Empresa;
import vista.ConsultaEmpresa;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CEmpresaConsulta {

    private final ConsultaEmpresa vista;
    private final Empresa modelo;
    private boolean hayCambiosSinGuardar = false;
    private List<Empresa> empresasOriginales;

    public CEmpresaConsulta(ConsultaEmpresa vista) {
        this.vista = vista;
        this.modelo = new Empresa();
        cargarDatos();
        agregarEventos();
    }

    private void cargarDatos() {
        empresasOriginales = modelo.obtenerTodo();
        String[] columnas = {"ID", "RFC", "Nombre", "Dirección", "Teléfono", "Correo"};
        DefaultTableModel tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // No editar ID
            }
        };

        for (Empresa e : empresasOriginales) {
            tableModel.addRow(new Object[]{
                    e.getId_empresa(),
                    e.getRfc(),
                    e.getNombre(),
                    e.getDireccion(),
                    e.getTelefono(),
                    e.getCorreo()
            });
        }

        vista.jTable1.setModel(tableModel);
        vista.lblContador.setText("Empresas registradas: " + empresasOriginales.size());

        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                hayCambiosSinGuardar = true;
            }
        });
    }

    private void agregarEventos() {
        vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });
    }

    private void confirmarSalida() {
        if (!hayCambiosSinGuardar) {
            vista.dispose();
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "Hay cambios sin guardar. ¿Deseas guardarlos antes de salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            guardarCambios();
            vista.dispose();
        } else if (opcion == JOptionPane.NO_OPTION) {
            vista.dispose(); // descarta cambios
        }
        // Si CANCELAR, no hace nada
    }

    public void guardarCambios() {
        DefaultTableModel model = (DefaultTableModel) vista.jTable1.getModel();
        int filas = model.getRowCount();
        int actualizados = 0;

        for (int i = 0; i < filas; i++) {
            int id = (int) model.getValueAt(i, 0);
            String rfc = model.getValueAt(i, 1).toString().trim();
            String nombre = model.getValueAt(i, 2).toString().trim();
            String direccion = model.getValueAt(i, 3).toString().trim();
            String telefono = model.getValueAt(i, 4).toString().trim();
            String correo = model.getValueAt(i, 5).toString().trim();

            Empresa actualizada = new Empresa(id, rfc, nombre, direccion, telefono, correo);
            modelo.actualizarEmpresa(actualizada);
            actualizados++;
        }

        hayCambiosSinGuardar = false;
        JOptionPane.showMessageDialog(vista, "Cambios guardados correctamente (" + actualizados + " registros).");
    }
}
