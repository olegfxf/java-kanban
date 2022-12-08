package manager;

import model.Epic;
import model.StatusTask;
import model.Subtask;
import model.Task;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected static final Map<Integer, Task> listTask = new HashMap<>(); // заменить на privat
    protected static final Map<Integer, Epic> listEpic = new HashMap<>();
    protected static final Map<Integer, Subtask> listSubtask = new HashMap<>();
    protected static final InMemoryHistoryManager inMemoryHistoryManager = Manager.getDefaultHistory();


    Task task = new Task();


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
        inMemoryHistoryManager.remove(id);
        listTask.remove(id);
    }

    @Override
    public Map<Integer, Task> getAllTask() {
        return listTask;
    }

    @Override
    public void clearTask() {
        for (Integer iter : listTask.keySet()) {
            Integer id = listTask.get(iter).getUid();
            if (inMemoryHistoryManager.getHashMapTask().size() != 0) { // история просмотра задач не пуста
                inMemoryHistoryManager.remove(id);
            }
        }
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
        inMemoryHistoryManager.remove(idEpic);
        clearSubtaskEpic(idEpic);
        listEpic.remove(idEpic);
    }

    // получить спиок всех подзадач эпика
    @Override
    public Map<Integer, Epic> getAllEpic() {
        return listEpic;
    }

    @Override
    public void clearEpic() {
        for (Integer iter : listEpic.keySet()) {
            Integer id = listEpic.get(iter).getUid();
            if (inMemoryHistoryManager.getHashMapTask().size() != 0) { // история просмотра задач не пуста
                inMemoryHistoryManager.remove(id);
            }
        }
        listSubtask.clear();
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
        inMemoryHistoryManager.add(task);
        return listSubtask.get(id);
    }

    @Override
    public void updateSubtaskById(Integer id, Subtask subtask) {
        listSubtask.put(id, subtask);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        if (inMemoryHistoryManager.getHashMapTask().size() != 0) { // история просмотра задач не пуста
            inMemoryHistoryManager.remove(id);
        }
        listSubtask.remove(id);
    }

    @Override
    public List<Subtask> getAllSubtaskEpic(Integer idEpic) {
        List<Subtask> listSubt = new ArrayList<>();
        for (Integer iter : listSubtask.keySet()) {
            if (listSubtask.get(iter).getIdEpic().equals(idEpic)) {
                listSubt.add(listSubtask.get(iter));
            }
        }
        return listSubt;
    }

    @Override
    public void clearSubtaskEpic(Integer idEpic) {
        List<Subtask> listSubt = getAllSubtaskEpic(idEpic);
        for (Subtask iter : listSubt) {
            removeSubtaskById(iter.getUid()); // метод removeSubtaskById удаляет историю просмотра задач
            inMemoryHistoryManager.remove(iter.getUid());
        }
    }

    @Override
    public void checkStatus(Integer idEpic) {
        int statusNEW = 0;
        int statusIN_PROGRESS = 0;
        int statusDONE = 0;
        List<Subtask> subtasks = this.getAllSubtaskEpic(idEpic);

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

