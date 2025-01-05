package service.tradings;

import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.http.Method;
import httpServer.server.Request;
import httpServer.server.Response;
import httpServer.server.Service;



public class TradingService implements Service {
    private final TradingsController tradingsController;

    public TradingService(TradingsController tradingsController) {
        this.tradingsController = tradingsController;
    }

    @Override
    public Response handleRequest(Request request){
        if(request.getMethod() == Method.POST){ //Make new Trade
            return this.tradingsController.createTrade(request);
        }else if(request.getMethod() == Method.DELETE){
            return this.tradingsController.deleteTrade(request);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }


}
