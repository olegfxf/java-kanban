package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.FileBackedTasksManager;
import manager.Manager;
import model.Subtask;
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


public class SubtaskHandler extends FileBackedTasksManager implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    Gson gson = Manager.getGson();


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        model.Endpoint endpoint = Path.getEndpoint(exchange, 3,
                2,"subtask");

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
        Integer idSubtask = Uid.getUid();

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

}
