package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.FileBackedTasksManager;
import manager.Manager;
import model.Epic;
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


public class EpicHandler extends FileBackedTasksManager implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    Gson gson = Manager.getGson();


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        model.Endpoint endpoint = Path.getEndpoint(exchange, 3,
                2,"epic");

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
        Integer idEpic = Uid.getUid();

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


}
