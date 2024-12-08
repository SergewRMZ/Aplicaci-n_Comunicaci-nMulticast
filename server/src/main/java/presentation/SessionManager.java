package presentation;

import com.google.gson.JsonObject;
import domain.dto.ChatRoomDto;
import domain.models.UserModel;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class SessionManager {
  private static SessionManager instance;
  private Map<UserModel, List<ChatRoomDto>> activeSessions;
  private final ReentrantLock lock = new ReentrantLock();
  
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
      System.out.println("Usuario " + userModel.username + " conectado");
      notifyGroupMembers(userModel, chatRooms);
    } finally {
      lock.unlock();
    }
  }

  public void removeActiveUser(UserModel userModel) {
    lock.lock();
    try {
      activeSessions.remove(userModel);
      System.out.println("Usuario " + userModel.username + "deconectado");
    } finally {
      lock.unlock();
    }
  }
  
  private void notifyGroupMembers (UserModel userModel, List<ChatRoomDto> chatRooms) {
    for(ChatRoomDto chatRoom: chatRooms) {
      try {
        JsonObject notification = new JsonObject();
        notification.addProperty("action", "notify");
        notification.addProperty("status", "user-connected");
        notification.addProperty("username", userModel.username);

        InetAddress multicastAddress = InetAddress.getByName(chatRoom.getAddress());
        int multicastPort = chatRoom.getPort();
        
        MulticastSocket socket = new MulticastSocket();
        MulticastSender ms = new MulticastSender(socket);
        ms.sendMessage(notification, multicastAddress, multicastPort);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
