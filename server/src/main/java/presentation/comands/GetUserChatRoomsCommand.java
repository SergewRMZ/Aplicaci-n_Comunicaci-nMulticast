package presentation.comands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import domain.dto.ChatRoomDto;
import java.util.ArrayList;
import java.util.List;
import repository.rooms.ChatRoomRepository;

/**
 * Comando para obtener los grupos a los que pertenece un usuario.
 * @author serge
 */
public class GetUserChatRoomsCommand implements Command {
  private JsonObject response;
  private JsonObject data;
  private ChatRoomRepository chatRoomRepository;
  
  public GetUserChatRoomsCommand (JsonObject data, ChatRoomRepository chatRoomRepository) {
    this.chatRoomRepository = chatRoomRepository;
    this.data = data;
    this.response = new JsonObject();
  }
  
  @Override
  public void execute() {
    int idUser = data.get("idUser").getAsInt();
    List<ChatRoomDto> userChats = new ArrayList<>();
    userChats = chatRoomRepository.getUserGroups(idUser);
    if(userChats != null) {
      response.addProperty("status", "success");
      for(ChatRoomDto chat : userChats) {
        Gson gson = new Gson();
        JsonObject groupJson = gson.toJsonTree(chat).getAsJsonObject();
        response.add("group", groupJson);
      }
    }
    
    else {
      response.addProperty("status", "not_found");
      response.addProperty("message", "Aún no te has unido a ningún grupo");
    }
  } 

  @Override
  public JsonObject getResponse() {
    return this.response;
  }
  
}
