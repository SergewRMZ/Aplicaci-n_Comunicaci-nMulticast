package views;
import Client.Client;
import components.PlaceholderPasswordField;
import components.PlaceholderTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import utils.FontAwesomeIcons;
import javax.swing.SwingConstants;
import utils.AppColors;
public class Login extends javax.swing.JFrame {
  private static Login instance;
  
  private Login() {
    initComponents();
    setLocationRelativeTo(null);
    setIconUser();
    setMouseListenerForLink();
  }
  
  public static Login getInstanceLogin () {
    if(instance == null) {
      instance = new Login();
    }
    
    return instance;
  }
  
  private void setIconUser () {
    this.IconLabelUser.setIcon(FontAwesomeIcons.userIcon(100));
    this.IconLabelUser.setHorizontalAlignment(SwingConstants.CENTER);
    this.IconLabelUser.setVerticalAlignment(SwingConstants.CENTER);
  }
  
  private void setMouseListenerForLink() {
    LinkRegister.setForeground(AppColors.WHITE_COLOR);
    LinkRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    LinkRegister.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        LinkRegister.setForeground(AppColors.PRIMARY_COLOR);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        LinkRegister.setForeground(AppColors.WHITE_COLOR); 
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        Register.getInstancerRegister().setVisible(true);
        setVisible(false);
      }
    });
  }
  
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    IconLabelUser = new javax.swing.JLabel();
    LoginLabel = new javax.swing.JLabel();
    inputUsername = new PlaceholderTextField("Usuario");
 ;
    InputPassword = new PlaceholderPasswordField("Contraseña");
    LoginBtn = new javax.swing.JButton();
    LoginBtn.setBackground(AppColors.PRIMARY_COLOR);
    LinkRegister = new javax.swing.JLabel();
    LabelAppName = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new java.awt.Dimension(500, 600));
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    getContentPane().add(IconLabelUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 150, 150));

    LoginLabel.setFont(new java.awt.Font("Lucida Sans", 1, 36)); // NOI18N
    LoginLabel.setForeground(new java.awt.Color(48, 162, 251));
    LoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    LoginLabel.setText("Iniciar Sesión");
    LoginLabel.setPreferredSize(new java.awt.Dimension(50, 50));
    getContentPane().add(LoginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 219, 350, -1));

    inputUsername.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
    inputUsername.setText("Usuario");
    inputUsername.setPreferredSize(new java.awt.Dimension(55, 40));
    inputUsername.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        inputUsernameActionPerformed(evt);
      }
    });
    getContentPane().add(inputUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 287, 350, -1));

    InputPassword.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
    InputPassword.setText("Contraseña");
    InputPassword.setMinimumSize(new java.awt.Dimension(15, 40));
    InputPassword.setPreferredSize(new java.awt.Dimension(55, 40));
    InputPassword.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        InputPasswordActionPerformed(evt);
      }
    });
    getContentPane().add(InputPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 345, 350, -1));

    LoginBtn.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
    LoginBtn.setForeground(new java.awt.Color(255, 255, 255));
    LoginBtn.setText("Aceptar");
    LoginBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    LoginBtn.setPreferredSize(new java.awt.Dimension(55, 40));
    LoginBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        LoginBtnActionPerformed(evt);
      }
    });
    getContentPane().add(LoginBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 503, 250, -1));

    LinkRegister.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
    LinkRegister.setForeground(new java.awt.Color(204, 204, 204));
    LinkRegister.setText("¿No tienes una cuenta? Registrate aquí.");
    LinkRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    getContentPane().add(LinkRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 474, -1, -1));

    LabelAppName.setFont(new java.awt.Font("Lucida Console", 0, 24)); // NOI18N
    LabelAppName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    LabelAppName.setText("Redcast");
    LabelAppName.setBackground(AppColors.SECONDARY_COLOR);
    LabelAppName.setForeground(AppColors.BLACK_COLOR);
    getContentPane().add(LabelAppName, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 50));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void inputUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputUsernameActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_inputUsernameActionPerformed

  private void InputPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputPasswordActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_InputPasswordActionPerformed

  private void LoginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginBtnActionPerformed
    String username = inputUsername.getText();
    String password = InputPassword.getText();
    
    if(username.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor ingresa tu nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    if(password.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Por favor ingresa tu contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Enviar datos al servidor
    Client client = Client.getInstanceClient();
    String response = client.loginUser(username, password);
    JOptionPane.showMessageDialog(this, response, "Success", JOptionPane.INFORMATION_MESSAGE);
    setVisible(false);
    MulticastChat.getInstance().setVisible(true);
  }//GEN-LAST:event_LoginBtnActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel IconLabelUser;
  private javax.swing.JPasswordField InputPassword;
  private javax.swing.JLabel LabelAppName;
  private javax.swing.JLabel LinkRegister;
  private javax.swing.JButton LoginBtn;
  private javax.swing.JLabel LoginLabel;
  private javax.swing.JTextField inputUsername;
  // End of variables declaration//GEN-END:variables
}
