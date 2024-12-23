package flowcontrol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SelectiveRejectManager {
  private final int WINDOW_SIZE = 8;
  private final int MAX_SEQUENCE_NUMBER = 16;
  private boolean [] packetReceived;
  private boolean [] missingPackets;
  private DatagramSocket socket;
  private int windowStart = 0;
  private int windowEnd = WINDOW_SIZE - 1;
  
  private InetAddress clientAddress;
  private int clientPort;
  
  public SelectiveRejectManager(DatagramSocket datagramSocket) {
    this.socket = datagramSocket;
    this.packetReceived = new boolean[MAX_SEQUENCE_NUMBER];
    this.missingPackets = new boolean[MAX_SEQUENCE_NUMBER];
  }
  
  public InetAddress getClientAddress() {
    return clientAddress;
  }
  
  public int getClientPort () {
    return clientPort;
  }
  
  public void setClientInfo (InetAddress inetAddress, int clientPort) {
    this.clientAddress = inetAddress;
    this.clientPort = clientPort;
  }
  
  public void processReceivedPacket (int sequenceNumber) {
    if (isWindowRange(sequenceNumber)) {
      if (!packetReceived[sequenceNumber]) {
        packetReceived[sequenceNumber] = true;
        
        // En caso de paquete perdido
        if (missingPackets[sequenceNumber]) 
          missingPackets[sequenceNumber] = false;
        
        sendACK(sequenceNumber);
          
        if (sequenceNumber == windowStart) {
          advanceWindow();
        }
        
        else {
          if (!missingPackets[windowStart]) {
            sendSREJ(windowStart);
            missingPackets[windowStart] = true;
          }
        }
      }
    }
  }
  
  private void advanceWindow() {
    while (packetReceived[windowStart]) {
      packetReceived[windowStart] = false;
      missingPackets[windowStart] = false;
      windowStart = (windowStart + 1) % MAX_SEQUENCE_NUMBER;
      windowEnd = (windowEnd + 1) % MAX_SEQUENCE_NUMBER;
    }
  }
  
  /**
   * Método para verificar que el número de secuencia recibido corresponda
   * al intervalo de la ventana
   * @param sequenceNumber
   * @return 
   */
  private boolean isWindowRange(int sequenceNumber) {
    if (windowStart <= windowEnd) {
        return (sequenceNumber >= windowStart && sequenceNumber <= windowEnd);
    } else {
        return (sequenceNumber >= windowStart || sequenceNumber <= windowEnd);
    }
  }

  private void sendACK (int sequenceNumber) {
    byte[] ackData = new byte[1];
    ackData[0] = (byte) sequenceNumber;
    DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, clientAddress, clientPort);
    try {
      socket.send(ackPacket);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  
  private void sendSREJ(int sequenceNumber) {
    byte[] ackData = new byte[1];
    ackData[0] = (byte) (sequenceNumber | 0x80);
    DatagramPacket srejPacket = new DatagramPacket(ackData, ackData.length, clientAddress, clientPort);
    
    try {
        socket.send(srejPacket);
        System.out.println("Enviando SREJ para el paquete: " + sequenceNumber);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}