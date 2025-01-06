package service.users;

import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.http.Method;
import httpServer.server.Request;
import httpServer.server.Response;
import httpServer.server.Service;

public class UserService implements Service{
    private final UsersController usersController;

    public UserService() {
        this.usersController = new UsersController();
    }

    @Override
    public Response handleRequest(Request request)
    {
        if (request.getMethod() == Method.POST) { //REGISTER USER
            return this.usersController.registerUser(request);
        }else if(request.getMethod() == Method.GET && request.getPathParts().size() == 2) {
            //TODO see if username is in path && get that username
            return this.usersController.getUserData(request);
        }else if(request.getMethod() == Method.PUT){
            return this.usersController.setUserData(request);
        }else if(request.getMethod() == Method.GET && request.getPathParts().get(0).equals("stats")){
            return this.usersController.getUserStats(request);
        }else if(request.getMethod() == Method.GET && request.getPathParts().get(0).equals("scoreboard")){
            return this.usersController.getScoreboard(request);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

}