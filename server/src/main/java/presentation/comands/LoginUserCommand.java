package presentation.comands;
import com.google.gson.JsonObject;
import domain.dto.ChatRoomDto;
import domain.dto.UserDto;
import domain.models.UserModel;
import java.util.List;
import presentation.SessionManager;
import presentation.comands.Command;
import repository.rooms.ChatRoomRepository;
import repository.users.UserRepository;

public class LoginUserCommand implements Command {
  private JsonObject response;
  private JsonObject data;
  private UserRepository UserRepository;
  private ChatRoomRepository chatRoomRepository;
  public LoginUserCommand (JsonObject data, UserRepository userRepository, ChatRoomRepository chatRoomRepository) {
    this.data = data;
    this.UserRepository = userRepository;
    this.chatRoomRepository = chatRoomRepository;
    this.response = new JsonObject();
  }
  
  @Override
  public void execute() {
    String username = data.getAsJsonObject("user").get("username").getAsString();
    String password = data.getAsJsonObject("user").get("password").getAsString();
    int port = data.get("port").getAsInt();
    System.out.println("Puerto recibido " + port);
    UserDto user = new UserDto(username, password);
    String id = UserRepository.loginUser(user);
    if (id != null) {
      
      // Agregar el usuario a la lista de conexiones y obtener sus grupos
      UserModel userModel = new UserModel(id, username, port);
      List<ChatRoomDto> chatRooms = chatRoomRepository.getUserGroups(Integer.parseInt(id));
      SessionManager.getInstance().addActiveUser(userModel, chatRooms);
      
      response.addProperty("status", "success");
      response.addProperty("id", id);
      response.addProperty("username", username);
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
