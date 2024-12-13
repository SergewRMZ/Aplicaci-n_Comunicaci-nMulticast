package threads;

import java.net.MulticastSocket;

public class UnicastListener implements Runnable {
  private MulticastSocket socket;
  
  public UnicastListener (MulticastSocket socket) {
    this.socket = socket;
  }
  
  
  
  @Override
  public void run() {
    
  }
  
}
