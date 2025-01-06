package service.cards;

import httpServer.server.HeaderMap;
import httpServer.server.Request;
import org.junit.Test;
import service.users.UsersController;

import java.util.ArrayList;
import java.util.List;

public class CardsControllerTest {

    @Test
    public void testgetDeck() {
        Request request = new Request();
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization: Bearer testuser-mtcgToken");
        request.setHeaderMap(headerMap);
        List<String> pathParts = new ArrayList<>();
        pathParts.add("/deck");
        //pathParts.add("testuser");
        request.setPathParts(pathParts);
        //request.setBody("{\"Name\": \"testuser\", \"Bio\": \"testbio\",  \"Image\": \"testimage\"}");
        CardsController cardsController = new CardsController();
        System.out.println(cardsController.getDeck(request).get());
    }

    @Test
    public void testgetDeck2() {
        Request request = new Request();
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization: WRONGTOKEN");
        request.setHeaderMap(headerMap);
        List<String> pathParts = new ArrayList<>();
        pathParts.add("/deck");
        //pathParts.add("testuser");
        request.setPathParts(pathParts);
        //request.setBody("{\"Name\": \"testuser\", \"Bio\": \"testbio\",  \"Image\": \"testimage\"}");
        CardsController cardsController = new CardsController();
        System.out.println(cardsController.getDeck(request).get());
    }

    @Test
    public void testGetCards() {
        Request request = new Request();
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization: Bearer testuser-mtcgToken");
        request.setHeaderMap(headerMap);
        List<String> pathParts = new ArrayList<>();
        pathParts.add("/deck");
        //pathParts.add("testuser");
        request.setPathParts(pathParts);
        //request.setBody("{\"Name\": \"testuser\", \"Bio\": \"testbio\",  \"Image\": \"testimage\"}");
        CardsController cardsController = new CardsController();
        System.out.println(cardsController.getCards(request).get());
    }

    @Test
    public void testGetCardsUNAUTHORIZED() {
        Request request = new Request();
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization: UNAUTHORIZED");
        request.setHeaderMap(headerMap);
        List<String> pathParts = new ArrayList<>();
        pathParts.add("/deck");
        //pathParts.add("testuser");
        request.setPathParts(pathParts);
        //request.setBody("{\"Name\": \"testuser\", \"Bio\": \"testbio\",  \"Image\": \"testimage\"}");
        CardsController cardsController = new CardsController();
        System.out.println(cardsController.getCards(request).get());
    }
}
