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
  private final String VERIFY_EXISTS = "SELECT COUNT(*) FROM groups WHERE multicast_address = ?";
  private final String FIND_BYNAME = "SELECT COUNT(*) FROM groups WHERE group_name = ?";
  private final String CREATE_ROOM = "INSERT INTO groups(group_name, multicast_address, port) VALUES (?, ?, ?)";
  private final String GET_CHATROOMS = "SELECT group_name FROM groups";
  private final String GET_CHATROOM_ADDRESS = "SELECT multicast_address || ':' || port AS address FROM groups WHERE group_name = ?";
  
  private final String ADD_USER_TO_GROUP = "INSERT INTO group_members(group_id, user_id) VALUES(?, ?)";
  private final String GET_GROUP_ID_BY_NAME = "SELECT id FROM groups WHERE group_name = ?";
  private final String VERIFY_USER_EXISTS_GROUP = "SELECT COUNT(*) group_members(group_id, user_id) VALUES(?, ?)";
  
  /**
   * Verificar si una dirección multicast ya está asignada
   * @param roomAddress: Dirección multicast.
   * @return 
   */
  @Override
  public synchronized boolean roomExists(String roomAddress) {
    try(Connection conn = DatabaseConfig.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(VERIFY_EXISTS);
      stmt.setString(1, roomAddress);
      ResultSet rs = stmt.executeQuery();
      if(rs.next()) {
        return rs.getInt(1) > 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    
    return false;
  }
  
  /**
   * Buscar una grupo de chat por el nombre.
   * @param roomName: Nombre de la sala de chat.
   * @return 
   */
  @Override
  public synchronized boolean findByName(String roomName) {
    try (Connection conn = DatabaseConfig.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(FIND_BYNAME);
      stmt.setString(1, roomName);
      ResultSet rs = stmt.executeQuery();
      if(rs.next()) {
        return rs.getInt(1) > 0;
      }
      
    } catch (SQLException ex) {
      Logger.getLogger(SqliteChatRoomRepository.class.getName()).log(Level.SEVERE, null, ex);
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

  /**
   * Obtener todos los grupos de chat
   * @return 
   */
  @Override
  public synchronized List<String> getChatRooms() {
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
  
  /**
   * Obtener la dirección multicast de un grupo de chat con base en el nombre.
   * @param groupName: Nombre de la sala de chat
   * @return 
   */
  @Override
  public synchronized String getChatRoomAddress(String groupName) {
    try {
      Connection conn = DatabaseConfig.getConnection();
      PreparedStatement stmt = conn.prepareStatement(GET_CHATROOM_ADDRESS);
      stmt.setString(1, groupName);
      ResultSet rs = stmt.executeQuery();
      while(rs.next()) {
        return rs.getString("address"); // Alias
      }
    } catch (SQLException e) {
      Logger.getLogger(SqliteChatRoomRepository.class.getName()).log(Level.SEVERE, "Error al obtener la dirección del grupo", e);
    }
    return null;
  }

  /**
   * Cuando un usuario desea unirse a un chat, se crea una relación que contiene
   * qué usuarios forman parte de un grupo multicast. Evita duplicidad
   * verificando que no exista el usuario en un chat antes de ingresarlo.
   * @param userId
   * @param groupId
   * @return 
   */
  @Override
  public synchronized boolean addUserToGroup(int userId, int groupId) {
    try (
      Connection conn = DatabaseConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(VERIFY_USER_EXISTS_GROUP);  
    ) {
      pstmt.setInt(1, groupId);
      pstmt.setInt(2, userId);
      ResultSet rs = pstmt.executeQuery();

      if(rs.next() && rs.getInt(1) > 0) {
        System.out.println("El usuario ya está asociado al grupo");
        return false;
      }
      
      try(PreparedStatement insertStmt = conn.prepareStatement(ADD_USER_TO_GROUP)) {
        insertStmt.setInt(1, groupId);
        insertStmt.setInt(2, userId);
        int rowsInserted = insertStmt.executeUpdate();
        return rowsInserted > 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Obtener el id de grupo con base en el nombre de grupo.
   * @param roomName: Nombre del grupo.
   * @return Id de grupo.
   */
  @Override
  public synchronized int getGroupIdByName(String roomName) {
      try (Connection conn = DatabaseConfig.getConnection()) {
        PreparedStatement stmt = conn.prepareStatement(GET_GROUP_ID_BY_NAME);
        stmt.setString(1, roomName);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        } else {
            throw new SQLException("Grupo no encontrado con el nombre: " + roomName);
        }
      } catch (SQLException e) {
          e.printStackTrace();
          return -1;
      }
  }
}
