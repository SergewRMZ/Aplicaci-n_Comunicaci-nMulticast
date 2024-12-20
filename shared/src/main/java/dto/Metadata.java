package dto;
public class Metadata {
  private final String filename;
  private final long filesize;
  private final String fileID;
  private final String fileparent;
  private final int totalPackets;
  
  /**
   * Constructor para representar un objeto que contenga información del archivo.
   * @param filename: Nombre del archivo
   * @param filepath: Nombre padre del archivo
   * @param filesize: Tamaño del archivo
   * @param fileID: ID de Archivo en bytes.
   * @param totalPackets: Número total de paquetes.
   */
  public Metadata(String filename, String fileparent, String fileID, long filesize, int totalPackets) {
    this.filename = filename;
    this.filesize = filesize;
    this.fileID = fileID;
    this.fileparent = fileparent;
    this.totalPackets = totalPackets;
  }
  
  public String getFileName () {
    return this.filename;
  }
  
  public long getFileSize () {
    return this.filesize;
  }
  
  public String getFileID () {
    return this.fileID;
  }
  
  public String getFileParent () {
    return this.fileparent;
  }
  
  public int getTotalPackets () {
    return this.totalPackets;
  }
  
  public byte[] getFileIdBytes () {
    return this.fileID.getBytes();
  }
  
  @Override
  public String toString() {
    return "Metadata{" +
            "filename='" + filename + '\'' +
            ", filesize=" + filesize +
            ", fileID='" + fileID + '\'' +
            ", fileparent='" + fileparent + '\'' +
            ", totalPackets=" + totalPackets +
            '}';
  }
}

