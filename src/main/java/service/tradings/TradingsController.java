package service.tradings;

import com.fasterxml.jackson.core.JsonProcessingException;
import dal.UnitOfWork;
import dal.repositories.TradingRepository;
import dal.repositories.UserRepository;
import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.server.Request;
import httpServer.server.Response;
import models.User;
import service.Controller;



public class TradingsController {
    public TradingsController() {}

    public Response createTrade(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            //TODO make trade of body

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
            //TODO get ID from Body
            String id = "TESTID";
            if(new TradingRepository(unitOfWork).deleteDeal(id)){
                return new Response(
                        HttpStatus.CREATED,
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
            //TODO get ID from Body and Card from body
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
