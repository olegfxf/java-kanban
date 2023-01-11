package test;

import manager.FileBackedTasksManager;
import manager.InMemoryHistoryManager;
import manager.Manager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    InMemoryHistoryManager historyManager = Manager.getDefaultHistory();
    File file = new File("filewriter.csv");

    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();



    @Test
    public void saveAndLoadFromFile(){

        System.out.println("\nСохраненные");
        Map<Integer, Task> taskList = inMemoryTaskManager.getAllTask();
        System.out.println(taskList);
        Map<Integer, Epic> epicList = inMemoryTaskManager.getAllEpic();
        System.out.println(epicList);
        Map<Integer, Subtask> subtaskList = inMemoryTaskManager.getAllSubtask();
        System.out.println(subtaskList);

        fileBackedTasksManager.saveObjects();       // сохранил все списки в файле
        fileBackedTasksManager.loadObjects(file); // восстановил из файла сохраненные списки

        System.out.println("\nВосстановленные");
        Map<Integer, Task> savedTaskList = inMemoryTaskManager.getAllTask();
        System.out.println(taskList);
        Map<Integer, Epic> savedEpicList = inMemoryTaskManager.getAllEpic();
        System.out.println(epicList);
        Map<Integer, Subtask> savedSubtaskList = inMemoryTaskManager.getAllSubtask();
        System.out.println(subtaskList);

        assertArrayEquals(taskList.values().toArray(),
                savedTaskList.values().toArray(), "Массивы не равны!");
        assertArrayEquals(epicList.values().toArray(),
                savedEpicList.values().toArray(), "Массивы не равны!");
        assertArrayEquals(subtaskList.values().toArray(),
                savedSubtaskList.values().toArray(), "Массивы не равны!");

    }





    @Test
    public void saveAndLoadFromFileHistory(){

        List<Integer> history = new ArrayList<>();
        historyManager.getHistory().stream().forEach(e -> history.add(e.getUid()));

        fileBackedTasksManager.saveObjects();
        fileBackedTasksManager.loadObjects(file);

        List<Integer> savedHistory = FileBackedTasksManager.getHistory();

        assertArrayEquals(history.toArray(),
                savedHistory.toArray(), "Массивы не равны!");
    }

    @Test
    public void saveAndLoadFromFileEmptyTaskList() {

        // корректируются данные из beforeEach() для тестирования пункта ТЗ 7 "a. Пустой список задач."
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();

        Map<Integer, Task> taskList = inMemoryTaskManager.getAllTask();
        Map<Integer, Epic> epicList = inMemoryTaskManager.getAllEpic();
        Map<Integer, Subtask> subtaskList = inMemoryTaskManager.getAllSubtask();

        fileBackedTasksManager.saveObjects();
        fileBackedTasksManager.loadObjects(file);

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
        // корректируются данные из beforeEach() для тестирования пункта ТЗ 7 "b. Эпик без подзадач."
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearSubtaskEpic(idEpic);
        inMemoryTaskManager.removeEpicById(idEpic2);
        inMemoryTaskManager.removeEpicById(idEpic3);


        Map<Integer, Task> taskList = inMemoryTaskManager.getAllTask();
        Map<Integer, Epic> epicList = inMemoryTaskManager.getAllEpic();
        Map<Integer, Subtask> subtaskList = inMemoryTaskManager.getAllSubtask();

        fileBackedTasksManager.saveObjects();
        fileBackedTasksManager.loadObjects(file);

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
        // корректируются данные из beforeEach() для тестирования пункта ТЗ 7  "c. Пустой список истории."
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();
        historyManager.clearAll();

        fileBackedTasksManager.saveObjects();
        fileBackedTasksManager.loadObjects(file);

        List<Integer> savedHistory = FileBackedTasksManager.getHistory();

        assertEquals(0, savedHistory.size(), "Список посещений должен быть пустой.");
    }
}