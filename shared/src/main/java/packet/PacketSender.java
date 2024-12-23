package packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketSender { 
  private final DatagramSocket socket;
  private final InetAddress destAddress;
  private final int destPort;
  
  public PacketSender (DatagramSocket socket, InetAddress destAddress, int destPort) {
    this.destPort = destPort;
    this.socket = socket;
    this.destAddress = destAddress;
  }
  
  public void sendPacket (byte [] data) {
    try { 
      System.out.println("Enviando paquete de datos");
      DatagramPacket packet = new DatagramPacket(data, data.length, this.destAddress, this.destPort);
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
