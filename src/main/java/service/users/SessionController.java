package service.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import dal.UnitOfWork;
import dal.repositories.UserRepository;
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
import service.Controller;

public class SessionController extends Controller{
    public SessionController() {

    }

    public Response loginUser(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);
            if(new UserRepository(unitOfWork).login(user)){
                return new Response(
                        HttpStatus.OK,
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
