package presentation.comands;

import com.google.gson.JsonObject;
import domain.dto.ChatRoomDto;
import repository.rooms.ChatRoomRepository;

public class CreateChatRoomCommand implements Command {
  private JsonObject data;
  private ChatRoomRepository chatRoomRepository;
  private JsonObject response;
  
  public CreateChatRoomCommand(JsonObject data, ChatRoomRepository chatRoomRepository) {
    this.data = data;
    this.chatRoomRepository = chatRoomRepository;
    this.response = new JsonObject();
  }
  
  @Override
  public void execute() {
    String roomName = data.get("roomName").getAsString();
    try {
      String address = getAvailableMulticastAddress();
      if (address != null) {
        int port = 8010;
        ChatRoomDto chatRoomDto = new ChatRoomDto(roomName, address, port);
        chatRoomRepository.createRoom(chatRoomDto);
        createSuccessResponse(roomName, address, port);
      }
      
      else {
        createErrorResponse("Error: No es posible crear otro grupo.");
      }
    } catch (Exception e) {
    }
  }
  
  private String getAvailableMulticastAddress() {
    String[] multicastAddress = {
      "230.0.0.1", "230.0.0.2", "230.0.0.3", "230.0.0.4"
    };
    
    for (String address : multicastAddress) {
      if(!this.chatRoomRepository.roomExists(address)) {
        return address;
      }
    }
    
    return null;
  } 
  
  private void createSuccessResponse(String roomName, String address, int port) {
    this.response.addProperty("status", "success");
    JsonObject roomDetails = new JsonObject();
    roomDetails.addProperty("roomName", roomName);
    roomDetails.addProperty("address", address);
    roomDetails.addProperty("port", port);
    response.add("data", roomDetails);
  }
  
  private void createErrorResponse(String message) {
    response.addProperty("status", "error");
    response.addProperty("message", message);
  }
  
  public JsonObject getResponse() {
    return response;
  }
}
