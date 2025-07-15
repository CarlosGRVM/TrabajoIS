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
        vista.setVisible(false);
        inicioSesion login = new inicioSesion();
        new CInicioSesion(login); // <- Asignas controlador al login
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }

    // Métodos para abrir vistas
    private void abrirNuevaLista() {
        vista.setVisible(false);
        RegistroCandidato registro = new RegistroCandidato();
        controlador.GestorVistas.registrarTransicion(vista, registro);  // ← registrar
        registro.setVisible(true);
        registro.setLocationRelativeTo(null);
    }

    private void abrirEditarLista() {
        vista.setVisible(false);
        ListaCandidato consultar = new ListaCandidato();
        controlador.GestorVistas.registrarTransicion(vista, consultar);  // ← registrar
        consultar.setVisible(true);
        consultar.setLocationRelativeTo(null);
    }

    private void abrirRegistrarDocente() {
        vista.setVisible(false);
        FormularioDocente docente = new FormularioDocente();
        controlador.GestorVistas.registrarTransicion(vista, docente);  // ← registrar
        docente.setVisible(true);
        docente.setLocationRelativeTo(null);
    }

    private void abrirEditarDocente() {
        vista.setVisible(false);
        RegistroCandidato registro = new RegistroCandidato();
        controlador.GestorVistas.registrarTransicion(vista, registro);  // ← registrar
        registro.setVisible(true);
        registro.setLocationRelativeTo(null);
    }

    private void abrirBancoProyecto() {
        vista.setVisible(false);
        MenuProyectos menuVista = new MenuProyectos();
        controlador.GestorVistas.registrarTransicion(vista, menuVista);  // ← registrar
        new CCentroBanco(menuVista);
        menuVista.setVisible(true);
        menuVista.setLocationRelativeTo(null);
    }

    private void abrirCentroEstudiante() {
        vista.setVisible(false);
        RegistroCandidato registro = new RegistroCandidato();
        controlador.GestorVistas.registrarTransicion(vista, registro);  // ← registrar
        registro.setVisible(true);
        registro.setLocationRelativeTo(null);
    }

    private void abrirCentroDocente() {
        vista.setVisible(false);
        FormularioDocente docente = new FormularioDocente();
        controlador.GestorVistas.registrarTransicion(vista, docente);  // ← registrar
        docente.setVisible(true);
        docente.setLocationRelativeTo(null);
    }

    private void abrirCentroBanco() {
        vista.setVisible(false);
        MenuProyectos menuVista = new MenuProyectos();
        controlador.GestorVistas.registrarTransicion(vista, menuVista);  // ← registrar
        new CCentroBanco(menuVista);
        menuVista.setVisible(true);
        menuVista.setLocationRelativeTo(null);
    }

}
