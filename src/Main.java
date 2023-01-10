import manager.*;
import server.HttpTaskServer;
import manager.InMemoryHistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVServer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();

        TaskManager inMemoryTaskManager = Manager.getDefault();

        InMemoryHistoryManager historyManager = Manager.getDefaultHistory();


        System.out.println();
        System.out.println();
        System.out.println("Исходные данные для проверки сервисов через Insomnia по ТЗ №8:");
        System.out.println();


        historyManager.clearAll();
        inMemoryTaskManager.clearTask(); // очищаем список задач

        // создаем новый список задач
        Task task = new Task("nameTask" + 1, "descriptionTask" + 1);
        task.setStartTimeDuration(LocalDateTime.of(1999,2, 1,1,
                        1), Duration.ofDays(60));
        Integer idTask1 = task.getUid();
        System.out.println("idTask1 = " + idTask1);
        inMemoryTaskManager.addTask(idTask1, task);

        task = new Task("nameTask" + 2, "descriptionTask" + 2);
        task.setStartTimeDuration(LocalDateTime.of(1999,1, 1,1,
                1), Duration.ofDays(6));
        Integer idTask2 = task.getUid();
        System.out.println("idTask2 = " + idTask2);
        inMemoryTaskManager.addTask(idTask2, task);


        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        // создаем новый список эпиков
        Epic epic = new Epic("nameEpic" + 1, "descriptionEpic" + 1);
        epic.setStartTimeDuration(LocalDateTime.of(1999,12, 1,1,
                1), Duration.ofDays(6));
        Integer idEpic1 = epic.getUid();
        System.out.println("idEpic1 = " + idEpic1);
        inMemoryTaskManager.addEpic(idEpic1, epic);


        epic = new Epic("nameEpic" + 2, "descriptionEpic" + 2);
        epic.setStartTimeDuration(LocalDateTime.of(1995,3, 1,1,
                1), Duration.ofDays(6));
        Integer idEpic2 = epic.getUid();
        System.out.println("idEpic2 = " + idEpic2);
        inMemoryTaskManager.addEpic(idEpic2, epic);


        // создаем новый список подзадач эпика 2
        Subtask subtask = new Subtask("nameSubtask" + 1, "descriptionSubtask" + 1);
        subtask.setStartTimeDuration(LocalDateTime.of(1999,3, 1,1,
                1), Duration.ofDays(6));
        subtask.setIdEpic(idEpic2);
        Integer idSubtask1 = subtask.getUid();
        System.out.println("idSutask1 = " + idSubtask1);
        inMemoryTaskManager.addSubtask(idSubtask1, subtask);

        subtask = new Subtask("nameSubtask" + 2, "descriptionSubtask" + 2);
        subtask.setStartTimeDuration(LocalDateTime.of(1999,4, 1,1,
                1), Duration.ofDays(6));
        subtask.setIdEpic(idEpic2);
        Integer idSubtask2 = subtask.getUid();
        System.out.println("idSutask2 = " + idSubtask2);
        inMemoryTaskManager.addSubtask(idSubtask2, subtask);

        subtask = new Subtask("nameSubtask" + 3, "descriptionSubtask" + 3);
        subtask.setStartTimeDuration(LocalDateTime.of(1999,5, 1,1,
                1), Duration.ofDays(6));
        subtask.setIdEpic(idEpic2);
        Integer idSubtask3 = subtask.getUid();
        System.out.println("idSutask3 = " + idSubtask3);
        inMemoryTaskManager.addSubtask(idSubtask3, subtask);


        System.out.println();
        // Создаем историю просмотров

        inMemoryTaskManager.getTaskById(idTask1);

        inMemoryTaskManager.getEpicById(idEpic1);
        inMemoryTaskManager.getEpicById(idEpic2);

        inMemoryTaskManager.getEpicById(idEpic2);
        inMemoryTaskManager.getEpicById(idEpic1);



        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

    }

}
