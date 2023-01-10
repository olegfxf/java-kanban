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


public class SubtaskHandler extends FileBackedTasksManager implements HttpHandler {
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
                getSubtask(exchange);
                break;
            }
            case GET_TASK_BY_ID: {
                getSubtaskById(exchange);
                break;
            }
            case POST_ADD: {
                addSubtask(exchange);
                break;
            }
            case POST_UPDATE: {
                updateSubtask(exchange);
                break;
            }
            case DELETE_ALL: {
                deleteAllSubtask(exchange);
                break;
            }
            case DELETE_BY_ID: {
                deleteSubtaskById(exchange);
                break;
            }
            case UNKNOWN:
                Write.response(exchange, "Такого эндпоинта не существует", 404);
        }
    }


    public void getSubtask(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = getAllSubtask().values().stream().collect(Collectors.toList());
        String subtaskSerialized = gson.toJson(subtasks);

        Write.response(exchange, subtaskSerialized, 200);
    }

    public void getSubtaskById(HttpExchange exchange) throws IOException {
        Integer idSubtask = 0;
        Optional idTaskOpt = IdFrom.request(exchange);
        if (idTaskOpt.isPresent())
            idSubtask = (Integer) idTaskOpt.get();

        if (getAllSubtask().keySet().contains(idSubtask)) {
            Subtask subtask = getSubtaskById(idSubtask);
            String subtaskSerialized = gson.toJson(subtask);
            Write.response(exchange, subtaskSerialized, 200);
        } else {
            Write.response(exchange,
                    "Подзадача с идентификатором " + idSubtask + " не найдена",
                    404);
        }
    }

    public void addSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String bodyRequest = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
       Subtask subtaskDeserialized = gson.fromJson(bodyRequest, Subtask.class);
        Subtask newSubtask = new Subtask("1", "2");
        Integer idSubtask = newSubtask.getUid();

        subtaskDeserialized.setUid(idSubtask);
        addSubtask(idSubtask, subtaskDeserialized);

        Write.response(exchange, gson.toJson(subtaskDeserialized), 200);
    }


    public void updateSubtask(HttpExchange exchange) throws IOException {
        Integer idSubtask = 0;
        Optional idTaskOpt = IdFrom.request(exchange);
        if (idTaskOpt.isPresent())
            idSubtask = (Integer) idTaskOpt.get();

        if (getAllSubtask().keySet().contains(idSubtask)) {
            InputStream inputStream = exchange.getRequestBody();
            String bodyRequest = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Subtask subtaskDeserialized = gson.fromJson(bodyRequest, Subtask.class);

            subtaskDeserialized.setUid(idSubtask);
            updateSubtaskById(idSubtask, subtaskDeserialized);

            Write.response(exchange, gson.toJson(subtaskDeserialized), 200);
        } else {
            Write.response(exchange, "Подзадача с идентификатором "
                    + idSubtask + "при обновлении не найдена", 404);
        }
    }

    public void deleteAllSubtask(HttpExchange exchange) throws IOException {
        clearSubtask();
        Write.response(exchange, gson.toJson("Все Подзадачи удалены"), 200);
    }

    public void deleteSubtaskById(HttpExchange exchange) throws IOException {
        Integer idSubtask = 0;
        Optional idTaskOpt = IdFrom.request(exchange);
        if (idTaskOpt.isPresent())
            idSubtask = (Integer) idTaskOpt.get();

        if (getAllSubtask().keySet().contains(idSubtask)) {
            removeSubtaskById(idSubtask);
            Write.response(exchange, gson.toJson("Подзадача удалена"), 200);
        } else {
            Write.response(exchange,
                    "Подзадача с идентификатором " + idSubtask + " не найдена",
                    404);
        }
    }



    public Endpoint getEndpoint(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        String requestMethod = exchange.getRequestMethod();

        Optional idTaskOpt = IdFrom.request(exchange);

        if (requestMethod.equals("GET") && pathParts.length == 3
                && pathParts[2].equals("subtask")) {
            if (idTaskOpt.isEmpty())
                return Endpoint.GET_TASKS;
            else
                return Endpoint.GET_TASK_BY_ID;
        }

        if (requestMethod.equals("POST") && pathParts.length == 3
                && pathParts[2].equals("subtask")) {
            if (idTaskOpt.isEmpty())
                return Endpoint.POST_ADD;
            else
                return Endpoint.POST_UPDATE;
        }

        if (requestMethod.equals("DELETE") && pathParts.length == 3
                && pathParts[2].equals("subtask")) {
            if (idTaskOpt.isEmpty()) {
                return Endpoint.DELETE_ALL;
            }
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
