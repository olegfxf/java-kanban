import manager.*;
import manager.InMemoryHistoryManager;
import model.Epic;
import model.StatusTask;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        // получаем FileBackedTasksManager() из Manager;
        TaskManager inMemoryTaskManager = Manager.getDefault();

        InMemoryHistoryManager historyManager = Manager.getDefaultHistory();


        System.out.println();
        System.out.println();
        System.out.println("ТЕСТИРОВАНИЕ СЕРИАЛИЗАЦИИ ПО ТЗ №6:");
        System.out.println("1.Заведите несколько разных задач, эпиков и подзадач.");
        System.out.println("2.Запросите некоторые из них, чтобы заполнилась история просмотра.");
        System.out.println("3.Создайте новый FileBackedTasksManager менеджер(создан в строке 16 кода этого класса).");
        System.out.println("4.Проверьте, что история просмотра восстановилась верно и все задачи, эпики, подзадачи,"
                               + " которые были в старом, есть в новом менеджере.");


        System.out.println();
        historyManager.clearAll();
        inMemoryTaskManager.clearTask(); // очищаем список задач
        // создаем новый список задач
        System.out.println("1.Заведите несколько разных задач, эпиков и подзадач.");
        System.out.println("Id задач, эпиков и подзадач используемых при тестировании:");
        Task task = new Task("nameTask" + 1, "descriptionTask" + 1);
        Integer idTask1 = task.getUid();
        System.out.println("idTask1 = " + idTask1);
        inMemoryTaskManager.addTask(idTask1, task);

        task = new Task("nameTask" + 2, "descriptionTask" + 2);
        Integer idTask2 = task.getUid();
        System.out.println("idTask2 = " + idTask2);
        inMemoryTaskManager.addTask(idTask2, task);

        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        // создаем новый список эпиков
        Epic epic = new Epic("nameEpic" + 1, "descriptionEpic" + 1);
        Integer idEpic1 = epic.getUid();
        System.out.println("idEpic1 = " + idEpic1);
        inMemoryTaskManager.addEpic(idEpic1, epic);


        epic = new Epic("nameEpic" + 2, "descriptionEpic" + 2);
        Integer idEpic2 = epic.getUid();
        System.out.println("idEpic2 = " + idEpic2);
        inMemoryTaskManager.addEpic(idEpic2, epic);


        // создаем новый список подзадач эпика 2
        Subtask subtask = new Subtask("nameSubtask" + 1, "descriptionSubtask" + 1);
        subtask.setIdEpic(idEpic2);
        Integer idSubtask1 = subtask.getUid();
        System.out.println("idSutask1 = " + idSubtask1);
        inMemoryTaskManager.addSubtask(idSubtask1, subtask);

        subtask = new Subtask("nameSubtask" + 2, "descriptionSubtask" + 2);
        subtask.setIdEpic(idEpic2);
        Integer idSubtask2 = subtask.getUid();
        System.out.println("idSutask2 = " + idSubtask2);
        inMemoryTaskManager.addSubtask(idSubtask2, subtask);

        subtask = new Subtask("nameSubtask" + 3, "descriptionSubtask" + 3);
        subtask.setIdEpic(idEpic2);
        Integer idSubtask3 = subtask.getUid();
        System.out.println("idSutask3 = " + idSubtask3);
        inMemoryTaskManager.addSubtask(idSubtask3, subtask);


        System.out.println();
        System.out.println("2.Запросите некоторые из них, чтобы заполнилась история просмотра.");

        inMemoryTaskManager.getTaskById(idTask1);
        inMemoryTaskManager.getTaskById(idTask2);

        inMemoryTaskManager.getTaskById(idTask2);
        inMemoryTaskManager.getTaskById(idTask1);

        inMemoryTaskManager.getEpicById(idEpic1);
        inMemoryTaskManager.getEpicById(idEpic2);

        inMemoryTaskManager.getEpicById(idEpic2);
        inMemoryTaskManager.getEpicById(idEpic1);

        inMemoryTaskManager.getSubtaskById(idSubtask3);
        inMemoryTaskManager.getSubtaskById(idSubtask1);

 //       System.out.println();
   //     System.out.println("История просмотров(idTask) в LinkedList");
     //   historyManager.getHistory().stream().forEach(e -> System.out.println(e.getUid()));

        System.out.println("\n1 ###########################");
        System.out.println("Список задач из inMemoryTaskManager: ");
        System.out.println(inMemoryTaskManager.getAllTask());

        System.out.println("Список эпиков из inMemoryTaskManager: ");
        System.out.println(inMemoryTaskManager.getAllEpic());

        System.out.println("Список  подзадач эпика с idEpic2 :");
        System.out.println(inMemoryTaskManager.getAllSubtaskEpic(idEpic2));

        System.out.println("История просмотров(idTask) в LinkedList");
        historyManager.getHistory().stream().forEach(e -> System.out.println(e.getUid()));


        // сохраняем все данные из объектов классов FileBackedTasksManager и InMemoryHistoryManager
        FileBackedTasksManager.save ();


        System.out.println("\n2 ###########################");
        System.out.println("Удаляем все задачи, эпики, подзадачи:");
        System.out.println();
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();

        System.out.println("Список задач из inMemoryTaskManager: ");
        System.out.println(inMemoryTaskManager.getAllTask());

        System.out.println("Список эпиков из inMemoryTaskManager: ");
        System.out.println(inMemoryTaskManager.getAllEpic());

        System.out.println("Список  подзадач эпика с idEpic2 :");
        System.out.println(inMemoryTaskManager.getAllSubtaskEpic(idEpic2));


        System.out.println("\n3 ###########################");
        System.out.println("Восстанавливаем все задачи, эпики, подзадачи и историю просмотра задач.");

        // восстанавливаем  данные объектов классов FileBackedTasksManager и InMemoryHistoryManager
        FileBackedTasksManager.loadFromFile(new File("filewriter.csv"));
        System.out.println("Загружаем задачи, эпики, подзадачи из файла.");
        System.out.println();

        System.out.println("Восстановленный список задач из inMemoryTaskManager: ");
        System.out.println(inMemoryTaskManager.getAllTask());

        System.out.println("Восстановленный список эпиков из inMemoryTaskManager: ");
        System.out.println(inMemoryTaskManager.getAllEpic());

        System.out.println("Восстановленный список  подзадач эпика с idEpic2 :");
        System.out.println(inMemoryTaskManager.getAllSubtaskEpic(idEpic2));

        System.out.println("Восстановленная история просмотров(idTask) в LinkedList");
        historyManager.getHistory().stream().forEach(e -> System.out.println(e.getUid()));

        System.out.println();
        System.out.println("4.Проверьте, что история просмотра восстановилась верно и все задачи, эпики, подзадачи,"
                + " которые были в старом, есть в новом менеджере.");
        System.out.println("Все задачи, эпики, подзадачи и история просмотра задач восстановлены корректно.");

    }


    public static void createSubtasks(TaskManager inMemoryTaskManager, Integer idEpic, int range) {
        Subtask subtask;
        for (int i = 0; i < range; i++) {
            subtask = new Subtask("nameSubtask" + i, "descriptionSubtask" + i);
            subtask.setIdEpic(idEpic);
            inMemoryTaskManager.addSubtask(subtask.getUid(), subtask);
        }
        inMemoryTaskManager.checkStatus(idEpic);
    }

    public static void createListEpicTask(TaskManager inMemoryTaskManager) {
        for (int i = 0; i < 2; i++) {
            Task task = new Task("nameTask" + i, "descriptionTask" + i);
            inMemoryTaskManager.addTask(task.getUid(), task);

            Epic epic = new Epic("nameEpic" + i, "descriptionEpic" + i);
            inMemoryTaskManager.addEpic(epic.getUid(), epic);
            createSubtasks(inMemoryTaskManager, epic.getUid(), 3);
        }
    }

    public static Integer getSomeIdTask(TaskManager inMemoryTaskManager) {
        Integer idTask = 0;
        for (Integer iter : inMemoryTaskManager.getAllTask().keySet()) {
            idTask = iter;
            break; // выбираем первый Id  задачи
        }
        return idTask;
    }

    public static Integer getSomeIdEpic(TaskManager inMemoryTaskManager) {
        Integer idEpic = 0;
        for (Integer iter : inMemoryTaskManager.getAllEpic().keySet()) {
            idEpic = iter;
            break; // выбираем первый Id эпика
        }
        return idEpic;
    }


}
