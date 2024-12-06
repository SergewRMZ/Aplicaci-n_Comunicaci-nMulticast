package domain.dto;
public class ChatRoomDto {
  private String roomName;
  private String address;
  private int port;

  public ChatRoomDto() {}
  
  public ChatRoomDto(String roomName, String address, int port) {
    this.roomName = roomName;
    this.address = address;
    this.port = port;
  }

  public String getRoomName() {
    return roomName;
  }

  public String getAddress() {
    return address;
  }
  
  public int getPort() {
    return port;
  }
}
