package controlador;

import modelo.*;
import vista.ConsultarProyecto;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CConsultarProyecto {

    private final ConsultarProyecto vista;
    private final Proyecto modeloProyecto;
    private final List<Empresa> empresas = new Empresa().obtenerTodo();
    private Empresa empresaSeleccionada = null;
    private List<Proyecto> proyectosActuales = new ArrayList<>();
    private boolean hayCambiosSinGuardar = false;

    private String campoOrden = "id_proyecto";
    private boolean ordenAscendente = true;

    public CConsultarProyecto(ConsultarProyecto vista) {
        this.vista = vista;
        this.modeloProyecto = new Proyecto();
        inicializar();
    }

    private void inicializar() {
        vista.lblErrorEmpresa.setVisible(false);

        // Configurar ComboBox
        vista.cboEmpresa.setEditable(true);
        vista.cboEmpresa.removeAllItems();
        for (Empresa e : empresas) {
            vista.cboEmpresa.addItem(e.getNombre());
        }

        // Filtrar dinámicamente
        JTextField editor = (JTextField) vista.cboEmpresa.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarEmpresas();
            }
        });

        // Detectar selección
        vista.cboEmpresa.addActionListener(e -> buscarEmpresa());

        // Buscar texto en proyectos
        vista.txtBuscarProyecto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarProyectos(); }
            public void removeUpdate(DocumentEvent e) { filtrarProyectos(); }
            public void changedUpdate(DocumentEvent e) { filtrarProyectos(); }
        });

        vista.btnFiltro.addActionListener(e -> cambiarFiltro());
        vista.btnOrdenar.addActionListener(e -> cambiarOrden());

        vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });

        vista.lblRegresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controlador.GestorVistas.regresar(vista);
            }
        });

        vista.jTable1.getModel().addTableModelListener(e -> hayCambiosSinGuardar = true);
    }

    private void buscarEmpresa() {
        String nombre = (String) vista.cboEmpresa.getEditor().getItem();

        empresaSeleccionada = null;
        for (Empresa e : empresas) {
            if (e.getNombre().equalsIgnoreCase(nombre)) {
                empresaSeleccionada = e;
                break;
            }
        }

        if (empresaSeleccionada != null) {
            vista.lblErrorEmpresa.setVisible(false);
            cargarProyectosDeEmpresa();
        } else {
            proyectosActuales.clear();
            actualizarTabla(new ArrayList<>());
            vista.lblErrorEmpresa.setVisible(true);
        }
    }

    private void filtrarEmpresas() {
        SwingUtilities.invokeLater(() -> {
            String texto = ((JTextField) vista.cboEmpresa.getEditor().getEditorComponent())
                    .getText().trim().toLowerCase();

            vista.cboEmpresa.removeAllItems();
            for (Empresa e : empresas) {
                if (e.getNombre().toLowerCase().contains(texto)) {
                    vista.cboEmpresa.addItem(e.getNombre());
                }
            }

            vista.cboEmpresa.getEditor().setItem(texto);
            vista.cboEmpresa.setPopupVisible(true);
        });
    }

    private void cargarProyectosDeEmpresa() {
        proyectosActuales = modeloProyecto.obtenerPorEmpresa(
                empresaSeleccionada.getId_empresa(), campoOrden, ordenAscendente
        );
        actualizarTabla(proyectosActuales);
    }

    private void actualizarTabla(List<Proyecto> lista) {
        String[] columnas = {"ID", "Título", "Descripción", "Espacios", "Disponible"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Proyecto p : lista) {
            modelo.addRow(new Object[]{
                    p.getId_proyecto(),
                    p.getTitulo(),
                    p.getDescripcion(),
                    p.getEspacios(),
                    p.getDisponible()
            });
        }

        vista.jTable1.setModel(modelo);
        vista.lblContador.setText("Proyectos registrados: " + lista.size());
        modelo.addTableModelListener(e -> hayCambiosSinGuardar = true);
    }

    private void filtrarProyectos() {
        String filtro = vista.txtBuscarProyecto.getText().trim().toLowerCase();

        if (empresaSeleccionada == null) {
            return;
        }

        List<Proyecto> filtrados = new ArrayList<>();
        for (Proyecto p : proyectosActuales) {
            if (p.getTitulo().toLowerCase().contains(filtro)
                    || p.getDescripcion().toLowerCase().contains(filtro)) {
                filtrados.add(p);
            }
        }

        actualizarTabla(filtrados);
    }

    private void cambiarFiltro() {
        String actual = vista.btnFiltro.getText();
        switch (actual) {
            case "Empresa" -> {
                campoOrden = "id_empresa";
                vista.btnFiltro.setText("Título");
            }
            case "Título" -> {
                campoOrden = "titulo";
                vista.btnFiltro.setText("Espacios");
            }
            case "Espacios" -> {
                campoOrden = "espacios";
                vista.btnFiltro.setText("Empresa");
            }
        }
        if (empresaSeleccionada != null) {
            cargarProyectosDeEmpresa();
        }
    }

    private void cambiarOrden() {
        ordenAscendente = !ordenAscendente;
        vista.btnOrdenar.setText(ordenAscendente ? "Ascendente" : "Descendente");
        if (empresaSeleccionada != null) {
            cargarProyectosDeEmpresa();
        }
    }

    public void guardarCambios() {
        TableModel modeloTabla = vista.jTable1.getModel();

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            int id = (int) modeloTabla.getValueAt(i, 0);
            String titulo = modeloTabla.getValueAt(i, 1).toString();
            String descripcion = modeloTabla.getValueAt(i, 2).toString();
            int espacios = Integer.parseInt(modeloTabla.getValueAt(i, 3).toString());
            String disponible = modeloTabla.getValueAt(i, 4).toString();

            Proyecto p = new Proyecto();
            p.setId_proyecto(id);
            p.setTitulo(titulo);
            p.setDescripcion(descripcion);
            p.setEspacios(espacios);
            p.setDisponible(disponible);
            p.setEmpresas(new Empresa[]{empresaSeleccionada});

            p.actualizarProyecto(p);
        }

        hayCambiosSinGuardar = false;
        JOptionPane.showMessageDialog(vista, "Cambios guardados correctamente.");
    }

    private void confirmarSalida() {
        if (hayCambiosSinGuardar) {
            int opcion = JOptionPane.showConfirmDialog(
                    vista,
                    "Tienes cambios sin guardar. ¿Deseas guardarlos antes de salir?",
                    "Cambios sin guardar",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                guardarCambios();
            } else if (opcion == JOptionPane.CANCEL_OPTION) {
                vista.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                return;
            }
        }

        vista.dispose();
    }
}
