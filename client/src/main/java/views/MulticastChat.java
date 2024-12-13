package views;

import components.ImageLabel;
import components.Gif;
import components.PlaceholderTextField;
import network.Client;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.UserModel;
import utils.AppColors;

public class MulticastChat extends javax.swing.JFrame {
  private static MulticastChat instance;
  private UserModel userModel;
  private final String PATH_IMG_LABEL = "/logo.png";
  Gif loveGif = new Gif("/love.gif");
  Gif pensarGif = new Gif("/pensar.gif");
  Gif siGif = new Gif("/si.gif");
  Gif noGif = new Gif("/no.gif");
  Gif risaGif = new Gif("/risa.gif");
  
  
  
  private Map<String, JPanel> chats = new HashMap<>();
  private boolean isMulticast = true;
  private String selectedUser = null;
  
  
  private MulticastChat() {
    initComponents();
    setLocationRelativeTo(null);
    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS)); // Panel de grupo
    paneFriends.setLayout(new BoxLayout(paneFriends, BoxLayout.Y_AXIS));
    setUsersOnline();
    EMOJIS.setVisible(false);
    
    love.setIcon(loveGif.getIcon());
    pensar.setIcon(pensarGif.getIcon());
    si.setIcon(siGif.getIcon());
    no.setIcon(noGif.getIcon());
    risa.setIcon(risaGif.getIcon());

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
  private void setUsersOnline() {
    Client client = Client.getInstanceClient();
    List<String> usersOnline = client.getUsersOnline();
    
    if(usersOnline!=null) {
      for(String username: usersOnline) {
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
  
  public void removeUser(String username) {
    for(int i = 0; i < paneFriends.getComponentCount(); i++) {
      JComponent component = (JComponent) paneFriends.getComponent(i);
      if(component instanceof JButton) {
        JButton userBtn = (JButton) component;
        if(userBtn.getText().equals(username)) {
          paneFriends.remove(i);
          break;
        }
      }
    }
    
    paneFriends.revalidate();
    paneFriends.repaint();
  }
  
  /**
   * Método se llama para agregar un nuevo usuario en la lista de usuarios conectados de la interfaz
   * @param username Nombre del usuario en línea.
   */
  public void addUser(String username) {
    JButton userBtn = new JButton(username);
    
    // Se establece la fuente, alineamiento, tamaño y cursor.
    userBtn.setFont(new Font("Lucida Sans", 0, 16));
    userBtn.setOpaque(true);
    userBtn.setAlignmentX(JComponent.CENTER_ALIGNMENT);
    userBtn.setPreferredSize(new Dimension(paneFriends.getWidth(), 25));
    userBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
    // Estableciendo el borde del botón
    userBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    
    // Se agrega la acción
    userBtn.addActionListener(e -> openChat(username));
    
    paneFriends.add(Box.createVerticalStrut(5));
    paneFriends.add(userBtn);
    
    paneFriends.revalidate();
    paneFriends.repaint();
  }
  
  private void openChat(String username) {
    this.isMulticast = username.equals("ChatGrupal");
    selectedUser = username;
    
    if(chats.containsKey(username)) {
      
      JPanel chatPanel = chats.get(username);
      ScrollPane.setViewportView(chatPanel);
      this.pane = chatPanel;
    }
    
    else {
      JPanel chatPanel = new JPanel();
      chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
      
      // Almacenar el chat
      chats.put(username, chatPanel);
      
      // Mostrar el nuevo chat
      ScrollPane.setViewportView(chatPanel);
      this.pane = chatPanel;
    }
    
    ScrollPane.revalidate();
    ScrollPane.repaint();
  }
  
  /**
   * Este método se llama para limpiar la lista de usuarios en línea y 
   * posteriormente actualizarla
   */
  public void clearUsersList() {
    paneFriends.removeAll();
    paneFriends.revalidate();
    paneFriends.repaint();
  }
  
  /**
   * Este método se implementa para poder agregar un mensaje en la vista.
   * Se agrega el mensaje del lado derecho cuando el mensaje pertenece al usuario
   * y se agrega del lado izquierdo cuando el mensaje es un mensaje externo.
   * @param username Nombre de usuario que envió el mensaje
   * @param message Mensaje del usuario
   * @param alignRight Alinear dependiendo el tipo de mensaje
   */
  public void addMessage(String username, String message, boolean alignRight) {
    JLabel usernameLabel = new JLabel(username);
    usernameLabel.setFont(new Font("Lucida Bright", Font.BOLD, 16));
    usernameLabel.setForeground(AppColors.getSECONDARY_COLOR());
    usernameLabel.setBackground(AppColors.getWHITE_COLOR());
    
    // Crear etiqueta para el mensaje
    JLabel messageLabel = new JLabel(message);
    messageLabel.setFont(new Font("Lucida Brigth", Font.PLAIN, 16));
    messageLabel.setForeground(AppColors.getWHITE_COLOR());
    messageLabel.setOpaque(true);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    // Crear contenedor para el mensaje
    JPanel messagePanel = new JPanel();
    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
    
    if (alignRight) {
        messageLabel.setBackground(AppColors.getBLACK_COLOR());
    } else {
        messageLabel.setBackground(AppColors.getGRAY_COLOR());
    }
    
    messagePanel.setOpaque(true);
    messagePanel.setBackground(Color.LIGHT_GRAY);
    
    // Agregar usuario y mensaje.
    messagePanel.add(usernameLabel);
    messagePanel.add(Box.createVerticalStrut(5));
    messagePanel.add(messageLabel);

    
    pane.add(messagePanel);
    pane.add(Box.createVerticalStrut(10));
    pane.revalidate();
    pane.repaint();
  }
  
  /* @param gifLabel gif del usuario*/
  public void addGif(String username, JLabel gifLabel, boolean alignRight) {
    JLabel usernameLabel = new JLabel(username);
    usernameLabel.setFont(new Font("Lucida Bright", Font.BOLD, 16));
    usernameLabel.setForeground(AppColors.getSECONDARY_COLOR());
    usernameLabel.setBackground(AppColors.getWHITE_COLOR());
    
    // Crear un contenedor
    JPanel messagePanel = new JPanel();
    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
    messagePanel.setOpaque(true);
    messagePanel.setBackground(Color.LIGHT_GRAY);

    // Agregar usuario
    messagePanel.add(usernameLabel);
    messagePanel.add(Box.createVerticalStrut(5));
    messagePanel.add(gifLabel);

    pane.add(messagePanel);
    pane.add(Box.createVerticalStrut(10));
    pane.revalidate();
    pane.repaint();
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
        EMOJIS = new javax.swing.JPanel();
        love = new javax.swing.JLabel();
        no = new javax.swing.JLabel();
        risa = new javax.swing.JLabel();
        pensar = new javax.swing.JLabel();
        si = new javax.swing.JLabel();
        PanelLateral = new javax.swing.JPanel();
        LabelUsers = new javax.swing.JLabel();
        labelImg = new ImageLabel(PATH_IMG_LABEL);
        BtnLogout = new javax.swing.JButton();
        userInfoComponent = new components.UserInfoComponent();
        scrollPaneFriends = new javax.swing.JScrollPane();
        paneFriends = new javax.swing.JPanel();
        btnGroupChat = new javax.swing.JButton();
        ContainerMessage = new javax.swing.JPanel();
        textFieldMessage = new PlaceholderTextField("Escribe un mensaje...");
        emojiComponent = new components.EmojiComponent();
        welcomePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ScrollPane = new javax.swing.JScrollPane();
        pane = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PanelChat.setBackground(new java.awt.Color(236, 236, 236));
        PanelChat.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        EMOJIS.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        love.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        love.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loveMouseClicked(evt);
            }
        });

        no.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        no.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                noMouseClicked(evt);
            }
        });

        risa.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        risa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                risaMouseClicked(evt);
            }
        });

        pensar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pensar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pensarMouseClicked(evt);
            }
        });

        si.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        si.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                siMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout EMOJISLayout = new javax.swing.GroupLayout(EMOJIS);
        EMOJIS.setLayout(EMOJISLayout);
        EMOJISLayout.setHorizontalGroup(
            EMOJISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EMOJISLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EMOJISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pensar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(love, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(EMOJISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(no, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(si, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(risa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        EMOJISLayout.setVerticalGroup(
            EMOJISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EMOJISLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EMOJISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EMOJISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(no, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                        .addComponent(love, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(risa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(EMOJISLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pensar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(si, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        PanelChat.add(EMOJIS, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 520, 160, 120));

        PanelLateral.setBackground(AppColors.getPRIMARY_COLOR());
        PanelLateral.setBackground(new java.awt.Color(204, 0, 51));
        PanelLateral.setForeground(new java.awt.Color(255, 255, 255));
        PanelLateral.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LabelUsers.setFont(new java.awt.Font("Goudy Old Style", 1, 18)); // NOI18N
        LabelUsers.setForeground(new java.awt.Color(255, 255, 255));
        LabelUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LabelUsers.setText("En línea");
        PanelLateral.add(LabelUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 70, -1));

        labelImg.setPreferredSize(new java.awt.Dimension(250, 250));
        PanelLateral.add(labelImg, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 180, 60));

        BtnLogout.setBackground(new java.awt.Color(204, 0, 0));
        BtnLogout.setFont(new java.awt.Font("Goudy Old Style", 0, 24)); // NOI18N
        BtnLogout.setForeground(new java.awt.Color(255, 255, 255));
        BtnLogout.setText("Cerrar Sesión");
        BtnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnLogoutActionPerformed(evt);
            }
        });
        PanelLateral.add(BtnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 650, 160, -1));

        userInfoComponent.setBackground(new java.awt.Color(0, 0, 0));
        PanelLateral.add(userInfoComponent, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 570, 200, 130));

        javax.swing.GroupLayout paneFriendsLayout = new javax.swing.GroupLayout(paneFriends);
        paneFriends.setLayout(paneFriendsLayout);
        paneFriendsLayout.setHorizontalGroup(
            paneFriendsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 177, Short.MAX_VALUE)
        );
        paneFriendsLayout.setVerticalGroup(
            paneFriendsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 247, Short.MAX_VALUE)
        );

        scrollPaneFriends.setViewportView(paneFriends);

        PanelLateral.add(scrollPaneFriends, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 180, 250));

        btnGroupChat.setBackground(new java.awt.Color(51, 51, 51));
        btnGroupChat.setFont(new java.awt.Font("Goudy Old Style", 1, 18)); // NOI18N
        btnGroupChat.setForeground(new java.awt.Color(255, 255, 255));
        btnGroupChat.setText("FainaifChat");
        btnGroupChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupChatActionPerformed(evt);
            }
        });
        PanelLateral.add(btnGroupChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 180, 30));

        PanelChat.add(PanelLateral, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 700));

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
        ContainerMessage.add(textFieldMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 630, 40));

        emojiComponent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emojiComponentMouseClicked(evt);
            }
        });
        ContainerMessage.add(emojiComponent, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        PanelChat.add(ContainerMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 630, 770, 60));

        welcomePanel.setBackground(new java.awt.Color(255, 102, 102));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 3, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Esta navidad lo importante es estar conectados.. ¡Feliz NaviChat!");
        jLabel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                jLabel1ComponentMoved(evt);
            }
        });

        javax.swing.GroupLayout welcomePanelLayout = new javax.swing.GroupLayout(welcomePanel);
        welcomePanel.setLayout(welcomePanelLayout);
        welcomePanelLayout.setHorizontalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(15, 15, 15))
        );

        PanelChat.add(welcomePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 780, 50));

        pane.setLayout(new javax.swing.BoxLayout(pane, javax.swing.BoxLayout.X_AXIS));
        ScrollPane.setViewportView(pane);

        PanelChat.add(ScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 760, 560));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(52, 52, 52))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(40, 40, 40))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void textFieldMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldMessageActionPerformed
    if(!textFieldMessage.equals("Escribe un mensaje...") && !textFieldMessage.equals("")) {
      Client client = Client.getInstanceClient();
      String message = textFieldMessage.getText();
      if(this.isMulticast) {
        client.sendMessage(message);
      }
      
      else {
        client.sendMessagePrivate(message, selectedUser);
      }
      
      addMessage(userModel.getUsername(), message, true);
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

    private void jLabel1ComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jLabel1ComponentMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1ComponentMoved

    private void emojiComponentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emojiComponentMouseClicked
        EMOJIS.setVisible(!EMOJIS.isVisible());
    }//GEN-LAST:event_emojiComponentMouseClicked

    private void loveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loveMouseClicked
        JLabel gifCopy = new JLabel(love.getIcon());
        addGif(userModel.getUsername(), gifCopy, true);
    }//GEN-LAST:event_loveMouseClicked

    private void noMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_noMouseClicked
        JLabel gifCopy = new JLabel(no.getIcon());
        addGif(userModel.getUsername(), gifCopy, true);
    }//GEN-LAST:event_noMouseClicked

    private void risaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_risaMouseClicked
        JLabel gifCopy = new JLabel(risa.getIcon());
        addGif(userModel.getUsername(), gifCopy, true);
    }//GEN-LAST:event_risaMouseClicked

    private void pensarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pensarMouseClicked
        JLabel gifCopy = new JLabel(pensar.getIcon());
        addGif(userModel.getUsername(), gifCopy, true);
    }//GEN-LAST:event_pensarMouseClicked

    private void siMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_siMouseClicked
        JLabel gifCopy = new JLabel(si.getIcon());
        addGif(userModel.getUsername(), gifCopy, true);
    }//GEN-LAST:event_siMouseClicked


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
    private javax.swing.JPanel EMOJIS;
    private javax.swing.JLabel LabelUsers;
    private javax.swing.JPanel PanelChat;
    private javax.swing.JPanel PanelLateral;
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JButton btnGroupChat;
    private components.EmojiComponent emojiComponent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelImg;
    private javax.swing.JLabel love;
    private javax.swing.JLabel no;
    private javax.swing.JPanel pane;
    private javax.swing.JPanel paneFriends;
    private javax.swing.JLabel pensar;
    private javax.swing.JLabel risa;
    private javax.swing.JScrollPane scrollPaneFriends;
    private javax.swing.JLabel si;
    private javax.swing.JTextField textFieldMessage;
    private components.UserInfoComponent userInfoComponent;
    private javax.swing.JPanel welcomePanel;
    // End of variables declaration//GEN-END:variables
}
