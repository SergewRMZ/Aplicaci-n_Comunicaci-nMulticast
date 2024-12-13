package threads;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import model.UserModel;
import network.UsersManager;
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
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        
        if(json.has("action")) {
          String action = json.get("action").getAsString();
          
          switch (action) {
            case "user-connected" -> handleNotifyUserConnected(json);
            case "user-disconnected" -> handleNotifyUserDisconnected(json);
            case "message" -> handleMessageMulticast(json);
            default -> throw new AssertionError();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void closeSocket() {
    if (socket != null && !socket.isClosed()) {
      try {
        InetAddress group = InetAddress.getByName(multicastAddress);
        socket.leaveGroup(group);
        socket.close();
        System.out.println("Socket multicast cerrado correctamente");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  private void handleNotifyUserConnected(JsonObject json) {
    UsersManager usersManager = UsersManager.getInstance(); 
    
    JsonObject user = json.getAsJsonObject("user");
    String username = user.get("username").getAsString();
    int port = user.get("port").getAsInt();
    String ipAddress = user.get("ipAddress").getAsString();
    
    // Agregar a lista de usuarios conectados.
    UserModel userModel = new UserModel(username, port, ipAddress);
    usersManager.addUserOnline(username, userModel);
    
    // Agregar usuario a la interfaz
    MulticastChat.getInstance().addUser(username);
  }
  
  private void handleNotifyUserDisconnected(JsonObject json) {
    UsersManager usersManager = UsersManager.getInstance(); 
    
    JsonObject user = json.getAsJsonObject("user");
    String username = user.get("username").getAsString();
    usersManager.removeUser(username);
    
    // Agregar usuario a la interfaz
    MulticastChat.getInstance().removeUser(username);
  }
  
  private void handleMessageMulticast(JsonObject json) {
    String username = json.get("username").getAsString();
    String message = json.get("message").getAsString(); // Obtener el mensaje
    MulticastChat.getInstance().addMessage(username, message, false);
  }
}
