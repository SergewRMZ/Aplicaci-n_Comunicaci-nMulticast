package packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketSender { 
  private final DatagramSocket socket;
  private final InetAddress serverAddress;
  private final int port;
  
  public PacketSender (DatagramSocket socket, InetAddress serverAddress, int port) {
    this.port = port;
    this.socket = socket;
    this.serverAddress = serverAddress;
  }
  
  public void sendPacket (byte [] data) {
    try { 
      DatagramPacket packet = new DatagramPacket(data, data.length, this.serverAddress, this.port);
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
