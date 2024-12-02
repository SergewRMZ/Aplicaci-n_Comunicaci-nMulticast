package utils;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageLoader {
  public static Icon setScaledImage (String path, int width, int height) {
    ImageIcon imageIcon = new ImageIcon(ImageLoader.class.getResource(path));
    Image image = imageIcon.getImage();
    
    if (image == null) {
      System.err.println("No se pudo cargar la imagen.");
    }
    
    return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_FAST));
  }
}
