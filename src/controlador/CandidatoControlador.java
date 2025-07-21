/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import modelo.ModeloCandidato;
import modelo.ModeloCandidatoBD;
import vista.RegistroCandidato;

/**
 *
 * @author marthy
 */

public class CandidatoControlador implements ActionListener {

    private RegistroCandidato vista;
   
    private ModeloCandidatoBD dao;
    private boolean ordenAscendente = true;

    public CandidatoControlador(RegistroCandidato vista) {
        this.vista = vista;
        
        this.dao = new ModeloCandidatoBD();

        this.vista.setVisible(true);
        this.vista.btnAñadir.addActionListener(this);
        this.vista.btnDescartar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnOrden.addActionListener(this);
        this.vista.btnRegresar.addActionListener(this);
        this.vista.btnRegresar.addActionListener(this);

        listar();
        agregarValidaciones();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAñadir) {
            agregarCandidato();
        } else if (e.getSource() == vista.btnDescartar) {
            limpiarCampos();
        } else if (e.getSource() == vista.btnBuscar) {
            buscar();
        } else if (e.getSource() == vista.btnOrden) {
            alternarOrden();
        } else if (e.getSource() == vista.btnRegresar) {
            controlador.GestorVistas.regresar(vista);
        }
    }

    private void agregarCandidato() {
        String numControl = vista.txtNumControl.getText().trim();
        String nombre = vista.txtNombre.getText().trim();
        String apeP = vista.txtApeP.getText().trim();
        String apeM = vista.txtApeM.getText().trim();
        String telefono = vista.txtTelefono.getText().trim();
        String correo = vista.txtCorreo.getText().trim();

        // Validar campos vacíos
        if (numControl.isEmpty() || nombre.isEmpty() || apeP.isEmpty() || apeM.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "⚠️ Todos los campos deben estar llenos");
            return;
        }

        // Validar número de control: 8-9 dígitos o c + 8 dígitos
        if (!numControl.matches("^\\d{8,9}$") && !numControl.matches("^c\\d{8}$")) {
            JOptionPane.showMessageDialog(null, "⚠️ Número de control inválido (debe tener 8 o 9 dígitos, o comenzar con 'c' seguido de 8 dígitos)");
            return;
        }

        // Validar que nombre y apellidos solo contengan letras y espacios
        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")
                || !apeP.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")
                || !apeM.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            JOptionPane.showMessageDialog(null, "⚠️ Los nombres y apellidos solo deben contener letras");
            return;
        }

        // Validar correo
        if (!correo.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(null, "⚠️ Correo electrónico inválido");
            return;
        }

        // Validar teléfono (exactamente 10 dígitos)
        if (!telefono.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(null, "⚠️ El teléfono debe tener exactamente 10 dígitos");
            return;
        }
        // Validar duplicado
        if (dao.existeNumeroControl(numControl)) {
            JOptionPane.showMessageDialog(null, "⚠️ Ya existe un candidato con ese número de control");
            return;
        }

        // Si pasa las validaciones, continuar con la inserción
        ModeloCandidato c = new ModeloCandidato(numControl, nombre, apeP, apeM, telefono, correo);

        if (dao.insertar(c)) {
            JOptionPane.showMessageDialog(null, "✅ Candidato registrado correctamente");
            listar();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(null, "❌ Error al registrar candidato en la base de datos");
        }
    }

    private void agregarValidaciones() {
        // Solo letras para nombre y apellidos
        KeyAdapter soloLetras = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        };

        vista.txtNombre.addKeyListener(soloLetras);
        vista.txtApeP.addKeyListener(soloLetras);
        vista.txtApeM.addKeyListener(soloLetras);

        // Validar número de control al perder foco
        vista.txtNumControl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String nc = vista.txtNumControl.getText();
                if (!nc.matches("^(\\d{8,9}|c\\d{8})$")) {
                    JOptionPane.showMessageDialog(null, "Número de control inválido");
                } else if (dao.existeNumeroControl(nc)) {
                    JOptionPane.showMessageDialog(null, "⚠️ Ya existe un candidato con este número de control.");
                }
            }
        });

        // Validar correo al perder foco
        vista.txtCorreo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String correo = vista.txtCorreo.getText();
                if (!correo.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(null, "Correo inválido");
                }
            }
        });

        // Validar teléfono al perder foco
        vista.txtTelefono.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String tel = vista.txtTelefono.getText();
                if (!tel.matches("^\\d{10}$")) {
                    JOptionPane.showMessageDialog(null, "Teléfono inválido (10 dígitos)");
                }
            }
        });

    }

    private void listar() {
        ArrayList<ModeloCandidato> lista = dao.listarTodos();
        DefaultTableModel modelo = (DefaultTableModel) vista.tblCandidato.getModel();
        modelo.setRowCount(0);

        for (ModeloCandidato c : lista) {
            modelo.addRow(new Object[]{
                c.getNumControl(),
                c.getNombre(),
                c.getApeP(),
                c.getApeM(),
                c.getTelefono(),
                c.getCorreo(),});
        }

        vista.etiCandidato.setText(lista.size() + "/" + lista.size());
        vista.tblCandidato.setRowSorter(null); // Elimina ordenamiento
        vista.txtBusqueda.setText("");         // Borra el texto de búsqueda 
        vista.btnOrden.setText("Ascendente");
        ordenAscendente = true;

    }

    private void buscar() {
        String texto = vista.txtBusqueda.getText();
        ArrayList<ModeloCandidato> lista = dao.buscar(texto);

        DefaultTableModel modelo = (DefaultTableModel) vista.tblCandidato.getModel();
        modelo.setRowCount(0);

        for (ModeloCandidato c : lista) {
            modelo.addRow(new Object[]{
                c.getNumControl(),
                c.getNombre(),
                c.getApeP(),
                c.getApeM(),
                c.getTelefono(),
                c.getCorreo(),});
        }

        vista.etiCandidato.setText(lista.size() + "/" + dao.listarTodos().size());
        vista.tblCandidato.setRowSorter(null); // quita ordenamientos anteriores
        vista.btnOrden.setText("Ascendente"); // Reinicia el texto del botón
        ordenAscendente = true;

    }

    private void alternarOrden() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tblCandidato.getModel();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(modelo);
        vista.tblCandidato.setRowSorter(sorter);

        // Ordena por la columna de nombre (índice 1)
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        if (ordenAscendente) {
            sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
            vista.btnOrden.setText("Descendente");
        } else {
            sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
            vista.btnOrden.setText("Ascendente");
        }

        sorter.setSortKeys(sortKeys);
        sorter.sort();

        ordenAscendente = !ordenAscendente; // Alternar para la próxima vez
    }

    private void limpiarCampos() {
        vista.txtNumControl.setText("");
        vista.txtNombre.setText("");
        vista.txtApeP.setText("");
        vista.txtApeM.setText("");
        vista.txtCorreo.setText("");
        vista.txtTelefono.setText("");
    }
}
