package service.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.server.Request;
import httpServer.server.Response;
import models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.server.Request;
import httpServer.server.Response;
import models.User;
import persistance.DummyPersistanceLayer;
import service.Controller;

public class SessionController extends Controller{
    private DummyPersistanceLayer persistenceLayer;
    public SessionController() {
        this.persistenceLayer = new DummyPersistanceLayer();
    }

    public Response loginUser(Request request){
        try {
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);
            if(this.persistenceLayer.loginUser(user)){
                return new Response(
                        HttpStatus.CREATED,
                        ContentType.JSON,
                        "{ \"token\": \"" + user.getUsername() + "-mctgToken\" }"
                );
            }else {
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ \"message\": \"Invalid Username or password!\" }"
                );
            }
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\": \"Internal Server Error \" }"
        );
    }

}
