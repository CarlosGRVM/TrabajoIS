package controlador;

import modelo.Docente;
import modelo.DocenteDAO;
import vista.docente;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;

public class DocenteControlador {

    private docente view;
    private DocenteDAO model;
    private DefaultTableModel tableModel;

    public DocenteControlador(docente view) {
        this.view = view;
        this.model = new DocenteDAO();
        String[] columnNames = {"No. Empleado", "Nombre", "Apellido Paterno", "Apellido Materno", "Teléfono", "Correo"};
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
        String telefono = "0000000000";
        String correo = view.getTxtCorreo().getText().trim();

        if (noEmpleado.isEmpty() || nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty()
                || telefono.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor complete todos los campos.");
            return;
        }

        if (!noEmpleado.matches("\\d+")) {
            JOptionPane.showMessageDialog(view, "El número de empleado solo debe contener números.");
            return;
        }

        if (!esNombreValido(nombre) || !esNombreValido(apellidoP) || !esNombreValido(apellidoM)) {
            JOptionPane.showMessageDialog(view, "Nombre y apellidos deben tener al menos 3 letras válidas y no ser repetitivas.");
            return;
        }

        if (!telefono.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(view, "El número de teléfono debe contener exactamente 10 dígitos.");
            return;
        }

        if (!esCorreoValido(correo)) {
            JOptionPane.showMessageDialog(view, "El correo electrónico no es válido. Ejemplo: usuario@dominio.com");
            return;
        }

        Docente nuevoDocente = new Docente(noEmpleado, nombre, apellidoP, apellidoM, telefono, correo);
        model.guardarDocente(nuevoDocente);
        limpiarCamposFormulario();
        deshabilitarCamposFormulario();
        cargarDocentes();
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

    private boolean esNombreValido(String texto) {
        return texto.length() >= 3 && !texto.matches("(.)\\1{2,}") &&
               texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    private boolean esCorreoValido(String correo) {
        return correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    private void cargarDocentes() {
        tableModel.setRowCount(0); // limpiar tabla
        List<Docente> lista = model.obtenerTodosLosDocentes();
        for (Docente d : lista) {
            Object[] fila = {
                d.getNumeroEmpleado(),
                d.getNombre(),
                d.getApellidoPaterno(),
                d.getApellidoMaterno(),
                d.getTelefono(),
                d.getCorreo()
            };
            tableModel.addRow(fila);
        }
    }
}
