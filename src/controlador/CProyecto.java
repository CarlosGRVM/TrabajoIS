package controlador;

import modelo.*;
import vista.FormatoProyecto;
import java.sql.SQLException;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

public class CProyecto implements ActionListener, DocumentListener {

    private final FormatoProyecto vista;
    private final Proyecto modeloProyecto;
    private final Empresa[] empresas;

    private String campoOrden = "id_proyecto";
    private boolean ordenAscendente = true;

    public CProyecto(FormatoProyecto vista, Empresa[] empresas) {
        this.vista = vista;
        this.empresas = empresas;
        this.modeloProyecto = new Proyecto();
        modeloProyecto.setEmpresas(empresas);

        inicializarVista();
        agregarEventos();
        mostrarProyectos();
    }

    private void inicializarVista() {
        vista.lblContador.setText("Proyectos registrados: 0");
        vista.btnAnadir.setEnabled(false);
        vista.btnDescartar.setEnabled(false);

        vista.lblEmpresaErr.setVisible(false);
        vista.lblTituloErr.setVisible(false);

        // Llenar el JComboBox con nombres de empresas
        vista.cboEmpresa.removeAllItems();
        for (Empresa e : empresas) {
            vista.cboEmpresa.addItem(e.getNombre());
        }

        // Evitar selección en la tabla
        vista.jTable1.setRowSelectionAllowed(true);
    }

    private void agregarEventos() {
        vista.btnAnadir.addActionListener(this);
        vista.btnDescartar.addActionListener(this);
        vista.btnFiltro.addActionListener(this);
        vista.btnOrdenar.addActionListener(this);

        vista.txtTitulo.getDocument().addDocumentListener(this);
        vista.txtDescripcion.getDocument().addDocumentListener(this);
        vista.txtEspacios.getDocument().addDocumentListener(this);
        vista.txtBuscar.getDocument().addDocumentListener(this);

        vista.cboEmpresa.setEditable(true);

        // Detectar escritura
        JTextField editor = (JTextField) vista.cboEmpresa.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarEmpresas();
            }
        });

        // Detectar selección de item
        vista.cboEmpresa.addActionListener(e -> validarCampos());

        vista.jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                vista.btnDescartar.setEnabled(vista.jTable1.getSelectedRow() != -1);
            }
        });

        vista.lblRegresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controlador.GestorVistas.regresar(vista);
            }
        });
    }

    private void validarCampos() {
        String titulo = vista.txtTitulo.getText().trim();
        String descripcion = vista.txtDescripcion.getText().trim();
        String espaciosStr = vista.txtEspacios.getText().trim();
        String nombreEmpresa = (String) vista.cboEmpresa.getSelectedItem(); // ← Cambio aquí

        boolean camposLlenos = !titulo.isEmpty() && !descripcion.isEmpty() && !espaciosStr.isEmpty() && nombreEmpresa != null;

        // Validación de espacios
        boolean espaciosValidos;
        try {
            int esp = Integer.parseInt(espaciosStr);
            espaciosValidos = esp > 0;
        } catch (NumberFormatException e) {
            espaciosValidos = false;
        }

        // Validación empresa
        boolean empresaExiste = false;
        for (Empresa e : empresas) {
            if (e.getNombre().equalsIgnoreCase(nombreEmpresa)) {
                empresaExiste = true;
                break;
            }
        }
        vista.lblEmpresaErr.setVisible(nombreEmpresa != null && !empresaExiste);

        // Validación título duplicado
        boolean tituloExiste = !titulo.isEmpty() && modeloProyecto.existeTitulo(titulo);
        vista.lblTituloErr.setVisible(tituloExiste);

        vista.btnAnadir.setEnabled(camposLlenos && espaciosValidos && empresaExiste && !tituloExiste);
    }

    private Proyecto construirProyectoDesdeVista() {
        Proyecto p = new Proyecto();
        p.setId_proyecto(modeloProyecto.generarSiguienteId());
        p.setTitulo(vista.txtTitulo.getText().trim());
        p.setDescripcion(vista.txtDescripcion.getText().trim());
        p.setEspacios(Integer.parseInt(vista.txtEspacios.getText().trim()));
        p.setDisponible("Disponible");
        p.setTipo("Anteproyecto");

        String nombreEmpresa = (String) vista.cboEmpresa.getSelectedItem();
        for (Empresa e : empresas) {
            if (e.getNombre().equalsIgnoreCase(nombreEmpresa)) {
                p.setEmpresas(new Empresa[]{e});
                break;
            }
        }

        return p;
    }

    private void mostrarProyectos() {
        List<Proyecto> lista = modeloProyecto.obtenerTodos(empresas, campoOrden, ordenAscendente);
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"ID", "Empresa", "Título", "Descripción", "Espacios", "Disponible"}, 0);

        for (Proyecto p : lista) {
            String nombreEmpresa = p.getEmpresas()[0] != null ? p.getEmpresas()[0].getNombre() : "N/D";
            modelo.addRow(new Object[]{
                p.getId_proyecto(),
                nombreEmpresa,
                p.getTitulo(),
                p.getDescripcion(),
                p.getEspacios(),
                p.getDisponible()
            });
        }

        vista.jTable1.setModel(modelo);
        vista.lblContador.setText("Proyectos registrados: " + lista.size());
    }

    private void insertarProyecto() {
        Proyecto nuevo = construirProyectoDesdeVista();

        if (nuevo.getEmpresas() == null || nuevo.getEmpresas()[0] == null) {
            vista.lblEmpresaErr.setVisible(true);
            return;
        }

        try {
            boolean exito = nuevo.insertarProyecto(nuevo.getEmpresas()[0].getId_empresa());

            if (exito) {
                JOptionPane.showMessageDialog(vista, "Proyecto registrado correctamente.");
                limpiarFormulario();
                mostrarProyectos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error desconocido al registrar proyecto.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "❌ Error al insertar proyecto:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // También en consola para depurar
        }
    }

    private void eliminarProyectoSeleccionado() {
        int fila = vista.jTable1.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona un proyecto primero.");
            return;
        }

        int idProyecto = (int) vista.jTable1.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(
                vista,
                "¿Deseas ocultar este proyecto del sistema?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (modeloProyecto.eliminarProyecto(idProyecto)) {
                JOptionPane.showMessageDialog(vista, "Proyecto eliminado correctamente.");
                mostrarProyectos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al ocultar proyecto.");
            }
        }
    }

    private void cambiarOrden() {
        ordenAscendente = !ordenAscendente;
        vista.btnOrdenar.setText(ordenAscendente ? "Ascendente" : "Descendente");
        mostrarProyectos();
    }

    private void limpiarFormulario() {
        if (vista.cboEmpresa.getItemCount() > 0) {
            vista.cboEmpresa.setSelectedIndex(0); // ← limpia selección a la primera empresa
        }
        vista.txtTitulo.setText("");
        vista.txtDescripcion.setText("");
        vista.txtEspacios.setText("");
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
        mostrarProyectos();
    }

    private void buscarProyectos() {
        String texto = vista.txtBuscar.getText().trim().toLowerCase();
        List<Proyecto> lista = modeloProyecto.obtenerTodos(empresas, campoOrden, ordenAscendente);

        List<Proyecto> filtrados = lista.stream()
                .filter(p -> p.getTitulo().toLowerCase().contains(texto)
                || p.getDescripcion().toLowerCase().contains(texto)
                || (p.getEmpresas()[0] != null && p.getEmpresas()[0].getNombre().toLowerCase().contains(texto)))
                .toList();

        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"ID", "Empresa", "Título", "Descripción", "Espacios", "Disponible"}, 0);

        for (Proyecto p : filtrados) {
            String nombreEmpresa = p.getEmpresas()[0] != null ? p.getEmpresas()[0].getNombre() : "N/D";
            modelo.addRow(new Object[]{
                p.getId_proyecto(),
                nombreEmpresa,
                p.getTitulo(),
                p.getDescripcion(),
                p.getEspacios(),
                p.getDisponible()
            });
        }

        vista.jTable1.setModel(modelo);
        vista.lblContador.setText("Proyectos registrados: " + filtrados.size());
    }

    // Eventos
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == vista.btnAnadir) {
            insertarProyecto();
        } else if (src == vista.btnDescartar) {
            eliminarProyectoSeleccionado();
        } else if (src == vista.btnOrdenar) {
            cambiarOrden();
        } else if (src == vista.btnFiltro) {
            cambiarFiltro();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        validarCampos();
        buscarProyectos();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validarCampos();
        buscarProyectos();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validarCampos();
        buscarProyectos();
    }

    private void filtrarEmpresas() {
        SwingUtilities.invokeLater(() -> {
            String texto = ((JTextField) vista.cboEmpresa.getEditor().getEditorComponent()).getText().trim().toLowerCase();

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

}
