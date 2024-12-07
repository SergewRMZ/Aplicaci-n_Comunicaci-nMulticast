package Client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.ChatRoomDto;
import dto.ResponseDto;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import threads.MulticastListener;

public class Client {
  // Información sobre la conexión al servidor.
  private static Client instance;
  private Gson gson;
  private int port = 8000;
  private final String SERVER_HOST = "127.0.0.1";
  private MulticastSocket socketClient;
  private InetAddress serverAddress;
  
  // Datos del Usuario
  private String username;
  private String idUser;
  
  // ALBERCA DE HILOS
  private ExecutorService threadPool;
  private static final int MAX_CHATROOMS = 4;
  
  // Información sobre el grupo
  private ChatRoomDto chatRoom;
  
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
  
  public String getUsername() {
    return this.username;
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
  
  public ResponseDto loginUser (String username, String password) {
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
        return new ResponseDto(false, message);
      }
      
      else {
        if (jsonResponse.get("status").getAsString().equals("error")) {
          String message = jsonResponse.get("message").getAsString();
          return new ResponseDto(true, message);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return null;
  } // LoginUser
  
  public void sendMessage(String message) {
    try {
      JsonObject jsonMessage = new JsonObject();
      jsonMessage.addProperty("action", "message");
      jsonMessage.addProperty("user", this.username);
      jsonMessage.addProperty("message", message);
      byte[] data = jsonMessage.toString().getBytes();
      DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(chatRoom.getAddress()), chatRoom.getPort());
      socketClient.send(packet);
      System.out.println("Mensaje enviado");
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
  
  public void getUserGroup () {
    try {
      JsonObject request = new JsonObject();
      request.addProperty("action", "getUserGroups");
      request.addProperty("idUser", idUser);
      sendRequest(request);
      String response = getServerResponse();
      JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
      System.out.println(jsonResponse);
      if (jsonResponse.has("status")) {
        String status = jsonResponse.get("status").getAsString();
        if (status.equals("success")) {
          JsonObject groupJson = jsonResponse.getAsJsonObject("group");
          this.chatRoom = gson.fromJson(groupJson, ChatRoomDto.class);
          
          // Inicio de hilo para escuchar en el grupo
          this.threadPool.submit(new MulticastListener(chatRoom.getAddress(), chatRoom.getPort()));
        }
        
        else if(status.equals("not_found")) {
          System.out.println("No te has unido al grupo");
        }
      }
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
