package repository.rooms;

import domain.dto.ChatRoomDto;
import java.util.List;

public interface ChatRoomRepository {
  boolean roomExists(String roomName);
  boolean createRoom(ChatRoomDto chatRoom);
  List<String> getChatRooms();
}

