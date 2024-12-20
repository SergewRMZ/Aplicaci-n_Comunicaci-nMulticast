package services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.ResponseDto;
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
}
