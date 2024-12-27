package threads;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.Metadata;
import file.FileAssembler;
import flowcontrol.SelectiveRejectManager;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import packet.Packet;
import packet.PacketManager;
import views.MulticastChat;

public class UnicastListener implements Runnable {
  private MulticastSocket socket;
  private boolean running;
  
  private SelectiveRejectManager selectiveRejectManager;
  private PacketManager packetManager;
  private FileAssembler fileAssembler;
  
  public UnicastListener (MulticastSocket socket, FileAssembler fileAssembler) {
    this.socket = socket;
    this.fileAssembler = fileAssembler;
    this.selectiveRejectManager = new SelectiveRejectManager(socket);
    this.packetManager = new PacketManager(fileAssembler);
    running = true;
  }
  
  @Override
  public void run() {
    System.out.println("Hilo Unicast escuchando en el puerto " + this.socket.getLocalPort());
    
    while(running) {
      byte[] buffer = new byte[1024];
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      try {
        socket.receive(packet);
        byte controlByte = buffer[0];
        if((controlByte & (byte) 0x80) != 0) {
          handlePacketFile(packet);
          continue;
        }
        
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Mensaje recibido en unicast: " + message);
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        
        if(json.has("action")) {
          String action = json.get("action").getAsString();
          
          switch (action) {
            case "message" -> handleMessagePrivate(json);
            case "file" -> handleFile(json, packet);
            default -> throw new AssertionError();
          }
        }
        
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }    
  }
  
  public void stopListening() {
    this.running = false;
  }
  
  private void handleMessagePrivate(JsonObject json) {
    String sender = json.get("sender").getAsString();
    String recipient = json.get("recipient").getAsString();
    String message = json.get("message").getAsString();
    MulticastChat.getInstance().addMessage(sender, recipient, message, false, true);
  }
  
  /**
   * Método para procesar los metadatos de un archivo, recibe un paquete de datos
   * del cual extrae la dirección, puerto del emisor del archivo y los metadatos
   * con los cuales inicializar el archivo y el handshake.
   * @param json
   * @param packet 
   */
  private void handleFile(JsonObject json, DatagramPacket packet) {
    // Establecer dirección y puerto de usuario que envia el archivo, para el envio de acuses
    if(selectiveRejectManager.getClientAddress() == null) {
      selectiveRejectManager.setClientInfo(packet.getAddress(), packet.getPort());
    }
    
    String sender = json.get("sender").getAsString();
    String recipient = json.get("recipient").getAsString();
    String metadataJson = json.get("metadata").getAsString();
    Gson gson = new Gson();
    Metadata metadata = gson.fromJson(metadataJson, Metadata.class);
    
    if(metadata != null) {
      try {
        this.fileAssembler.initializeFile(metadata);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    String path = "usuarios/" + recipient + "/" + metadata.getFileName();
    String filesize = Long.toString(metadata.getFileSize());
    MulticastChat.getInstance().addFileMessage(sender, recipient, path, metadata.getFileName(), filesize, false);
  } 
  
  /**
   * Método para recibir los paquetes de un archivo. Recibe el paquete
   * y posteriormente se procesa con el método de rechazo selectivo.
   * @param packet Paquete que contiene los datos.
   */
  private void handlePacketFile(DatagramPacket packet) {
    int sequenceNumber = Packet.verifyPacket(packet.getData());
    if(sequenceNumber != -1) {
      this.packetManager.processPacketData(packet);
      selectiveRejectManager.processReceivedPacket(sequenceNumber);
    }
  }
}
