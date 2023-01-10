package handler;

import adapter.DurationAdapter;
import adapter.LocalDateAdapterTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import manager.FileBackedTasksManager;

import model.*;
import util.Write;


public class TaskHandler extends FileBackedTasksManager implements HttpHandler {
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
                getTasks(exchange);
                break;
            }
            case GET_TASK_BY_ID: {
                getTaskById(exchange);
                break;
            }
            case POST_ADD: {
                addTask(exchange);
                break;
            }
            case POST_UPDATE: {
                updateTask(exchange);
                break;
            }
            case DELETE_ALL: {
                deleteAllTask(exchange);
                break;
            }
            case DELETE_BY_ID: {
                deleteTaskById(exchange);
                break;
            }
            case UNKNOWN:
                Write.response(exchange, "Такого эндпоинта не существует", 404);
        }
    }


    public void getTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = getAllTask().values().stream().collect(Collectors.toList());
        String tasksSerialized = gson.toJson(tasks);

        Write.response(exchange, tasksSerialized, 200);
    }

    public void getTaskById(HttpExchange exchange) throws IOException {
        Integer idTask = 0;
        Optional idTaskOpt = getTaskId(exchange);
        if (idTaskOpt.isPresent())
            idTask = (Integer) idTaskOpt.get();

        if (getAllTask().keySet().contains(idTask)) {
            Task task = getTaskById(idTask);
            String taskSerialized = gson.toJson(task);
            Write.response(exchange, taskSerialized, 200);
        } else {
            Write.response(exchange,
                    "Задача с идентификатором " + idTask + " не найдена",
                    404);
        }
    }

    public void addTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String bodyRequest = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        Task taskDeserialized = gson.fromJson(bodyRequest, Task.class);
        Task newTask = new Task("1", "2");
        Integer idTask = newTask.getUid();

        taskDeserialized.setUid(idTask);
        addTask(idTask, taskDeserialized);

        Write.response(exchange, gson.toJson(taskDeserialized), 200);
    }


    public void updateTask(HttpExchange exchange) throws IOException {
        Integer idTask = 0;
        Optional idTaskOpt = getTaskId(exchange);
        if (idTaskOpt.isPresent())
            idTask = (Integer) idTaskOpt.get();

        if (getAllTask().keySet().contains(idTask)) {
            InputStream inputStream = exchange.getRequestBody();
            String bodyRequest = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task taskDeserialized = gson.fromJson(bodyRequest, Task.class);

            taskDeserialized.setUid(idTask);
            updateTask(idTask, taskDeserialized);

            Write.response(exchange, gson.toJson(taskDeserialized), 200);
        } else {
            Write.response(exchange, "Задача с идентификатором "
                            + idTask + "при обновлении не найдена", 404);
        }
    }

    public void deleteAllTask(HttpExchange exchange) throws IOException {
        clearTask();
        Write.response(exchange, gson.toJson("Все задачи удалены"), 200);
    }

    public void deleteTaskById(HttpExchange exchange) throws IOException {
        Integer idTask = 0;
        Optional idTaskOpt = getTaskId(exchange);
        if (idTaskOpt.isPresent())
            idTask = (Integer) idTaskOpt.get();

        if (getAllTask().keySet().contains(idTask)) {
            removeTaskById(idTask);
            Write.response(exchange, gson.toJson("Задача удалена"), 200);
        } else {
            Write.response(exchange,
                    "Задача с идентификатором " + idTask + " не найдена",
                    404);
        }
    }










    public Endpoint getEndpoint(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        String requestMethod = exchange.getRequestMethod();



        Optional idTaskOpt = getTaskId(exchange);

        if (requestMethod.equals("GET") && pathParts.length == 3
                && pathParts[2].equals("task")) {
            if (idTaskOpt.isEmpty())
                return Endpoint.GET_TASKS;
            else
                return Endpoint.GET_TASK_BY_ID;
        }

        if (requestMethod.equals("POST") && pathParts.length == 3
                && pathParts[2].equals("task")) {
            if (idTaskOpt.isEmpty())
                return Endpoint.POST_ADD;
            else
                return Endpoint.POST_UPDATE;
        }

        if (requestMethod.equals("DELETE") && pathParts.length == 3
                && pathParts[2].equals("task")) {
            if (idTaskOpt.isEmpty())
                return Endpoint.DELETE_ALL;
            else
                return Endpoint.DELETE_BY_ID;
        }

        return Endpoint.UNKNOWN;
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



    enum Endpoint {
        GET_TASKS, GET_TASK_BY_ID, POST_ADD,  POST_UPDATE,
        DELETE_ALL, DELETE_BY_ID, UNKNOWN
    }

}
