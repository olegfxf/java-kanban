package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import manager.*;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.*;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryHandlerTest {
    TaskManager inMemoryTaskManager = Manager.getDefault();
    HistoryManager historyManager = Manager.getDefaultHistory();
    private static final int PORT = 8080;

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    static KVServer kvServer;
    HttpTaskServer httpTaskServer;


    Task task;
    Task task2;
    Task task3;
    Integer idTask;
    Integer idTask2;
    Integer idTask3;


    Epic epic;
    Epic epic2;
    Epic epic3;
    Integer idEpic;
    Integer idEpic2;
    Integer idEpic3;

    Subtask subtask1, subtask2, subtask3;
    Subtask subtask21, subtask22, subtask23;

    Integer idSubtask1, idSubtask2, idSubtask3;
    Integer idSubtask21, idSubtask22, idSubtask23;

    @BeforeAll
    static void beforeAll() throws IOException{
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    void beforeEach() throws IOException {


        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();

        task = new Task("nameTask" + 1, "descriptionTask" + 1);
        idTask = task.getUid();
        task.setStartTimeDuration(LocalDateTime.of(2022, 2, 2, 10, 0),
                Duration.ofDays(2));
        inMemoryTaskManager.addTask(idTask, task);
        task2 = new Task("nameTask" + 2, "descriptionTask" + 2);
        idTask2 = task2.getUid();
        task2.setStartTimeDuration(LocalDateTime.of(2022, 5, 2, 20, 0),
                Duration.ofDays(5));
        inMemoryTaskManager.addTask(idTask2, task2);
        task3 = new Task("nameTask" + 3, "descriptionTask" + 3);
        idTask3 = task3.getUid();
        task3.setStartTimeDuration(LocalDateTime.of(2022, 3, 2, 12, 0),
                Duration.ofDays(20));
        inMemoryTaskManager.addTask(idTask3, task3);

        epic = new Epic("nameEpic" + 1, "descriptionEpic" + 1);
        idEpic = epic.getUid();
        inMemoryTaskManager.addEpic(idEpic, epic);
        epic2 = new Epic("nameEpic" + 2, "descriptionEpic" + 2);
        idEpic2 = epic2.getUid();
        inMemoryTaskManager.addEpic(idEpic2, epic2);
        epic3 = new Epic("nameEpic" + 3, "descriptionEpic" + 3);
        idEpic3 = epic3.getUid();
        inMemoryTaskManager.addEpic(idEpic3, epic3);


        subtask1 = new Subtask("nameSubtask_" + 1, "descriptionSubtask_" + 1);
        idSubtask1 = subtask1.getUid();
        subtask1.setStartTimeDuration(LocalDateTime.of(2000, 4, 22, 10, 0),
                Duration.ofDays(2));
        subtask1.setIdEpic(idEpic);
        inMemoryTaskManager.addSubtask(idSubtask1, subtask1);
        subtask2 = new Subtask("nameSubtask_" + 2, "descriptionSubtask_" + 2);
        subtask2.setStartTimeDuration(LocalDateTime.of(2000, 3, 22, 10, 0),
                Duration.ofDays(2));
        idSubtask2 = subtask2.getUid();
        subtask2.setIdEpic(idEpic);
        inMemoryTaskManager.addSubtask(idSubtask2, subtask2);
        subtask3 = new Subtask("nameSubtask_" + 3, "descriptionSubtask_" + 3);
        subtask3.setStartTimeDuration(LocalDateTime.of(2000, 2, 22, 10, 0),
                Duration.ofDays(2));
        idSubtask3 = subtask3.getUid();
        subtask3.setIdEpic(idEpic);
        inMemoryTaskManager.addSubtask(idSubtask3, subtask3);

        subtask21 = new Subtask("nameSubtask" + 21, "descriptionSubtask" + 21);
        idSubtask21 = subtask21.getUid();
        subtask21.setIdEpic(idEpic2);
        inMemoryTaskManager.addSubtask(idSubtask21, subtask21);
        subtask22 = new Subtask("nameSubtask" + 22, "descriptionSubtask" + 22);
        idSubtask22 = subtask22.getUid();
        subtask22.setIdEpic(idEpic2);
        inMemoryTaskManager.addSubtask(idSubtask22, subtask22);
        subtask23 = new Subtask("nameSubtask" + 23, "descriptionSubtask" + 23);
        idSubtask23 = subtask23.getUid();
        subtask23.setIdEpic(idEpic2);
        inMemoryTaskManager.addSubtask(idSubtask23, subtask23);

        inMemoryTaskManager.getTaskById(idTask2);
        inMemoryTaskManager.getTaskById(idTask);
        inMemoryTaskManager.getTaskById(idTask3);

        inMemoryTaskManager.getTaskById(idTask);
        inMemoryTaskManager.getTaskById(idTask2);

        inMemoryTaskManager.getTaskById(idTask2);
        inMemoryTaskManager.getTaskById(idTask);

        inMemoryTaskManager.getEpicById(idEpic);
        inMemoryTaskManager.getEpicById(idEpic2);

        inMemoryTaskManager.getEpicById(idEpic2);
        inMemoryTaskManager.getEpicById(idEpic);

        inMemoryTaskManager.getEpicById(idEpic2);
        inMemoryTaskManager.getEpicById(idEpic3);

        inMemoryTaskManager.getSubtaskById(idSubtask3);
        inMemoryTaskManager.getSubtaskById(idSubtask1);
        inMemoryTaskManager.getSubtaskById(idSubtask2);


        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }


    @AfterEach
    void afterEach() {
        httpTaskServer.stop();
    }

    @AfterAll
    static void afterAll(){
        kvServer.stop();
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpResponse<String> response = clientHttp("GET", uri, null);

        Type listType = new TypeToken<List<Integer>>(){}.getType();
        List<Integer> history = gson.fromJson(response.body(), listType);

        assertArrayEquals(history.toArray(),
                historyManager.getHistory().stream().map(e->e.getUid()).toArray(),
                "Списки историй посещений задач не совпадают");
    }


    public HttpResponse<String> clientHttp(String method, URI uri,
                                           String requestBody) throws IOException, InterruptedException {
        HttpRequest request = null;
        if (method.equals("GET"))
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);


        return response;
    }

}