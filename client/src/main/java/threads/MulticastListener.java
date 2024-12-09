package threads;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import views.MulticastChat;

public class MulticastListener implements Runnable {
  private final String multicastAddress;
  private final int port;
  private final int clientPort;
  private MulticastSocket socket;
  public MulticastListener(String multicastAddress, int port, int clientPort) {
    this.multicastAddress = multicastAddress;
    this.port = port;
    this.clientPort = clientPort;
    
    System.out.println("Multicast Address: " + multicastAddress);
    System.out.println("Puerto multicast: " + port);
    System.out.println("Puerto de cliente: " + clientPort);
  }
  
  public MulticastSocket getSocket() {
    return this.socket;
  }
  
  @Override
  public void run() {
    try (MulticastSocket socket = new MulticastSocket(port)){
      this.socket = socket;
      
      InetAddress group = InetAddress.getByName(multicastAddress);
      socket.joinGroup(group);
      System.out.println("Escuchando en el grupo multicast: " + multicastAddress);
      
      byte[] buffer = new byte[1024];
      while(true) {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        
        int senderPort = packet.getPort();
        
        // Verificar que el paquete no sea uno enviado por el mismo usuario.
        if(senderPort == this.clientPort) {
          continue;
        }
        
        System.out.println("Mensaje recibido: " + new String(packet.getData(), 0, packet.getLength()));
        String message = new String(packet.getData(), 0, packet.getLength());
        JsonObject messageJson = JsonParser.parseString(message).getAsJsonObject();
        
        if(messageJson.has("action") && messageJson.get("action").getAsString().equals("users-list")) {
          handleNotifyUserConnected(messageJson);
        }
        
        else if(messageJson.has("action") && messageJson.get("action").getAsString().equals("message")) {
          handleMessageMulticast(messageJson);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void handleNotifyUserConnected(JsonObject messageJson) {
    JsonArray users = messageJson.getAsJsonArray("users");
    System.out.println(users);
    for(JsonElement user : users) {
      JsonObject userJson = user.getAsJsonObject();
      String username = userJson.get("username").getAsString();
      int port = userJson.get("port").getAsInt();
      MulticastChat.getInstance().addUser(username);
    }
  }
  
  private void handleMessageMulticast(JsonObject messageJson) {
    String message = messageJson.get("message").getAsString(); // Obtener el mensaje
    MulticastChat.getInstance().addMessage(message, false);
  }
}
