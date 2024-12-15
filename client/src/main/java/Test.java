import network.Client;
import java.util.Scanner;
public class Test {
  /**
   * Una vez que se conecta un cliente, debemos de obtener el grupo y correr un hilo 
   * en escucha de mensajes o de notificaciones por parte del servidor
   * @param args 
   */
  public static void main (String args[]) {
    Client client = Client.getInstanceClient();
    client.loginUser("Axel", "1234");
    client.getUserGroup();
    
    Scanner scanner = new Scanner(System.in);
    while (true) {      
      String message = scanner.nextLine();
      if(message.equalsIgnoreCase("exit")) {
        System.out.println("Saliendo...");
        client.disconnect();
        break;
      }
      
      client.sendMessage(message, "ChatGrupal");
    }
    System.out.println("Programa terminado");
    scanner.close();
  }
}
