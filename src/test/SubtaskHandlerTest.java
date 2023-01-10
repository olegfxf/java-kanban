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
import model.Epic;
import model.Subtask;
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

class SubtaskHandlerTest{
    TaskManager inMemoryTaskManager = Manager.getDefault();
    private static final int PORT = 8080;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapterTime())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    static KVServer kvServer;
    HttpTaskServer httpTaskServer;

    Epic epic;
    Epic epic2;
    Epic epic3;
    Integer idEpic;
    Integer idEpic2;
    Integer idEpic3;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Integer idSubtask1;
    Integer idSubtask2;
    Integer idSubtask3;


    @BeforeAll
    static void beforeAll() throws IOException{
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    void beforeEach() throws IOException {

        inMemoryTaskManager.clearSubtask();
        inMemoryTaskManager.clearEpic();

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
        subtask3.setIdEpic(idEpic);
        idSubtask3 = subtask3.getUid();
        subtask3.setIdEpic(idEpic);
        inMemoryTaskManager.addSubtask(idSubtask3, subtask3);


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
    public void getSubtasksTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpResponse<String> response = clientHttp("GET", uri, null);

        Type listType = new TypeToken<List<Subtask>>(){}.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), listType);

        assertArrayEquals(subtasks.toArray(),
                inMemoryTaskManager.getAllSubtask().values().toArray(),
                "Списки подзадач не совпадают");
    }

    @Test
    public void getSubtaskByIdTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=" + idSubtask1);
        HttpResponse<String> response = clientHttp("GET", uri, null);

        Subtask subtaskDeserialized = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask1, subtaskDeserialized, "Запрос подзадачи по идентификатору"
                + "работает некорректно");
    }

    @Test
    public  void addSubtaskTest() throws IOException, InterruptedException{
        Subtask subtask = new Subtask("nameSubtask" + 10, "descriptionSubtask" + 10);
        subtask.setStartTimeDuration(LocalDateTime.of(2022, 2, 2,
                10, 0), Duration.ofSeconds(22));
        subtask.setIdEpic(idEpic);

        String subtaskSerialized = gson.toJson(subtask);
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpResponse<String> response = clientHttp("POST", uri, subtaskSerialized);
        Subtask subtaskDeserialized = gson.fromJson(response.body(), Subtask.class);
        assertEquals("nameSubtask10", subtaskDeserialized.getTitle(),
                "Подзадача не загрузилась");
    }

    @Test
    public  void updateSubtaskTest() throws IOException, InterruptedException{
        subtask3.setTitle("nameSubtask" + 100);
        String subtasksSerialized = gson.toJson(subtask3);

        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=" + idSubtask3);
        HttpResponse<String> response = clientHttp("POST", uri, subtasksSerialized);
        Subtask subtaskDeserialized = gson.fromJson(response.body(), Subtask.class);
        assertEquals("nameSubtask100", subtaskDeserialized.getTitle(),
                "Подзадача не обновилась");
    }

    @Test
    public  void deleteSubtaskByIdTest() throws IOException, InterruptedException{
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=" + idSubtask2);
        clientHttp("DELETE", uri, null);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getSubtaskById(idSubtask2));
        System.out.printf("\nТестирование DeleteSubtaskById() - подзадача удалена: "
                + exception.getMessage());
    }

    @Test
    public  void deleteAllSubtaskTest() throws IOException, InterruptedException{
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        clientHttp("DELETE", uri, null);

        assertEquals(0, inMemoryTaskManager.getAllSubtask().size(),
                "Список подзадач не очищен");
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