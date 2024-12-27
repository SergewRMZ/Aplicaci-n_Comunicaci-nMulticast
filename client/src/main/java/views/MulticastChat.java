package views;

import components.BtnFriend;
import components.FileComponent;
import components.ImageLabel;
import components.PlaceholderTextField;
import network.Client;
import java.awt.Font;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.UserModel;
import utils.AppColors;
import utils.FontAwesomeIcons;

public class MulticastChat extends javax.swing.JFrame {
  private static MulticastChat instance;
  private UserModel userModel;
  private final String PATH_IMG_LABEL = "/logo.png";
  
  private Map<String, JPanel> chats = new HashMap<>();
  private String selectedUser = null;
  
  
  private MulticastChat() {
    initComponents();
    setLocationRelativeTo(null);
    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS)); // Panel de grupo
    paneFriends.setLayout(new BoxLayout(paneFriends, BoxLayout.Y_AXIS));
    openChat("ChatGrupal");
  }
  
  /**
   * Patrón singleton para obtener una única instancia de MulticastChat
   * @return MulticastChat
   */
  public static MulticastChat getInstance() {
    if(instance == null) {
      instance = new MulticastChat();
    }
    
    return instance;
  }
  
  /**
   * Método para establecer los usuarios en linea. Realiza una petición get al servidor
   * el cual devuelve la lista de usuarios activos.
   */
  public void setUsersOnline() {
    Client client = Client.getInstanceClient();
    List<String> usersOnline = client.getUsersOnline();
    
    if(usersOnline!=null) {
      for(String username: usersOnline) {
        // Condición para evitar que coloque al usuario en la lista.
        if (!username.equals(userModel.getUsername()))
          addUser(username);
      }
    }
  }
  
  /**
   * Método público para establecer los parámetros clave del usuario, como 
   * su nombre e identificador.
   * @param userModel 
   */
  public void setUserModel (UserModel userModel) {
    this.userModel = userModel;
    this.userInfoComponent.setUsernameLabel(userModel.getUsername());
  }
  
  /**
   * Método para remover a un usuario de la interfaz.
   * @param username 
   */
  public void removeUser(String username) {
    int index = getIndexOfBtnFriend(username);
    if(index != -1) {
      paneFriends.remove(index);
      paneFriends.revalidate();
      paneFriends.repaint();
    }
  }
  
  private int getIndexOfBtnFriend(String username) {
    for(int i = 0; i < paneFriends.getComponentCount(); i++) {
      JComponent component = (JComponent) paneFriends.getComponent(i);
      if(component instanceof BtnFriend userBtn) {
        if(userBtn.getBtnUsername().getText().equals(username)) {
          return i;
        }
      }
    }
    
    return -1;
  }
  
  private BtnFriend createBtnUserConnected(String username) {
    BtnFriend btnUserConnected = new BtnFriend();
    btnUserConnected.setUsername(username);
    return btnUserConnected;
  }
 
  public void addUser(String username) {
    BtnFriend btnUserConnected = createBtnUserConnected(username);
    btnUserConnected.getBtnUsername().addActionListener(e -> openChat(username));
    paneFriends.add(Box.createVerticalStrut(5));
    paneFriends.add(btnUserConnected);
    paneFriends.revalidate();
    paneFriends.repaint();
  }
  
  private void openChat(String username) {
    selectedUser = username;
    JPanel chatPanel;
    if(chats.containsKey(username)) {
      chatPanel = chats.get(username);
      this.labelDestLabel.setText(username);
    }
    
    else {
      chatPanel = new JPanel();
      chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
      chats.put(username, chatPanel);
    }
    
    ScrollPane.setViewportView(chatPanel);
    this.pane = chatPanel;
    
    ScrollPane.revalidate();
    ScrollPane.repaint();
  }
  
  private JPanel createMessage(String username, String message, boolean alignRight) {
    JLabel usernameLabel = new JLabel(username);
    usernameLabel.setFont(new Font("Lucida Sans", Font.PLAIN, 16));
    usernameLabel.setForeground(AppColors.getGRAY_COLOR());
    usernameLabel.setBackground(AppColors.getWHITE_COLOR());
    
    JLabel messageLabel = new JLabel(message);
    messageLabel.setFont(new Font("Lucida Sans", Font.PLAIN, 16));
    messageLabel.setForeground(AppColors.getWHITE_COLOR());
    messageLabel.setOpaque(true);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    JPanel messagePanel = new JPanel();
    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
    
    if (alignRight) {
      messageLabel.setBackground(AppColors.getPRIMARY_COLOR());
    } else {
      messageLabel.setBackground(AppColors.getBLUE_LIGHT_COLOR());
    }
    
    messagePanel.setOpaque(true);
    messagePanel.setBackground(AppColors.getGRAY_LIGHT_COLOR());
    
    // Agregar usuario y mensaje.
    messagePanel.add(usernameLabel);
    messagePanel.add(Box.createVerticalStrut(5));
    messagePanel.add(messageLabel);
    return messagePanel;
  }
  
  public void addFileMessage(String username, String recipient, String path, String filename, String filesize, boolean isSender) {
    FileComponent fileComponent = new FileComponent(filename, filesize, username, path);
    String paneToSearch;
    
    if(isSender) paneToSearch = username;
    else paneToSearch = recipient;
    int index = getIndexOfBtnFriend(paneToSearch);
    JPanel chatPanel = chats.computeIfAbsent(paneToSearch, k -> {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      return panel;
    });
    
    if(recipient.equals(selectedUser) || username.equals(selectedUser)) {
      pane.add(fileComponent);
      pane.add(Box.createVerticalStrut(10));
      pane.revalidate();
      pane.repaint();
    }
    
    else { 
      chatPanel.add(fileComponent);
      chatPanel.add(Box.createVerticalStrut(10));
      chatPanel.revalidate();
      chatPanel.repaint();
      increaseMessageNotification(index);
    }
  }
  
  /**
   * Este método inserta un mensaje en la interfaz gráfica.
   * @param username Nombre de usuario que envió el mensaje.
   * @param recipient Nombre del usuario destino.
   * @param message Mensaje del usuario.
   * @param alignRight Alinear dependiendo el tipo de mensaje.
   * @param isPrivate True si el mensaje a insertar en la interfaz es un mensaje privado.
   */
  public void addMessage(String username, String recipient, String message, boolean alignRight, boolean isPrivate) {
    JPanel messagePanel = createMessage(username, message, alignRight);
    String paneToSearch;
    int index;
    
    // Si es un mensaje privado, se inserta en el panel del que envió el mensaje.
    if(isPrivate) {
      paneToSearch = username;
      index = getIndexOfBtnFriend(username);
    }
    
    else {
      paneToSearch = recipient; 
      index = getIndexOfBtnFriend(recipient);
    }
    
    JPanel chatPanel = chats.computeIfAbsent(paneToSearch, k -> {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      return panel;
    });
    
    // Si el destinatario es el del panel actual, se agrega el mensaje en el panel actual
    if(recipient.equals(selectedUser) || username.equals(selectedUser)) {
      pane.add(messagePanel);
      pane.add(Box.createVerticalStrut(10));
      pane.revalidate();
      pane.repaint();
    }
    
    else {
      chatPanel.add(messagePanel);
      chatPanel.add(Box.createVerticalStrut(10));
      increaseMessageNotification(index);
    }
  }
  
  private void increaseMessageNotification(int index) {
    BtnFriend component = (BtnFriend) paneFriends.getComponent(index);
    component.incrementCounter();
    paneFriends.revalidate();
    paneFriends.repaint();
  }
  
  private void resetMessageNotification(int index) {
    BtnFriend component = (BtnFriend) paneFriends.getComponent(index);
    component.resetCounter();
    paneFriends.revalidate();
    paneFriends.repaint();
  }
  
  
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    PanelChat = new javax.swing.JPanel();
    PanelLateral = new javax.swing.JPanel();
    LabelUsers = new javax.swing.JLabel();
    labelImg = new ImageLabel(PATH_IMG_LABEL);
    BtnLogout = new javax.swing.JButton();
    scrollPaneFriends = new javax.swing.JScrollPane();
    paneFriends = new javax.swing.JPanel();
    btnGroupChat = new javax.swing.JButton();
    userInfoComponent = new components.UserInfoComponent();
    ContainerMessage = new javax.swing.JPanel();
    textFieldMessage = new PlaceholderTextField("Escribe un mensaje...");
    fileBtn = new javax.swing.JButton();
    emojiBtn = new javax.swing.JButton();
    sendBtn = new javax.swing.JButton();
    welcomePanel = new javax.swing.JPanel();
    labelDestLabel = new javax.swing.JLabel();
    ScrollPane = new javax.swing.JScrollPane();
    pane = new javax.swing.JPanel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    PanelChat.setBackground(new java.awt.Color(236, 236, 236));
    PanelChat.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    PanelLateral.setBackground(AppColors.getPRIMARY_COLOR());
    PanelLateral.setBackground(new java.awt.Color(204, 0, 51));
    PanelLateral.setForeground(new java.awt.Color(255, 255, 255));
    PanelLateral.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    LabelUsers.setFont(new java.awt.Font("Lucida Sans", 1, 18)); // NOI18N
    LabelUsers.setForeground(new java.awt.Color(255, 255, 255));
    LabelUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    LabelUsers.setText("En línea");
    PanelLateral.add(LabelUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 100, -1));

    labelImg.setPreferredSize(new java.awt.Dimension(250, 250));
    PanelLateral.add(labelImg, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 180, 60));

    BtnLogout.setBackground(new java.awt.Color(204, 0, 0));
    BtnLogout.setFont(new java.awt.Font("JetBrains Mono", 0, 14)); // NOI18N
    BtnLogout.setForeground(new java.awt.Color(255, 255, 255));
    BtnLogout.setText("Cerrar Sesión");
    BtnLogout.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        BtnLogoutActionPerformed(evt);
      }
    });
    PanelLateral.add(BtnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 710, 160, -1));

    scrollPaneFriends.setMaximumSize(new java.awt.Dimension(180, 250));
    scrollPaneFriends.setPreferredSize(new java.awt.Dimension(180, 250));

    paneFriends.setMaximumSize(new java.awt.Dimension(180, 250));
    paneFriends.setPreferredSize(new java.awt.Dimension(180, 250));

    javax.swing.GroupLayout paneFriendsLayout = new javax.swing.GroupLayout(paneFriends);
    paneFriends.setLayout(paneFriendsLayout);
    paneFriendsLayout.setHorizontalGroup(
      paneFriendsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 184, Short.MAX_VALUE)
    );
    paneFriendsLayout.setVerticalGroup(
      paneFriendsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 250, Short.MAX_VALUE)
    );

    scrollPaneFriends.setViewportView(paneFriends);

    PanelLateral.add(scrollPaneFriends, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 200, 250));

    btnGroupChat.setBackground(AppColors.getSECONDARY_COLOR());
    btnGroupChat.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
    btnGroupChat.setForeground(new java.awt.Color(255, 255, 255));
    btnGroupChat.setText("Chat Grupal");
    btnGroupChat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGroupChatActionPerformed(evt);
      }
    });
    PanelLateral.add(btnGroupChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 180, 30));
    PanelLateral.add(userInfoComponent, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 630, 200, 110));

    PanelChat.add(PanelLateral, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 740));

    ContainerMessage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    textFieldMessage.setBackground(new java.awt.Color(190, 190, 190));
    textFieldMessage.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
    textFieldMessage.setForeground(new java.awt.Color(255, 255, 255));
    textFieldMessage.setBorder(null);
    textFieldMessage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        textFieldMessageActionPerformed(evt);
      }
    });
    ContainerMessage.add(textFieldMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 650, 40));

    fileBtn.setIcon(FontAwesomeIcons.fileIcon(25));
    fileBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileBtnActionPerformed(evt);
      }
    });
    ContainerMessage.add(fileBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 40, 40));

    emojiBtn.setIcon(FontAwesomeIcons.emojiIcon(25));
    emojiBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        emojiBtnActionPerformed(evt);
      }
    });
    ContainerMessage.add(emojiBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 40));

    sendBtn.setIcon(FontAwesomeIcons.sendIcon(25));
    sendBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sendBtnActionPerformed(evt);
      }
    });
    ContainerMessage.add(sendBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 10, 40, 40));

    PanelChat.add(ContainerMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 630, 840, 60));

    welcomePanel.setBackground(AppColors.getBLUE_COLOR());

    labelDestLabel.setFont(new java.awt.Font("Lucida Sans", 1, 18)); // NOI18N
    labelDestLabel.setForeground(new java.awt.Color(255, 255, 255));
    labelDestLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    labelDestLabel.setText("ChatGrupal");

    javax.swing.GroupLayout welcomePanelLayout = new javax.swing.GroupLayout(welcomePanel);
    welcomePanel.setLayout(welcomePanelLayout);
    welcomePanelLayout.setHorizontalGroup(
      welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(welcomePanelLayout.createSequentialGroup()
        .addGap(16, 16, 16)
        .addComponent(labelDestLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(634, Short.MAX_VALUE))
    );
    welcomePanelLayout.setVerticalGroup(
      welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(welcomePanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(labelDestLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        .addContainerGap())
    );

    PanelChat.add(welcomePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 850, 50));

    pane.setLayout(new javax.swing.BoxLayout(pane, javax.swing.BoxLayout.X_AXIS));
    ScrollPane.setViewportView(pane);

    PanelChat.add(ScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 840, 560));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(PanelChat, javax.swing.GroupLayout.DEFAULT_SIZE, 1062, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(PanelChat, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void textFieldMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldMessageActionPerformed
    if(!textFieldMessage.equals("Escribe un mensaje...") && !textFieldMessage.equals("")) {
      Client client = Client.getInstanceClient();
      String message = textFieldMessage.getText();
      client.sendMessage(message, selectedUser);
      addMessage(userModel.getUsername(), selectedUser, message, true, false);
      textFieldMessage.setText("");
    }
  }//GEN-LAST:event_textFieldMessageActionPerformed

  private void BtnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLogoutActionPerformed
    Client.getInstanceClient().disconnect();
    this.setVisible(false);
    Login.getInstanceLogin().setVisible(true);
  }//GEN-LAST:event_BtnLogoutActionPerformed

  private void btnGroupChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupChatActionPerformed
    openChat("ChatGrupal");
  }//GEN-LAST:event_btnGroupChatActionPerformed

  private void fileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBtnActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Selecciona un archivo para enviar");
    
    int result = fileChooser.showOpenDialog(this);
    if(result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      long fileSizeBytes = selectedFile.length(); // Obtiene el tamaño en bytes
      double fileSizeMB = fileSizeBytes / (1024.0 * 1024.0); // Convierte a MB
        
      String formattedSize = String.format("%.2f MB", fileSizeMB);
      JOptionPane.showMessageDialog(this, "Archivo seleccionado " + selectedFile.getAbsolutePath());
      addFileMessage(userModel.getUsername(), selectedUser, selectedFile.getPath(), selectedFile.getName(), formattedSize, true);

      Client.getInstanceClient().sendFile(selectedFile, selectedUser);
      JOptionPane.showMessageDialog(this, "Archivo enviado correctamente a " + selectedUser);
    }

    else {
      JOptionPane.showMessageDialog(this, "No se seleccionó ningún archivo");
    }
  }//GEN-LAST:event_fileBtnActionPerformed

  private void emojiBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emojiBtnActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_emojiBtnActionPerformed

  private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendBtnActionPerformed
    
  }//GEN-LAST:event_sendBtnActionPerformed

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
      java.util.logging.Logger.getLogger(MulticastChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(MulticastChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(MulticastChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(MulticastChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new MulticastChat().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton BtnLogout;
  private javax.swing.JPanel ContainerMessage;
  private javax.swing.JLabel LabelUsers;
  private javax.swing.JPanel PanelChat;
  private javax.swing.JPanel PanelLateral;
  private javax.swing.JScrollPane ScrollPane;
  private javax.swing.JButton btnGroupChat;
  private javax.swing.JButton emojiBtn;
  private javax.swing.JButton fileBtn;
  private javax.swing.JLabel labelDestLabel;
  private javax.swing.JLabel labelImg;
  private javax.swing.JPanel pane;
  private javax.swing.JPanel paneFriends;
  private javax.swing.JScrollPane scrollPaneFriends;
  private javax.swing.JButton sendBtn;
  private javax.swing.JTextField textFieldMessage;
  private components.UserInfoComponent userInfoComponent;
  private javax.swing.JPanel welcomePanel;
  // End of variables declaration//GEN-END:variables
}
