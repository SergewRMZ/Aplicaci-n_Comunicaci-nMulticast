package presentation;

import com.google.gson.JsonObject;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender {
  private static MulticastSender instance;
  private MulticastSocket socket;
  
  public MulticastSender () {}
  
  public static MulticastSender getInstance() {
    if(instance == null) {
      instance = new MulticastSender();
    }
    
    return instance;
  }

  public void setSocket(MulticastSocket socket) {
    this.socket = socket;
  }
  
  public void sendMessage(JsonObject json, InetAddress multicastAddress, int multicastPort) {
    try {
      byte[] buffer = json.toString().getBytes();
      DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, multicastAddress, multicastPort);
      socket.send(datagramPacket);
      System.out.println("Notificación enviada a los clientes");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
