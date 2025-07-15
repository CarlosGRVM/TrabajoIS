/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import modelo.Docente;
import modelo.DocenteDAO;
import vista.FormularioDocente; 
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocenteControlador {

    private FormularioDocente view; 
    private DocenteDAO model; 
    private DefaultTableModel tableModel; 
    
    public DocenteControlador(FormularioDocente view) {
        this.view = view;
        this.model = new DocenteDAO(); 
        String[] columnNames = {"ID", "No. Empleado", "Nombre", "Apellido Paterno", "Apellido Materno", "Correo"};
        this.tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
    }

    public void guardarDocente() {
        String noEmpleado = view.getTxtEmpleado().getText().trim();
    String nombre = view.getTxtNombre().getText().trim();
    String apellidoP = view.getTxtApellidoP().getText().trim();
    String apellidoM = view.getTxtApellidoM().getText().trim();
    String correo = view.getTxtCorreo().getText().trim();

    // Validación de campos vacíos
    if (noEmpleado.isEmpty() || nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty() || correo.isEmpty()) {
        JOptionPane.showMessageDialog(view, "Por favor complete todos los campos antes de guardar.");
        return;
    }

    Docente nuevoDocente = new Docente(noEmpleado, nombre, apellidoP, apellidoM, correo);

    model.guardarDocente(nuevoDocente);
    limpiarCamposFormulario();
    deshabilitarCamposFormulario();
}

    public void habilitarCamposFormulario() {
        view.getTxtEmpleado().setEnabled(true);
        view.getTxtNombre().setEnabled(true);
        view.getTxtApellidoP().setEnabled(true);
        view.getTxtApellidoM().setEnabled(true);
        view.getTxtCorreo().setEnabled(true);
        view.getBGUARDAR().setEnabled(true);
    }

    public void deshabilitarCamposFormulario() {
        view.getTxtEmpleado().setEnabled(false);
        view.getTxtNombre().setEnabled(false);
        view.getTxtApellidoP().setEnabled(false);
        view.getTxtApellidoM().setEnabled(false);
        view.getTxtCorreo().setEnabled(false);
        view.getBGUARDAR().setEnabled(false);
    }

    public void limpiarCamposFormulario() {
        view.getTxtEmpleado().setText("");
        view.getTxtNombre().setText("");
        view.getTxtApellidoP().setText("");
        view.getTxtApellidoM().setText("");
        view.getTxtCorreo().setText("");
    }

    public void regresarVentana() {
        view.dispose();
    }
} 
