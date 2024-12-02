package presentation.comands;

import com.google.gson.JsonObject;
import repository.rooms.ChatRoomRepository;

public class JoinChatRoomCommand implements Command {
  private JsonObject response, data;
  private ChatRoomRepository chatRoomRepository;
  
  
  public JoinChatRoomCommand(JsonObject data, ChatRoomRepository chatRoomRepository) {
    this.data = data;
    this.chatRoomRepository = chatRoomRepository;
    this.response = new JsonObject();
  }
  
  @Override
  public void execute() {
    try {
      if(!data.has("roomName")) {
        throw new IllegalArgumentException("El nombre del grupo es obligatorio");
      }
      
      String roomName = data.get("roomName").getAsString();
      if(!chatRoomRepository.findByName(roomName)) {
        response.addProperty("status", "error");
        response.addProperty("message", "El grupo no existe");
        return;
      }
      
      int id = data.get("id").getAsInt();
      int groupId = chatRoomRepository.getGroupIdByName(roomName);
      
      boolean userAdded = chatRoomRepository.addUserToGroup(id, groupId);
      if(userAdded) {
        String groupAddress = chatRoomRepository.getChatRoomAddress(roomName);
        System.out.println("Group: " + groupAddress);
        response.addProperty("status", "success");
        response.addProperty("message", "Unido al grupo con éxito");
        response.addProperty("groupAddress", groupAddress);
      }
    } catch (Exception e) {
      response.addProperty("status", "error");
      response.addProperty("message", e.getMessage());
    }
  }

  @Override
  public JsonObject getResponse() {
    return this.response;
  }
  
}
