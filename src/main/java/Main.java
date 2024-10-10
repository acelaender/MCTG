import httpServer.server.Server;
import httpServer.utils.Router;
import service.users.UserService;

import java.io.IOException;

public class Main {
    private static Router configureRouter()
    {
        Router router = new Router();
        router.addService("/users", new UserService());

        return router;
    }

    public static void main(String[] args){
        Server server = new Server(1234, configureRouter());

        try {
            server.start();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
