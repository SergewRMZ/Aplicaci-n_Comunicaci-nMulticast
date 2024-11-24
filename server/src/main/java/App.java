import presentation.Server;

public class App {
  public static void main(String[] args) {
    Server server = Server.getInstanceServer();
    server.listen();
  }
}
