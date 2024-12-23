package network;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.ChatRoomDto;
import dto.Metadata;
import dto.ResponseDto;
import file.FileAssembler;
import file.FileSender;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.UserModel;
import packet.Packet;
import packet.PacketManager;
import packet.PacketSender;
import services.UserService;
import threads.MulticastListener;
import threads.UnicastListener;

public class Client {
  private static Client instance;
  private int port = 8000;
  private final String SERVER_HOST = "127.0.0.1";
  private MulticastSocket socketClient;
  private InetAddress serverAddress;
  
  // Datos de red del usuario
  String localIpAddress;
  int localPort;
  
  // Información del Usuario
  private UserModel user;
 
  // Servicios
  UsersManager usersManager;
  UserService userService;
  FileAssembler fileAssembler;
  
  // ALBERCA DE HILOS
  private ExecutorService threadPool;
  private static final int MAX_CHATROOMS = 4;
  
  // Información sobre el grupo
  private ChatRoomDto chatRoom;
  
  private Client() {
    initializeSocket();
    initializeChatRoom();
    initializeServices();
    System.out.println("Cliente unido al servidor " + SERVER_HOST);
    System.out.println("Dirección local " + localIpAddress);
  }
  
  public static Client getInstanceClient() {
    if(instance == null) {
      instance = new Client();
    }
    return instance;
  }
  
  private void initializeSocket() {
    try {
      socketClient = new MulticastSocket();
      socketClient.setLoopbackMode(false);
      socketClient.setTimeToLive(255);
      socketClient.setReceiveBufferSize(1024 * 16);
      serverAddress = InetAddress.getByName(SERVER_HOST);
      localIpAddress = InetAddress.getLocalHost().getHostAddress();
      localPort = socketClient.getLocalPort();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void initializeChatRoom() {
    chatRoom = new ChatRoomDto("Grupo", "230.0.0.1", 8010);
  }
  
  private void initializeServices() {
    usersManager = UsersManager.getInstance();
    userService = new UserService();
    this.threadPool = Executors.newFixedThreadPool(MAX_CHATROOMS);
  }
  
  public UserModel getUser() {
    return this.user;
  }
  
  public ResponseDto registerUser(String username, String password) {
    try {
      JsonObject data = userService.createRegisterData(username, password);
      sendRequest(data);
      String response = getServerResponse();
      JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
      return userService.createRegisterResponse(jsonResponse);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null; 
  } // registerUser
  
  public ResponseDto loginUser (String username, String password) {
    try {
      JsonObject data = userService.createLoginData(username, password, localPort, localIpAddress);
      sendRequest(data);
      String response = getServerResponse();
      JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
      if (jsonResponse.has("status") && jsonResponse.get("status").getAsString().equals("success")) {
        this.user = new UserModel(jsonResponse.get("id").getAsString(), jsonResponse.get("username").getAsString(), socketClient.getLocalPort(), localIpAddress);
        
        // Crear directorio del usuario
        userService.createUserDirectory(this.user.getUsername());
        
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
  } //loginUser
  
  public void disconnect () {
    try {
      JsonObject request = new JsonObject();
      request.addProperty("action", "disconnect");
      request.addProperty("id", user.getUserId());
      sendRequest(request);
      System.out.println("Cerrando sesión");
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Método para el envio de mensajes. Recibe el nombre del usuario
   * y con base en este se obtiene su dirección ip y puerto específico
   * para enviar el mensaje.
   * @param message Mensaje a enviar
   * @param recipient Usuario destinatario.
   */
  public void sendMessage(String message, String recipient) {
    UserModel userDest = usersManager.getModelByUsername(recipient);
    try {
      JsonObject jsonMessage = userService.createSendMessageData(user.getUsername(), recipient, message);
      byte[] data = jsonMessage.toString().getBytes();
      InetAddress ipAddressDest = InetAddress.getByName(userDest.getIpAddress());
      DatagramPacket packet = new DatagramPacket(
        data, 
        data.length, 
        ipAddressDest, 
        userDest.getPort()
      );

      socketClient.send(packet);
      System.out.println("Mensaje enviado a " + recipient);
    } catch (Exception e) {
      System.err.println("Error al enviar el mensaje privado");
      e.printStackTrace();
    }
  }
  
  /**
   * Método para el envio de archivos a un destinatario.
   * @param file Objeto de tipo File que corresponde al flujo de archivo que se enviará
   * @param recipient Nombre del Destinatario.
   */
  
  public void sendFile(File file, String recipient) {
    UserModel userDest = usersManager.getModelByUsername(recipient);
    try {
      DatagramSocket fileSocket = new DatagramSocket();
      JsonObject data = new JsonObject();
      data.addProperty("action", "file");
      data.addProperty("sender", user.getUsername());
      data.addProperty("recipient", recipient);
      Metadata metadata = Packet.createFileMetadata(file.getName(), file.length(), null);
      Gson gson = new Gson();
      String metadataJson = gson.toJson(metadata);
      
      data.addProperty("metadata", metadataJson);
      
      byte[] bytesData = data.toString().getBytes();
      InetAddress ipAddressDest = InetAddress.getByName(userDest.getIpAddress());
      int portDest = userDest.getPort();
      DatagramPacket packet = new DatagramPacket(
        bytesData,
        bytesData.length,
        ipAddressDest,
        portDest
      );
      
      fileSocket.send(packet);
      System.out.println("Metadatados enviados");
      
      PacketManager packetManager = new PacketManager(this.fileAssembler);
      PacketSender packetSender = new PacketSender(fileSocket, ipAddressDest, portDest);
      packetManager.setPacketSender(packetSender);
      
      FileSender fileSender = new FileSender(packetManager, fileSocket);
      fileSender.sendFile(file);
      
      System.out.println("Metadatos del archivo enviado a " + recipient);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Método para iniciar el hilo de escucha de mensajes multicast.
   */
  public void getUserGroup() {
    String groupName = "ChatGrupal";
    UserModel userModel = new UserModel(groupName, chatRoom.getPort(), chatRoom.getAddress());
    UsersManager.getInstance().addUserOnline(groupName, userModel);
    this.threadPool.submit(new MulticastListener(chatRoom.getAddress(), chatRoom.getPort(), socketClient.getLocalPort()));
  }
  
  public void listenUnicast() {
    this.fileAssembler = new FileAssembler("usuarios/" + this.user.getUsername());
    this.threadPool.submit(new UnicastListener(socketClient, fileAssembler));
  }
  
  public List<String> getUsersOnline() {
    List<String> usersOnline = new ArrayList<>();
    
    try {
      JsonObject request = userService.createGetUsersData(user.getUserId());
      sendRequest(request);
      String response = getServerResponse();
      JsonObject responseJson = JsonParser.parseString(response).getAsJsonObject();
      return userService.getUsersOnlineList(responseJson, usersManager);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return usersOnline;
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
