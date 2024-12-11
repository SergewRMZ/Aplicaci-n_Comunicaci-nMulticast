package model;
public class UserModel {
  private String userId;
  private String username;
  private int port;
  
  public UserModel (String userId, String username, int port) {
    this.userId = userId;
    this.username = username;
    this.port = port;
  }

  public String getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }
  
  public int getPort() {
    return port;
  }
}
