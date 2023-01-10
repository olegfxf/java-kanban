package manager;

import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVTaskClient;

import java.io.*;
import java.util.Map;

import static manager.HistoryFromString.historyFromString;
import static manager.HistoryToString.historyToString;

public class HttpTaskManager extends FileBackedTasksManager {
    public KVTaskClient kvTaskClient = new KVTaskClient();


    @Override
    public void saveToFile() throws ManagerSaveException {
        String request = "";

        String titleField = "id,type,name,status,description,epic, startTime, duration";
        request += titleField + "\n";

        for (Map.Entry<Integer, Task> taskEntry : listTask.entrySet()) {
            String taskData = taskEntry.getValue().toString();
            request += taskData + "\n";
        }

        for (Map.Entry<Integer, Epic> epicEntry : listEpic.entrySet()) {
            String epicData = epicEntry.getValue().toString();
            request += epicData + "\n";
        }

        for (Map.Entry<Integer, Subtask> subtaskEntry : listSubtask.entrySet()) {
            String subtaskData = subtaskEntry.getValue().toString();
            request += subtaskData + "\n";
        }

        request += "\n";

        request += historyToString(inMemoryHistoryManager);
        // сохраняем историю просмотров
        kvTaskClient.save(request);
    }

    @Override
    public void loadFromFile(File file) {
        String response = kvTaskClient.load();

        String[] taskData;
        try (BufferedReader br = new BufferedReader(new StringReader(response))) {

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
