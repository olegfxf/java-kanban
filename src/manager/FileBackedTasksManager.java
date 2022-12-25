package manager;

import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskType;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import static manager.HistoryFromString.historyFromString;
import static manager.HistoryToString.historyToString;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private static List<Integer> history = new ArrayList<>();

    @Override
    public void addTask(Integer uid, Task task) {
        super.addTask(uid, task);
        saveToFile();
    }

    @Override
    public void updateTask(Integer id, Task task) {
        super.updateTask(id, task);
        saveToFile();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        saveToFile();
    }

    @Override
    public void clearTask() {
        super.clearTask();
        saveToFile();
    }

    @Override
    public void addEpic(Integer uid, Epic subTask) {
        super.addEpic(uid, subTask);
        saveToFile();
    }

    @Override
    public void updateEpic(Integer idEpic, Epic subTask) {
        super.updateEpic(idEpic, subTask);
        saveToFile();
    }

    @Override
    public void removeEpicById(Integer idEpic) {
        super.removeEpicById(idEpic);
        saveToFile();
    }


    @Override
    public void addSubtask(Integer uid, Subtask subtask) {
        super.addSubtask(uid, subtask);
        saveToFile();
    }

    @Override
    public void updateSubtaskById(Integer id, Subtask subtask) {
        super.updateSubtaskById(id, subtask);
        saveToFile();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        saveToFile();
    }

    public static List<Integer> getHistory() {
        return history;
    }

    public static void fromString(String[] split) {

        switch (TaskType.valueOf(split[1])) {

            case TASK:
                listTask.put(Integer.valueOf(split[0]), new Task(Integer.valueOf(split[0]),
                        split[2], split[4], split[3],
                        LocalDateTime.parse(split[5]), Duration.parse(split[6])));
                break;

            case EPIC:
                listEpic.put(Integer.valueOf(split[0]), new Epic(Integer.valueOf(split[0]),
                        split[2], split[4], split[3]));
                break;

            case SUBTASK:
                listSubtask.put(Integer.valueOf(split[0]), new Subtask(Integer.valueOf(split[0]),
                        split[2], split[4], split[3], Integer.valueOf(split[5]),
                        split[6].equals("null") ? null : LocalDateTime.parse(split[6]),
                        split[7].equals("null") ? null : Duration.parse(split[7])));
                break;
        }

    }


    public static void saveToFile() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter("filewriter.csv")) {

            String titelFiles = "id,type,name,status,description,epic, startTime, duration";
            fileWriter.write(titelFiles + "\n");

            for (Map.Entry<Integer, Task> taskEntry : listTask.entrySet()) {
                String taskData = taskEntry.getValue().toString();
                fileWriter.write(taskData + "\n");
            }

            for (Map.Entry<Integer, Epic> epicEntry : listEpic.entrySet()) {
                String epicData = epicEntry.getValue().toString();
                fileWriter.write(epicData + "\n");
            }

            for (Map.Entry<Integer, Subtask> subtaskEntry : listSubtask.entrySet()) {
                String subtaskData = subtaskEntry.getValue().toString();
                fileWriter.write(subtaskData + "\n");
            }

            fileWriter.write("\n"); // печатаем пустую строку в файл согласно ТЗ 6

            fileWriter.write(historyToString(inMemoryHistoryManager)); // сохраняем историю просмотров

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных задач, эпиков, подзадач и истории просмотров.");
        }

    }


    public static void loadFromFile(File file) {
        String[] taskData;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String readLine = "";
            br.readLine(); // читаем шапку файла

            while (!(readLine = br.readLine()).equals("")) {
                taskData = readLine.split(",");
                fromString(taskData);
            }

            readLine = br.readLine();

            if (readLine != null) history = historyFromString(readLine);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка выгрузки данных из файла.");
        }
    }


}
