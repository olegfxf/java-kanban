package manager;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    //public static List<Task> history = new ArrayList<>();
    public static LinkedList history = new LinkedList();
    final int maxSize = 10;

    @Override
    public void add(Task task) {
        if(history.size() == maxSize) {
            history.removeLast();
        }
        history.addFirst(task);
    }
    @Override
     public  List<Task> getHistory() {
        return history;
    }



}
