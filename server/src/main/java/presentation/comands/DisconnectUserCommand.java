package presentation.comands;

import com.google.gson.JsonObject;
import domain.models.UserModel;
import presentation.SessionManager;

public class DisconnectUserCommand implements Command {
  JsonObject data;
  JsonObject response;
  
  public DisconnectUserCommand (JsonObject data) {
    this.data = data;
    this.response = new JsonObject();
  }
  
  @Override
  public void execute() {
    if(data.has("id") && data.has("username")) {
      String id = data.get("id").getAsString();
      String username = data.get("username").getAsString();
      int port = data.get("port").getAsInt();
      UserModel userModelDelete = new UserModel(id, username, port);
      SessionManager.getInstance().removeActiveUser(userModelDelete);

      response.addProperty("status", "success");
      response.addProperty("message", "Sesión cerrada correctamente");
    }

    else {
      response.addProperty("status", "error");
      response.addProperty("message", "Es necesario enviar el identificador de usuario, su nombre de usuario y su puerto");
    }
  }

  @Override
  public JsonObject getResponse() {
    return this.response;
  }
  
}
