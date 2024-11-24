
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
  private static final String DB_URL = "jdbc:sqlite:chat.db";
  private static Connection connection;
  
  public static Connection getConnection() {
    if (connection == null) {
      try {
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        
        // Permitir varios lectores y un solo escritor, evitando bloqueos en operaciones concurrentes o largas
        stmt.execute("PRAGMA journal_mode = WAL");
        
        // Si una trasacción intenta acceder a un archivo bloqueado, espera 5 segundos.
        stmt.execute("PRAGMA busy_timeout = 5000; ");
        System.out.println("Conexión exitósa a la base de datos");
      } catch (SQLException e) {
        System.err.println("Error al conectar con la base de datos: " + e.getMessage());
      }
    }
    return connection;
  }
}
