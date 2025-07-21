/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.*;
import vista.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author alexv
 */
public class Controladordocente implements ActionListener, DocumentListener {

    private final Docente docentes;
    private final VistaDocente vista;

    public Controladordocente(Docente docentes, VistaDocente vista) {
        this.docentes = docentes;
        this.vista = vista;

        inicialisarvista();
        inicialisareventos();
        mostrarDocentesEnTabla();
    }

    private void inicialisarvista() {
        vista.lblerrorempleado.setVisible(false);
        vista.errornombre.setVisible(false);
        vista.errorapellidop.setVisible(false);
        vista.errorapellidom.setVisible(false);
        vista.errortelefono.setVisible(false);
        vista.errorcorreo.setVisible(false);
    }

    private void inicialisareventos() {
        vista.BGUARDAR.addActionListener(this);
        vista.botonregre.addActionListener(this);
        vista.discardBtn.addActionListener(this);

        vista.txtEmpleado.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!vista.txtEmpleado.getText().matches("\\d+")) {
                    vista.lblerrorempleado.setText("Solo números");
                    vista.lblerrorempleado.setVisible(true);
                } else {
                    vista.lblerrorempleado.setVisible(false);
                }
            }
        });

        KeyAdapter validadorTexto = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField campo = (JTextField) e.getSource();
                String texto = campo.getText().trim();
                boolean invalido = !texto.matches("[a-zA-Z]{3,}") || texto.matches(".*(.)\\1{2,}.*");

                if (campo == vista.txtNombre) {
                    vista.errornombre.setVisible(invalido);
                    vista.errornombre.setText("Min 3 letras, sin repetidas");
                } else if (campo == vista.txtApellidoP) {
                    vista.errorapellidop.setVisible(invalido);
                    vista.errorapellidop.setText("Min 3 letras, sin repetidas");
                } else if (campo == vista.txtApellidoM) {
                    if (!texto.isEmpty()) {
                        vista.errorapellidom.setVisible(invalido);
                        vista.errorapellidom.setText("Min 3 letras, sin repetidas");
                    } else {
                        vista.errorapellidom.setVisible(false);
                    }
                }
            }
        };

        vista.txtNombre.addKeyListener(validadorTexto);
        vista.txtApellidoP.addKeyListener(validadorTexto);
        vista.txtApellidoM.addKeyListener(validadorTexto);

        vista.txtTelefono.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!vista.txtTelefono.getText().matches("\\d+")) {
                    vista.errortelefono.setText("Solo números");
                    vista.errortelefono.setVisible(true);
                } else {
                    vista.errortelefono.setVisible(false);
                }
            }
        });

        vista.txtCorreo.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String correo = vista.txtCorreo.getText();
                boolean valido = correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
                vista.errorcorreo.setVisible(!valido);
                vista.errorcorreo.setText("Correo inválido");
            }
        });
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == vista.BGUARDAR) {
            insertardocente();
        } else if (src == vista.botonregre) {
            manejarRegreso();
        } else if (src == vista.discardBtn) {
            limpiarCampos();
        }
    }

    @Override public void insertUpdate(DocumentEvent e) {}
    @Override public void removeUpdate(DocumentEvent e) {}
    @Override public void changedUpdate(DocumentEvent e) {}

    private Docente contruirdocente() {
        Docente docente = new Docente();
        docente.setNoEmpleado(Integer.parseInt(vista.txtEmpleado.getText().trim()));
        docente.setNombre(vista.txtNombre.getText().trim());
        docente.setApellidoPaterno(vista.txtApellidoP.getText().trim());
        docente.setApellidoMaterno(vista.txtApellidoM.getText().trim());
        docente.setTelefono(vista.txtTelefono.getText().trim());
        docente.setCorreo(vista.txtCorreo.getText().trim());
        return docente;
    }

    private void insertardocente() {
        if (vista.txtEmpleado.getText().isEmpty() ||
            vista.txtNombre.getText().isEmpty() ||
            vista.txtApellidoP.getText().isEmpty() ||
            vista.txtTelefono.getText().isEmpty() ||
            vista.txtCorreo.getText().isEmpty()) {

            JOptionPane.showMessageDialog(vista, "Todos los campos deben llenarse (excepto Apellido Materno).");
            return;
        }

        Docente docente = contruirdocente();
        boolean exito = this.docentes.insertarDocente(docente);

        if (!exito) {
            JOptionPane.showMessageDialog(vista, "Credencial ya existente o error al insertar.");
        } else {
            JOptionPane.showMessageDialog(vista, "Docente ingresado con éxito");
            mostrarDocentesEnTabla();
            limpiarCampos();
        }
    }

    private void mostrarDocentesEnTabla() {
        List<Docente> lista = docentes.obtenerTodosLosDocentes();
        DefaultTableModel modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("No Empleado");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido P.");
        modeloTabla.addColumn("Apellido M.");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Correo");

        for (Docente d : lista) {
            modeloTabla.addRow(new Object[]{
                d.getNoEmpleado(),
                d.getNombre(),
                d.getApellidoPaterno(),
                d.getApellidoMaterno(),
                d.getTelefono(),
                d.getCorreo()
            });
        }

        vista.jTable2.setModel(modeloTabla);
    }

    private void limpiarCampos() {
        vista.txtEmpleado.setText("");
        vista.txtNombre.setText("");
        vista.txtApellidoP.setText("");
        vista.txtApellidoM.setText("");
        vista.txtTelefono.setText("");
        vista.txtCorreo.setText("");
        inicialisarvista();
    }

    private boolean hayDatosSinGuardar() {
        return !vista.txtEmpleado.getText().isEmpty() ||
               !vista.txtNombre.getText().isEmpty() ||
               !vista.txtApellidoP.getText().isEmpty() ||
               !vista.txtApellidoM.getText().isEmpty() ||
               !vista.txtTelefono.getText().isEmpty() ||
               !vista.txtCorreo.getText().isEmpty();
    }

    private void manejarRegreso() {
        if (hayDatosSinGuardar()) {
            int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Deseas guardar los cambios antes de salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                insertardocente();
            }
        }

       controlador.GestorVistas.regresar(vista); // cerrar la vista después
    }

    
}