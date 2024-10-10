package httpServer.utils;

import java.net.Socket;

public class RequestHandler implements Runnable {
    private Socket clientSocket;
    private Router router;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;


}
