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
    final String saveKey = "keyForStorage";
    public KVTaskClient kvTaskClient = new KVTaskClient();

    // Наименование метода изменил. В методе сохраняются сразу все объекты
    // приложения - задачи, эпики, субзадачи и история согласно рекомендациям ТЗ 6.
    // Поэтому параметры в методе отсутствуют.
    @Override
    public void saveObjects() throws ManagerSaveException {
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
        kvTaskClient.save(saveKey, request);
    }

    // Наименование метода изменил. В методе загружаются сразу все объекты
    // приложения - задачи, эпики, субзадачи и история согласно рекомендациям ТЗ 6.
    // Поэтому параметры в методе отсутствуют.
    @Override
    public void loadObjects(File file) {
        String response = kvTaskClient.load(saveKey);

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
