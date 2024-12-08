package domain.models;
public class UserModel {
  public String userId;
  public String username;
  public String password;
  public int port;
  
  public UserModel(String userId, String username, String password, int port) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.port = port;
  }
}
