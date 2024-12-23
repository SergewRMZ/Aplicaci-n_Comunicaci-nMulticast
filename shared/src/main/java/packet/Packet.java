package packet;

import com.google.gson.Gson;
import dto.Metadata;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.CRC32;
import dto.PacketInfo;
public class Packet {
  private final static int PACKET_SIZE = 1024;
  private final static int HEADER_SIZE = 25;
  private final static Gson gson = new Gson();
  public Packet () {}
  private static Metadata metadata;
  
  /**
   * Función para crear un paquete de datos.
   * @param isLastPacket: Indicar si el paquete a envíar es el último o no.
   * @param numPacket
   * @param sequenceNumber: Número de secuencia del paquete. Número máximo será 7.
   * @param data: Arreglo de bytes que contiene la información a envíar.
   * @return 
   */
  public static byte [] createPacketData(boolean isLastPacket, int numPacket, int sequenceNumber, byte[] data) {
    byte[] packetData = new byte[data.length + HEADER_SIZE];
    byte controlByte = createControlByte(true, !isLastPacket, false, sequenceNumber);
    byte[] fileID = metadata.getFileIdBytes();
    byte[] numPacketBytes = ByteBuffer.allocate(4).putInt(numPacket).array();
    packetData[0] = controlByte;
    
    // Agregar número de paquete (64 bits)
    System.arraycopy(numPacketBytes, 0, packetData, 1, numPacketBytes.length);
    
    // Agregar identificador de archivo (128 bits)
    System.arraycopy(fileID, 0, packetData, 5, fileID.length);
    
    // Agregar datos de archivo a la trama
    System.arraycopy(data, 0, packetData, 25, data.length);
    
    // Poner a 0x00 los bytes de CRC antes del cálculo
    for (int i = 21; i < 25; i++) {
        packetData[i] = 0x00;
    }
    
    byte[] crc = calculateCRC(packetData);
    // Agregar crc
    System.arraycopy(crc, 0, packetData, 21, crc.length);
    return packetData;
  }
  
  private static int calculatePackets(long fileSize) {
    return (int) Math.ceil((double) fileSize / (PACKET_SIZE - HEADER_SIZE));
  }
  
  public static Metadata createFileMetadata (String filename, long filesize, String fileparent) {
    int totalPackets = calculatePackets(filesize);
    metadata = new Metadata(filename, fileparent, generateFileID(filename), filesize, totalPackets);
    return metadata;
  }
  
  public static PacketInfo processData (DatagramPacket packet) {
    PacketInfo packetInfo = PacketInfo.create(packet.getData());
    return packetInfo;
  }
  
  /**
   * Método para calcular el checksum de cada paquete
   * @param packetData: Paquete de datos.
   * @return Arreglo de 4 bytes.
   */
  private static byte [] calculateCRC (byte [] packetData) {
    CRC32 crc = new CRC32();
    crc.update(packetData);
    int crcInt = (int) crc.getValue();
    return ByteBuffer.allocate(4).putInt(crcInt).array();
  }
  
  /**
   * Método para generar un identificador de tiempo
   * @param filename: Nombre del archivo
   * @return Hash de cuatro bytes.
   */
  public static String generateFileID(String filename) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] hash = md.digest(filename.getBytes());
      byte[] truncatedHash = Arrays.copyOf(hash, 8);
      BigInteger number = new BigInteger(1, truncatedHash);
      String hashText = number.toString(16);
      while (hashText.length() < 16) {
          hashText = "0" + hashText;
      }
      return hashText;
  } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error al calcular MD5", e);
  }
}

  /**
   * Método para crear el byte de control
   * @param dontFragment: Indica si es un paquete independiente
   * @param moreFragment: Indica si el k-ésimo paquete es el último o faltan más
   * @param sequenceNumber: Número de secuencia de cada paquete.
   * @return Retornar el byte de control.
   */
  private static byte createControlByte (boolean dontFragment, boolean moreFragment, boolean metadata, int sequenceNumber) {
    byte controlByte = 0;
    if (dontFragment) controlByte |= (1 << 7);
    if (moreFragment) controlByte |= (1 << 6);
    if (metadata) controlByte |= (1 << 5);
    controlByte |= (sequenceNumber & 0x0F);
    return controlByte;
  }
  
  private static boolean verifyCRC(byte[] packet, byte[] crcReceivedBytes) {
    byte[] originalCRC = new byte[4];
    System.arraycopy(packet, 21, originalCRC, 0, 4);

    for (int i = 21; i < 25; i++) {
        packet[i] = 0x00;
    }
    
    CRC32 crc = new CRC32();
    crc.update(packet);
    int crcInt = (int) crc.getValue();
    byte[] crcCalculatedBytes = ByteBuffer.allocate(4).putInt(crcInt).array();

    // Restaurar los valores originales de CRC en el paquete
    System.arraycopy(originalCRC, 0, packet, 21, 4);
    // Comparar los bytes de CRC calculado con los bytes de CRC recibido
    return java.util.Arrays.equals(crcCalculatedBytes, crcReceivedBytes);
  }
  
  public static int verifyPacket(byte[] packet) {
    byte[] crcReceivedBytes = new byte[4];
    System.arraycopy(packet, 21, crcReceivedBytes, 0, 4);
    
    if (verifyCRC(packet, crcReceivedBytes)) {
        byte controlByte = packet[0];
        return controlByte & 0x0F; // Regresar el número de secuencia
    }
    return -1; 
  }
}
