package controlador;

import modelo.*;
import vista.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CInicio {

    private final Inicio vista;
    private boolean menuExpandido = true;
    private boolean estudianteExpandido = false;
    private boolean docenteExpandido = false;

    public CInicio(Inicio vista, String nombreUsuario) {
        this.vista = vista;

        inicializarVista(nombreUsuario);
        agregarEventos();
    }

    private void inicializarVista(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            nombreUsuario = "Usuario";
        }
        String nombreFormateado = nombreUsuario.substring(0, 1).toUpperCase() + nombreUsuario.substring(1);
        vista.lblBienvenida.setText("Bienvenido " + nombreFormateado);
        vista.panelSubEstudiante.setVisible(false);
        vista.panelSubDocente.setVisible(false);
    }

    private void agregarEventos() {
        // Menu principal (icono hamburguesa)
        vista.lblMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                alternarMenu();
            }
        });

        // Submenú Estudiante
        vista.lblEstudiante.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                estudianteExpandido = !estudianteExpandido;
                vista.panelSubEstudiante.setVisible(estudianteExpandido);
            }
        });

        // Submenú Docente
        vista.lblDocente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                docenteExpandido = !docenteExpandido;
                vista.panelSubDocente.setVisible(docenteExpandido);
            }
        });

        // Efecto hover para botones
        configurarHover(vista.btnNuevaLista);
        configurarHover(vista.btnEditarLista);
        configurarHover(vista.btnRegistrarDocente);
        configurarHover(vista.btnEditarDocente);
        configurarHover(vista.btnBancoProyecto);
        configurarHover(vista.btnCentroEstudiante);
        configurarHover(vista.btnCentroDocente);
        configurarHover(vista.btnCentroBanco);

        // Cerrar sesión
        vista.btnSalir.addActionListener(e -> cerrarSesion());
        vista.lblCerrarSesion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cerrarSesion();
            }
        });

        // Navegación a otras vistas
        vista.btnNuevaLista.addActionListener(e -> abrirNuevaLista());
        vista.btnEditarLista.addActionListener(e -> abrirEditarLista());
        vista.btnRegistrarDocente.addActionListener(e -> abrirRegistrarDocente());
        vista.btnEditarDocente.addActionListener(e -> abrirEditarDocente());
        vista.btnBancoProyecto.addActionListener(e -> abrirBancoProyecto());
        vista.btnCentroEstudiante.addActionListener(e -> abrirCentroEstudiante());
        vista.btnCentroDocente.addActionListener(e -> abrirCentroDocente());
        vista.btnCentroBanco.addActionListener(e -> abrirCentroBanco());
    }

    private void alternarMenu() {
        menuExpandido = !menuExpandido;
        vista.panelMenu.setPreferredSize(new Dimension(menuExpandido ? 250 : 60, vista.panelMenu.getHeight()));
        vista.panelMenu.revalidate();
    }

    private void configurarHover(JButton boton) {
        Color colorOriginal = boton.getBackground();
        Color colorHover = new Color(0, 51, 204); // azul más claro al pasar el mouse

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorOriginal);
            }
        });
    }

    private void cerrarSesion() {
        vista.dispose();
        inicioSesion login = new inicioSesion();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }

    // Métodos para abrir vistas
    private void abrirNuevaLista() {
        vista.setVisible(false);
        RegistroCandidato registro = new RegistroCandidato();
        registro.setVisible(true);
        registro.setLocationRelativeTo(null);
    }

    private void abrirEditarLista() {
        // Abrir vista correspondiente
    }

    private void abrirRegistrarDocente() {
        // Abrir vista correspondiente
    }

    private void abrirEditarDocente() {
        // Abrir vista correspondiente
    }

    private void abrirBancoProyecto() {
        vista.setVisible(false);

        Empresa modeloEmpresa = new Empresa(); // Instancia del modelo
        FormatoEmpresa empresaVista = new FormatoEmpresa(); // Instancia de la vista
        new CEmpresa(modeloEmpresa, empresaVista); // Instancia del controlador con modelo y vista

        empresaVista.setVisible(true);
        empresaVista.setLocationRelativeTo(null);
    }

    private void abrirCentroEstudiante() {
        // Abrir vista correspondiente
    }

    private void abrirCentroDocente() {
        // Abrir vista correspondiente
    }

    private void abrirCentroBanco() {
        // Abrir vista correspondiente
    }
}
