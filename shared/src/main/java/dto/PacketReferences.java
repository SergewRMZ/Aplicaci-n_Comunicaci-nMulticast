package dto;
public class PacketReferences {
  public long startOffset;
  public long endOffset;
  public int sequenceNumber;
  public int packetNumber;
  
  public PacketReferences (long startOffset, long endOffset, int sequenceNumber, int packetNumber) {
    this.startOffset = startOffset;
    this.endOffset = endOffset;
    this.sequenceNumber = sequenceNumber;
    this.packetNumber = packetNumber;
  }
  
  @Override
  public String toString () {
    StringBuilder sb = new StringBuilder();
    sb.append("PacketReferences {");
    sb.append("Star Offset: ").append(startOffset).append(", ");
    sb.append("EndOffset: ").append(endOffset).append(", ");
    sb.append("Sequence Number: ").append(sequenceNumber).append(", ");
    sb.append("Packet Number: ").append(packetNumber).append(", ");
    
    return sb.toString();
  }
}
