package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.ChatRoomDto;
import dto.ResponseDto;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.UserModel;
import threads.MulticastListener;

public class Client {
  // Información sobre la conexión al servidor.
  private static Client instance;
  private Gson gson;
  private int port = 8000;
  private int CLIENT_PORT;
  private final String SERVER_HOST = "127.0.0.1";
  private MulticastSocket socketClient;
  private InetAddress serverAddress;
  String ipAddress;
  
  // Datos del Usuario
  private UserModel user;
  
  // Lista de amigos o usuarios en linea
  private HashMap<String, UserModel> usersOnline = new HashMap<String, UserModel>();
  
  // ALBERCA DE HILOS
  private ExecutorService threadPool;
  private static final int MAX_CHATROOMS = 4;
  
  // Información sobre el grupo
  private ChatRoomDto chatRoom;
  
  private Client() {
    try {
      socketClient = new MulticastSocket();
      socketClient.setLoopbackMode(false);
      socketClient.setTimeToLive(255);
      serverAddress = InetAddress.getByName(SERVER_HOST);
      ipAddress = InetAddress.getLocalHost().getHostAddress();
      CLIENT_PORT = socketClient.getLocalPort();
      chatRoom = new ChatRoomDto("Grupo", "230.0.0.1", 8010);
      this.gson = new Gson();
      this.threadPool = Executors.newFixedThreadPool(MAX_CHATROOMS);
      System.out.println("Cliente unido al servidor " + SERVER_HOST);
      System.out.println("Dirección local " + ipAddress);
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
  
  public UserModel getUser() {
    return this.user;
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
      data.addProperty("port", socketClient.getLocalPort());
      data.addProperty("ipAddress", ipAddress);
      
      sendRequest(data);
      String response = getServerResponse();
      System.out.println(response);
      JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
      if (jsonResponse.has("status") && jsonResponse.get("status").getAsString().equals("success")) {
        this.user = new UserModel(jsonResponse.get("id").getAsString(), jsonResponse.get("username").getAsString(), socketClient.getLocalPort(), ipAddress);
        String message = jsonResponse.get("message").getAsString();
        return new ResponseDto(false, message, this.user);
      }
      
      else {
        if (jsonResponse.get("status").getAsString().equals("error")) {
          String message = jsonResponse.get("message").getAsString();
          return new ResponseDto(true, message, null);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return null;
  } // LoginUser
  
  public void disconnect () {
    try {
      JsonObject request = new JsonObject();
      request.addProperty("action", "disconnect");
      request.addProperty("id", user.getUserId());
      
      sendRequest(request);
      System.out.println("Cerrando sesión");
      String response = getServerResponse();
      System.out.println(response);
      
      if(threadPool != null) {
        threadPool.shutdownNow();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void sendMessage(String message) {
    try {
      JsonObject jsonMessage = new JsonObject();
      jsonMessage.addProperty("action", "message");
      jsonMessage.addProperty("username", this.user.getUsername());
      jsonMessage.addProperty("message", message);
      byte[] data = jsonMessage.toString().getBytes();
      DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(chatRoom.getAddress()), chatRoom.getPort());
      socketClient.send(packet);
      System.out.println("Mensaje enviado al grupo multicast");
    } catch (Exception e) {
    }
  }
  
  public void getUserGroup() {
    this.threadPool.submit(new MulticastListener(chatRoom.getAddress(), chatRoom.getPort(), this.CLIENT_PORT));
  }
  
  public List<String> getUsersOnline() {
    List<String> usersOnline = new ArrayList<>();
    
    try {
      JsonObject request = new JsonObject();
      request.addProperty("action", "getUsersOnline");
      request.addProperty("idUser", user.getUserId());
      sendRequest(request);
      
      String response = getServerResponse();
      JsonObject responseJson = JsonParser.parseString(response).getAsJsonObject();
      System.out.println(responseJson);
      
      if(responseJson.has("status")) {
        String status = responseJson.get("status").getAsString();
        
        if(status.equals("success")) {
          JsonArray usersJson = responseJson.getAsJsonArray("users");
          for(JsonElement userElement: usersJson) {
            JsonObject userJson = userElement.getAsJsonObject();
            String username = userJson.get("username").getAsString();
            int port = userJson.get("port").getAsInt();
            String ipAddress = userJson.get("ipAddress").getAsString();
            
            addUserOnlineList(username, port, ipAddress);
            usersOnline.add(username);
          }
        }
        
        else if(status.equals("not_found")) {
          System.out.println("No hay usuarios activos");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return usersOnline;
  }
  
  private void addUserOnlineList(String username, int port, String ipAddress) {
    UserModel userModel = new UserModel(username, port, ipAddress);
    usersOnline.put(username, userModel);
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
