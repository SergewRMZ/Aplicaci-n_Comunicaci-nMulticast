package file;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import packet.PacketManager;
import flowcontrol.AckReceiver;

public class FileSender {
  private final PacketManager packetManager;
  private DatagramSocket socket;
  private AckReceiver ackReceiver;
  private Thread ackThread;
  private final int MAX_SEQUENCE_NUMBER = 16;
  private final int WINDOW_SIZE = 8;
  private final int PACKET_SIZE = 1024;
  private final int HEADER_SIZE = 25;
  
  private int sequenceNumber;
  private int windowStart;
  private int windowEnd;

  public FileSender(PacketManager packetManager) {
    this.packetManager = packetManager;
    this.ackReceiver = new AckReceiver(socket, packetManager);
    ackReceiver.setFileSender(this);
  }
  
  public void sendFile (File file) throws IOException {
    try (FileInputStream flujo = new FileInputStream(file)) {
      packetManager.setFile(file);
      // packetManager.sendMetaData(file.getName(), file.length(), null);
      
      int bytesRead;
      byte[] buffer = new byte[PACKET_SIZE - HEADER_SIZE];
      long currentOffset = 0;
      this.startAckReceiver();
      
      while ((bytesRead = flujo.read(buffer)) != -1) {
        synchronized (this) {
          while ((windowEnd - windowStart + MAX_SEQUENCE_NUMBER) % MAX_SEQUENCE_NUMBER >= WINDOW_SIZE) {
            try {
              wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }  
          
          boolean isLastPacket = flujo.available() == 0;
          packetManager.putPacketReference(currentOffset, bytesRead, sequenceNumber);
          packetManager.sendPacketData(buffer, sequenceNumber, isLastPacket);
          currentOffset += bytesRead;
          sequenceNumber = (sequenceNumber + 1) % this.MAX_SEQUENCE_NUMBER;
          windowEnd = (windowEnd + 1) % this.MAX_SEQUENCE_NUMBER;
        }
      }
      
      stopAckReceiver();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public int getWindowStart() {
    return this.windowStart;
  }
  
  public synchronized void updateWindowStart(int windowStart) {
    this.windowStart = windowStart;
    System.out.println("windowStart: " + windowStart + ", windowEnd: " + windowEnd + ", sequence: " + sequenceNumber);
  }
  
  private void startAckReceiver() {
    ackThread = new Thread(ackReceiver);
    ackThread.start();
  }
  
  private void stopAckReceiver() {
    ackReceiver.stopAckReceiver();
    if (ackThread != null) {
      ackThread.interrupt();
    }
  }
}
