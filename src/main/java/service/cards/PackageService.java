package service.cards;

import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.http.Method;
import httpServer.server.Request;
import httpServer.server.Response;
import httpServer.server.Service;

public class PackageService implements Service {
    private final PackagesController packagesController;

    public PackageService() {this.packagesController = new PackagesController();}

    @Override
    public Response handleRequest(Request request) {
        if(request.getPathParts().get(0).equals("transactions") && request.getMethod() == Method.POST) {
            return this.packagesController.aquirePackage(request);
        }else if(request.getMethod() == Method.POST){
            return this.packagesController.createPackage(request);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
