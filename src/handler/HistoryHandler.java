package handler;

import adapter.DurationAdapter;
import adapter.LocalDateAdapterTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryHistoryManager;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryHandler extends InMemoryHistoryManager implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapterTime())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);

        switch (endpoint) {
            case GET_HISTORY: {
                getHistory(exchange);
                break;
            }
            case UNKNOWN:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    public void getHistory(HttpExchange exchange) throws IOException {
        List<Integer> history = getHistory().stream().map(e->e.getUid())
                .collect(Collectors.toList());

        String historySerialized = gson.toJson(history);
        writeResponse(exchange, historySerialized, 200);
    }

    public Endpoint getEndpoint(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        String requestMethod = exchange.getRequestMethod();

        if (requestMethod.equals("GET") && pathParts.length == 3
                && pathParts[2].equals("history"))
            return Endpoint.GET_HISTORY;
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
        GET_HISTORY, UNKNOWN
    }
}