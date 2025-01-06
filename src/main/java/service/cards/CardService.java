package service.cards;

import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.http.Method;
import httpServer.server.Request;
import httpServer.server.Response;
import httpServer.server.Service;

public class CardService implements Service {
    private final CardsController cardsController;

    public CardService() {this.cardsController = new CardsController();}

    @Override
    public Response handleRequest(Request request) {
        if(request.getMethod() == Method.GET & request.getPathParts().get(0).contains("cards")) {
            return this.cardsController.getCards(request);
        }else if(request.getMethod() == Method.GET & request.getPathParts().get(0).contains("deck")) {
            return this.cardsController.getDeck(request);
        }else if(request.getMethod() == Method.PUT & request.getPathParts().get(0).contains("deck")) {
            return this.cardsController.setDeck(request);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
