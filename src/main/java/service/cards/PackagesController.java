package service.cards;

import com.fasterxml.jackson.core.JsonProcessingException;

import dal.UnitOfWork;
import dal.repositories.CardRepository;
import dal.repositories.UserRepository;
import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.server.Request;
import httpServer.server.Response;
import models.Card;
import service.Controller;
import java.util.ArrayList;

public class PackagesController extends Controller {
    public PackagesController() {}

    public Response createPackage(Request request){
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

        try(unitOfWork) {

            if(new UserRepository(unitOfWork).isAdmin(username)){
                ArrayList<Card> cards = this.getObjectMapper().readValue(request.getBody(), this.getObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, Card.class));
                if(new CardRepository(unitOfWork).createPackage(cards)){
                    return new Response(
                            HttpStatus.CREATED,
                            ContentType.JSON,
                            "{ \"message\": \"Package created!\" }"
                    );
                }
            }else{
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ \"message\": \"You are not an admin!\" }"
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

    public Response aquirePackage(Request request){
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
            if(new UserRepository(unitOfWork).subractCoins(username)){
                if(new CardRepository(unitOfWork).buyPackage(username)) {
                    unitOfWork.commitTransaction();
                    return new Response(
                            HttpStatus.CREATED,
                            ContentType.JSON,
                            "{ \"message\": \"Package aquired!\" }"
                    );
                }else return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        "{ \"message\": \"No Packages free to buy! \" }"
                );
            }else {
                unitOfWork.rollbackTransaction();
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ \"message\": \"Not enough money! \" }"
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

}
