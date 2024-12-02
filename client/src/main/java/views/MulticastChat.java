package views;

import utils.AppColors;

public class MulticastChat extends javax.swing.JFrame {
  private static MulticastChat instance;
  private MulticastChat() {
    initComponents();
    setLocationRelativeTo(null);
  }
  
  public static MulticastChat getInstance() {
    if(instance == null) {
      instance = new MulticastChat();
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

    PanelChat = new javax.swing.JPanel();
    PanelLateral = new javax.swing.JPanel();
    LabelDashboard = new javax.swing.JLabel();
    LabelGroups = new javax.swing.JLabel();
    LabelUsers = new javax.swing.JLabel();
    scrollGroups = new javax.swing.JScrollPane();
    panelGroups = new javax.swing.JPanel();
    BtnGroup = new javax.swing.JButton();
    BtnGroup1 = new javax.swing.JButton();
    BtnGroup2 = new javax.swing.JButton();
    userInfoComponent1 = new components.UserInfoComponent();
    ContainerMessage = new javax.swing.JPanel();
    textFieldMessage = new javax.swing.JTextField();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    PanelChat.setBackground(new java.awt.Color(51, 51, 51));
    PanelChat.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    PanelLateral.setBackground(AppColors.getPRIMARY_COLOR());
    PanelLateral.setBackground(new java.awt.Color(37, 37, 37));
    PanelLateral.setForeground(new java.awt.Color(255, 255, 255));
    PanelLateral.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    LabelDashboard.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
    LabelDashboard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    LabelDashboard.setText("Dashboard");
    PanelLateral.add(LabelDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 183, -1));

    LabelGroups.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    LabelGroups.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    LabelGroups.setText("Grupos");
    PanelLateral.add(LabelGroups, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 60, -1));

    LabelUsers.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    LabelUsers.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    LabelUsers.setText("Usuarios Conectados");
    PanelLateral.add(LabelUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 150, -1));

    panelGroups.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    BtnGroup.setText("Grupo2");
    BtnGroup.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        BtnGroupActionPerformed(evt);
      }
    });
    panelGroups.add(BtnGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 200, -1));

    BtnGroup1.setText("Grupo 1");
    BtnGroup1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        BtnGroup1ActionPerformed(evt);
      }
    });
    panelGroups.add(BtnGroup1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 21, 200, -1));

    BtnGroup2.setText("Grupo2");
    BtnGroup2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        BtnGroup2ActionPerformed(evt);
      }
    });
    panelGroups.add(BtnGroup2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 200, -1));

    scrollGroups.setViewportView(panelGroups);

    PanelLateral.add(scrollGroups, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 200, 250));
    PanelLateral.add(userInfoComponent1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 600, 200, -1));

    PanelChat.add(PanelLateral, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 700));

    ContainerMessage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    textFieldMessage.setBackground(new java.awt.Color(60, 63, 65));
    textFieldMessage.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
    textFieldMessage.setText("Enviar mensaje");
    textFieldMessage.setBorder(null);
    textFieldMessage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        textFieldMessageActionPerformed(evt);
      }
    });
    ContainerMessage.add(textFieldMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 630, 40));

    PanelChat.add(ContainerMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 630, 770, 60));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(PanelChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(PanelChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void BtnGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGroupActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_BtnGroupActionPerformed

  private void BtnGroup1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGroup1ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_BtnGroup1ActionPerformed

  private void BtnGroup2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGroup2ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_BtnGroup2ActionPerformed

  private void textFieldMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldMessageActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_textFieldMessageActionPerformed

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
  private javax.swing.JButton BtnGroup;
  private javax.swing.JButton BtnGroup1;
  private javax.swing.JButton BtnGroup2;
  private javax.swing.JPanel ContainerMessage;
  private javax.swing.JLabel LabelDashboard;
  private javax.swing.JLabel LabelGroups;
  private javax.swing.JLabel LabelUsers;
  private javax.swing.JPanel PanelChat;
  private javax.swing.JPanel PanelLateral;
  private javax.swing.JPanel panelGroups;
  private javax.swing.JScrollPane scrollGroups;
  private javax.swing.JTextField textFieldMessage;
  private components.UserInfoComponent userInfoComponent1;
  // End of variables declaration//GEN-END:variables
}
