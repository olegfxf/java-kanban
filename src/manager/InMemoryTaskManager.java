package manager;

import model.Epic;
import model.StatusTask;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> listTask = new HashMap<>();
    HashMap<Integer, Epic> listEpic = new HashMap<>();
    HashMap<Integer, Subtask> listSubtask = new HashMap<>();

    InMemoryHistoryManager inMemoryHistoryManager = Manager.getDefaultHistory();

    @Override
    public InMemoryHistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    Task task = new Task("", "");


    ////////////Task/////////////////////////////
    @Override
    public void addTask(Integer uid, Task task) {
        listTask.put(uid, task);
    }

    @Override
    public Task getTaskById(Integer id) {
        task = listTask.get(id);
        inMemoryHistoryManager.add(task);
        return task;
    }

    @Override
    public void updateTask(Integer id, Task task) {
        listTask.put(id, task);
    }

    @Override
    public void removeTaskById(Integer id) {
        listTask.remove(id);
    }

    @Override
    public HashMap<Integer, Task> getAllTask() {
        return listTask;
    }

    @Override
    public void clearTask() {
        listTask.clear();
    }


    ////////////Epic//////////////////////////////////////
    @Override
    public void addEpic(Integer uid, Epic subTask) {
        listEpic.put(uid, subTask);
    }

    @Override
    public Epic getEpicById(Integer idEpic) {
        task = listEpic.get(idEpic);
        inMemoryHistoryManager.add(task);
        return listEpic.get(idEpic);
    }

    @Override
    public void updateEpic(Integer idEpic, Epic subTask) {
        listEpic.put(idEpic, subTask);
    }

    @Override
    public void removeEpicById(Integer idEpic) {
        clearSubtaskEpic(idEpic);
        listEpic.remove(idEpic);
    }

    // получить спиок всех подзадач эпика
    @Override
    public HashMap<Integer, Epic> getAllEpic() {
        return listEpic;
    }

    @Override
    public void clearEpic() {
        listEpic.clear();
    }


    ////////////// Subtask /////////////////////////////
    @Override
    public void addSubtask(Integer uid, Subtask subtask) {
        listSubtask.put(uid, subtask);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        task = listSubtask.get(id);
        return listSubtask.get(id);
    }

    @Override
    public void updateSubtaskById(Integer id, Subtask subtask) {
        listSubtask.put(id, subtask);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        listSubtask.remove(id);
    }

    @Override
    public ArrayList<Subtask> getAllSubtaskEpic(Integer idEpic) {
        ArrayList<Subtask> listSubt = new ArrayList<>();
        for (Integer iter : listSubtask.keySet()) {
            if (listSubtask.get(iter).getIdEpic().equals(idEpic)) {
                listSubt.add(listSubtask.get(iter));
            }
        }
        return listSubt;
    }

    @Override
    public void clearSubtaskEpic(Integer idEpic) {
        ArrayList<Subtask> listSubt = getAllSubtaskEpic(idEpic);
        for (Subtask iter : listSubt) {
            removeSubtaskById(iter.getUid());
        }
    }

    @Override
    public void checkStatus(Integer idEpic) {
        int statusNEW = 0;
        int statusIN_PROGRESS = 0;
        int statusDONE = 0;
        ArrayList<Subtask> subtasks = this.getAllSubtaskEpic(idEpic);

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == StatusTask.NEW) {
                statusNEW++;
            } else if (subtask.getStatus() == StatusTask.IN_PROGRESS) {
                statusIN_PROGRESS++;
            } else if (subtask.getStatus() == StatusTask.DONE) {
                statusDONE++;
            }
        }

        Epic epic = getEpicById(idEpic);
        if (statusDONE == 0 && statusIN_PROGRESS == 0 && listSubtask.size() != 0) {
            epic.setStatusTask(StatusTask.NEW);
        } else if ((statusNEW == 0 && statusIN_PROGRESS == 0) || listSubtask.size() == 0) {
            epic.setStatusTask(StatusTask.DONE);
        } else {
            epic.setStatusTask(StatusTask.IN_PROGRESS);
        }
        updateEpic(idEpic, epic);
    }


    @Override
    public String toString() {
        return "manger.InMemoryTaskManager{" +
                "listTask=" + listTask +
                ", listEpic=" + listEpic +
                '}';
    }
}

