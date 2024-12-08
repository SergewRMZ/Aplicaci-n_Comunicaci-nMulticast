package presentation.comands;

import com.google.gson.JsonObject;
import repository.rooms.ChatRoomRepository;
import repository.rooms.SqliteChatRoomRepository;
import repository.users.SqliteUserRepository;
import repository.users.UserRepository;

public class CommandFactory {
  private static final CommandInvoker commandInvoker = new CommandInvoker();
  private static final UserRepository userRepository = new SqliteUserRepository();
  private static final ChatRoomRepository chatRoomRepository = new SqliteChatRoomRepository();
  
  public static JsonObject createCommand(String action, JsonObject data) {
    Command command;
    switch (action) {
      case "register" -> command = new RegisterUserCommand(data, userRepository);
      case "login" -> command = new LoginUserCommand(data, userRepository, chatRoomRepository);
      case "createRoom" -> command = new CreateChatRoomCommand(data, chatRoomRepository);
      case "getRooms" -> command = new GetChatRoomsCommand(data, chatRoomRepository);
      case "joinRoom" -> command = new JoinChatRoomCommand(data, chatRoomRepository);
      case "getUserGroups" -> command = new GetUserChatRoomsCommand(data, chatRoomRepository);
      default -> throw new AssertionError();
    }
    
    commandInvoker.addCommand(action, command);
    return commandInvoker.invokeCommand(action);
  }
}
