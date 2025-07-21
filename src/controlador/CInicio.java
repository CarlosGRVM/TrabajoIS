package controlador;

import modelo.*;
import vista.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;

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
        vista.lblMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                alternarMenu();
            }
        });

        vista.lblEstudiante.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                estudianteExpandido = !estudianteExpandido;
                vista.panelSubEstudiante.setVisible(estudianteExpandido);
            }
        });

        vista.lblDocente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                docenteExpandido = !docenteExpandido;
                vista.panelSubDocente.setVisible(docenteExpandido);
            }
        });

        vista.lblProyectos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.setVisible(false);
                MenuProyectos menuVista = new MenuProyectos();
                controlador.GestorVistas.registrarTransicion(vista, menuVista);
                new CCentroBanco(menuVista);
                menuVista.setVisible(true);
                menuVista.setLocationRelativeTo(null);
            }
        });

        vista.btnEstudianteMenu.addActionListener(e -> {
            estudianteExpandido = !estudianteExpandido;
            vista.panelSubEstudiante.setVisible(estudianteExpandido);
        });

        vista.btnDocenteMenu.addActionListener(e -> {
            docenteExpandido = !docenteExpandido;
            vista.panelSubDocente.setVisible(docenteExpandido);
        });

        vista.btnSalir.addActionListener(e -> cerrarSesion());
        vista.lblCerrarSesion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cerrarSesion();
            }
        });

        // Colores para la animación
        Color azulOscuro = new Color(0, 38, 84);
        Color azulClaro = new Color(51, 102, 204);
        Color rojoOscuro = new Color(176, 0, 0);
        Color rojoClaro = new Color(255, 47, 47);
        Color azulTono = new Color(0, 32, 96);

        // Aplicar animación a todos los botones
        List<JButton> botones = Arrays.asList(
                vista.btnNuevaLista,
                vista.btnEditarLista,
                vista.btnRegistrarDocente,
                vista.btnCentroEstudiante,
                vista.btnCentroDocente,
                vista.btnCentroBanco
        );

        List<JButton> menuBotones = Arrays.asList(
                vista.btnEstudianteMenu,
                vista.btnDocenteMenu,
                vista.btnBancoProyecto);

        for (JButton boton : botones) {
            aplicarTransicionHover(boton, azulOscuro, azulClaro, 10, 15);
        }

        for (JButton menuBoton : menuBotones) {
            aplicarTransicionHover(menuBoton, azulTono, azulClaro, 10, 15);
        }

        aplicarTransicionHover(vista.btnSalir, rojoOscuro, rojoClaro, 10, 15);

        // Menú lateral con transición
        vista.btnNuevaLista.addActionListener(e -> abrirNuevaLista());
        vista.btnEditarLista.addActionListener(e -> abrirEditarLista());
        vista.btnRegistrarDocente.addActionListener(e -> abrirRegistrarDocente());
        vista.btnBancoProyecto.addActionListener(e -> abrirBancoProyecto());
        vista.btnCentroEstudiante.addActionListener(e -> abrirCentroEstudiante());
        vista.btnCentroDocente.addActionListener(e -> abrirCentroDocente());
        vista.btnCentroBanco.addActionListener(e -> abrirCentroBanco());
    }

    private void alternarMenu() {
        int anchoInicial = vista.panelMenu.getWidth();
        int anchoFinal = menuExpandido ? 60 : 368;
        int pasos = 15;
        int delayMs = 10;

        Timer timer = new Timer(delayMs, null);
        timer.addActionListener(new ActionListener() {
            int pasoActual = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                float ratio = (float) pasoActual / pasos;
                int nuevoAncho = (int) (anchoInicial + ratio * (anchoFinal - anchoInicial));
                vista.panelMenu.setPreferredSize(new Dimension(nuevoAncho, vista.panelMenu.getHeight()));
                vista.panelMenu.revalidate();
                pasoActual++;
                if (pasoActual > pasos) {
                    timer.stop();
                    menuExpandido = !menuExpandido;
                }
            }
        });
        timer.start();
    }

    private void aplicarTransicionHover(JButton boton, Color colorInicial, Color colorFinal, int pasos, int delayMs) {
        final Timer[] timerEntrada = new Timer[1];
        final Timer[] timerSalida = new Timer[1];

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (timerSalida[0] != null && timerSalida[0].isRunning()) {
                    timerSalida[0].stop();
                }
                timerEntrada[0] = new Timer(delayMs, null);
                timerEntrada[0].addActionListener(new ActionListener() {
                    int paso = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (paso <= pasos) {
                            float ratio = (float) paso / pasos;
                            int r = (int) (colorInicial.getRed() + ratio * (colorFinal.getRed() - colorInicial.getRed()));
                            int g = (int) (colorInicial.getGreen() + ratio * (colorFinal.getGreen() - colorInicial.getGreen()));
                            int b = (int) (colorInicial.getBlue() + ratio * (colorFinal.getBlue() - colorInicial.getBlue()));
                            boton.setBackground(new Color(r, g, b));
                            paso++;
                        } else {
                            timerEntrada[0].stop();
                        }
                    }
                });
                timerEntrada[0].start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (timerEntrada[0] != null && timerEntrada[0].isRunning()) {
                    timerEntrada[0].stop();
                }
                timerSalida[0] = new Timer(delayMs, null);
                timerSalida[0].addActionListener(new ActionListener() {
                    int paso = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (paso <= pasos) {
                            float ratio = (float) paso / pasos;
                            int r = (int) (colorFinal.getRed() + ratio * (colorInicial.getRed() - colorFinal.getRed()));
                            int g = (int) (colorFinal.getGreen() + ratio * (colorInicial.getGreen() - colorFinal.getGreen()));
                            int b = (int) (colorFinal.getBlue() + ratio * (colorInicial.getBlue() - colorFinal.getBlue()));
                            boton.setBackground(new Color(r, g, b));
                            paso++;
                        } else {
                            timerSalida[0].stop();
                        }
                    }
                });
                timerSalida[0].start();
            }
        });
    }

    private void cerrarSesion() {
        vista.setVisible(false);
        inicioSesion login = new inicioSesion();
        new CInicioSesion(login);
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }

    private void abrirNuevaLista() {
        vista.setVisible(false);
        RegistroCandidato registro = new RegistroCandidato();
        controlador.GestorVistas.registrarTransicion(vista, registro);

        registro.setVisible(true);
        registro.setLocationRelativeTo(null);
    }

    private void abrirEditarLista() {
        vista.setVisible(false);
        ListaCandidato consultar = new ListaCandidato();
        controlador.GestorVistas.registrarTransicion(vista, consultar);
        consultar.setVisible(true);
        consultar.setLocationRelativeTo(null);
    }

    private void abrirRegistrarDocente() {
        vista.setVisible(false);
        Docente docente = new Docente();
        VistaDocente vistaDocente = new VistaDocente();
        controlador.GestorVistas.registrarTransicion(vista, vistaDocente);
        new Controladordocente(docente, vistaDocente); // ✅ Aquí se le asigna el controlador
        vistaDocente.setVisible(true);
        vistaDocente.setLocationRelativeTo(null);
    }

    private void abrirBancoProyecto() {
        vista.setVisible(false);
        MenuProyectos menuVista = new MenuProyectos();
        controlador.GestorVistas.registrarTransicion(vista, menuVista);
        new CCentroBanco(menuVista);
        menuVista.setVisible(true);
        menuVista.setLocationRelativeTo(null);

    }

    private void abrirCentroEstudiante() {
        vista.setVisible(false);
        RegistroCandidato vistaCandidato = new RegistroCandidato();
        controlador.GestorVistas.registrarTransicion(vista, vistaCandidato);
        new CandidatoControlador(vistaCandidato);
        vistaCandidato.setVisible(true);
        vistaCandidato.setLocationRelativeTo(null);
    }

    private void abrirCentroDocente() {
        vista.setVisible(false);
        Docente docente = new Docente();
        VistaDocente vistaDocente = new VistaDocente();
        controlador.GestorVistas.registrarTransicion(vista, vistaDocente);
        new Controladordocente(docente, vistaDocente);
        vistaDocente.setVisible(true);
        vistaDocente.setLocationRelativeTo(null);
    }

    private void abrirCentroBanco() {
        vista.setVisible(false);
        MenuProyectos menuVista = new MenuProyectos();
        controlador.GestorVistas.registrarTransicion(vista, menuVista);
        new CCentroBanco(menuVista);
        menuVista.setVisible(true);
        menuVista.setLocationRelativeTo(null);

    }
}
