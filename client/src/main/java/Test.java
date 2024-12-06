import Client.Client;
import java.util.Scanner;
public class Test {
  public static void main (String args[]) {
    Client client = Client.getInstanceClient();
    client.loginUser("Sergew", "1234");
    client.getUserGroup();
    
    Scanner scanner = new Scanner(System.in);
    while (true) {      
      String message = scanner.nextLine();
      if(message.equalsIgnoreCase("exit")) {
        System.out.println("Saliendo...");
        break;
      }
      
      client.sendMessage(message);
    }
    
    scanner.close();
  }
}
