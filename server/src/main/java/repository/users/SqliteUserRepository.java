package repository.users;

import config.DatabaseConfig;
import domain.dto.UserDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqliteUserRepository implements UserRepository {
  private static final String INSERT_USER = "INSERT INTO users (username, password) VALUES (?, ?)";

  @Override
  public boolean registerUser(UserDto user) {
    try (
      Connection conn = DatabaseConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(INSERT_USER))
    {
      pstmt.setString(1, user.username);
      pstmt.setString(2, user.password);
      int rowInserted = pstmt.executeUpdate();
      return rowInserted > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }  
}
