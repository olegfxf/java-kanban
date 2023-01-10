package test;

import manager.*;
import model.Epic;

import model.Task;
import org.junit.jupiter.api.*;
import server.KVServer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    static KVServer kvServer;

    TaskManager inMemoryTaskManager = Manager.getDefault();
    InMemoryHistoryManager historyManager = Manager.getDefaultHistory();

    Task task, task2, task3;
    Integer idTask, idTask2, idTask3;

    Epic epic, epic2, epic3;
    Integer idEpic, idEpic2, idEpic3;

    @BeforeAll
    static void beforeAll() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();
        historyManager.clearAll();

        task = new Task("nameTask" + 1, "descriptionTask" + 1);
        idTask = task.getUid();
        task.setStartTimeDuration(LocalDateTime.of(2022,12,22,10,0),
                Duration.ofDays(2));
        inMemoryTaskManager.addTask(idTask, task);
        task2 = new Task("nameTask" + 2, "descriptionTask" + 2);
        idTask2 = task2.getUid();
        task2.setStartTimeDuration(LocalDateTime.of(2022,11,25,20,0),
                Duration.ofDays(5));
        inMemoryTaskManager.addTask(idTask2, task2);
        task3 = new Task("nameTask" + 3, "descriptionTask" + 3);
        idTask3 = task3.getUid();
        task3.setStartTimeDuration(LocalDateTime.of(2022,10,25,12,0),
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
        inMemoryTaskManager.getEpicById(idEpic3);

    }
    @AfterEach
    public void afterEach(){
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();
        historyManager.clearAll();
    }

    @AfterAll
    static void afterAll(){
        kvServer.stop();
    }

    @Test
    void add() {

        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();
        historyManager.clearAll();

        task = new Task("nameTask" + 1, "descriptionTask" + 1);
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История посещений отсутствует.");
        assertEquals(1, history.size(), "История посещений " +
                "должна содержать одно посещение.");


        inMemoryTaskManager.clearTask();
        historyManager.clearAll();
        task = new Task("nameTask" + 1, "descriptionTask" + 1);
        historyManager.add(task);
        historyManager.add(task); // дублирование
        final List<Task> history2 = historyManager.getHistory();
        assertEquals(1, history2.size(), "История посещений " +
                "должна содержать одно посещение.");

    }
    @Test
    void remove() {

        int numberId = historyManager.getHistory().size();


        int endId = Integer.parseInt(HistoryToString
                .historyToString(historyManager).split(",")[numberId-1]);
        historyManager.remove(endId);
        assertEquals(numberId-1, historyManager.getHistory().size(),
                "Количество элементов истории не равно " + (numberId-1));


        int beginId = Integer.parseInt(HistoryToString
                .historyToString(historyManager).split(",")[0]);
        historyManager.remove(beginId);
        assertEquals(numberId-2, historyManager.getHistory().size(),
                "Количество элементов истории не равно " + (numberId-2));

        // середина
        int mediumId = Integer.parseInt(HistoryToString
                .historyToString(historyManager).split(",")[(numberId-1)/2]);
        historyManager.remove(mediumId);
        assertEquals(numberId-3, historyManager.getHistory().size(),
                "Количество элементов истории не равно " + (numberId-3));

    }

    @Test
    void getHistory() {
        int numberId = historyManager.getHistory().size();
        assertEquals(6, numberId, "Количество посещений" +
                "в истории посещений не равно " + 6);
    }
}