/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import javax.swing.JOptionPane;

public class Correo {
    // Configuración del correo emisor
    private static final String CORREO_EMISOR = "xavezz16@gmail.com"; // ¡IMPORTANTE! Tu correo
    private static final String CONTRASENA_EMISOR = "nxfy jfog cgue qxpg"; // ¡IMPORTANTE! Contraseña de aplicación si usas Gmail

    public boolean enviarCorreo(String correoDestino, String asunto, String mensaje) {
        Properties props = new Properties();
        // Configuración para Gmail (cambia si usas otro servidor SMTP)
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Requerido para versiones recientes de Java

        Session session = Session.getDefaultInstance(props, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(CORREO_EMISOR, CONTRASENA_EMISOR);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(CORREO_EMISOR));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDestino));
            message.setSubject(asunto);
            message.setText(mensaje);
            Transport.send(message);
            return true;
        } catch (MessagingException ex) {
            JOptionPane.showMessageDialog(null, "Error al enviar el correo: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}