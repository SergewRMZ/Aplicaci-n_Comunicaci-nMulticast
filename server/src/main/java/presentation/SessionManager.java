package presentation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import domain.dto.ChatRoomDto;
import domain.models.UserModel;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class SessionManager {
  private static SessionManager instance;
  private Map<UserModel, ChatRoomDto> activeSessions;
  private final ReentrantLock lock = new ReentrantLock();
  private final String hostMulticast = "230.0.0.1";
  private final int portMulticast = 8010;
  
  private SessionManager() {
    activeSessions = new ConcurrentHashMap<>();
  }
  
  public static synchronized SessionManager getInstance() {
    if(instance == null) {
      instance = new SessionManager();
    }
    return instance;
  }
  
  /**
   * Método para agregar un usuario a la lista de conexiones, además 
   * de agregar los grupos a los que pertenece el usuario.
   * @param userModel Modelo de negocio del Usuario
   * @param chatRoom Sala de chat
   */
  public void addActiveUser(UserModel userModel, ChatRoomDto chatRoom) {
    lock.lock();
    try {
      activeSessions.put(userModel, chatRoom);
      sendNewUserNotification(userModel);
      showUsers();
    } finally {
      lock.unlock();
    }
  }

  /**
   * Método para eliminar un usuario de la lista de usuarios activos. Se ejecuta
   * cuando un usuario decide cerrar la sesión.
   * @param userId 
   */
  public void removeActiveUser(String userId) {
    lock.lock();
    try {
      UserModel userToRemove = null;
      
      for(UserModel user: activeSessions.keySet()) {
        if(user.getUserId().equals(userId)) {
          userToRemove = user;
          System.out.println("Usuario encontrado");
          break;
        }
      }
      
      activeSessions.remove(userToRemove);
      sendUserDisconnectedNotification(userToRemove);
      showUsers();
    } finally {
      lock.unlock();
    }
  }
  
  public JsonArray getActiveUsers () {
    JsonArray activeUsers = new JsonArray();
    for(UserModel user : activeSessions.keySet()) {
      JsonObject userObject = new JsonObject();
      userObject.addProperty("username", user.getUsername());
      userObject.addProperty("port", user.getPort());
      userObject.addProperty("ipAddress", user.getIpAddress());
      
      // Agregar el objecto de usuario
      activeUsers.add(userObject);
    }
    
    return activeUsers;
  }
  
  /**
   * Este método se invoca siempre que un usuario inicia sesión. Al iniciar sesión, 
   * el servidor envia a todos los usuarios la notificación de que un usuario está en linea.
   */
  private void sendNewUserNotification(UserModel user) {
    JsonObject newUserNotification = new JsonObject();
    newUserNotification.addProperty("action", "user-connected");
    newUserNotification.addProperty("status", "success");
    
    JsonObject userObject = new JsonObject();
    userObject.addProperty("username", user.getUsername());
    userObject.addProperty("port", user.getPort());
    userObject.addProperty("ipAddress", user.getIpAddress());   

    newUserNotification.add("user", userObject);

    try {
      InetAddress multicastAddress = InetAddress.getByName(hostMulticast);
      MulticastSender ms = MulticastSender.getInstance();
      ms.sendMessage(newUserNotification, multicastAddress, portMulticast);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Método que se invoca cuando un usuario se desconecta del grupo multicast. Se envia notificación a todos
   * los usuarios del grupo.
   * @param user
   */

  private void sendUserDisconnectedNotification(UserModel user) {
    JsonObject disconnectNotification = new JsonObject();
    disconnectNotification.addProperty("action", "user-disconnected");
    disconnectNotification.addProperty("status", "success");
    
    JsonObject userObject = new JsonObject();
    userObject.addProperty("username", user.getUsername());
    userObject.addProperty("port", user.getPort());
    userObject.addProperty("ipAddress", user.getIpAddress());
    
    disconnectNotification.add("user", userObject);
    
    try {
        InetAddress multicastAddress = InetAddress.getByName(hostMulticast);
        MulticastSender ms = MulticastSender.getInstance();
        ms.sendMessage(disconnectNotification, multicastAddress, portMulticast);
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  
  private void showUsers() {
    System.out.println("Usuarios conectados: ");
    for(UserModel user : activeSessions.keySet()) {
      System.out.println("\t" + user.getUsername());
    }
  }
}
