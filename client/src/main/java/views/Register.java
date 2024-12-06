package views;
import Client.Client;
import components.ImageLabel;
import components.PlaceholderPasswordField;
import components.PlaceholderTextField;
import javax.swing.JOptionPane;
import utils.AppColors;
import utils.FontAwesomeIcons;
public class Register extends javax.swing.JFrame {
  private static Register instance;
  private final String PATH_IMG_LABEL = "/PersonasAnimadas.jpg";
  private Register() {
    initComponents();
    setLocationRelativeTo(null); // Centrar vista
  }
  
  public static Register getInstancerRegister() {
    if(instance == null) {
      instance = new Register();
    }
    
    return instance;
  }
  
  

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    RegisterPanel = new javax.swing.JPanel();
    inputUsername = new PlaceholderTextField("Usuario");
 ;
    InputPassword = new PlaceholderPasswordField("Contraseña");
    RegisterLabel = new javax.swing.JLabel();
    labelImg = new ImageLabel(PATH_IMG_LABEL);
    RegisterBtn = new javax.swing.JButton();
    RegisterBtn.setBackground(AppColors.PRIMARY_COLOR);
    BtnBack = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    RegisterPanel.setBackground(new java.awt.Color(255, 255, 255));
    RegisterPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    inputUsername.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
    inputUsername.setText("Usuario");
    inputUsername.setPreferredSize(new java.awt.Dimension(55, 40));
    inputUsername.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        inputUsernameActionPerformed(evt);
      }
    });
    RegisterPanel.add(inputUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 350, -1));

    InputPassword.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
    InputPassword.setText("Contraseña");
    InputPassword.setMinimumSize(new java.awt.Dimension(15, 40));
    InputPassword.setPreferredSize(new java.awt.Dimension(55, 40));
    InputPassword.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        InputPasswordActionPerformed(evt);
      }
    });
    RegisterPanel.add(InputPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 350, -1));

    RegisterLabel.setFont(new java.awt.Font("SansSerif", 1, 32)); // NOI18N
    RegisterLabel.setForeground(new java.awt.Color(48, 162, 251));
    RegisterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    RegisterLabel.setText("Registrarse");
    RegisterLabel.setPreferredSize(new java.awt.Dimension(50, 50));
    RegisterPanel.add(RegisterLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 280, 350, -1));

    labelImg.setPreferredSize(new java.awt.Dimension(250, 250));
    RegisterPanel.add(labelImg, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 250, 250));

    RegisterBtn.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
    RegisterBtn.setForeground(new java.awt.Color(255, 255, 255));
    RegisterBtn.setText("Aceptar");
    RegisterBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    RegisterBtn.setPreferredSize(new java.awt.Dimension(55, 40));
    RegisterBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        RegisterBtnActionPerformed(evt);
      }
    });
    RegisterPanel.add(RegisterBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 500, 250, -1));

    BtnBack.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        BtnBackActionPerformed(evt);
      }
    });
    BtnBack.setIcon(FontAwesomeIcons.backIcon(20));
    RegisterPanel.add(BtnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 30));

    getContentPane().add(RegisterPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 600));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void inputUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputUsernameActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_inputUsernameActionPerformed

  private void InputPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputPasswordActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_InputPasswordActionPerformed

  private void RegisterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegisterBtnActionPerformed
    String username = inputUsername.getText();
    String password = InputPassword.getText();

    if(username.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor ingresa tu nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    if(password.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor ingresa una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    Client client = Client.getInstanceClient();
    String response = client.registerUser(username, password);
    JOptionPane.showMessageDialog(this, response, "Success", JOptionPane.INFORMATION_MESSAGE);
    
    // Limpiar campos y dirigir al Login.
    inputUsername.setText("");
    InputPassword.setText("");
    Login.getInstanceLogin().setVisible(true);
    setVisible(false);
  }//GEN-LAST:event_RegisterBtnActionPerformed

  private void BtnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBackActionPerformed
    setVisible(false);
    Login.getInstanceLogin().setVisible(true);
  }//GEN-LAST:event_BtnBackActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Register().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton BtnBack;
  private javax.swing.JPasswordField InputPassword;
  private javax.swing.JButton RegisterBtn;
  private javax.swing.JLabel RegisterLabel;
  private javax.swing.JPanel RegisterPanel;
  private javax.swing.JTextField inputUsername;
  private javax.swing.JLabel labelImg;
  // End of variables declaration//GEN-END:variables
}
