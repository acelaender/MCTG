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
        }/*
        else if (request.getMethod() == Method.GET)
        {
            return this.usersController.getWeatherPerRepository();
            //return this.weatherController.getWeatherPerRepository();
        }
        else if (request.getMethod() == Method.POST)
        {
            return this.usersController.addWeather(request);
        }
        */

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

}