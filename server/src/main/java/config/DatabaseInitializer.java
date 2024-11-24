package config;
import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password NOT NULL
            );
        """;

        String createGroupsTable = """
            CREATE TABLE IF NOT EXISTS groups (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                group_name TEXT UNIQUE NOT NULL,
                multicast_address TEXT NOT NULL,
                port INTEGER NOT NULL
            );
        """;

        String createGroupMembersTable = """
            CREATE TABLE IF NOT EXISTS group_members (
                group_id INTEGER NOT NULL,
                user_id INTEGER NOT NULL,
                PRIMARY KEY (group_id, user_id),
                FOREIGN KEY (group_id) REFERENCES groups (id),
                FOREIGN KEY (user_id) REFERENCES users (id)
            );
        """;
        
        String createMessageTable = """
          CREATE TABLE IF NOT EXISTS messages (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            sender_id INTEGER NOT NULL,
            group_id INTEGER,
            content TEXT,
            file_path TEXT,    -- Ruta al archivo si el mensaje tiene un archivo adjunto
            is_private BOOLEAN NOT NULL,
            recipient_id INTEGER, -- Solo se usa para mensajes privados
            timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (sender_id) REFERENCES users(id),
            FOREIGN KEY (group_id) REFERENCES groups(id)    
          );              
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUsersTable);
            stmt.execute(createGroupsTable);
            stmt.execute(createGroupMembersTable);
            stmt.execute(createMessageTable);

            System.out.println("Tablas creadas o ya existen en la base de datos.");
        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
