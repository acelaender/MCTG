package service.battles;

import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.http.Method;
import httpServer.server.Request;
import httpServer.server.Response;
import httpServer.server.Service;
import service.users.SessionController;

public class BattleService implements Service {
    private final BattlesController battlesController;

    public BattleService() {
        this.battlesController = new BattlesController();
    }

    @Override
    public Response handleRequest(Request request)
    {
        if (request.getMethod() == Method.POST) { //BATTLE
            return this.battlesController.battle(request);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }


}
