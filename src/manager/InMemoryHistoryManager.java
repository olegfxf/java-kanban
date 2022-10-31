package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    public static List<Task> history = new ArrayList<>();
    int count = 0;

    @Override
    public void add(Task task) {
        if(count > 9) history.remove(9);
        history.add(0, task);
        count++;
    }
    @Override
     public  List<Task> getHistory() {
        return history;
    }



}
