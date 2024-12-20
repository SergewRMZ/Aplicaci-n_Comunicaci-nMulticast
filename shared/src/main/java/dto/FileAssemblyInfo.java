package dto;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileAssemblyInfo {
    private int totalPackets;
    private FileOutputStream fileOutputStream;
    private boolean[] packetsReceived;
    private List<byte[]> packetStorage;

    public FileAssemblyInfo(int totalPackets, FileOutputStream fileOutputStream) {
        this.totalPackets = totalPackets;
        this.fileOutputStream = fileOutputStream;
        this.packetsReceived = new boolean[totalPackets];
        this.packetStorage = new ArrayList<>(Collections.nCopies(16, null));
    }

    public int getTotalPackets() {
        return totalPackets;
    }

    public FileOutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    public boolean[] getPacketsReceived() {
        return packetsReceived;
    }

    public List<byte[]> getPacketStorage() {
        return packetStorage;
    }
}


