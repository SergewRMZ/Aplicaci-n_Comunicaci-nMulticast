package components;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;

public class Gif extends JLabel {
    private static final int WIDTH = 50; 
    private static final int HEIGHT = 50;
    private String id; // ID del GIF

    public Gif(String gifPath, String id) {
        this.id = id; // Establecer ID
        ImageIcon originalGif = new ImageIcon(getClass().getResource(gifPath));

        if (originalGif.getImageLoadStatus() == java.awt.MediaTracker.ERRORED) {
            System.err.println("Error: No se pudo cargar el GIF desde la ruta: " + gifPath);
            return;
        }
        
        Image scaledImage = originalGif.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
        ImageIcon scaledGif = new ImageIcon(scaledImage);
        setIcon(scaledGif);
        setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
    }

    // Método para obtener el ID
    public String getId() {
        return id;
    }
}

