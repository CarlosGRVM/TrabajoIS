package controlador;

import modelo.*;
import vista.*;
import java.awt.event.*;

public class CCentroBanco {

    private final MenuProyectos vista;

    public CCentroBanco(MenuProyectos vista) {
        this.vista = vista;
        agregarEventos();
    }

    private void agregarEventos() {
        // Panel EMPRESA (FormatoEmpresa)
        vista.panelEmpresa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.setVisible(false);
                FormatoEmpresa empVista = new FormatoEmpresa();
                Empresa modelo = new Empresa();
                CEmpresa controlador = new CEmpresa(modelo, empVista);
                controlador.mostrarEmpresas(empVista.jTable1);

                GestorVistas.registrarTransicion(vista, empVista); // ← registrar

                empVista.setVisible(true);
                empVista.setLocationRelativeTo(null);
            }
        });

// Panel PROYECTO (FormatoProyecto)
        vista.panelProyecto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.setVisible(false);
                FormatoProyecto vistaProyecto = new FormatoProyecto();
                Empresa[] empresas = new Empresa().obtenerTodo().toArray(new Empresa[0]);
                new CProyecto(vistaProyecto, empresas);

                GestorVistas.registrarTransicion(vista, vistaProyecto); // ← registrar

                vistaProyecto.setVisible(true);
                vistaProyecto.setLocationRelativeTo(null);
            }
        });

// Panel CONSULTAR EMPRESA (ConsultaEmpresa)
        vista.panelConsultarEmpresa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.setVisible(false);
                ConsultaEmpresa vistaConsulta = new ConsultaEmpresa();
                new CEmpresaConsulta(vistaConsulta);

                GestorVistas.registrarTransicion(vista, vistaConsulta); // ← registrar

                vistaConsulta.setVisible(true);
                vistaConsulta.setLocationRelativeTo(null);
            }
        });

// Panel CONSULTAR PROYECTO (ConsultarProyecto)
        vista.panelConsultarProyecto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.setVisible(false);
                ConsultarProyecto vistaProyecto = new ConsultarProyecto();
                new CConsultarProyecto(vistaProyecto);

                GestorVistas.registrarTransicion(vista, vistaProyecto); // ← registrar

                vistaProyecto.setVisible(true);
                vistaProyecto.setLocationRelativeTo(null);
            }
        });

        // Botón REGRESAR
        vista.lblRegresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controlador.GestorVistas.regresar(vista); // ← método universal para volver
            }
        });
    }
}
