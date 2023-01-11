package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.FileBackedTasksManager;
import manager.Manager;
import model.Endpoint;
import model.Task;
import model.Uid;
import util.IdFrom;
import util.Path;
import util.Write;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class TaskHandler extends FileBackedTasksManager implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

       Gson gson = Manager.getGson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Endpoint endpoint = Path.getEndpoint(exchange, 3,
                2,"task");

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
        Optional idTaskOpt = IdFrom.request(exchange);
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
        Integer idTask = Uid.getUid();

        taskDeserialized.setUid(idTask);
        addTask(idTask, taskDeserialized);

        Write.response(exchange, gson.toJson(taskDeserialized), 200);
    }


    public void updateTask(HttpExchange exchange) throws IOException {
        Integer idTask = 0;
        Optional idTaskOpt = IdFrom.request(exchange);
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
        Optional idTaskOpt = IdFrom.request(exchange);
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


}
