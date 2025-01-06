package service.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import dal.UnitOfWork;
import dal.repositories.UserRepository;
import httpServer.server.HeaderMap;
import httpServer.server.Request;
import httpServer.server.Response;
import models.User;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.server.Request;
import httpServer.server.Response;
import models.User;
import models.UserData;
import models.UserStats;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import service.users.UsersController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsersControllerTest {

    @Test
    public void testgetScoreboard() {
        System.out.println(new UsersController().getScoreboard(new Request()).get());

    }

    @Test
    public void testsetUsers() {
        Request request = new Request();
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization: Bearer testuser-mtcgToken");
        request.setHeaderMap(headerMap);
        List<String> pathParts = new ArrayList<>();
        pathParts.add("/users");
        pathParts.add("testuser");
        request.setPathParts(pathParts);
        request.setBody("{\"Name\": \"testuser\", \"Bio\": \"testbio\",  \"Image\": \"testimage\"}");
        UsersController usersController = new UsersController();
        System.out.println(usersController.setUserData(request).get());
    }

    @Test
    public void testsetUsersUnAuthorized() {
        Request request = new Request();
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization: UNAUTHORIZED");
        request.setHeaderMap(headerMap);
        List<String> pathParts = new ArrayList<>();
        pathParts.add("/users");
        pathParts.add("testuser");
        request.setPathParts(pathParts);
        request.setBody("{\"Name\": \"testuser\", \"Bio\": \"testbio\",  \"Image\": \"testimage\"}");
        UsersController usersController = new UsersController();
        System.out.println(usersController.setUserData(request).get());
    }

    @Test
    public void testgetUsers() {
        Request request = new Request();
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization: Bearer testuser-mtcgToken");
        request.setHeaderMap(headerMap);
        List<String> pathParts = new ArrayList<>();
        pathParts.add("/users");
        pathParts.add("testuser");
        request.setPathParts(pathParts);
        UsersController usersController = new UsersController();
        System.out.println(usersController.getUserData(request).get());
    }


    @Test
    public void testgetUsers2() {
        Request request = new Request();
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization: UNAUTHORIZED");
        request.setHeaderMap(headerMap);
        List<String> pathParts = new ArrayList<>();
        pathParts.add("/users");
        pathParts.add("testuser");
        request.setPathParts(pathParts);
        UsersController usersController = new UsersController();
        System.out.println(usersController.getUserData(request).get());
    }




}
