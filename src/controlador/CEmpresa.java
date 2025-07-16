/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.*;
import vista.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author carlo
 */
public class CEmpresa implements ActionListener, KeyListener, MouseListener, DocumentListener {

    private final Empresa empresas;
    private final FormatoEmpresa vista;
    private String categoriaOrden = "id_empresa";
    private boolean ordenAscendente = true;

    public CEmpresa(Empresa empresas, FormatoEmpresa vista) {
        this.empresas = empresas;
        this.vista = vista;

        inicializarVista();
        inicializarListener();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == vista.btnAnadir) {
            validarCampos();
            insertarEmpresa();
        } else if (src == vista.btnDescartar) {
            eliminarEmpresaSeleccionada();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == vista.txtTelefono) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c) || vista.txtTelefono.getText().length() >= 10) {
                e.consume();;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        validarCampos();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validarCampos();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validarCampos();
    }

    public void validarCampos() {
        String rfc = vista.txtRfc.getText().trim();
        String nombre = vista.txtNombre.getText().trim();
        String direccion = vista.txtDireccion.getText().trim();
        String telefono = vista.txtTelefono.getText().trim();
        String correo = vista.txtCorreo.getText().trim();

        boolean camposLlenos = !rfc.isEmpty() && !nombre.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty() && !correo.isEmpty();
        boolean rfcValido = rfc.length() >= 12;
        boolean telefonoValidoFormato = telefono.matches("\\d{10}");

        boolean rfcDuplicado = empresas.existeEmpresa(rfc);
        boolean nombreDuplicado = empresas.existeNombre(nombre);
        boolean telefonoDuplicado = empresas.existeTelefono(telefono);
        boolean correoDuplicado = empresas.existeCorreo(correo);

        boolean correcto = camposLlenos && rfcValido && telefonoValidoFormato
                && !rfcDuplicado && !nombreDuplicado && !telefonoDuplicado && !correoDuplicado;

        // Mostrar errores visuales
        vista.lblRfcError.setVisible(rfcDuplicado);
        vista.lblNombreError.setVisible(nombreDuplicado);
        vista.lblTelefonoErrror.setVisible(telefonoDuplicado || !telefonoValidoFormato);
        vista.lblCorreoError.setVisible(correoDuplicado);

        vista.btnAnadir.setEnabled(correcto);
    }

    private Empresa construirEmpresa() {
        Empresa empresa = new Empresa();

        empresa.setRfc(vista.txtRfc.getText().trim());
        empresa.setNombre(vista.txtNombre.getText().trim());
        empresa.setDireccion(vista.txtDireccion.getText().trim());
        empresa.setTelefono(vista.txtTelefono.getText().trim());
        empresa.setCorreo(vista.txtCorreo.getText().trim());

        return empresa;
    }

    private void insertarEmpresa() {
        Empresa empresa = construirEmpresa();
        boolean exito = this.empresas.InsertarEmpresa(empresa);

        if (!exito) {
            JOptionPane.showMessageDialog(vista, "Error al insertar empresa");
        } else {
            mostrarEmpresas(vista.jTable1);
            limpiarFormulario();
        }
    }

    public void mostrarEmpresas(JTable tabla) {
        List<Empresa> lista = empresas.obtenerTodo();
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

        tabla.setModel(modelo);
        vista.lblContador.setText("Empresas registradas: " + lista.size());

    }

    private void eliminarEmpresaSeleccionada() {
        int fila = vista.jTable1.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una empresa primero");
            return;
        }

        int idEmpresa = (int) vista.jTable1.getValueAt(fila, 0); // Columna "No. Lista"

        if (empresas.tieneProyectos(idEmpresa)) {
            JOptionPane.showMessageDialog(vista, "No es posible eliminar esta empresa: tiene proyectos registrados");
            return;
        }

        int confirmacion = JOptionPane.showOptionDialog(
                vista,
                "¿Deseas ocultar esta empresa del sistema?",
                "Confirmar ocultación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Sí", "No"},
                "No"
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (empresas.desactivarEmpresa(idEmpresa)) {
                JOptionPane.showMessageDialog(vista, "Empresa ocultada correctamente.");
                mostrarEmpresas(vista.jTable1);
            } else {
                JOptionPane.showMessageDialog(vista, "Error al ocultar empresa.");
            }
        }
    }

    public void buscarEmpresas(String tipo, String valor, JTable tabla) {
        List<Empresa> lista = empresas.buscar(tipo, valor);
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

        tabla.setModel(modelo);
        vista.lblContador.setText("Empresas registradas: " + lista.size());
    }

    public void buscarEmpresasMulticampo(String valor, JTable tabla) {
        List<Empresa> lista = empresas.buscarMulticampo(valor);
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

        tabla.setModel(modelo);
        vista.lblContador.setText("Empresas registradas: " + lista.size());
    }

    public void ordenarEmpresas(String campo, boolean ascendente, JTable tabla) {
        List<Empresa> lista = empresas.ordenarPor(campo, ascendente);
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

        tabla.setModel(modelo);
        vista.lblContador.setText("Empresas registradas: " + lista.size());
    }

    private void limpiarFormulario() {
        vista.txtRfc.setText("");
        vista.txtNombre.setText("");
        vista.txtDireccion.setText("");
        vista.txtTelefono.setText("");
        vista.txtCorreo.setText("");
    }

    private void inicializarVista() {
        vista.btnAnadir.setEnabled(false);
        vista.btnDescartar.setEnabled(false);

        vista.lblRfcError.setVisible(false);
        vista.lblNombreError.setVisible(false);
        vista.lblTelefonoErrror.setVisible(false);
        vista.lblCorreoError.setVisible(false);

        mostrarEmpresas(vista.jTable1);
    }

    private void inicializarListener() {

        vista.lblContador.addMouseListener(this);
        vista.lblRegresar.addMouseListener(this);

        vista.btnAnadir.addActionListener(this);
        vista.btnDescartar.addActionListener(this);
        vista.btnFiltro.addActionListener(this);
        vista.btnOrden.addActionListener(this);

        vista.txtRfc.getDocument().addDocumentListener(this);
        vista.txtNombre.getDocument().addDocumentListener(this);
        vista.txtDireccion.getDocument().addDocumentListener(this);
        vista.txtTelefono.getDocument().addDocumentListener(this);
        vista.txtCorreo.getDocument().addDocumentListener(this);
        vista.txtBuscador.getDocument().addDocumentListener(this);

        vista.txtTelefono.addKeyListener(this);

        vista.jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Evitar doble evento y errores
                if (!e.getValueIsAdjusting()) {
                    int filaSeleccionada = vista.jTable1.getSelectedRow();
                    vista.btnDescartar.setEnabled(filaSeleccionada != -1);
                } else {
                    vista.btnDescartar.setEnabled(false);
                }
            }
        });

        vista.btnFiltro.addActionListener(e -> cambiarCategoria());
        vista.btnOrden.addActionListener(e -> cambiarOrden());
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

        vista.lblRegresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controlador.GestorVistas.regresar(vista); // ← método universal para volver
            }
        });

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
            listaFiltrada = empresas.ordenarPor(categoriaOrden, ordenAscendente);
        } else {
            // Si hay texto, buscar en todos los campos (multicampo)
            listaFiltrada = empresas.buscarMulticampo(valor);
        }

        mostrarEmpresasFiltradas(listaFiltrada);
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

    private void cambiarOrden() {
        ordenAscendente = !ordenAscendente;
        vista.btnOrden.setText(ordenAscendente ? "Ascendente" : "Descendente");
        filtrarYOrdenar();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    class GestorVistas {

        public GestorVistas() {
        }
    }

}
