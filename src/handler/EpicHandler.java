package handler;

import adapter.DurationAdapter;
import adapter.LocalDateAdapterTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import manager.FileBackedTasksManager;
import model.*;
import util.IdFrom;
import util.Write;


public class EpicHandler extends FileBackedTasksManager implements HttpHandler {
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
            case GET_TASKS: {
                getEpics(exchange);
                break;
            }
            case GET_TASK_BY_ID: {
                getEpicById(exchange);
                break;
            }
            case POST_ADD: {
                addEpic(exchange);
                break;
            }
            case POST_UPDATE: {
                updateEpic(exchange);
                break;
            }
            case DELETE_ALL: {
                deleteAllEpic(exchange);
                break;
            }
            case DELETE_BY_ID: {
                deleteEpicById(exchange);
                break;
            }
            case UNKNOWN:
                Write.response(exchange, "Такого эндпоинта не существует", 404);
        }
    }


    public void getEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = getAllEpic().values().stream().collect(Collectors.toList());
        String epicsSerialized = gson.toJson(epics);

        Write.response(exchange, epicsSerialized, 200);
    }

    public void getEpicById(HttpExchange exchange) throws IOException {
        Integer idEpic = 0;
        Optional idEpicOpt = IdFrom.request(exchange);
        if (idEpicOpt.isPresent())
            idEpic = (Integer) idEpicOpt.get();

        if (getAllEpic().keySet().contains(idEpic)) {
            Epic epic = getEpicById(idEpic);
            String epicSerialized = gson.toJson(epic);
            Write.response(exchange, epicSerialized, 200);
        } else {
            Write.response(exchange,
                    "Эпик с идентификатором " + idEpic + " не найден",
                    404);
        }
    }

    public void addEpic(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String bodyRequest = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        Epic epicDeserialized = gson.fromJson(bodyRequest, Epic.class);
        Epic newEpic = new Epic("1", "2");
        Integer idEpic = newEpic.getUid();

        epicDeserialized.setUid(idEpic);
        addEpic(idEpic, epicDeserialized);

        Write.response(exchange, gson.toJson(epicDeserialized), 200);
    }


    public void updateEpic(HttpExchange exchange) throws IOException {
        Integer idEpic = 0;
        Optional idTaskOpt = IdFrom.request(exchange);
        if (idTaskOpt.isPresent())
            idEpic = (Integer) idTaskOpt.get();

        if (getAllEpic().keySet().contains(idEpic)) {
            InputStream inputStream = exchange.getRequestBody();
            String bodyRequest = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Epic epicDeserialized = gson.fromJson(bodyRequest, Epic.class);

            epicDeserialized.setUid(idEpic);
            updateEpic(idEpic, epicDeserialized);

            Write.response(exchange, gson.toJson(epicDeserialized), 200);
        } else {
            Write.response(exchange, "Эпик с идентификатором "
                    + idEpic + " при обновлении не найден", 404);
        }
    }

    public void deleteAllEpic(HttpExchange exchange) throws IOException {
        clearEpic();
        Write.response(exchange, gson.toJson("Все эпики удалены"), 200);
    }

    public void deleteEpicById(HttpExchange exchange) throws IOException {
        Integer idEpic = 0;
        Optional idTaskOpt = IdFrom.request(exchange);
        if (idTaskOpt.isPresent())
            idEpic = (Integer) idTaskOpt.get();

        if (getAllEpic().keySet().contains(idEpic)) {
            removeEpicById(idEpic);
            Write.response(exchange, gson.toJson("Эпик удален"), 200);
        } else {
            Write.response(exchange,
                    "Эпик с идентификатором " + idEpic + " не найден",
                    404);
        }
    }


    public Endpoint getEndpoint(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        String requestMethod = exchange.getRequestMethod();

        Optional idTaskOpt = IdFrom.request(exchange);

        if (requestMethod.equals("GET") && pathParts.length == 3
                && pathParts[2].equals("epic")) {
            if (idTaskOpt.isEmpty())
                return Endpoint.GET_TASKS;
            else
                return Endpoint.GET_TASK_BY_ID;
        }

        if (requestMethod.equals("POST") && pathParts.length == 3
                && pathParts[2].equals("epic")) {
            if (idTaskOpt.isEmpty())
                return Endpoint.POST_ADD;
            else
                return Endpoint.POST_UPDATE;
        }

        if (requestMethod.equals("DELETE") && pathParts.length == 3
                && pathParts[2].equals("epic")) {
            if (idTaskOpt.isEmpty())
                return Endpoint.DELETE_ALL;
            else
                return Endpoint.DELETE_BY_ID;
        }

        return Endpoint.UNKNOWN;
    }



    enum Endpoint {
        GET_TASKS, GET_TASK_BY_ID, POST_ADD,  POST_UPDATE,
        DELETE_ALL, DELETE_BY_ID, UNKNOWN
    }

}
