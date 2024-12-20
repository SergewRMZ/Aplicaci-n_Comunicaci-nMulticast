package packet;

import file.FileAssembler;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dto.Metadata;
import dto.PacketInfo;
import dto.PacketReferences;
import java.io.File;
import java.io.RandomAccessFile;

public class PacketManager {
  private PacketSender packetSender;
  private FileAssembler fileAssembler;
  private File file;
  private final int MAX_NUMBER_SEQUENCE = 16;
  private int packetCounter = 0;
  List<PacketReferences> listPackets;
  
  public PacketManager(FileAssembler fileAssembler) {
    this.fileAssembler = fileAssembler;
    listPackets = new ArrayList<>(Collections.nCopies(MAX_NUMBER_SEQUENCE, null));  
  }
  
  public void setPacketSender(PacketSender packetSender) {
    this.packetSender = packetSender;
  }
  
  public void setFile (File file) {
    this.file = file;
  }
  
  private void showPacket (byte [] data) {
    for (int i = 0; i < data.length; i++) {
      System.out.printf("%02x ", data[i]);
      if ((i + 1) % 16 == 0) {
        System.out.println();
      }
    }
    
    System.out.println("Cantidad de datos: " + data.length);
  }
  
  public void sendMetaData (String filename, long filesize, String fileparent) {
    byte[] metadataPacket = Packet.createMetadata(filename, filesize, fileparent);
    packetSender.sendPacket(metadataPacket);
  }
  
  public void sendPacketData (byte[] data, int sequenceNumber, boolean isLastPacket) {
    byte[] packet = Packet.createPacketData(isLastPacket, packetCounter, sequenceNumber, data);
    System.out.println("Enviando paquete: " + sequenceNumber);
    this.packetSender.sendPacket(packet);
    packetCounter++;
  }
  
  public void resendPacket(int packetNumberLoss) {
    PacketReferences pr = getPacketByIndex(packetNumberLoss);
    System.out.println("Paquete perdido: " + pr.startOffset + " a " + pr.endOffset + " con n = " + packetNumberLoss);
    System.out.println("Número de paquete perdido: " + pr.packetNumber);
    try (RandomAccessFile f = new RandomAccessFile(file, "r")){
      int packetSize = (int) (pr.endOffset - pr.startOffset);
      byte[] bytesRead = new byte[packetSize];
      f.seek(pr.startOffset);
      f.readFully(bytesRead);
      
      boolean isLastPacket = (pr.endOffset == file.length());
      sendPacketData(bytesRead, packetNumberLoss, isLastPacket);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void processMetadataPacket (DatagramPacket packet) {
    Metadata metadata = Packet.processMetadata(packet);
    if (metadata != null) {
      try {
        this.fileAssembler.initializeFile(metadata);
      } catch (IOException e) {
        System.err.println("Error al inicializar archivo: " + e.getMessage());
        e.printStackTrace();
      }
    }
    
    else {
      System.err.println("No se pudo procesar el paquete de metadatos");
    }
  }
  
  public void processPacketData (DatagramPacket packet) {
    PacketInfo packetInfo = Packet.processData(packet);
    System.out.println(packetInfo.toString());
    if (packetInfo != null) {
      try {
        this.fileAssembler.addPacket(packetInfo);
      } catch (Exception e) {
        System.err.println("Error al procesar paquete de datos");
        e.printStackTrace();
      }
    }
  }
  
  public void putPacketReference(long currentOffset, int bytesRead, int numberSequence) {
    if (numberSequence >= 0 && numberSequence < listPackets.size()) {
        PacketReferences pr = new PacketReferences(currentOffset, currentOffset + bytesRead, numberSequence, packetCounter);
        listPackets.set(numberSequence, pr);
    } else {
        System.out.println("Número de secuencia fuera de rango: " + numberSequence);
    }
  }
  
  public void removePacketReferenceByIndex (int index) {
    listPackets.set(index, null);
  }
  
  public long getPacketCounter () {
    return this.packetCounter;
  }
  
  public PacketReferences getPacketByIndex (int index) {
    System.out.println("Indice a obtener: " + index);
    return listPackets.get(index);
  }
  
  public void showPacketReferences() {
    System.out.println("Lista de referencias");
    
    for(int i = 0; i < listPackets.size(); i++) {
      PacketReferences packetReferences = listPackets.get(i);
      if (packetReferences != null) {
        System.out.println("Indice: " + i + " - " + packetReferences.toString());
      }
      
      else {
        System.out.println("Indice: " + i + " - " + "null");
      }
    }
  }
  
  public void sendFileList(byte [] data) {
    packetSender.sendPacket(data);
  }
}
