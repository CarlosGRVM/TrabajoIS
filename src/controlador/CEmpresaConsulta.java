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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CEmpresaConsulta {

    private final ConsultaEmpresa vista;
    private final Empresa modelo;
    private boolean hayCambiosSinGuardar = false;
    private List<Empresa> empresasOriginales;
    private String categoriaOrden = "id_empresa";
    private boolean ordenAscendente = true;

    public CEmpresaConsulta(ConsultaEmpresa vista) {
        this.vista = vista;
        this.modelo = new Empresa();
        cargarDatos();
        agregarEventos();
        vista.btnGuardar.setEnabled(false);

    }

    private void cargarDatos() {
        empresasOriginales = modelo.obtenerTodo();
        String[] columnas = {"ID", "RFC", "Nombre", "Dirección", "Teléfono", "Correo"};
        DefaultTableModel tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // ID no editable
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
                vista.btnGuardar.setEnabled(true); // ✔ habilitar botón
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

        vista.lblRegresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                confirmarSalida();
                controlador.GestorVistas.regresar(vista); // ← método universal para volver
            }
        });

        vista.btnFiltro.addActionListener(e -> cambiarCategoria());
        vista.btnOrden.addActionListener(e -> cambiarOrden());
        vista.btnGuardar.addActionListener(e -> guardarCambios());
        vista.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        vista.txtBuscador.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarYOrdenar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarYOrdenar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarYOrdenar();
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
        vista.btnGuardar.setEnabled(false);

    }

    private void cambiarCategoria() {
        String[] categorias = {"No. Lista", "RFC", "Nombre", "Teléfono", "Correo"};
        String actual = vista.btnFiltro.getText();  // Por ejemplo: "Nombre"
        int index = java.util.Arrays.asList(categorias).indexOf(actual);
        int siguiente = (index + 1) % categorias.length;

        vista.btnFiltro.setText(categorias[siguiente]);

        // Mapear al campo de BD
        categoriaOrden = switch (categorias[siguiente]) {
            case "No. Lista" ->
                "id_empresa";
            case "RFC" ->
                "rfc";
            case "Nombre" ->
                "nombre";
            case "Teléfono" ->
                "telefono";
            case "Correo" ->
                "correo";
            default ->
                "id_empresa";
        };

        filtrarYOrdenar();
    }

    private void filtrarYOrdenar() {
        String valor = vista.txtBuscador.getText().trim();
        List<Empresa> listaFiltrada;

        if (valor.isEmpty()) {
            // Si el buscador está vacío, ordenar por categoría
            listaFiltrada = modelo.ordenarPor(categoriaOrden, ordenAscendente);
        } else {
            // Si hay texto, buscar en todos los campos (multicampo)
            listaFiltrada = modelo.buscarMulticampo(valor);
        }

        mostrarEmpresasFiltradas(listaFiltrada);
    }

    private void cambiarOrden() {
        ordenAscendente = !ordenAscendente;
        vista.btnOrden.setText(ordenAscendente ? "Ascendente" : "Descendente");
        filtrarYOrdenar();
    }

    private void mostrarEmpresasFiltradas(List<Empresa> lista) {
        String[] columnas = {"No. Lista", "RFC", "Nombre", "Dirección", "Teléfono", "Correo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Empresa e : lista) {
            Object[] fila = {
                e.getId_empresa(),
                e.getRfc(),
                e.getNombre(),
                e.getDireccion(),
                e.getTelefono(),
                e.getCorreo()
            };
            modelo.addRow(fila);
        }

        vista.jTable1.setModel(modelo);
        vista.lblContador.setText("Empresas registradas: " + lista.size());
    }
}
