package controlador;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class GestorVistas {
    private static final Map<JFrame, JFrame> mapaVistas = new HashMap<>();
    /**
     * Registra que la vista "actual" fue abierta por "anterior"
     */
    public static void registrarTransicion(JFrame anterior, JFrame actual) {
        if (anterior != null && actual != null) {
            mapaVistas.put(actual, anterior);
        }
    }
    /**
     * Regresa a la vista anterior desde la actual, si está registrada
     */
    public static void regresar(JFrame actual) {
        if (actual == null) {
            return;
        }

        JFrame anterior = mapaVistas.remove(actual);  // Elimina del mapa una vez usada
        if (anterior != null) {
            actual.dispose(); // Cierra la vista actual
            anterior.setVisible(true); // Muestra la vista anterior
        } else {
            JOptionPane.showMessageDialog(null, "Error al regresar a la vista");
        }
    }
}