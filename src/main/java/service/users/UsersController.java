package service.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import dal.UnitOfWork;
import dal.repositories.UserRepository;
import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.server.Request;
import httpServer.server.Response;
import models.User;
import models.UserData;
import models.UserStats;
import service.Controller;

import java.util.ArrayList;

public class UsersController extends Controller {
    public UsersController() {}

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

    public Response getUserData(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        String username = request.getHeaderMap().getHeader("Authorization");

        if (username != null && username.startsWith("Bearer ")) {
            username = username.substring(7);
            username = username.replace("-mtcgToken", "").trim();
        } else {
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"You must be logged in to do that!\" }"
            );
        }
        if(!username.equals(request.getPathParts().get(1))){
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"You can only see your own account!\" }"
            );
        }

        try(unitOfWork){
            username = request.getPathParts().get(1);
            UserData userData = new UserRepository(unitOfWork).getUserCredentials(username);
            if(userData != null){
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        this.getObjectMapper().writeValueAsString(userData)
                );
            }else{
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\": \"User not found!\" }"
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\": \"Internal Server Error \" }"
        );
    }

    public Response getUserStats(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        String username = request.getHeaderMap().getHeader("Authorization");

        if (username != null && username.startsWith("Bearer ")) {
            username = username.substring(7);
            username = username.replace("-mtcgToken", "").trim();
        } else {
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"You must be logged in to do that!\" }"
            );
        }

        try(unitOfWork){
            UserStats userStats = new UserRepository(unitOfWork).getUserStats(username);
            if(userStats != null){
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        this.getObjectMapper().writeValueAsString(userStats)
                );
            }else{
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\": \"User not found!\" }"
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\": \"Internal Server Error \" }"
        );
    }

    public Response alterUser(Request request){return null;}

    public Response setUserData(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        String username = request.getHeaderMap().getHeader("Authorization");

        if (username != null && username.startsWith("Bearer ")) {
            username = username.substring(7);
            username = username.replace("-mtcgToken", "").trim();
        } else {
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"You must be logged in to do that!\" }"
            );
        }
        if(!username.equals(request.getPathParts().get(1))){
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"You can only alter your own account!\" }"
            );
        }
        try(unitOfWork){
            UserData userData = this.getObjectMapper().readValue(request.getBody(), UserData.class);
            userData.setUsername(username);
            if(new UserRepository(unitOfWork).setUserData(userData)){
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ \"message\": \"Success\" }"
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\": \"Internal Server Error \" }"
        );
    }

    public Response getScoreboard(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            ArrayList<UserStats> scoreBoard = new UserRepository(unitOfWork).getScoreBoard();
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    this.getObjectMapper().writeValueAsString(scoreBoard)
            );
        }catch (Exception e){
            e.printStackTrace();
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\": \"Internal Server Error \" }"
        );
    }


}
