package presentation.comands;


import com.google.gson.JsonObject;
import domain.dto.UserDto;
import presentation.comands.Command;
import repository.users.UserRepository;

public class LoginUserCommand implements Command {
  private JsonObject response;
  private JsonObject data;
  private UserRepository UserRepository;
          
  public LoginUserCommand (JsonObject data, UserRepository userRepository) {
    this.data = data;
    this.UserRepository = userRepository;
    this.response = new JsonObject();
  }
  
  @Override
  public void execute() {
    String username = data.getAsJsonObject("user").get("username").getAsString();
    String password = data.getAsJsonObject("user").get("password").getAsString();
        System.out.println(username);
        System.out.println(password);

    UserDto user = new UserDto(username, password);
    String id = UserRepository.loginUser(user);
    if (id != null) {
      response.addProperty("status", "success");
      response.addProperty("id", id);
      response.addProperty("message", "Login successful");
    }
    
    else {
      response.addProperty("status", "error");
      response.addProperty("message", "El usuario no existe o la contraseña es incorrecta");
    }
  }

  @Override
  public JsonObject getResponse() {
    return this.response;
  }
  
}
