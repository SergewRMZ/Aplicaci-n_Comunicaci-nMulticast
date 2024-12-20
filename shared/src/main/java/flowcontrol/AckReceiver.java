package flowcontrol;

import file.FileSender;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import packet.PacketManager;

public class AckReceiver implements Runnable {
  private final DatagramSocket socket;
  private final PacketManager packetManager;
  private FileSender fileSender;
  private volatile boolean running = true;
  private final int MAX_SEQUENCE_NUMBER = 16;
  private boolean[] ackReceived;
  
  public AckReceiver(DatagramSocket socket, PacketManager packetManager) {
    this.socket = socket;
    this.packetManager = packetManager;
    ackReceived = new boolean[MAX_SEQUENCE_NUMBER];
  }
  
  public void setFileSender(FileSender fileSender) {
    this.fileSender = fileSender;
  }
  
  public void stopAckReceiver() {
    running = false;
  }
  
  @Override
  public void run() {
    byte[] ack = new byte[1];
    DatagramPacket ackPacket = new DatagramPacket(ack, ack.length);
    try {
      while(running) {
        socket.receive(ackPacket);
        int receiveAck = ack[0] & 0xFF;
        boolean isREJ = (receiveAck & 0x80) != 0;
        int ackNumber = receiveAck & 0x7F;

        synchronized (fileSender) {
          if (isREJ) {
            System.out.println("SREJ: " + ackNumber);
            packetManager.resendPacket(ackNumber);
          } else {
            System.out.println("ACK: " + ackNumber);
            ackReceived[ackNumber] = true;
            packetManager.removePacketReferenceByIndex(ackNumber);
            
            while (ackReceived[fileSender.getWindowStart()]) {
              ackReceived[fileSender.getWindowStart()] = false;
              int ws = (fileSender.getWindowStart() + 1) % MAX_SEQUENCE_NUMBER;
              fileSender.updateWindowStart(ws);
            }
            fileSender.notify();
          }
        }
      }
    }
    catch (IOException e) {
      if (running) {
        e.printStackTrace();
      }
    }
  }
}
