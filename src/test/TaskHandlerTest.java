package test;

import adapter.DurationAdapter;
import adapter.LocalDateAdapterTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import server.HttpTaskServer;
import server.KVServer;
import manager.Manager;
import manager.TaskManager;

import model.Task;
import org.junit.jupiter.api.*;

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

class TaskHandlerTest {
    TaskManager inMemoryTaskManager = Manager.getDefault();

    private static final int PORT = 8080;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapterTime())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    static KVServer kvServer;
    HttpTaskServer httpTaskServer;


    Task task;
    Task task2;
    Task task3;
    Integer idTask;
    Integer idTask2;
    Integer idTask3;


    @BeforeAll
    static void beforeAll() throws IOException {
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


        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }


    @AfterEach
    void afterEach() {
        httpTaskServer.stop();
    }

    @AfterAll
    static void afterAll() {
        kvServer.stop();
    }


    @Test
    public void getTasksTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpResponse<String> response = clientHttp("GET", uri, null);

        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), listType);
//      tasks.stream().forEach(e-> System.out.println(e.getTitle()));
        assertArrayEquals(tasks.toArray(),
                inMemoryTaskManager.getAllTask().values().toArray(),
                "Списки задач не совпадают");
    }

    @Test
    public void getTaskByIdTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=" + idTask2);
        HttpResponse<String> response = clientHttp("GET", uri, null);

        Task taskDeserialized = gson.fromJson(response.body(), Task.class);
        assertEquals(task2, taskDeserialized, "Запрос задачи по идентификатору"
                + "работет некорректно");
    }

    @Test
    public void addTaskTest() throws IOException, InterruptedException {
        Task task = new Task("nameTask" + 10, "descriptionTask" + 10);
        task.setStartTimeDuration(LocalDateTime.of(2022, 2, 2,
                10, 0), Duration.ofSeconds(22));

        String tasksSerialized = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpResponse<String> response = clientHttp("POST", uri, tasksSerialized);
        Task taskDeserialized = gson.fromJson(response.body(), Task.class);
        assertEquals("nameTask10", taskDeserialized.getTitle(),
                "Задача не загрузилась");
    }

    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        task3.setTitle("nameTask" + 100);

        String tasksSerialized = gson.toJson(task3);
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=" + idTask3);
        HttpResponse<String> response = clientHttp("POST", uri, tasksSerialized);
        Task taskDeserialized = gson.fromJson(response.body(), Task.class);
        assertEquals("nameTask100", taskDeserialized.getTitle(),
                "Задача не обновилась");
    }

    @Test
    public void deleteTaskByIdTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=" + idTask2);
        clientHttp("DELETE", uri, null);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getTaskById(idTask2));
        System.out.printf("\nТестирование DeleteTaskById() - задача удалена: "
                + exception.getMessage());
    }

    @Test
    public void deleteAllTaskTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");
        clientHttp("DELETE", uri, null);

        assertEquals(0, inMemoryTaskManager.getAllTask().size(),
                "Список задач не очищен");
    }


    public HttpResponse<String> clientHttp(String method, URI uri,
                                           String requestBody) throws IOException, InterruptedException {
        HttpRequest request = null;
        if (method.equals("GET"))
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
        else if (method.equals("POST"))
            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(uri)
                    .build();
        else if (method.equals("DELETE")) {
            request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(uri)
                    .build();
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
//        System.out.println("Код ответа: " + response.statusCode());
//        System.out.println("Тело ответа: " + response.body());

        return response;
    }

}