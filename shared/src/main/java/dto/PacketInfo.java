package dto;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PacketInfo {
    private static final int HEADER_SIZE = 25;
    private int numPacket;
    private String fileID;
    private byte[] data;
    private byte controlByte;

    private PacketInfo(int numPacket, String fileID, byte[] data, byte controlByte) {
      this.numPacket = numPacket;
      this.fileID = fileID;
      this.data = data;
      this.controlByte = controlByte;
    }
    
    public static PacketInfo create (byte[] packetData) {
      byte controlByte = packetData[0];
      int numPacketBytes = ByteBuffer.wrap(packetData, 1, 4).getInt();
      String fileID = new String(packetData, 5, 16, StandardCharsets.UTF_8);
      byte[] data = Arrays.copyOfRange(packetData, HEADER_SIZE, packetData.length);
      return new PacketInfo(numPacketBytes, fileID, data, controlByte);
    }
    
    public int getPacketNumber() {
      return this.numPacket;
    }
    
    public String getFileID() {
      return this.fileID;
    }
    
    public byte[] getDataFile() {
      return this.data;
    }
    
    public byte getControlByte () {
      return this.controlByte;
    }
    
    public int getSequenceNumber () {
      return this.controlByte & 0x0F;
    }
    
    @Override
    public String toString () {
      StringBuilder sb = new StringBuilder();
      sb.append("PacketInfo {");
      sb.append("Número de Secuencia: ").append(getSequenceNumber()).append(", ");
      sb.append("Número de Paquete: ").append(numPacket).append(", ");
      sb.append("Identificador de Archivo: ").append(fileID).append(", ");
      sb.append("Data: ").append(data.length);
      sb.append("}");
      return sb.toString();
    }
}

