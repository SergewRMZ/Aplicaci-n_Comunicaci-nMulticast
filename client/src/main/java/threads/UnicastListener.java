package threads;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import views.MulticastChat;

public class UnicastListener implements Runnable {
  private MulticastSocket socket;
  private boolean running;
  
  public UnicastListener (MulticastSocket socket) {
    this.socket = socket;
    running = true;
  }
  
  @Override
  public void run() {
    System.out.println("Escuchando mensajes privados");
    byte[] buffer = new byte[1024];
    
    while(running) {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      try {
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        
        System.out.println("Mensaje recibido en unicast: " + message);
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        
        if(json.has("action")) {
          String action = json.get("action").getAsString();
          
          switch (action) {
            case "message" -> handleMessagePrivate(json);
            default -> throw new AssertionError();
          }
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }    
  }
  
  public void stopListening() {
    this.running = false;
  }
  
  private void handleMessagePrivate(JsonObject json) {
    String sender = json.get("sender").getAsString();
    String recipient = json.get("recipient").getAsString();
    String message = json.get("message").getAsString();
    MulticastChat.getInstance().addMessage(sender, recipient, message, false, true);
  }
}
