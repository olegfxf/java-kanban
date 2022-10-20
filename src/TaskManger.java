import java.util.HashMap;

public class TaskManger {
    HashMap<Integer, Task> listTask = new HashMap<>();

    HashMap<Integer, Epic> listEpic = new HashMap<>();


    public void addTask(Integer uid, Task task) {
        listTask.put(uid, task);
    }

    public void clearTask() {
        listTask.clear();
    }

    public void removeTaskById(Integer id) {
        listTask.remove(id);
    }

    public Task getTaskById(Integer id) {
        return listTask.get(id);
    }

    public HashMap<Integer, Task> getAllTask() {
        return listTask;
    }

    public void updateTask(Integer id, Task task) {
        listTask.put(id, task);
    }


    public void addEpic(Integer uid, Epic subTask) {
        listEpic.put(uid, subTask);
    }

    public void clearEpic() {
        listEpic.clear();
    }

    public void removeEpicById(Integer id) {
        listEpic.remove(id);
    }

    public Epic getEpicById(Integer id) {
        return listEpic.get(id);
    }

    // получить спиок всех подзадач эпика
    public HashMap<Integer, Epic> getAllEpic() {
        return listEpic;
    }

    public void updateEpic(Integer id, Epic subTask) {
        listEpic.put(id, subTask);
    }


    @Override
    public String toString() {
        return "TaskManger{" +
                "listTask=" + listTask +
                ", listEpic=" + listEpic +
                '}';
    }
}

