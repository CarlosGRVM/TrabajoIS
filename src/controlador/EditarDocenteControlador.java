/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author alexv
 */
import modelo.Docente;
import modelo.DocenteDAO;
import vista.vistaeditar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;
import java.sql.Connection;


public class EditarDocenteControlador {

    private vistaeditar view;
    private DocenteDAO model;
    private DefaultTableModel tableModel;

    public EditarDocenteControlador(vistaeditar view) {
        this.view = view;
        this.model = new DocenteDAO();

        tableModel = (DefaultTableModel) view.getTableDocentes().getModel();
        cargarDatosTabla();

        view.getTableDocentes().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Columnas: " + tableModel.getColumnCount());

                int fila = view.getTableDocentes().getSelectedRow();
                if (fila != -1 && tableModel.getColumnCount() > 5) {
                    view.getTxtEmpleado().setText(tableModel.getValueAt(fila, 1).toString());
                    view.getTxtNombre().setText(tableModel.getValueAt(fila, 2).toString());
                    view.getTxtApellidoP().setText(tableModel.getValueAt(fila, 3).toString());
                    view.getTxtApellidoM().setText(tableModel.getValueAt(fila, 4).toString());
                    view.getTxtCorreo().setText(tableModel.getValueAt(fila, 5).toString());
                }
            }
        });

        view.getBtnGuardar().addActionListener(e -> actualizarDocente());
        view.getBtnEliminarTabla().addActionListener(e -> eliminarFilaTabla());
        view.getBtnCerrar().addActionListener(e -> view.dispose());
    }

    private void cargarDatosTabla() {
        List<Docente> lista = model.obtenerTodosLosDocentes();
        tableModel.setRowCount(0);
        for (Docente d : lista) {
            tableModel.addRow(new Object[]{
                d.getNumeroEmpleado(),
                d.getNombre(),
                d.getApellidoPaterno(),
                d.getApellidoMaterno(),
                d.getCorreo()
            });
        }
    }

    private void actualizarDocente() {
        int fila = view.getTableDocentes().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Seleccione un docente para actualizar.");
            return;
        }

        String noEmpleado = view.getTxtEmpleado().getText().trim();
        String nombre = view.getTxtNombre().getText().trim();
        String apellidoP = view.getTxtApellidoP().getText().trim();
        String apellidoM = view.getTxtApellidoM().getText().trim();
        String telefono = "0000000000";
        String correo = view.getTxtCorreo().getText().trim();

        if (noEmpleado.isEmpty() || nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, complete todos los campos.");
            return;
        }

        if (!noEmpleado.matches("\\d+")) {
            JOptionPane.showMessageDialog(view, "El número de empleado solo debe contener números.");
            return;
        }

        if (!esTextoValido(nombre) || !esTextoValido(apellidoP) || !esTextoValido(apellidoM)) {
            JOptionPane.showMessageDialog(view, "Nombre y apellidos deben tener al menos 3 letras válidas y no ser repetitivos.");
            return;
        }

        if (!esCorreoValido(correo)) {
            JOptionPane.showMessageDialog(view, "El correo electrónico no es válido. Ejemplo: usuario@gmail.com");
            return;
        }

        Docente docente = new Docente(noEmpleado, nombre, apellidoP, apellidoM, telefono ,correo);
        if (model.actualizarDocente(docente)) {
            JOptionPane.showMessageDialog(view, "Docente actualizado correctamente.");
            cargarDatosTabla();
        } else {
            JOptionPane.showMessageDialog(view, "No se pudo actualizar el docente.");
        }
    }

    private void eliminarFilaTabla() {
        int fila = view.getTableDocentes().getSelectedRow();
        if (fila != -1) {
            tableModel.removeRow(fila);
        } else {
            JOptionPane.showMessageDialog(view, "Seleccione una fila para eliminar de la tabla.");
        }
    }

    private boolean esTextoValido(String texto) {
        if (texto.length() < 3) return false;
        if (texto.matches("(.)\\1{2,}")) return false;
        return texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    private boolean esCorreoValido(String correo) {
        return correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }
}
