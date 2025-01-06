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

public class CardsController extends Controller {
    public CardsController() {}

    public Response getCards(Request request) {
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

        try (unitOfWork){
            ArrayList<Card> cards = new CardRepository(unitOfWork).getCards(username);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    this.getObjectMapper().writeValueAsString(cards)
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\": \"Internal Server Error!\" }"

        );
    }

    public Response getDeck(Request request) {
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

        try (unitOfWork){
            ArrayList<Card> cards = new CardRepository(unitOfWork).getDeck(username);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    this.getObjectMapper().writeValueAsString(cards)
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\": \"Internal Server Error!\" }"

        );
    }

    public Response setDeck(Request request) {
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

        try (unitOfWork){

            ArrayList<Card> cards = this.getObjectMapper().readValue(request.getBody(), this.getObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, Card.class));
            if(cards.size() != 4){
                return new Response(
                        HttpStatus.BAD_REQUEST,
                        ContentType.JSON,
                        "{ \"message\": \"Deck has to consist of 4 cards!\" }"
                );
            }
            if(new CardRepository(unitOfWork).setDeck(username, cards)){
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ \"message\": \"Deck successfully updated!\" }"
                );
            }else{
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ \"message\": \"Cards are not in your inventory!\" }"
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\": \"Internal Server Error!\" }"

        );


    }

}
