package presentation.comands;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
  private Map<String, Command> commandMap;
  public CommandInvoker() {
    commandMap = new HashMap<>();
  }

  public void addCommand(String commandName, Command command) {
    this.commandMap.put(commandName, command);
  }
  
  public JsonObject invokeCommand(String action) {
    Command command = commandMap.get(action);
    if (command != null) {
      command.execute();
      return command.getResponse();
    }
    
    return null;
  }
 
}
