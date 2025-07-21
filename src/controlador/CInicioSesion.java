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

            // Paso 1: Validar si el usuario existe
            String verificarUsuario = "SELECT nombre FROM usuario WHERE correo = ?";
            PreparedStatement ps1 = conn.prepareStatement(verificarUsuario);
            ps1.setString(1, correo);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                JOptionPane.showMessageDialog(vista, "⚠ Usuario no registrado.");
                return;
            }

            // Paso 2: Verificar contraseña
            String sql = "SELECT nombre FROM usuario WHERE correo = ? AND contrasena = SHA2(?, 256)";
            PreparedStatement ps2 = conn.prepareStatement(sql);
            ps2.setString(1, correo);
            ps2.setString(2, contrasenaTexto);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                // ✅ Contraseña correcta
                String nombreUsuario = rs2.getString("nombre");
                JOptionPane.showMessageDialog(vista, "✅ Inicio de sesión exitoso");

                Inicio siguiente = new Inicio();
                new CInicio(siguiente, nombreUsuario);
                siguiente.setVisible(true);
                siguiente.setLocationRelativeTo(null);
                vista.dispose();

                intentosFallidos = 0;
                seDesbloqueo = false;
                return;
            } else {
                // ❌ Contraseña incorrecta
                intentosFallidos++;

                if (intentosFallidos == 3) {
                    JOptionPane.showMessageDialog(vista, "⚠ Cuenta bloqueada. Espera 10 segundos.");
                    vista.ingresar.setEnabled(false);

                    Timer timer = new Timer(10000, e -> {
                        vista.ingresar.setEnabled(true);
                        seDesbloqueo = true; // 🔥 ahora sí desbloqueado pero no reiniciamos el contador
                        JOptionPane.showMessageDialog(vista, "Ya puedes volver a intentar.");
                    });
                    timer.setRepeats(false);
                    timer.start();
                    return;
                }

                // Si ya fue desbloqueado y se vuelve a equivocar, lo mandamos a Recuperar
                if (seDesbloqueo && intentosFallidos > 3) {
                    JOptionPane.showMessageDialog(vista, "🔒 Demasiados intentos después del desbloqueo. Serás redirigido para recuperar tu contraseña.");
                    vista.dispose();
                    Recuperar recuperar = new Recuperar();
                    CRecuperar cRecuperar = new CRecuperar(recuperar); // si tienes controlador
                    cRecuperar.iniciar(); // si lo usas
                    recuperar.setVisible(true);
                    recuperar.setLocationRelativeTo(null);

                    // Reiniciar todo
                    intentosFallidos = 0;
                    seDesbloqueo = false;
                    return;
                }

                JOptionPane.showMessageDialog(vista, "❌ Contraseña incorrecta. Intento " + intentosFallidos + " de 3.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "❌ Error de conexión: " + e.getMessage());
        }

    }
    public static void reiniciarEstado() {
    intentosFallidos = 0;
    seDesbloqueo = false;
}


}