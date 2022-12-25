package manager;

import model.Epic;
import model.StatusTask;
import model.Subtask;
import model.Task;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected static final Map<Integer, Task> listTask = new HashMap<>(); // заменить на privat
    protected static final Map<Integer, Epic> listEpic = new HashMap<>();
    protected static final Map<Integer, Subtask> listSubtask = new HashMap<>();
    protected static final InMemoryHistoryManager inMemoryHistoryManager = Manager.getDefaultHistory();
    public static final TreeSet<Task> sortedTask = new TreeSet<>();

    Task task = new Task();

    @Override
    public Map<Integer, Subtask> getAllSubtask() {
        return listSubtask;
    }

    public TreeSet<Task> getSortedTask() {
        return sortedTask;
    }


    ////////////Task/////////////////////////////

    /**
     * Вставляет задачи  в отсортированный список sortedTask.
     * Задача вствляется в список и проверяются условия неперекрытия
     * временных интервалов с нижестоящими и вышестоящими элементами списка.
     * При наличии перекрытия задача удаляется из списка.
     */
    public boolean addTaskToSet(Task task) {

        sortedTask.add(task);
        Task taskLower = sortedTask.lower(task);
        Task taskHigher = sortedTask.higher(task);

        try {
            if (taskLower.getEndTime().isAfter(task.getStartTime())) {
                sortedTask.remove(task);
                return false;
            }
        } catch (NullPointerException e) { }

        try {
            if (task.getEndTime().isAfter(taskHigher.getStartTime())) {
                sortedTask.remove(task);
                return false;
            }
        } catch (NullPointerException e) { }

        return true;
    }

    @Override
    public void addTask(Integer uid, Task task) {
        if (addTaskToSet(task))
            listTask.put(uid, task);
    }

    @Override
    public Task getTaskById(Integer id) {
        if (listTask.isEmpty())
            throw new IllegalArgumentException("Список задач пуст.");
        else if (listTask.containsKey(id))
            task = listTask.get(id);
        else
            throw new IllegalArgumentException("Неверный идентификатор задачи - " + id);
        inMemoryHistoryManager.add(task);
        return task;
    }

    @Override
    public void updateTask(Integer id, Task task) {
        if (listTask.isEmpty())
            throw new IllegalArgumentException("Список задач пуст.");
        else if (listTask.containsKey(id)) {
            if (addTaskToSet(task))
                listTask.put(id, task);
        } else
            throw new IllegalArgumentException("Неверный идентификатор задачи - " + id);
    }

    @Override
    public void removeTaskById(Integer id) {
        if (listTask.isEmpty())
            throw new IllegalArgumentException("Список задач пуст.");
        else if (listTask.containsKey(id)) {
            inMemoryHistoryManager.remove(id);
            sortedTask.remove(listTask.get(id));
            listTask.remove(id);
        } else
            throw new IllegalArgumentException("Неверный идентификатор задачи - " + id);
    }

    @Override
    public Map<Integer, Task> getAllTask() {
        return listTask;
    }

    @Override
    public void clearTask() {
        for (Integer idTask : listTask.keySet()) {
            sortedTask.remove(listTask.get(idTask));
            Integer id = listTask.get(idTask).getUid();
            if (inMemoryHistoryManager.getHashMapTask().size() != 0) { // история просмотра задач не пуста
                inMemoryHistoryManager.remove(id);
            }
        }
        listTask.clear();
    }


    ////////////Epic//////////////////////////////////////

    /**
     * Вставляет эпик в отсортированный список sortedTask.
     * Подзадачи вствляется в список и проверяются условия неперекрытия
     * временных интервалов с нижестоящими и вышестоящими элементами списка.
     * При наличии перекрытия  у одной из подзадач эпика с соседними элементами
     * списка, удаляются все подзадачи эпика.
     */
    public boolean addEpicToSet(Epic epic) {

        List<Subtask> subtasks = getAllSubtaskEpic(epic.getUid());
        List<Subtask> subtasksAdd = new ArrayList<>();

        for (Subtask subtask : subtasks) {
            sortedTask.add(subtask);
            Task subtaskHigher = sortedTask.higher(subtask);
            Task subtaskLower = sortedTask.lower(subtask);

            try {
                if (subtaskLower.getEndTime().isAfter(subtask.getStartTime())) {
                    sortedTask.removeAll(subtasksAdd);
                    return false;
                } else {
                    subtasksAdd.add(subtask);
                }
            } catch (NullPointerException e) { }

            try {
                if (subtask.getEndTime().isAfter(subtaskHigher.getStartTime())) {
                    sortedTask.removeAll(subtasksAdd);
                    return false;
                } else {
                    subtasksAdd.add(subtask);
                }
            } catch (NullPointerException e) { }
        }
        return true;
    }

    @Override
    public void addEpic(Integer uid, Epic epic) {
        if (addEpicToSet(epic))
            listEpic.put(uid, epic);
    }

    @Override
    public Epic getEpicById(Integer idEpic) {
        if (listEpic.isEmpty())
            throw new IllegalArgumentException("Список эпиков пуст.");
        else if (listEpic.containsKey(idEpic)) {
            inMemoryHistoryManager.add(listEpic.get(idEpic));
            return listEpic.get(idEpic);
        } else
            throw new IllegalArgumentException("Неверный идентификатор эпика - " + idEpic);

    }

    @Override
    public void updateEpic(Integer idEpic, Epic epic) {
        if (listEpic.isEmpty())
            throw new IllegalArgumentException("Список эпиков пуст.");
        else if (listEpic.containsKey(idEpic)) {
            if (addEpicToSet(epic))
                listEpic.put(idEpic, epic);
        } else
            throw new IllegalArgumentException("Неверный идентификатор эпика - " + idEpic);
    }

    @Override
    public void removeEpicById(Integer idEpic) {
        if (listEpic.isEmpty())
            throw new IllegalArgumentException("Список эпиков пуст.");
        else if (listEpic.containsKey(idEpic)) {
            inMemoryHistoryManager.remove(idEpic);
            clearSubtaskEpic(idEpic);
            listEpic.remove(idEpic);
        } else
            throw new IllegalArgumentException("Неверный идентификатор эпика - " + idEpic);
    }

    // получить список всех подзадач эпика
    @Override
    public Map<Integer, Epic> getAllEpic() {
        return listEpic;
    }

    @Override
    public void clearEpic() {
        for (Integer idEpic : listEpic.keySet()) {
            if (inMemoryHistoryManager.getHashMapTask().size() != 0) {
                inMemoryHistoryManager.getHistory().stream()
                        .filter(e -> e instanceof Epic)
                        .forEach(e -> inMemoryHistoryManager.remove(e.getUid()));
            }
            clearSubtaskEpic(idEpic);
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
        if (listSubtask.isEmpty())
            throw new IllegalArgumentException("Список подзадач пуст.");
        else if (listSubtask.containsKey(id)) {
            task = listSubtask.get(id);
            inMemoryHistoryManager.add(task);
        } else
            throw new IllegalArgumentException("Неверный идентификатор подзадачи - " + id);
        return listSubtask.get(id);
    }

    @Override
    public void updateSubtaskById(Integer id, Subtask subtask) {
        if (listSubtask.isEmpty())
            throw new IllegalArgumentException("Список подзадач пуст.");
        else if (listSubtask.containsKey(id)) {
            listSubtask.put(id, subtask);
            checkStatus(subtask.getIdEpic());
        } else
            throw new IllegalArgumentException("Неверный идентификатор подзадачи - " + id);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        if (inMemoryHistoryManager.getHashMapTask().size() != 0) {
            inMemoryHistoryManager.remove(id);
        }

        if (listSubtask.isEmpty())
            throw new IllegalArgumentException("Список подзадач пуст.");
        else if (listSubtask.containsKey(id)) {
            Subtask subtask = getSubtaskById(id);
            listSubtask.remove(id);
            checkStatus(subtask.getIdEpic());
            sortedTask.remove(subtask);
        } else
            throw new IllegalArgumentException("Неверный идентификатор подзадачи - " + id);
    }

    @Override
    public List<Subtask> getAllSubtaskEpic(Integer idEpic) {
        List<Subtask> listSubt = new ArrayList<>();
        for (Integer idSubtask : listSubtask.keySet()) {
            if (listSubtask.get(idSubtask).getIdEpic().equals(idEpic)) {
                listSubt.add(listSubtask.get(idSubtask));
            }
        }
        return listSubt;
    }

    @Override
    public void clearSubtaskEpic(Integer idEpic) {
        List<Subtask> listSubt = getAllSubtaskEpic(idEpic);
        for (Subtask subtask : listSubt) {
            removeSubtaskById(subtask.getUid()); // метод removeSubtaskById удаляет историю просмотра задач
            inMemoryHistoryManager.remove(subtask.getUid());
            sortedTask.remove(subtask);
        }
    }

    @Override
    public void checkStatus(Integer idEpic) {
        int statusNEW = 0;
        int statusIN_PROGRESS = 0;
        int statusDONE = 0;
        List<Subtask> subtasks = this.getAllSubtaskEpic(idEpic);

        if (subtasks.isEmpty()) {
            Epic epic = listEpic.get(idEpic);
            epic.setStatusTask(StatusTask.DONE);
            updateEpic(idEpic, epic);
            return;
        }

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

    public void getPrioritizedTasks() {
        TreeSet<Task> sortedTask = getSortedTask();
        for (Task task1 : sortedTask) {
            if (sortedTask.higher(task1) != null) {
                if (task1.getEndTime().isAfter(sortedTask.higher(task1).getStartTime()))
                    System.out.println("Неверная последовательность задач");
            }
        }
    }


    @Override
    public String toString() {
        return "manger.InMemoryTaskManager{" +
                "listTask=" + listTask +
                ", listEpic=" + listEpic +
                '}';
    }
}

