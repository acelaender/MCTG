import httpServer.server.Server;
import httpServer.utils.Router;
import service.battles.BattleService;
import service.cards.CardService;
import service.cards.PackageService;
import service.tradings.TradingService;
import service.users.SessionService;
import service.users.UserService;

import java.io.IOException;

public class Main {
    private static Router configureRouter()
    {
        Router router = new Router();
        router.addService("/users", new UserService());
        router.addService("/sessions", new SessionService());
        router.addService("/packages", new PackageService());
        router.addService("/transactions", new PackageService());
        router.addService("/cards", new CardService());
        router.addService("/deck", new CardService());
        router.addService("/stats", new UserService());
        router.addService("/scoreboard", new UserService());
        router.addService("/tradings", new TradingService());
        router.addService("/battles", new BattleService());
        return router;
    }

    public static void main(String[] args){
        Server server = new Server(10001, configureRouter());

        try {
            server.start();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
