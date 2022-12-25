package Test;

import manager.FileBackedTasksManager;
import manager.InMemoryHistoryManager;
import manager.Manager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    InMemoryHistoryManager historyManager = Manager.getDefaultHistory();

    @Test
    void addTask() {
        super.addTask();
        (new File ("filewriter.csv")).delete();
        FileBackedTasksManager.saveToFile();
        assertTrue(Files.exists(Paths.get("filewriter.csv")),
                "Данные в файл не сохранены");
    }

    @Test
    void updateTask() {
        super.updateTask();
        (new File ("filewriter.csv")).delete();
        FileBackedTasksManager.saveToFile();
        assertTrue(Files.exists(Paths.get("filewriter.csv")),
                "Данные в файл не сохранены");
    }

    @Test
    void removeTaskById() {
        super.removeTaskById();
        (new File ("filewriter.csv")).delete();
        FileBackedTasksManager.saveToFile();
        assertTrue(Files.exists(Paths.get("filewriter.csv")),
                "Данные в файл не сохранены");
    }
    @Test
    public void clearTask() {
        super.clearTask();
        FileBackedTasksManager.saveToFile();
        assertTrue(Files.exists(Paths.get("filewriter.csv")),
                "Данные в файл не сохранены");
    }


    /////////////// epic testing /////////////////////////////
    // Для подзадач нужно дополнительно проверить наличие эпика,
    // а для эпика — расчёт статуса.

    @Test
    void addEpic() {
        super.addEpic();
        (new File ("filewriter.csv")).delete();
        FileBackedTasksManager.saveToFile();
        assertTrue(Files.exists(Paths.get("filewriter.csv")),
                "Данные в файл не сохранены");
    }

    @Test
    void updateEpic() {
        super.updateEpic();
        (new File ("filewriter.csv")).delete();
        FileBackedTasksManager.saveToFile();
        assertTrue(Files.exists(Paths.get("filewriter.csv")),
                "Данные в файл не сохранены");
    }

    @Test
    void removeEpicById() {
        super.removeEpicById();
        (new File ("filewriter.csv")).delete();
        FileBackedTasksManager.saveToFile();
        assertTrue(Files.exists(Paths.get("filewriter.csv")),
                "Данные в файл не сохранены");
    }

    @Test
    void addSubtask() {
        super.addSubtask();
        (new File ("filewriter.csv")).delete();
        FileBackedTasksManager.saveToFile();
        assertTrue(Files.exists(Paths.get("filewriter.csv")),
                "Данные в файл не сохранены");

    }

    @Test
    void updateSubtaskById() {
        super.updateSubtaskById();
        (new File ("filewriter.csv")).delete();
        FileBackedTasksManager.saveToFile();
        assertTrue(Files.exists(Paths.get("filewriter.csv")),
                "Данные в файл не сохранены");
    }

    @Test
    void removeSubtaskById() {
        super.removeSubtaskById();
        (new File ("filewriter.csv")).delete();
        FileBackedTasksManager.saveToFile();
        assertTrue(Files.exists(Paths.get("filewriter.csv")),
                "Данные в файл не сохранены");
    }




    @Test
    public void saveAndLoadFromFileHistory(){

        List<Integer> history = new ArrayList<>();
        historyManager.getHistory().stream().forEach(e -> history.add(e.getUid()));

        FileBackedTasksManager.saveToFile();
        FileBackedTasksManager.loadFromFile(new File("filewriter.csv"));

        List<Integer> savedHistory = FileBackedTasksManager.getHistory();

        assertArrayEquals(history.toArray(),
                savedHistory.toArray(), "Массивы не равны!");
    }

    @Test
    public void saveAndLoadFromFileEmptyTaskList() {

        // a. Пустой список задач.
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();

        Map<Integer, Task> taskList = inMemoryTaskManager.getAllTask();
        Map<Integer, Epic> epicList = inMemoryTaskManager.getAllEpic();
        Map<Integer, Subtask> subtaskList = inMemoryTaskManager.getAllSubtask();

        FileBackedTasksManager.saveToFile();
        FileBackedTasksManager.loadFromFile(new File("filewriter.csv"));

        Map<Integer, Task> savedTaskList = inMemoryTaskManager.getAllTask();
        Map<Integer, Epic> savedEpicList = inMemoryTaskManager.getAllEpic();
        Map<Integer, Subtask> savedSubtaskList = inMemoryTaskManager.getAllSubtask();

        assertArrayEquals(taskList.values().toArray(),
                savedTaskList.values().toArray(), "Массивы не равны!");
        assertArrayEquals(epicList.values().toArray(),
                savedEpicList.values().toArray(), "Массивы не равны!");
        assertArrayEquals(subtaskList.values().toArray(),
                savedSubtaskList.values().toArray(), "Массивы не равны!");
    }


    @Test
    void saveAndLoadFromFileEpicListWithoutSubtasks() {
        // b. Эпик без подзадач.
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearSubtaskEpic(idEpic);
        inMemoryTaskManager.removeEpicById(idEpic2);
        inMemoryTaskManager.removeEpicById(idEpic3);


        Map<Integer, Task> taskList = inMemoryTaskManager.getAllTask();
        Map<Integer, Epic> epicList = inMemoryTaskManager.getAllEpic();
        Map<Integer, Subtask> subtaskList = inMemoryTaskManager.getAllSubtask();

        FileBackedTasksManager.saveToFile();
        FileBackedTasksManager.loadFromFile(new File("filewriter.csv"));

        Map<Integer, Task> savedTaskList = inMemoryTaskManager.getAllTask();
        Map<Integer, Epic> savedEpicList = inMemoryTaskManager.getAllEpic();
        Map<Integer, Subtask> savedSubtaskList = inMemoryTaskManager.getAllSubtask();

        assertArrayEquals(taskList.values().toArray(),
                savedTaskList.values().toArray(), "Массивы не равны!");
        assertArrayEquals(epicList.values().toArray(),
                savedEpicList.values().toArray(), "Массивы не равны!");
        assertArrayEquals(subtaskList.values().toArray(),
                savedSubtaskList.values().toArray(), "Массивы не равны!");

    }

    @Test
    void saveAndLoadFromFileEmptyHistoryList() {

        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();
        historyManager.clearAll();

        FileBackedTasksManager.saveToFile();
        FileBackedTasksManager.loadFromFile(new File("filewriter.csv"));

        List<Integer> savedHistory = FileBackedTasksManager.getHistory();

        assertEquals(0, savedHistory.size(), "Список посещений должен быть пустой.");
    }
}