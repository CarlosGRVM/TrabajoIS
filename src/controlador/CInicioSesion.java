package controlador;

import vista.*;
import modelo.ConexionSQL;

import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class CInicioSesion {

    private final inicioSesion vista;
    private static int intentosFallidos = 0;
    private static boolean seDesbloqueo = false;

    public CInicioSesion(inicioSesion vista) {
        this.vista = vista;
        configurarEventos();
    }

    private void configurarEventos() {
        vista.ingresar.addActionListener(e -> validarUsuario());
        vista.salir.addActionListener(e -> vista.dispose());

        vista.mostrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                vista.contrasena.setEchoChar((char) 0);
                vista.mostrar.setVisible(false);
                vista.ocultar.setVisible(true);
            }
        });
        vista.ocultar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                vista.contrasena.setEchoChar('•');
                vista.mostrar.setVisible(true);
                vista.ocultar.setVisible(false);
            }
        });

        vista.mostrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        vista.ocultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        vista.olvide_contrsena.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        vista.olvide_contrsena.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista.dispose();
                Recuperar recuperar = new Recuperar();
                CRecuperar cRecuperar = new CRecuperar(recuperar);
                cRecuperar.iniciar();

                recuperar.setVisible(true);
                recuperar.setLocationRelativeTo(null);
            }
        });

    }

    private void validarUsuario() {
        if (intentosFallidos >= 3 && !seDesbloqueo) {
            JOptionPane.showMessageDialog(vista, "Cuenta bloqueada. Espere 10 segundos.");
            return;
        }

        String correo = vista.usuario.getText().trim();
        String contrasenaTexto = new String(vista.contrasena.getPassword()).trim();

        if (correo.isEmpty() || contrasenaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor completa todos los campos.");
            return;
        }

        try (Connection conn = ConexionSQL.conectar()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(vista, "❌ No se pudo establecer la conexión.");
                return;
            }

            String sql = "SELECT nombre FROM usuario WHERE correo = ? AND contrasena = SHA2(?, 256)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, contrasenaTexto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nombreUsuario = rs.getString("nombre");
                JOptionPane.showMessageDialog(vista, "✅ Inicio de sesión exitoso");
                Inicio siguiente = new Inicio();
                new CInicio(siguiente, nombreUsuario);
                siguiente.setVisible(true);
                siguiente.setLocationRelativeTo(null);
                vista.dispose();
                intentosFallidos = 0;
                seDesbloqueo = false;
            } else {
                intentosFallidos++;
                JOptionPane.showMessageDialog(vista, "❌ Correo o contraseña incorrectos. Intento " + intentosFallidos + " de 3.");

                if (intentosFallidos == 3) {
                    vista.ingresar.setEnabled(false);
                    JOptionPane.showMessageDialog(vista, "Cuenta bloqueada. Espera 10 segundos.");

                    Timer timer = new Timer(10000, e -> {
                        vista.ingresar.setEnabled(true);
                        seDesbloqueo = true;
                        JOptionPane.showMessageDialog(vista, "Ya puedes volver a intentar.");
                    });
                    timer.setRepeats(false);
                    timer.start();
                }

                if (seDesbloqueo && intentosFallidos > 3) {
                    vista.dispose();
                    Recuperar recuperar = new Recuperar();
                    recuperar.setVisible(true);
                    recuperar.setLocationRelativeTo(null);
                    intentosFallidos = 0;
                    seDesbloqueo = false;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "❌ Error de conexión: " + e.getMessage());
        }
    }
}
