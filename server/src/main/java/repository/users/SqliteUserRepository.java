package repository.users;

import config.DatabaseConfig;
import domain.dto.UserDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SqliteUserRepository implements UserRepository {
  private static final String INSERT_USER = "INSERT INTO users (username, password) VALUES (?, ?)";
  private static final String LOGIN_USER = "SELECT id FROM users WHERE username = ? AND password = ?";
  
  private final Lock lock = new ReentrantLock();
  
  @Override
  public boolean registerUser(UserDto user) {
    lock.lock();
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
    } finally {
      lock.unlock();
    }
  }  

  @Override
  public String loginUser(UserDto user) {
    lock.lock();
    try (
      Connection conn = DatabaseConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(LOGIN_USER)) {
      
      pstmt.setString(1, user.username);
      pstmt.setString(2, user.password);
      var resultSet = pstmt.executeQuery();

      if (resultSet.next()) {
          return resultSet.getString("id");
      } else {
          return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      lock.unlock();
    }
  }
}
