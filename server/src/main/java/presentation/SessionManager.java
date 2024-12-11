package presentation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import domain.dto.ChatRoomDto;
import domain.models.UserModel;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class SessionManager {
  private static SessionManager instance;
  private Map<UserModel, List<ChatRoomDto>> activeSessions;
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
   * @param chatRooms Salas de chat
   */
  public void addActiveUser(UserModel userModel, List<ChatRoomDto> chatRooms) {
    lock.lock();
    try {
      activeSessions.put(userModel, chatRooms);
      sendActiveUserList();
      System.out.println("Usuario " + userModel.getUsername() + " conectado del puerto " + userModel.getPort());
    } finally {
      lock.unlock();
    }
  }

  /**
   * Método para eliminar un usuario de la lista de usuarios activos. Se ejecuta
   * cuando un usuario decide cerrar la sesión.
   * @param userModel 
   */
  public void removeActiveUser(UserModel userModel) {
    lock.lock();
    try {
      activeSessions.remove(userModel);
      sendActiveUserList();
      System.out.println("Usuario " + userModel.getUsername() + " deconectado");
    } finally {
      lock.unlock();
    }
  }
  
  /**
   * Método para obtener todos los usuarios que están activos.
   * @return Una lista de usuarios si es que existe un usuario en sesión.
   */
  public List<String> getActiveUserList() {
    if(!activeSessions.isEmpty()) {
      List<String> users = new ArrayList<>();
      for(UserModel userModel : activeSessions.keySet()) {
        users.add(userModel.getUsername());
      }
      
      return users;
    }
    
    return null;
  }
  
  /**
   * Este método se invoca siempre que un usuario inicia sesión. Al iniciar sesión, 
   * el servidor envia la lista de usuarios conectados actualizada.
   */
  private void sendActiveUserList() {
    JsonObject activeUsersList = new JsonObject();
    activeUsersList.addProperty("action", "users-list");
    activeUsersList.addProperty("status", "success");
    JsonArray usersArray = new JsonArray();
    for(UserModel user : activeSessions.keySet()) {
      JsonObject userObject = new JsonObject();
      userObject.addProperty("username", user.getUsername());
      userObject.addProperty("port", user.getPort());
      
      // Agregar el objecto de usuario
      usersArray.add(userObject);
    }
    
    activeUsersList.add("users", usersArray);
    
    try {
      InetAddress multicastAddress = InetAddress.getByName(hostMulticast);
      MulticastSender ms = MulticastSender.getInstance();
      ms.sendMessage(activeUsersList, multicastAddress, portMulticast);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
