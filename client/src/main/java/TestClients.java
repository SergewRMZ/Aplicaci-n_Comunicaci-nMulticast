
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import controller.Client;
import java.util.UUID;
public class TestClients {
  public static void main(String args[]) {
    int numClients = 5;
    ExecutorService clientPool = Executors.newFixedThreadPool(numClients);
    for(int i = 0; i < numClients; i++) {
      clientPool.execute(() -> {
        simulateClient();
      });
    }
  }
  
  private static void simulateClient() {
    try {
      Client client = Client.getInstanceClient();
      String username = "User_" + UUID.randomUUID().toString().substring(0, 8);
      String password = "1234";
      
      client.registerUser(username, password);
      
      client.loginUser(username, password);
      client.joinChatRoom("Gamers");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


