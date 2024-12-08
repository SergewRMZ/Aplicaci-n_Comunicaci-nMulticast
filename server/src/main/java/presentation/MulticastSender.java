package presentation;

import com.google.gson.JsonObject;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender {
  private MulticastSocket socket;
  
  public MulticastSender (MulticastSocket socket) {
    this.socket = socket;
  }
  
  public void sendMessage(JsonObject notification, InetAddress multicastAddress, int multicastPort) {
    try {
      byte[] buffer = notification.toString().getBytes();
      DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, multicastAddress, multicastPort);
      socket.send(datagramPacket);
      System.out.println("Notificación enviada a los clientes");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
