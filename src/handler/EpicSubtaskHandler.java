package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.FileBackedTasksManager;
import manager.Manager;
import model.Subtask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EpicSubtaskHandler extends FileBackedTasksManager implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
        Gson gson = Manager.getGson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);

        switch (endpoint) {
            case GET_SUBTASKS_EPIC: {
                getEpicSubtaskById(exchange);
                break;
            }
            case UNKNOWN:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    public void getEpicSubtaskById(HttpExchange exchange) throws IOException {
        Integer idEpic1 = 0;
        Optional idEpicOpt = getTaskId(exchange);
        if (idEpicOpt.isPresent())
            idEpic1 = (Integer) idEpicOpt.get();

        final Integer idEpic = idEpic1;

        if (getAllEpic().keySet().contains(idEpic)) {
            List<Subtask> subtasks = getAllSubtask().values().stream()
                    .filter(e-> e.getIdEpic()== idEpic).collect(Collectors.toList());
            String subtaskSerialized = gson.toJson(subtasks);

            writeResponse(exchange, subtaskSerialized, 200);
        } else {
            writeResponse(exchange,
                    "Эпик с идентификатором " + idEpic + " не найден",
                    404);
        }
    }

    public EpicSubtaskHandler.Endpoint getEndpoint(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        String requestMethod = exchange.getRequestMethod();

        Optional idTaskOpt = getTaskId(exchange);

        if (requestMethod.equals("GET") && pathParts.length == 4
                && pathParts[3].equals("epic")) {
            if (idTaskOpt.isEmpty())
                return EpicSubtaskHandler.Endpoint.UNKNOWN;
            else
                return EpicSubtaskHandler.Endpoint.GET_SUBTASKS_EPIC;
        }
        return null;
    }


    private Optional<Integer> getTaskId(HttpExchange exchange) {
        URI requestedUri = exchange.getRequestURI();

        if (requestedUri.toString().contains("id=") ) {
            int indexId = requestedUri.toString().indexOf("id=") + "id=".length();
            return Optional.of(Integer.parseInt(requestedUri
                    .toString().substring(indexId)));
        }
        else
            return Optional.empty();
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
        GET_SUBTASKS_EPIC, UNKNOWN
    }
}
