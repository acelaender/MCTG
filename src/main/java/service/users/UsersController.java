package service.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.server.Request;
import httpServer.server.Response;
import models.User;
import persistance.DummyPersistanceLayer;
import service.Controller;

public class UsersController extends Controller {
    private DummyPersistanceLayer persistenceLayer;
    public UsersController() {
        this.persistenceLayer = new DummyPersistanceLayer();
    }

    public Response registerUser(Request request){
        try {
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);
            this.persistenceLayer.registerUser(user);
            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    "{ \"message\": \"Success\" }"
            );
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
