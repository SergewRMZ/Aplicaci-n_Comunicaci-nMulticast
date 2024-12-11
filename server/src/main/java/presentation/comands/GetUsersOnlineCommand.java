package presentation.comands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import presentation.SessionManager;

public class GetUsersOnlineCommand implements Command {
  JsonObject request;
  JsonObject response;
  
  public GetUsersOnlineCommand (JsonObject request) {
    this.request = request;
    this.response = new JsonObject();
  }
  
  @Override
  public void execute() {
    if (request.has("idUser")) {
      String idUser = request.get("idUser").getAsString();
      
      
      List<String> users = SessionManager.getInstance().getActiveUserList();
      
      if(users != null) {
        JsonArray userArray = new JsonArray();
        for(String user: users) {
          JsonObject userJson = new JsonObject();
          userJson.addProperty("username", user);
          userArray.add(userJson);
        }
        
        response.addProperty("status", "success");
        response.add("users", userArray);
        response.addProperty("message", "Lista de usuarios obtenida con éxito");
      }
      
      else {
        response.addProperty("status", "not_found");
        response.addProperty("message", "No hay usuarios activos");
      }
    }
    
    else {
      response.addProperty("status", "error");
      response.addProperty("message", "User ID missing");
    }
  }

  @Override
  public JsonObject getResponse() {
    return this.response;
  }
  
}
