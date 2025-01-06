package service.tradings;

import com.fasterxml.jackson.core.JsonProcessingException;
import dal.UnitOfWork;
import dal.repositories.TradingRepository;
import dal.repositories.UserRepository;
import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.server.Request;
import httpServer.server.Response;
import models.TradingDeal;
import models.User;
import service.Controller;

import java.util.ArrayList;


public class TradingsController extends Controller {
    public TradingsController() {}

    public Response createTrade(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            TradingDeal tradingDeal = this.getObjectMapper().readValue(request.getBody(), TradingDeal.class);
            new TradingRepository(unitOfWork).createDeal(tradingDeal);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{ \"message\": \"Deal created!\" }"
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

    public Response deleteTrade(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try (unitOfWork){

            String id = request.getPathParts().get(1);

            if(new TradingRepository(unitOfWork).deleteDeal(id)){
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ \"message\": \"Success\" }"
                );
            }else {
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        "{ \"message\": \"Something went wrong!\" }"
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

    public Response makeTrade(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try (unitOfWork) {
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

            String id = request.getPathParts().get(1);

            String tradedCard = this.getObjectMapper().readValue(request.getBody(), String.class);

            if(new TradingRepository(unitOfWork).makeDeal(id, tradedCard, username)){
                return new Response(
                        HttpStatus.ACCEPTED,
                        ContentType.JSON,
                        "{ \"message\": \"Deal successful!\" }"
                );
            }else{
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\": \"Deal not found or card has too little damage\" }"
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

    public Response getTrades(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            ArrayList<TradingDeal> deals = new TradingRepository(unitOfWork).getTrades();
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    this.getObjectMapper().writeValueAsString(deals)
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
