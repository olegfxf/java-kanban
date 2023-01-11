package handler;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import manager.InMemoryTaskManager;
import manager.Manager;
import model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


public class PrioritizedTasksHandler extends InMemoryTaskManager implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    Gson gson = Manager.getGson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);

        switch (endpoint) {
            case GET_PRIORITIZED_TASK: {
                getPrioritizedTasks(exchange);
                break;
            }
            case UNKNOWN:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    public void getPrioritizedTasks(HttpExchange exchange) throws IOException {
        List<Task> prioritizedTasks = getSortedTask().stream().collect(Collectors.toList());

        String prioritizedTasksSerialized = gson.toJson(prioritizedTasks);
        writeResponse(exchange, prioritizedTasksSerialized, 200);
    }


    public Endpoint getEndpoint(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        String requestMethod = exchange.getRequestMethod();



        if (requestMethod.equals("GET") && pathParts.length == 2
                && pathParts[1].equals("tasks"))
            return Endpoint.GET_PRIORITIZED_TASK;
        else
            return Endpoint.UNKNOWN;
    }



    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    enum Endpoint {
        GET_PRIORITIZED_TASK, UNKNOWN
    }
}
