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
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicHandlerTest {

    TaskManager inMemoryTaskManager = Manager.getDefault();

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
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


    @BeforeAll
    static void beforeAll() throws IOException{
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    void beforeEach() throws IOException {

        inMemoryTaskManager.clearEpic();

        epic = new Epic("nameEpic" + 1, "descriptionEpic" + 1);
        idEpic = epic.getUid();
        epic.setStartTimeDuration(LocalDateTime.of(2022, 2, 2, 10, 0),
                Duration.ofDays(2));
        inMemoryTaskManager.addEpic(idEpic, epic);
        epic2 = new Epic("nameEpic" + 2, "descriptionEpic" + 2);
        idEpic2 = epic2.getUid();
        epic2.setStartTimeDuration(LocalDateTime.of(2022, 5, 2, 20, 0),
                Duration.ofDays(5));
        inMemoryTaskManager.addEpic(idEpic2, epic2);
        epic3 = new Epic("nameEpic" + 3, "descriptionEpic" + 3);
        idEpic3 = epic3.getUid();
        epic3.setStartTimeDuration(LocalDateTime.of(2022, 3, 2, 12, 0),
                Duration.ofDays(20));
        inMemoryTaskManager.addEpic(idEpic3, epic3);


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
    public void getEpicsTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpResponse<String> response = clientHttp("GET", uri, null);

        Type listType = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> epics = gson.fromJson(response.body(), listType);
        //tasks.stream().forEach(e-> System.out.println(e.getTitle()));
        assertArrayEquals(epics.toArray(),
                inMemoryTaskManager.getAllEpic().values().toArray(),
                "Списки эпиков не совпадают");
    }

    @Test
    public void getEpicByIdTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=" + idEpic2);
        HttpResponse<String> response = clientHttp("GET", uri, null);

        Epic epicDeserialized = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic2, epicDeserialized, "Запрос эпика по идентификатору"
                + "работет некорректно");
    }

    @Test
    public  void addEpicTest() throws IOException, InterruptedException{
        Epic epic = new Epic("nameTask" + 10, "descriptionTask" + 10);
        epic.setStartTimeDuration(LocalDateTime.of(2022, 2, 2,
                10, 0), Duration.ofSeconds(22));

        String epicsSerialized = gson.toJson(epic);
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpResponse<String> response = clientHttp("POST", uri, epicsSerialized);
        Task taskDeserialized = gson.fromJson(response.body(), Epic.class);
        assertEquals("nameTask10", taskDeserialized.getTitle(),
                "Задача не загрузилась");
    }

    @Test
    public  void updateEpicTest() throws IOException, InterruptedException{
        epic3.setTitle("nameEpic" + 100);

        String tasksSerialized = gson.toJson(epic3);
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=" + idEpic3);
        HttpResponse<String> response = clientHttp("POST", uri, tasksSerialized);
        Task taskDeserialized = gson.fromJson(response.body(), Epic.class);
        assertEquals("nameEpic100", taskDeserialized.getTitle(),
                "Задача не обновилась");
    }

    @Test
    public  void deleteEpicByIdTest() throws IOException, InterruptedException{
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=" + idEpic3);
        clientHttp("DELETE", uri, null);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getEpicById(idEpic3));
        System.out.printf("\nТестирование DeleteEpicById() - эпик удален: "
                + exception.getMessage());
    }

    @Test
    public  void deleteAllEpicTest() throws IOException, InterruptedException{
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        clientHttp("DELETE", uri, null);

        assertEquals(0, inMemoryTaskManager.getAllEpic().size(),
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
                    .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        }


        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);


        return response;
    }


}