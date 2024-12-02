package repository.rooms;

import domain.dto.ChatRoomDto;
import java.util.List;

public interface ChatRoomRepository {
  boolean roomExists(String roomAddress);
  boolean findByName(String roomName);
  boolean createRoom(ChatRoomDto chatRoom);
  boolean addUserToGroup(int userId, int groupId);
  List<String> getChatRooms();
  String getChatRoomAddress(String groupName);
  int getGroupIdByName(String roomName);
}

