package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    ////////////Task/////////////////////////////
    void addTask(Integer uid, Task task);

    Task getTaskById(Integer id);

    void updateTask(Integer id, Task task);

    void removeTaskById(Integer id);

    HashMap<Integer, Task> getAllTask();

    void clearTask();

    ////////////Epic//////////////////////////////////////
    void addEpic(Integer uid, Epic subTask);

    Epic getEpicById(Integer idEpic);

    void updateEpic(Integer idEpic, Epic subTask);

    void removeEpicById(Integer idEpic);

    // получить спиок всех подзадач эпика
    HashMap<Integer, Epic> getAllEpic();

    void clearEpic();


    ////////////// Subtask /////////////////////////////
    void addSubtask(Integer uid, Subtask subtask);

    Subtask getSubtaskById(Integer id);

    void updateSubtaskById(Integer id, Subtask subtask);

    void removeSubtaskById(Integer id);

    ArrayList<Subtask> getAllSubtaskEpic(Integer idEpic);

    void clearSubtaskEpic(Integer idEpic);

    void checkStatus(Integer idEpic);

    InMemoryHistoryManager getInMemoryHistoryManager();

}
