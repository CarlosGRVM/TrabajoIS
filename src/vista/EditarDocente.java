/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author alexv
 */
import javax.swing.*;
import java.sql.Connection;


public class EditarDocente extends javax.swing.JFrame {

    public EditarDocente() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar Docentes");

        JLabel lblTitulo = new JLabel("Editar Docentes", SwingConstants.CENTER);
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18));

        JScrollPane scrollPane = new JScrollPane();
        tableDocentes = new JTable();
        tableDocentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"No. Empleado", "Nombre", "Apellido Paterno", "Apellido Materno", "Correo"}
        ));
        scrollPane.setViewportView(tableDocentes);

        JLabel lblEmpleado = new JLabel("No. Empleado:");
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblApellidoP = new JLabel("Apellido Paterno:");
        JLabel lblApellidoM = new JLabel("Apellido Materno:");
        JLabel lblCorreo = new JLabel("Correo:");

        txtEmpleado = new JTextField();
        txtNombre = new JTextField();
        txtApellidoP = new JTextField();
        txtApellidoM = new JTextField();
        txtCorreo = new JTextField();

        btnGuardar = new JButton("Guardar Cambios");
        btnEliminarTabla = new JButton("Eliminar de Tabla");
        btnCerrar = new JButton("Cerrar");

        // Layout (usa GroupLayout o AbsoluteLayout según prefieras)
        // Aquí simplificado con BoxLayout:
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(lblTitulo);
        panel.add(scrollPane);

        panel.add(lblEmpleado); panel.add(txtEmpleado);
        panel.add(lblNombre); panel.add(txtNombre);
        panel.add(lblApellidoP); panel.add(txtApellidoP);
        panel.add(lblApellidoM); panel.add(txtApellidoM);
        panel.add(lblCorreo); panel.add(txtCorreo);

        panel.add(btnGuardar);
        panel.add(btnEliminarTabla);
        panel.add(btnCerrar);

        add(panel);
        pack();
    }

    // Getters
    public JTable getTableDocentes() { return tableDocentes; }
    public JTextField getTxtEmpleado() { return txtEmpleado; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtApellidoP() { return txtApellidoP; }
    public JTextField getTxtApellidoM() { return txtApellidoM; }
    public JTextField getTxtCorreo() { return txtCorreo; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnEliminarTabla() { return btnEliminarTabla; }
    public JButton getBtnCerrar() { return btnCerrar; }

    private JTable tableDocentes;
    private JTextField txtEmpleado, txtNombre, txtApellidoP, txtApellidoM, txtCorreo;
    private JButton btnGuardar, btnEliminarTabla, btnCerrar;
}

