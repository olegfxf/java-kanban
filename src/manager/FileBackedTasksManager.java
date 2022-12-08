package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import static java.util.Map.*;


public class FileBackedTasksManager extends InMemoryTaskManager {

    @Override
    public void addTask(Integer uid, Task task) {
        super.addTask(uid, task);
        save();
    }

    @Override
    public void updateTask(Integer id, Task task) {
        super.updateTask(id, task);
        save();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void addEpic(Integer uid, Epic subTask) {
        super.addEpic(uid, subTask);
        save();
    }

    @Override
    public void updateEpic(Integer idEpic, Epic subTask) {
        super.updateEpic(idEpic, subTask);
        save();
    }

    @Override
    public void removeEpicById(Integer idEpic) {
        super.removeEpicById(idEpic);
        save();
    }


    @Override
    public void addSubtask(Integer uid, Subtask subtask) {
        super.addSubtask(uid, subtask);
        save();
    }

    @Override
    public void updateSubtaskById(Integer id, Subtask subtask) {
        super.updateSubtaskById(id, subtask);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        save();
    }


    public static void fromString(String[] split) {
        switch (split[1]) {

            case "TASK":
                listTask.put(Integer.valueOf(split[0]), new Task(Integer.valueOf(split[0]), split[2], split[4], split[3]));
                break;

            case "EPIC":
                listEpic.put(Integer.valueOf(split[0]), new Epic(Integer.valueOf(split[0]), split[2], split[4], split[3]));
                break;

            case "SUBTASK":
                listSubtask.put(Integer.valueOf(split[0]), new Subtask(Integer.valueOf(split[0]), split[2], split[4], split[3], Integer.valueOf(split[5])));
                break;
        }
    }


    public static void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter("./resources/filewriter.txt")) {

            String titelFiles = "id,type,name,status,description,epic";
            fileWriter.write(titelFiles + "\n");

            for (Map.Entry<Integer, Task> iter : listTask.entrySet()) {
                String str1 = iter.getValue().toString();
                fileWriter.write(str1 + "\n");
            }

            for (Map.Entry<Integer, Epic> iter : listEpic.entrySet()) {
                String str1 = iter.getValue().toString();
                fileWriter.write(str1 + "\n");
            }

            for (Map.Entry<Integer, Subtask> iter : listSubtask.entrySet()) {
                String str1 = iter.getValue().toString();
                fileWriter.write(str1 + "\n");
            }

            fileWriter.write("\n"); // печатаем пустую строку в файл согласно ТЗ 6

            fileWriter.write(historyToString()); // сохраняем историю просмотров

        } catch (IOException e) {
            System.out.println("Ошибки загрузки данных в файла  " + e.getCause());
        }

    }

    public static class ManagerSaveException extends RuntimeException {
        // Из ТЗ №6 : "... исходим из того, что наш менеджер работает в идеальных условиях.
        // Над ним не совершаются недопустимые операции, и все его действия со средой
        // (например, сохранение файла) завершаются успешно.
    }

    static String historyToString() {
        String str1 = "";
        try { // сохраняем историю просмотров задач в строку
            for (Task iter3 : inMemoryHistoryManager.getHistory()) {
                str1 += iter3.getUid() + ",";
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }
        return str1;
    }


    public static void loadFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String readLine = "";
            br.readLine(); // читаем шапку файла

            while (!(readLine = br.readLine()).equals("")) {
                String[] split = readLine.split(",");
                fromString(split);
            }

            readLine = br.readLine();

           if(readLine != null) historyFromString(readLine);

        } catch (IOException e) {
            System.out.println("Ошибка выгрузки данных из файла " + e.getMessage());
        }
    }

    static List<Integer> historyFromString(String value) {

        String[] split = value.split(",");
        List<Integer> listId = new ArrayList<>();
        for (String s : split) {
            listId.add(Integer.valueOf(s));
        }

        // inMemoryHistoryManager сохраняет историю просмотров c сохранением всех данных по
        // просмотренным задачам. В файле сохранены только id задач. Восстанавливаем по id
        // данные для  inMemoryHistoryManager.
        for (Integer iter : listId) {

            for (Entry iterTask : listTask.entrySet()) {
                if (iter.equals(iterTask.getKey())) {
                    inMemoryHistoryManager.add((Task) iterTask.getValue());
                    break;
                }
            }

            for (Entry iterEpic : listEpic.entrySet()) {
                if (iter.equals(iterEpic.getKey())) {
                    inMemoryHistoryManager.add((Task)iterEpic.getValue());
                    break;
                }
            }


            for (Entry iterSubtask : listSubtask.entrySet()) {
                if (iter.equals(iterSubtask.getKey())) {
                    inMemoryHistoryManager.add((Task) iterSubtask.getValue());
                    break;
                }
            }

        }
        return listId;
    }


}
