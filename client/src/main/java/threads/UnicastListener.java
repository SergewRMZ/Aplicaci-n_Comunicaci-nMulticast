package threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }    
  }
  
  public void stopListening() {
    this.running = false;
  }
}
