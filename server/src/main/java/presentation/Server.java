package presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import config.DatabaseInitializer;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import presentation.comands.CommandFactory;
public class Server {
  private static final int SERVER_PORT = 8000;
  private static final int CLIENT_PORT = 5000;
  
  private MulticastSocket socket;
  private static Server instance;  
  private ExecutorService threadPool;
  private Gson gson = null;
  private Server () {
    try {
      socket = new MulticastSocket(SERVER_PORT);
      gson = new GsonBuilder().setPrettyPrinting().create();
      threadPool = Executors.newFixedThreadPool(16);
      DatabaseInitializer.initialize();
      System.out.println("Servidor multicast iniciado en puerto: " + SERVER_PORT);
      listen();
    } catch (Exception e) {
    }
  }
  
  public static Server getInstanceServer () {
    if (instance == null) {
      instance = new Server();
    } 
    return instance;
  }
  
  public static void restartServer() {
    if(instance != null) {
      try {
        instance.socket.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    instance = null;
    getInstanceServer();
  }
  
  public void listen() {
    try {
      while(true) {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        
        threadPool.execute(() -> handleRequest(packet));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void handleRequest (DatagramPacket packet) {
    try {
      System.out.println("Puerto: " + packet.getPort());
      System.out.println("Address: " + packet.getAddress());

      String message = new String(packet.getData(), 0, packet.getLength());
      JsonObject json = JsonParser.parseString(message).getAsJsonObject();
      String action = json.get("action").getAsString();

      JsonObject jsonResponse = CommandFactory.createCommand(action, json);
      send(jsonResponse, packet.getPort(), packet.getAddress());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void send(JsonObject jsonResponse, int clientPort, InetAddress clientAddress) {
    try {
      String response = jsonResponse.toString();
      DatagramPacket datagramPacketResponse = new DatagramPacket(
        response.getBytes(), 
        response.getBytes().length,
        clientAddress,
        clientPort);
      
      socket.send(datagramPacketResponse);
      System.out.println("Respuesta enviada al cliente");
    } catch (Exception e) {
    }
  }
}