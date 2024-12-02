package Client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import threads.MulticastListener;

public class Client {
  private static Client instance;
  private Gson gson;
  private int port = 8000;
  private final String SERVER_HOST = "127.0.0.1";
  private MulticastSocket socketClient;
  private InetAddress serverAddress;
  
  private String username;
  private String idUser;
  
  private ExecutorService threadPool;
  private static final int MAX_CHATROOMS = 4;
  private Client() {
    try {
      socketClient = new MulticastSocket();
      serverAddress = InetAddress.getByName(SERVER_HOST);
      this.gson = new Gson();
      this.threadPool = Executors.newFixedThreadPool(MAX_CHATROOMS);
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
  
  public String registerUser(String username, String password) {
    try {
      JsonObject data = new JsonObject();
      data.addProperty("action", "register");
      JsonObject user = new JsonObject();
      
      user.addProperty("username", username);
      user.addProperty("password", password);
      data.add("user", user);
      
      sendRequest(data);
      String response = getServerResponse();
      JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
      if (jsonResponse.has("status") && jsonResponse.get("status").getAsString().equals("success")) {
        String message = jsonResponse.get("message").getAsString();
        return message;
      }
      
      else {
        String message = jsonResponse.get("message").getAsString();
        return message;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return null; // Si no hay respuesta devuelve null
  } // RegisterUser
  
  public String loginUser (String username, String password) {
    try {
      JsonObject data = new JsonObject();
      data.addProperty("action", "login");
      JsonObject user = new JsonObject();
      user.addProperty("username", username);
      user.addProperty("password", password);
      data.add("user", user);
      
      sendRequest(data);
      String response = getServerResponse();
      JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
      if (jsonResponse.has("status") && jsonResponse.get("status").getAsString().equals("success")) {
        this.username = username;
        this.idUser = jsonResponse.get("id").getAsString();
        
        String message = jsonResponse.get("message").getAsString();
        return message;
      }
      
      else {
        if (jsonResponse.get("status").getAsString().equals("error")) {
          String message = jsonResponse.get("message").getAsString();
          return message;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return null;
  } // LoginUser
  
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
  
  public void joinChatRoom(String roomName) {
    try {
      JsonObject request = new JsonObject();
      request.addProperty("action", "joinRoom");
      request.addProperty("roomName", roomName);
      request.addProperty("username", username);
      request.addProperty("id", idUser);
      sendRequest(request);
      
      String response = getServerResponse();
      JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
      if(jsonResponse.has("status")) {
        String status = jsonResponse.get("status").getAsString();
        if("success".equals(status)) {
          if(jsonResponse.has("groupAddress")) {
            String groupAddress = jsonResponse.get("groupAddress").getAsString();
            String multicastAddress = groupAddress.split(":")[0];
            int port = Integer.parseInt(groupAddress.split(":")[1]);
            this.threadPool.submit(new MulticastListener(multicastAddress, port));
          }
        } 
      }
      System.out.println(response);
    } catch (Exception e) {
    }
  }
  
  public void sendMessage(String message) {
    
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
