package manager;

import model.Task;

import java.util.HashMap;

public interface HistoryManager {
    void add(Task task);

    void remove(int id, TaskManager inMemoryTaskManager);

    InMemoryHistoryManager.CustomLinkedList<Task> getHistory();

    HashMap<Integer, Node<Task>> getHashMapTask();

    void clearAll();

}
