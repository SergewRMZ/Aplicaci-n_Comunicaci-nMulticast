import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Client {
  private static Client instance;
  private Gson gson;
  private int port = 8000;
  private final String SERVER_HOST = "127.0.0.1";
  private MulticastSocket socketClient;
  private InetAddress serverAddress;
  private Client() {
    try {
      socketClient = new MulticastSocket();
      serverAddress = InetAddress.getByName(SERVER_HOST);
      this.gson = new Gson();
      //socket = new MulticastSocket(port);
      //group = InetAddress.getByName(MULTICAST_GROUP);
      //socket.joinGroup(group);
      System.out.println("Cliente unido al servidor " + SERVER_HOST);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static Client getInstanceClient() {
    if(instance == null) {
      instance = new Client();
    }
    
    return instance;
  }
  
  public void registerUser(String username, String password) {
    try {
      JsonObject data = new JsonObject();
      data.addProperty("action", "register");
      JsonObject user = new JsonObject();
      
      user.addProperty("username", username);
      user.addProperty("password", password);
      data.add("user", user);
      
      sendRequest(data);
      String response = getServerResponse();
      System.out.println(response);
    } catch (Exception e) {
    }
  }
  
  public void getChatRooms() {
    try {
      JsonObject request = new JsonObject();
      request.addProperty("action", "getRooms");
      sendRequest(request);
      String response = getServerResponse();
      JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
      System.out.println(jsonResponse);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Método para solicitar al servidor la creación de un nuevo grupo
   * @param roomName Nombre del grupo
   */
  public void createChatRoom(String roomName) {
    try {
      JsonObject request = new JsonObject();
      request.addProperty("action", "createRoom");
      request.addProperty("roomName", roomName);
      sendRequest(request);
    } catch (Exception e) {
    }
  }
  
  /**
   * Método para enviar una solicitud al servidor.
   * @param request Recibe un objecto de tipo JsonObject con la infromación necesaria.
   */
  private void sendRequest(JsonObject request) {
    try {
      byte[] data = request.toString().getBytes();
      DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
      socketClient.send(packet);
    } catch (Exception e) {
    }
  }
  
  /**
   * Método para obtener la respuesta del servidor como un String
   * @return response (String).
   */
  private String getServerResponse() {
    try {
      byte[] buffer = new byte[1024];
      DatagramPacket response = new DatagramPacket(buffer, buffer.length);
      socketClient.receive(response);
      return new String(response.getData(), 0, response.getLength());
    } catch (Exception e) {
      e.printStackTrace();
    }
      return null;
  }
}
