package repository.rooms;

import config.DatabaseConfig;
import domain.dto.ChatRoomDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteChatRoomRepository implements ChatRoomRepository {
  private static final String VERIFY_EXISTS = "SELECT COUNT(*) FROM groups WHERE group_name = ?";
  private final String CREATE_ROOM = "INSERT INTO groups(group_name, multicast_address, port) VALUES (?, ?, ?)";
  private final String GET_CHATROOMS = "SELECT group_name FROM groups";
  @Override
  public synchronized boolean roomExists(String roomName) {
    try {
      Connection conn = DatabaseConfig.getConnection();
      PreparedStatement stmt = conn.prepareStatement(VERIFY_EXISTS);
      stmt.setString(1, roomName);
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next()) {
        return rs.getInt(1) > 0;
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return false;
  }

  @Override
  public synchronized boolean createRoom(ChatRoomDto chatRoom) {
    String sql = CREATE_ROOM;
    try (Connection conn = DatabaseConfig.getConnection()) {
      if (conn != null) {
        conn.setAutoCommit(false); 
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, chatRoom.getRoomName());
          stmt.setString(2, chatRoom.getAddress());
          stmt.setInt(3, chatRoom.getPort());

          int rowsAffected = stmt.executeUpdate();
          if (rowsAffected > 0) {
            conn.commit();
            return true;
          } else {
            conn.rollback();
          }
        } catch (SQLException ex) {
          conn.rollback();
          Logger.getLogger(SqliteChatRoomRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    } catch (SQLException ex) {
      Logger.getLogger(SqliteChatRoomRepository.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

  @Override
  public List<String> getChatRooms() {
    List<String> chatRooms = new ArrayList<>();
    try (Connection conn = DatabaseConfig.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(GET_CHATROOMS);
      ResultSet rs = stmt.executeQuery();
      while(rs.next()) {
        chatRooms.add(rs.getString("group_name"));
      }
      
      return chatRooms;
    } catch (SQLException ex) {
      Logger.getLogger(SqliteChatRoomRepository.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return null;
  }
}
