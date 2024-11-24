package presentation.comands;

import com.google.gson.JsonObject;

public interface Command {
  void execute();
  JsonObject getResponse();
}
