import manager.*;
import model.Epic;
import model.StatusTask;
import model.Subtask;
import model.Task;

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Manager.getDefault();

        HistoryManager historyManager = Manager.getDefaultHistory();


        ////////////ТЕСТИРОВАНИЕ ЗАДАЧ  ////////////////////

        System.out.println();
        System.out.println("ТЕСТИРОВАНИЕ ЗАДАЧ");
        System.out.println();

        System.out.println("ПОЛУЧЕНИЕ СПИСКА ВСЕХ ЗАДАЧ");
        inMemoryTaskManager.clearTask();     // очищаем список задач
        createListEpicTask(inMemoryTaskManager); // создаем новый список задач
        System.out.println("Список всех задач =\n" + inMemoryTaskManager.getAllTask());
        System.out.println();


        System.out.println("УДАЛЕНИЕ ВСЕХ ЗАДАЧ");
        inMemoryTaskManager.clearTask();     // очищаем список задач
        createListEpicTask(inMemoryTaskManager); // создаем новый список задач
        System.out.println("Количество всех задач до удаления = "
                + inMemoryTaskManager.getAllTask().size());
        inMemoryTaskManager.clearTask();
        System.out.println("Количество всех задач в после удаления = "
                + inMemoryTaskManager.getAllTask().size());
        System.out.println();


        System.out.println("ПОЛУЧЕНИЕ ЗАДАЧИ ПО Id");
        inMemoryTaskManager.clearTask();
        createListEpicTask(inMemoryTaskManager);
        Integer idTask = getSomeIdTask(inMemoryTaskManager);
        System.out.println("Результат получения задачи по Id:");
        System.out.println("Задача[" + idTask + "] = " + inMemoryTaskManager.getTaskById(idTask));
        System.out.println();


        System.out.println("УДАЛЕНИЕ ЗАДАЧИ ПО Id");
        inMemoryTaskManager.clearTask();     // очищаем список задач
        createListEpicTask(inMemoryTaskManager); // создаем новый список задач
        System.out.println("Количество всех задач до удаления по Id = "
                + inMemoryTaskManager.getAllTask().size());
        idTask = getSomeIdTask(inMemoryTaskManager); // получаем случайный id из inMemoryTaskManager
        inMemoryTaskManager.removeTaskById(idTask);
        System.out.println("Количество всех задач  после удаления по Id = "
                + inMemoryTaskManager.getAllTask().size());
        System.out.println();

        System.out.println("ОБНОВЛЕНИЕ ЗАДАЧИ ПО Id");
        inMemoryTaskManager.clearEpic();     // очищаем список задач
        createListEpicTask(inMemoryTaskManager); // создаем новый список задач
        idTask = getSomeIdTask(inMemoryTaskManager); // получаем некоторый Id задачи
        Task task = inMemoryTaskManager.getTaskById(idTask); // достаем задачу из списка
        task.setTitle("@@@@@@@"); // обновляем поле Title
        inMemoryTaskManager.updateTask(idTask, task); // возвращаем задачу в список
        System.out.println("Результат обновления поля Title задачи по Id новым значением @@@@@@@:");
        System.out.println(inMemoryTaskManager.getTaskById(idTask));
        System.out.println();


        System.out.println("ПРОВЕРКА ИЗМЕНЕНИЯ СТАТУСА ЗАДАЧИ В IN_PROGRESS");
        inMemoryTaskManager.clearEpic();     // очищаем список задач
        createListEpicTask(inMemoryTaskManager); // создаем новый список задач
        Integer id = getSomeIdTask(inMemoryTaskManager); // получаем некоторый id из inMemoryTaskManager
        System.out.println("Статус задачи до его изменения = "
                + inMemoryTaskManager.getTaskById(id).getStatus());
        inMemoryTaskManager.getTaskById(id).startTask();
        System.out.println("Статус задачи после его изменения = "
                + inMemoryTaskManager.getTaskById(id).getStatus());
        System.out.println();


        System.out.println("ПРОВЕРКА ИЗМЕНЕНИЯ СТАТУСА ЗАДАЧИ В DONE");
        inMemoryTaskManager.clearTask();     // очищаем список задач
        createListEpicTask(inMemoryTaskManager); // создаем новый список задач
        id = getSomeIdTask(inMemoryTaskManager); // получаем некоторый id из inMemoryTaskManager
        System.out.println("Статус задачи до его изменения = "
                + inMemoryTaskManager.getTaskById(id).getStatus());
        inMemoryTaskManager.getTaskById(id).finishTask();
        System.out.println("Статус задачи после его изменения = "
                + inMemoryTaskManager.getTaskById(id).getStatus());
        System.out.println();


        System.out.println();
        System.out.println("ТЕСТИРОВАНИЕ ЭПИКОВ");
        System.out.println();

        //////// ТЕСТИРОВАНИЕ  ЭПИКОВ ///////////////////


        System.out.println("ПОЛУЧЕНИЕ СПИСКА ВСЕХ ПОДЗАДАЧ ЭПИКА");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println();


        System.out.println("УДАЛЕНИЕ ВСЕХ ЭПИКОВ");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        System.out.println("Количество всех эпиков до удаления = "
                + inMemoryTaskManager.getAllEpic().size());
        inMemoryTaskManager.clearEpic();
        System.out.println("Количество всех эпиков после удаления = "
                + inMemoryTaskManager.getAllEpic().size());
        System.out.println();


        System.out.println("УДАЛЕНИЕ ЭПИКА ПО Id");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        id = getSomeIdEpic(inMemoryTaskManager);
        System.out.println("Количество всех эпиков до удаления по Id = "
                + inMemoryTaskManager.getAllEpic().size());
        inMemoryTaskManager.removeEpicById(id);
        System.out.println("Количество всех эпиков после удаления по Id = "
                + inMemoryTaskManager.getAllEpic().size());
        System.out.println();


        System.out.println("ОБНОВЛЕНИЕ ЭПИКА ПО Id");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        Integer idEpic = getSomeIdEpic(inMemoryTaskManager);
        Epic epic = inMemoryTaskManager.getEpicById(idEpic);
        epic.setTitle("@@@@@@@"); // обновляем поле Title
        inMemoryTaskManager.updateEpic(idEpic, epic); // возвращаем задачу в список
        System.out.println("Результат обновления поля Title эпика по Id новым значением @@@@@@@:");
        System.out.println(inMemoryTaskManager.getEpicById(idEpic));
        System.out.println();


        System.out.println("ПРОВЕРКА ИЗМЕНЕНИЯ СТАТУСА ЭПИКА");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        idEpic = getSomeIdEpic(inMemoryTaskManager); // получаем некоторый id из inMemoryTaskManager
        System.out.println("Статус всех подзадач эпика устанавливаем в DONE");
        for (Subtask subtask : inMemoryTaskManager.getAllSubtaskEpic(idEpic)) {
            subtask.setStatus(StatusTask.DONE);
            inMemoryTaskManager.updateSubtaskById(subtask.getUid(), subtask);
        }
        inMemoryTaskManager.checkStatus(idEpic);
        System.out.println("Статус эпика становится:  " + inMemoryTaskManager.getEpicById(idEpic));

        System.out.println("Статус всех подзадач эпика устанавливаем в NEW");
        for (Subtask subtask : inMemoryTaskManager.getAllSubtaskEpic(idEpic)) {
            subtask.setStatus(StatusTask.NEW);
            inMemoryTaskManager.updateSubtaskById(subtask.getUid(), subtask);
        }
        inMemoryTaskManager.checkStatus(idEpic);
        System.out.println("Статус эпика становится:  " + inMemoryTaskManager.getEpicById(idEpic));

        System.out.println("Статус всех подзадач эпика устанавливаем в DONE кроме последней с NEW");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        idEpic = getSomeIdEpic(inMemoryTaskManager); // получаем некоторый id из inMemoryTaskManager
        Integer idEndSubtask = 0;
        for (Subtask subtask : inMemoryTaskManager.getAllSubtaskEpic(idEpic)) {
            subtask.setStatus(StatusTask.DONE);
            inMemoryTaskManager.updateSubtaskById(subtask.getUid(), subtask);
            idEndSubtask = subtask.getUid();
        }
        Subtask subtask = inMemoryTaskManager.getSubtaskById(idEndSubtask);
        subtask.setStatus(StatusTask.NEW);  // у последней подзадачи статус NEW
        inMemoryTaskManager.updateSubtaskById(subtask.getUid(), subtask);
        inMemoryTaskManager.checkStatus(idEpic);
        System.out.println("Статус эпика становится:  " + inMemoryTaskManager.getEpicById(idEpic));

        System.out.println("Статус всех подзадач эпика устанавливаем в DONE кроме последней с IN_PROGRES");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        idEpic = getSomeIdEpic(inMemoryTaskManager); // получаем некоторый id из inMemoryTaskManager
        idEndSubtask = 0;
        for (Subtask subtask1 : inMemoryTaskManager.getAllSubtaskEpic(idEpic)) {
            subtask1.setStatus(StatusTask.DONE);
            inMemoryTaskManager.updateSubtaskById(subtask.getUid(), subtask1);
            idEndSubtask = subtask1.getUid();
        }
        subtask = inMemoryTaskManager.getSubtaskById(idEndSubtask);
        subtask.setStatus(StatusTask.IN_PROGRESS);  // у последней подзадачи статус IN_PROGRESS
        inMemoryTaskManager.updateSubtaskById(subtask.getUid(), subtask);
        inMemoryTaskManager.checkStatus(idEpic);
        System.out.println("Статус эпика становится:  " + inMemoryTaskManager.getEpicById(idEpic));
        System.out.println();


        ////////// ТЕСТИРОВАНИЕ ПОДЗАДАЧ //////////////////////////////////////
        System.out.println();
        System.out.println("ТЕСТИРОВАНИЕ ПОДЗАДАЧ");
        System.out.println();


        System.out.println("ПОЛУЧЕНИЕ СПИСКА ВСЕХ ПОДЗАДАЧ ЭПИКА");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        idEpic = getSomeIdEpic(inMemoryTaskManager);// получаем некоторый id эпика из inMemoryTaskManager
        ArrayList<Subtask> subt = inMemoryTaskManager.getAllSubtaskEpic(idEpic);
        System.out.println("Список подзадач эпика:\n" + "model.Epic[" + idEpic
                + "]=" + subt);
        System.out.println();

        System.out.println("УДАЛЕНИЕ ВСЕХ ПОДЗАДАЧ ЭПИКА");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        idEpic = getSomeIdEpic(inMemoryTaskManager);// получаем некоторый id эпика из inMemoryTaskManager
        System.out.println("Список подзадач эпика до удаления:\n" + inMemoryTaskManager.getAllSubtaskEpic(idEpic));
        inMemoryTaskManager.clearSubtaskEpic(idEpic);
        System.out.println("Список подзадач эпика после удаления:\n" + inMemoryTaskManager.getAllSubtaskEpic(idEpic));
        System.out.println();

        System.out.println("ПОЛУЧЕНИЕ ПОДЗАДАЧИ ЭПИКА ПО Id");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        idEpic = getSomeIdEpic(inMemoryTaskManager);// получаем некоторый id эпика из inMemoryTaskManager
        Integer idSubtask;
        for (Subtask iter : inMemoryTaskManager.getAllSubtaskEpic(idEpic)) {
            idSubtask = iter.getUid();
            subtask = inMemoryTaskManager.getSubtaskById(idSubtask);
            System.out.println("Подзадача эпика с Id"
                    + " эпика " + idEpic + " и Id подзадачи " + idSubtask + "\n" + subtask);
            break; // выбираем первый Id подзадачи
        }
        System.out.println();

        System.out.println("ОБНОВЛЕНИЕ ПОЛЯ ПОДЗАДАЧИ ЭПИКА");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        idEpic = getSomeIdEpic(inMemoryTaskManager);// получаем некоторый id эпика из inMemoryTaskManager
        idSubtask = 0;
        for (Subtask iter : inMemoryTaskManager.getAllSubtaskEpic(idEpic)) {
            subtask = iter; // достали задачу из списка задач
            idSubtask = subtask.getUid();
            subtask.setTitle("@@@@@@@@@@"); // изменили наименование подзадачи
            inMemoryTaskManager.updateSubtaskById(idSubtask, subtask); // обновили список задач эпик
            break; // выбираем первый Id подзадачи
        }
        System.out.println("Обновление поля Title подзадачи c Id " + idSubtask + " и idEpic "
                + idEpic + " значением @@@@@@@@@@:\n" + inMemoryTaskManager.getSubtaskById(idSubtask).toString() + " \n ");


        System.out.println("УДАЛЕНИЕ ПОДЗАДАЧИ ИЗ СПИСКА ПОДЗАДАЧ ЭПИКА");
        inMemoryTaskManager.clearEpic();     // очищаем список эпиков
        createListEpicTask(inMemoryTaskManager); // создаем новый список эпиков
        idEpic = getSomeIdEpic(inMemoryTaskManager);// получаем случайный id эпика из inMemoryTaskManager
        System.out.println("Длина списка подзадач до удаления подзадачи: "
                + inMemoryTaskManager.getAllSubtaskEpic(idEpic).size());
        for (Subtask iter : inMemoryTaskManager.getAllSubtaskEpic(idEpic)) {
            idSubtask = iter.getUid();
            inMemoryTaskManager.removeSubtaskById(idSubtask); // удалили из списка подзадачу
            break; // взяли для обработки только первый элемент списка
        }
        System.out.println("Длина списка подзадач после удаления подзадачи: "
                + (inMemoryTaskManager.getAllSubtaskEpic(idEpic).size()));
        System.out.println();


        System.out.println("ТЕСТИРОВАНИЕ ИСТОРИИ ПРОСМОТРА ЗАДАЧ - Исходное состояние");
        for (Task iter : historyManager.getHistory()){
            System.out.println("Uid = " + iter.getUid() + "  Title = " + iter.getTitle());
        }



            System.out.println();

            idTask = getSomeIdTask(inMemoryTaskManager);
            inMemoryTaskManager.getTaskById(idTask);




            System.out.println("ТЕСТИРОВАНИЕ ИСТОРИИ ПРОСМОТРА ЗАДАЧ - Сотояние после просмотра задачи с Id " + idTask);
            for (Task iter : historyManager.getHistory()){
                System.out.println("Uid = " + iter.getUid() + "  Title = " + iter.getTitle());
            }






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
        for (int i = 0; i < 5; i++) {
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
