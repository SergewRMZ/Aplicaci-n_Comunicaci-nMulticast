/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;

/**
 *
 * @author ville
 */
public class Gif extends JLabel {
    private static final int WIDTH = 40; 
    private static final int HEIGHT = 40;
    public Gif(String gifPath) {
        // Intentar cargar el recurso
        ImageIcon originalGif = new ImageIcon(getClass().getResource(gifPath));

        if (originalGif.getImageLoadStatus() == java.awt.MediaTracker.ERRORED) {
            System.err.println("Error: No se pudo cargar el GIF desde la ruta: " + gifPath);
            return;
        }

        // Redimensionar el GIF al tamaño deseado
        Image scaledImage = originalGif.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
        ImageIcon scaledGif = new ImageIcon(scaledImage);

        // Configurar el GIF redimensionado en el JLabel
        setIcon(scaledGif);
        setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
    }
}
