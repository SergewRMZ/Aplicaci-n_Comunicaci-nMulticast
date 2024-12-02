package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.Border;

public class RoundedBorder implements Border {
  private int radius;
  private Color borderColor;
  private int thickness;

  public RoundedBorder(int radius, Color borderColor, int thickness) {
    this.radius = radius;
    this.borderColor = borderColor;
    this.thickness = thickness;
  }
  
  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    if (g instanceof Graphics2D) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setColor(borderColor);
      g2d.setStroke(new BasicStroke(thickness));
      g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
  }
  
  @Override
  public Insets getBorderInsets(Component c) {
    return new Insets(thickness, thickness, thickness, thickness);
  }

  @Override
  public boolean isBorderOpaque() {
    return false;
  }
  
}
