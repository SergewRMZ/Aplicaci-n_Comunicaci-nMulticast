package model;
public class UserModel {
  private String userId;
  private String username;
  
  public UserModel (String userId, String username) {
    this.userId = userId;
    this.username = username;
  }

  public String getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }
  
  
}
