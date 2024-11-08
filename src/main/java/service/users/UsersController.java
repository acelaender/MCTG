package service.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import dal.UnitOfWork;
import dal.repositories.UserRepository;
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
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);
            if(new UserRepository(unitOfWork).registerUser(user)){
                return new Response(
                        HttpStatus.CREATED,
                        ContentType.JSON,
                        "{ \"message\": \"Success\" }"
                );
            }else {
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        "{ \"message\": \"User exists!\" }"
                );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\": \"Internal Server Error \" }"
        );
    }

}
