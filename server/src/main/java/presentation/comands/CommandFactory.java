package presentation.comands;

import com.google.gson.JsonObject;
import repository.users.SqliteUserRepository;
import repository.users.UserRepository;

public class CommandFactory {
  private static final CommandInvoker commandInvoker = new CommandInvoker();
  private static final UserRepository userRepository = new SqliteUserRepository();
  
  public static JsonObject createCommand(String action, JsonObject data) {
    Command command;
    switch (action) {
      case "register" -> command = new RegisterUserCommand(data, userRepository);
      case "login" -> command = new LoginUserCommand(data, userRepository);
      case "disconnect" -> command = new DisconnectUserCommand(data);
      case "getUsersOnline" -> command = new GetUsersOnlineCommand(data);
      default -> throw new AssertionError();
    }
    
    commandInvoker.addCommand(action, command);
    return commandInvoker.invokeCommand(action);
  }
}
