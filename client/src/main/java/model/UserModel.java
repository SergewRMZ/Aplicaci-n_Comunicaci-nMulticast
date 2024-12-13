package model;
public class UserModel {
  private String userId;
  private String username;
  private int port;
  private String ipAddress;
  
  public UserModel(String username, int port, String ipAddress) {
    this.username = username;
    this.port = port;
    this.ipAddress = ipAddress;
  }
  
  public UserModel(String userId, String username, int port, String ipAddress) {
    this.userId = userId;
    this.username = username;
    this.port = port;
    this.ipAddress = ipAddress;
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
  
  public String getIpAddress() {
    return ipAddress;
  }
}
