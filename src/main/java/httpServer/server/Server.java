package httpServer.server;

import httpServer.utils.RequestHandler;
import httpServer.utils.Router;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private Router router;

    public Server(int port, Router router) {
        this.port = port;
        this.router = router;
    }

    public void start() throws IOException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        System.out.println("Server started on port " + port);

        try(ServerSocket serverSocket = new ServerSocket(this.port)) {
            while (true) {
                final Socket clientConnection = serverSocket.accept();
                final RequestHandler socketHandler = new RequestHandler(clientConnection, router);
                executorService.submit(socketHandler);
            }
        }
    }
}
