package server;

import com.sun.net.httpserver.HttpServer;
import handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    HttpServer httpServer = HttpServer.create();

    public HttpTaskServer() throws IOException {
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/subtask/epic", new EpicSubtaskHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.createContext("/tasks", new PrioritizedTasksHandler());

    }

    public void start() {
//        System.out.println("Запускаем HTTP TASK SERVER  на порту " + PORT);
        httpServer.start();
    }

    public void stop(){
        httpServer.stop(0);
    }

}
