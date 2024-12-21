package services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.ResponseDto;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.UserModel;
import network.UsersManager;

public class UserService {
  public UserService() {}
  
  public JsonObject createRegisterData(String username, String password) {
    JsonObject data = new JsonObject();
    data.addProperty("action", "register");
    JsonObject user = new JsonObject();

    user.addProperty("username", username);
    user.addProperty("password", password);
    data.add("user", user);
    return data;
  }
  
  public ResponseDto createRegisterResponse(JsonObject jsonResponse) {
    if (jsonResponse.has("status")) {
      boolean isError = true;
      if(jsonResponse.get("status").getAsString().equals("success")) {
        isError = false;
      }
      
      String message = jsonResponse.get("message").getAsString();
      return new ResponseDto(isError, message, null);
    }
    
    return null;
  }
  
  public JsonObject createLoginData(String username, String password, int port, String ipAddress) {
    JsonObject data = new JsonObject();
    data.addProperty("action", "login");
    JsonObject user = new JsonObject();
    user.addProperty("username", username);
    user.addProperty("password", password);
    data.add("user", user);
    data.addProperty("port", port);
    data.addProperty("ipAddress", ipAddress);
    return data;
  }
  
  public JsonObject createGetUsersData(String userId) {
    JsonObject data = new JsonObject();
    data.addProperty("action", "getUsersOnline");
    data.addProperty("idUser", userId);
    return data;
  }
  
  /**
   * Método que recibe un JSON con los datos de la petición de obtener usuarios. Se encarga
   * de deserializar la respuesta para obtener cada uno de los usuarios en linea e irlos agregando
   * a la lista de usuarios conectados.
   * @param response JSON que contiene la lista de usuarios conectados.
   * @param usersManager Objecto encargado de gestionar la lista de usuarios conectados.
   * @return Lista de usuarios conectados.
   */
  public List<String> getUsersOnlineList(JsonObject response, UsersManager usersManager) {
    List<String> usersOnline = new ArrayList<>();
    
    if(response.has("status")) {
      String status = response.get("status").getAsString();
      
      if(status.equals("success")) {
        JsonArray usersJson = response.getAsJsonArray("users");
        
        for(JsonElement userElement: usersJson) {
          JsonObject userJson = userElement.getAsJsonObject();
          String username = userJson.get("username").getAsString();
          int port = userJson.get("port").getAsInt();
          String ipAddress = userJson.get("ipAddress").getAsString();

          // Agregar a la lista de usuarios conectados
          UserModel userModel = new UserModel(username, port, ipAddress);
          usersManager.addUserOnline(username, userModel);

          usersOnline.add(username);
        }
        
        return usersOnline;
      }
      
      else if(status.equals("not_found"))
        System.out.println("No hay usuarios activos");
    }
    
    return null;
  }
  
  /**
   * Método para crear el objeto JSON para realizar la request de enviar un mensaje
   * a un usuario destinatario.
   * @param sender Usuario que envia el mensaje.
   * @param recipient Usuario que recibirá el mensaje.
   * @param message Mensaje que se enviará.
   * @return 
   */
  public JsonObject createSendMessageData(String sender, String recipient, String message) {
    JsonObject jsonMessage = new JsonObject();
    jsonMessage.addProperty("action", "message");
    jsonMessage.addProperty("sender", sender);
    jsonMessage.addProperty("recipient", recipient);
    jsonMessage.addProperty("message", message);
    return jsonMessage;
  }
  
  
  /**
   * Método para crear el directorio de un usuario de la aplicación.
   * @param username Nombre de usuario.
   */
  public void createUserDirectory(String username) {
    File directory = new File("usuarios/" + username);
    if(!directory.exists()) {
      if(directory.mkdirs()) {
        System.out.println("Carpeta creada para el usuario: " + username);
      }
      
      else {
        System.err.println("Error al crear la carpeta de usuario: " + username);
      }
    }
    
    else {
      System.out.println("La carpeta para el usuario " + username + " ya existe");
    }
  }
}
