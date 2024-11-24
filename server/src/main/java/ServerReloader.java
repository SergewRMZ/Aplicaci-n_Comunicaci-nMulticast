import java.nio.file.*;
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import presentation.Server;

public class ServerReloader {
    public static void startWatching() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get("src/main/java/presentation");
        System.out.println("Ruta: " + path);
        
        registerAll(path, watchService);
        path.register(
            watchService, 
            StandardWatchEventKinds.ENTRY_MODIFY,
            StandardWatchEventKinds.ENTRY_CREATE, 
            StandardWatchEventKinds.ENTRY_DELETE
        );

        System.out.println("Watcher iniciado, supervisando cambios...");

        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println("Cambio detectado en: " + event.context());
                restartServer();
            }
            boolean valid = key.reset();
            if(!valid) { System.out.println("Clave no válida"); break;}
        }
    }
    
    private static void registerAll(Path start, WatchService watchService) throws IOException {
        // Registra el directorio inicial
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                file.getParent().register(watchService, 
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE);
                return FileVisitResult.CONTINUE;
            }
             
            public FileVisitResult visitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dir.register(watchService, 
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void restartServer() {
        System.out.println("Reiniciando servidor...");
        new Thread(() -> {
          Server.restartServer();
          Server server = Server.getInstanceServer();
          server.listen();
        }).start();
    }
}
