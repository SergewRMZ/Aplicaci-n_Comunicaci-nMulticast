package views;
import components.ImageLabel;
import network.Client;
import components.PlaceholderPasswordField;
import components.PlaceholderTextField;
import dto.ResponseDto;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import model.UserModel;
import utils.AppColors;
public class Login extends javax.swing.JFrame {
  private static Login instance;
  private final String PATH_IMG_LABEL = "/IconUser.png";
  
  private Login() {
    initComponents();
    setLocationRelativeTo(null);
    setMouseListenerForLink();
  }
  
  public static Login getInstanceLogin () {
    if(instance == null) {
      instance = new Login();
    }
    
    return instance;
  }
  
  
  private void setMouseListenerForLink() {
    LinkRegister.setForeground(AppColors.GRAY_COLOR);
    LinkRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    LinkRegister.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        LinkRegister.setForeground(AppColors.PRIMARY_COLOR);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        LinkRegister.setForeground(AppColors.GRAY_COLOR); 
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

        jPanel1 = new javax.swing.JPanel();
        LoginBtn = new javax.swing.JButton();
        LoginBtn.setBackground(AppColors.PRIMARY_COLOR);
        LinkRegister = new javax.swing.JLabel();
        InputPassword = new PlaceholderPasswordField("Contraseña");
        jLabel1 = new javax.swing.JLabel();
        inputUsername = new PlaceholderTextField("Usuario");
 ;
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        LoginLabel = new javax.swing.JLabel();
        labelImg = new ImageLabel(PATH_IMG_LABEL);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 0, 0));
        setForeground(new java.awt.Color(153, 0, 0));
        setMinimumSize(new java.awt.Dimension(500, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(null);

        LoginBtn.setFont(new java.awt.Font("Goudy Old Style", 3, 24)); // NOI18N
        LoginBtn.setForeground(new java.awt.Color(255, 255, 255));
        LoginBtn.setText("Aceptar");
        LoginBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LoginBtn.setPreferredSize(new java.awt.Dimension(55, 40));
        LoginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginBtnActionPerformed(evt);
            }
        });
        jPanel1.add(LoginBtn);
        LoginBtn.setBounds(130, 250, 250, 40);

        LinkRegister.setFont(new java.awt.Font("Goudy Old Style", 2, 18)); // NOI18N
        LinkRegister.setForeground(new java.awt.Color(51, 51, 51));
        LinkRegister.setText("¿No tienes una cuenta? Registrate aquí.");
        LinkRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.add(LinkRegister);
        LinkRegister.setBounds(130, 220, 258, 23);

        InputPassword.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
        InputPassword.setText("Contraseña");
        InputPassword.setMinimumSize(new java.awt.Dimension(15, 40));
        InputPassword.setPreferredSize(new java.awt.Dimension(55, 40));
        InputPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputPasswordActionPerformed(evt);
            }
        });
        jPanel1.add(InputPassword);
        InputPassword.setBounds(80, 150, 350, 40);

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 0, 0));
        jLabel1.setText("Contraseña:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(90, 120, 86, 17);

        inputUsername.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
        inputUsername.setText("Usuario");
        inputUsername.setPreferredSize(new java.awt.Dimension(55, 40));
        inputUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputUsernameActionPerformed(evt);
            }
        });
        jPanel1.add(inputUsername);
        inputUsername.setBounds(80, 60, 350, 40);

        jLabel3.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 0, 0));
        jLabel3.setText("Usuario:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(90, 30, 70, 17);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 510, 330));

        jPanel2.setBackground(new java.awt.Color(153, 0, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LoginLabel.setFont(new java.awt.Font("Goudy Old Style", 1, 36)); // NOI18N
        LoginLabel.setForeground(new java.awt.Color(255, 255, 255));
        LoginLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LoginLabel.setText("Iniciar Sesión");
        LoginLabel.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel2.add(LoginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 510, -1));

        labelImg.setPreferredSize(new java.awt.Dimension(250, 250));
        jPanel2.add(labelImg, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 240, 210));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 510, 600));

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
    
    if(username.isEmpty() && username.equals("Usuario")) {
      JOptionPane.showMessageDialog(this, "Por favor ingresa tu nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    if(password.isEmpty() && password.equals("Contraseña")) {
      JOptionPane.showMessageDialog(this, "Por favor ingresa tu contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    // Enviar datos al servidor e iniciar sesión
    Client client = Client.getInstanceClient();
    ResponseDto response = client.loginUser(username, password);
    
    if(response.getError()) {
        JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    else {
        JOptionPane.showMessageDialog(this, response.getMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
        MulticastChat main = MulticastChat.getInstance();
        UserModel userModel = (UserModel) response.getObject();

        setVisible(false);
        
        client.getUserGroup(); // Abrir socket multicast
        main.setUserModel(userModel);
        main.setUsersOnline();
        client.listenUnicast();
        main.setVisible(true);
    }
  }//GEN-LAST:event_LoginBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField InputPassword;
    private javax.swing.JLabel LinkRegister;
    private javax.swing.JButton LoginBtn;
    private javax.swing.JLabel LoginLabel;
    private javax.swing.JTextField inputUsername;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labelImg;
    // End of variables declaration//GEN-END:variables
}
