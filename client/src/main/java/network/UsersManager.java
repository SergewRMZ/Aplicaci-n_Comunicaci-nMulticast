package network;

import java.util.HashMap;
import java.util.Map;
import model.UserModel;

public class UsersManager {
  private static UsersManager instance;
  private Map<String, UserModel> usersOnline;
  
  private UsersManager () {
    usersOnline = new HashMap<>();
  }
  
  public static UsersManager getInstance() {
    if(instance == null) {
      instance = new UsersManager();
    }
    
    return instance;
  }
  
  public void addUserOnline(String username, UserModel userModel) {
    usersOnline.put(username, userModel);
  }
  
  public void removeUser(String username) {
    usersOnline.remove(username);
  }
  
  public Map<String, UserModel> getUsersOnline() {
    return this.usersOnline;
  }
  
  public UserModel getModelByUsername(String username) {
    if(usersOnline.containsKey(username)) {
      return usersOnline.get(username);
    }
    
    return null;
  }
}
