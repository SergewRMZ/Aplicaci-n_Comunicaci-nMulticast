package presentation.comands;

import com.google.gson.JsonObject;
import domain.dto.UserDto;
import repository.users.UserRepository;

public class RegisterUserCommand implements Command {
  private JsonObject data, response;
  private UserRepository userRepository;
  public RegisterUserCommand(JsonObject data, UserRepository userRepository) {
    this.data = data;
    this.userRepository = userRepository;
    this.response = new JsonObject();
  }

  @Override
  public void execute() {
    String username = data.getAsJsonObject("user").get("username").getAsString();
    String password = data.getAsJsonObject("user").get("password").getAsString();
    
    UserDto user = new UserDto(username, password);
    boolean created = userRepository.registerUser(user);
    if(created) createSuccessResponse(); 
    else createErrorResponse("Error: No se pudo crear el usuario");    
  }
  
  public void createSuccessResponse() {
    response.addProperty("status", "success");
    response.addProperty("message", "Usuario registrado correctamente");
  }
  
  public void createErrorResponse(String message) {
    response.addProperty("status", "error");
    response.addProperty("message", message);
  }

  @Override
  public JsonObject getResponse() {
    return response;
  }
}
