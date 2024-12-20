package file;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import dto.FileAssemblyInfo;
import dto.Metadata;
import dto.PacketInfo;

public class FileAssembler {
  private HashMap<String, FileAssemblyInfo> fileInfoMap = new HashMap<>();
  private String baseDirectory;
  public FileAssembler () {}
  
  public FileAssembler (String baseDirectory) {
    this.baseDirectory = baseDirectory;
    File baseDir = new File(baseDirectory);
    if (!baseDir.exists()) {
      baseDir.mkdirs();
    }
  }
  /**
   * Inicializa el nuevo archivo con base en los metadatos recibidos del cliente
   * @param metadata Recibe un objeto de tipo Metadata que contiene la información de
   * los metadatos del archivo que se está enviando.
   * @throws IOException
   */
  public void initializeFile (Metadata metadata) throws IOException {
    File outputFile;
    
    if (metadata.getFileParent() == null) {
        outputFile = new File(baseDirectory, metadata.getFileName());
    } else {
        outputFile = new File(baseDirectory + File.separator + metadata.getFileParent(), metadata.getFileName());
        File parentDir = outputFile.getParentFile();
        parentDir.mkdirs();
    }
    
    System.out.println("Inicializando " + metadata.getFileID());
    FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
    FileAssemblyInfo fai = new FileAssemblyInfo(metadata.getTotalPackets(), fileOutputStream);
    fileInfoMap.put(metadata.getFileID(), fai);
  }
  
  /**
   * @param packetInfo: Objeto PacketInfo que contiene la información necesaria
   * de cada paquete de datos.
   * @throws IOException 
   */
  public void addPacket (PacketInfo packetInfo) throws IOException {
    FileAssemblyInfo fileInfo = fileInfoMap.get(packetInfo.getFileID());
    if (fileInfo == null) return;
    int packetNumber = packetInfo.getPacketNumber();
    
    int sequenceNumberPacket = packetInfo.getControlByte() & 0x0F;
    fileInfo.getPacketsReceived()[packetNumber] = true;
    fileInfo.getPacketStorage().set(sequenceNumberPacket, packetInfo.getDataFile());
    if(allPacketsNotNull(fileInfo)) {
      assembleFile(packetInfo.getFileID(), fileInfo.getPacketStorage());
      fileInfo.getPacketStorage().clear();
      fileInfo.getPacketStorage().addAll(Collections.nCopies(16, null));
    }
    
    if (allPacketsReceived(fileInfo)) {
      try {
        fileInfo.getFileOutputStream().close();
      } finally {
        fileInfoMap.remove(packetInfo.getFileID());
      }
    }
  }
  
  private void assembleFile (String fileID, List<byte[]> packets) throws IOException {
    FileOutputStream fileOutputStream = fileInfoMap.get(fileID).getFileOutputStream();
    for (byte[] packet: packets) {
      fileOutputStream.write(packet);
    }
    fileOutputStream.flush();
  }
  
  private boolean allPacketsReceived(FileAssemblyInfo fileInfo) {
    for (boolean received : fileInfo.getPacketsReceived()) {
      if (!received) {
          return false;
      }
    }
    return true;
  }
  
  private boolean allPacketsNotNull (FileAssemblyInfo fileInfo) {
    for (byte[] data: fileInfo.getPacketStorage()) {
      if (data == null) {
        return false;
      }
    }
    return true;
  }
}
