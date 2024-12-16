/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package components;

import javax.swing.SwingConstants;
import utils.FontAwesomeIcons;

/**
 *
 * @author serge
 */
public class EmojiComponent extends javax.swing.JPanel {

  /**
   * Creates new form Component
   */
  public EmojiComponent() {
    initComponents();
    setIconEmoji();
  }
  
  private void setIconEmoji () {
    this.labelEmojiIcon.setIcon(FontAwesomeIcons.EmojiIcon(35));
    this.labelEmojiIcon.setHorizontalAlignment(SwingConstants.CENTER);
    this.labelEmojiIcon.setVerticalAlignment(SwingConstants.CENTER);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelEmojiIcon = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(labelEmojiIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelEmojiIcon;
    // End of variables declaration//GEN-END:variables
}
