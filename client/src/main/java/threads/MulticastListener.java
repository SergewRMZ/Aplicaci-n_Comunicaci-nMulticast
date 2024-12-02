package threads;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastListener implements Runnable {
  private final String multicastAddress;
  private final int port;
  private MulticastSocket socket;
  public MulticastListener(String multicastAddress, int port) {
    this.multicastAddress = multicastAddress;
    this.port = port;
    
    System.out.println("Multicast Address: " + multicastAddress);
    System.out.println("Puerto: " + port);
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
        System.out.println("Mensaje recibido: " + new String(packet.getData(), 0, packet.getLength()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
