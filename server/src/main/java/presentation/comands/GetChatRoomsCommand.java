package presentation.comands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import repository.rooms.ChatRoomRepository;

public class GetChatRoomsCommand implements Command {
  JsonObject response;
  private ChatRoomRepository chatRoomRepository;

  public GetChatRoomsCommand(JsonObject data, ChatRoomRepository chatRoomRepository) {
    this.chatRoomRepository = chatRoomRepository;
    this.response = new JsonObject();
  }
  
  @Override
  public void execute() {
    try {
      List<String> chatRooms = chatRoomRepository.getChatRooms();
      if(chatRooms != null) {
        JsonArray roomArray = new JsonArray();
        for(String room : chatRooms) {
          System.out.println(room);
          roomArray.add(room);
        }
        
        response.addProperty("status", "success");
        response.add("chat_rooms", roomArray);
      }
    } catch (Exception e) {
      response.addProperty("status", "error");
      response.addProperty("message", e.getMessage());
    }
  }

  @Override
  public JsonObject getResponse() {
    return response;
  }
}
