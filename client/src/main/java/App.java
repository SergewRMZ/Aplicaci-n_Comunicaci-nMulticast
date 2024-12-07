import com.formdev.flatlaf.FlatLightLaf;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import views.Login;


public class App {
  public Scanner input = new Scanner(System.in);
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(new FlatLightLaf());
        UIManager.put("Panel.arc", 100);
        Login main = Login.getInstanceLogin();
        SwingUtilities.updateComponentTreeUI(main);
        main.setVisible(true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
