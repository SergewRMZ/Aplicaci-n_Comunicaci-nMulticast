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
      
      
      JsonArray activeUsers = SessionManager.getInstance().getActiveUsers();
      
      if(activeUsers != null) {
        response.addProperty("status", "success");
        response.add("users", activeUsers);
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
