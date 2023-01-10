package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface TaskManager {

    ////////////Task/////////////////////////////
    void addTask(Integer uid, Task task);

    Task getTaskById(Integer id);

    void updateTask(Integer id, Task task);

    void removeTaskById(Integer id);

    Map<Integer, Task> getAllTask();

    void clearTask();

    ////////////Epic//////////////////////////////////////
    void addEpic(Integer uid, Epic subTask);

    Epic getEpicById(Integer idEpic);

    void updateEpic(Integer idEpic, Epic subTask);

    void removeEpicById(Integer idEpic);

    // получить спиок всех подзадач эпика
    Map<Integer, Epic> getAllEpic();

    void clearEpic();


    ////////////// Subtask /////////////////////////////
    void addSubtask(Integer uid, Subtask subtask);

    Subtask getSubtaskById(Integer id);

    void updateSubtaskById(Integer id, Subtask subtask);

    void removeSubtaskById(Integer id);

    List<Subtask> getAllSubtaskEpic(Integer idEpic);

    void clearSubtaskEpic(Integer idEpic);

    void checkStatus(Integer idEpic);

    Map<Integer, Subtask> getAllSubtask();

    TreeSet<Task> getSortedTask();

    void getPrioritizedTasks ();

    void getPrioritizedTasks2 ();

    void clearSortedTask();
    void clearSubtask();

}
