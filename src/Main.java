import java.util.HashMap;


public class Main {

    public static void main(String[] args) {

        TaskManger taskManger = new TaskManger();

        ////////////ТЕСТИРОВАНИЕ ЗАДАЧ  ////////////////////

        System.out.println();
        System.out.println("ТЕСТИРОВАНИЕ ЗАДАЧ");
        System.out.println();

        System.out.println("ПОЛУЧЕНИЕ СПИСКА ВСЕХ ЗАДАЧ");
        taskManger.clearTask();     // очищаем список задач
        createListEpicTask(taskManger); // создаем новый список задач
        System.out.println("Список всех задач =\n" + taskManger.getAllTask());
        System.out.println();


        System.out.println("УДАЛЕНИЕ ВСЕХ ЗАДАЧ");
        taskManger.clearTask();     // очищаем список задач
        createListEpicTask(taskManger); // создаем новый список задач
        System.out.println("Количество всех задач до удаления = "
                + taskManger.getAllTask().size());
        taskManger.clearTask();
        System.out.println("Количество всех задач в после удаления = "
                + taskManger.getAllTask().size());
        System.out.println();


        System.out.println("ПОЛУЧЕНИЕ ЗАДАЧИ ПО Id");
        taskManger.clearTask();
        createListEpicTask(taskManger);
        Integer idTask = getSomeId(taskManger);
        System.out.println("Результат получения задачи по Id:");
        System.out.println("Задача[" + idTask + "] = " + taskManger.getTaskById(idTask));
        System.out.println();


        System.out.println("УДАЛЕНИЕ ЗАДАЧИ ПО Id");
        taskManger.clearTask();     // очищаем список задач
        createListEpicTask(taskManger); // создаем новый список задач
        System.out.println("Количество всех задач до удаления по Id = "
                + taskManger.getAllTask().size());
        idTask = getSomeId(taskManger); // получаем случайный id из taskManger
        taskManger.removeTaskById(idTask);
        System.out.println("Количество всех задач  после удаления по Id = "
                + taskManger.getAllTask().size());
        System.out.println();

        System.out.println("ОБНОВЛЕНИЕ ЗАДАЧИ ПО Id");
        taskManger.clearEpic();     // очищаем список задач
        createListEpicTask(taskManger); // создаем новый список задач
        idTask = getSomeId(taskManger); // получаем некоторый Id задачи
        Task task = taskManger.getTaskById(idTask); // достаем задачу из списка
        task.setTitle("@@@@@@@"); // обновляем поле Title
        taskManger.updateTask(idTask, task); // возвращаем задачу в список
        System.out.println("Результат обновления поля Title задачи по Id новым значением @@@@@@@:");
        System.out.println(taskManger.getTaskById(idTask));
        System.out.println();


        System.out.println("ПРОВЕРКА ИЗМЕНЕНИЯ СТАТУСА ЗАДАЧИ В IN_PROGRESS");
        taskManger.clearEpic();     // очищаем список задач
        createListEpicTask(taskManger); // создаем новый список задач
        Integer id = getSomeId(taskManger); // получаем некоторый id из taskManger
        System.out.println("Статус задачи до его изменения = "
                + taskManger.getTaskById(id).getStatus());
        taskManger.getTaskById(id).startTask();
        System.out.println("Статус задачи после его изменения = "
                + taskManger.getTaskById(id).getStatus());
        System.out.println();


        System.out.println("ПРОВЕРКА ИЗМЕНЕНИЯ СТАТУСА ЗАДАЧИ В DONE");
        taskManger.clearTask();     // очищаем список задач
        createListEpicTask(taskManger); // создаем новый список задач
        id = getSomeId(taskManger); // получаем некоторый id из taskManger
        System.out.println("Статус задачи до его изменения = "
                + taskManger.getTaskById(id).getStatus());
        taskManger.getTaskById(id).finishTask();
        System.out.println("Статус задачи после его изменения = "
                + taskManger.getTaskById(id).getStatus());
        System.out.println();


        System.out.println();
        System.out.println("ТЕСТИРОВАНИЕ ЭПИКОВ");
        System.out.println();

        //////// ТЕСТИРОВАНИЕ  ЭПИКОВ ///////////////////


        System.out.println("ПОЛУЧЕНИЕ СПИСКА ВСЕХ ПОДЗАДАЧ ЭПИКА");
        taskManger.clearEpic();     // очищаем список эпиков
        createListEpicTask(taskManger); // создаем новый список эпиков
        System.out.println(taskManger.getAllEpic());
        System.out.println();


        System.out.println("УДАЛЕНИЕ ВСЕХ ЭПИКОВ");
        taskManger.clearEpic();     // очищаем список эпиков
        createListEpicTask(taskManger); // создаем новый список эпиков
        System.out.println("Количество всех эпиков до удаления = "
                + taskManger.getAllEpic().size());
        taskManger.clearEpic();
        System.out.println("Количество всех эпиков после удаления = "
                + taskManger.getAllEpic().size());
        System.out.println();


        System.out.println("УДАЛЕНИЕ ЭПИКА ПО Id");
        taskManger.clearEpic();     // очищаем список эпиков
        createListEpicTask(taskManger); // создаем новый список эпиков
        id = getSomeIdEpic(taskManger);
        System.out.println("Количество всех эпиков до удаления по Id = "
                + taskManger.getAllEpic().size());
        taskManger.removeEpicById(id);
        System.out.println("Количество всех эпиков после удаления по Id = "
                + taskManger.getAllEpic().size());
        System.out.println();


        System.out.println("ОБНОВЛЕНИЕ ЭПИКА ПО Id");
        taskManger.clearEpic();     // очищаем список эпиков
        createListEpicTask(taskManger); // создаем новый список эпиков
        Integer idEpic = getSomeIdEpic(taskManger);
        Epic epic = taskManger.getEpicById(idEpic);
        epic.setTitle("@@@@@@@"); // обновляем поле Title
        taskManger.updateEpic(idEpic, epic); // возвращаем задачу в список
        System.out.println("Результат обновления поля Title эпика по Id новым значением @@@@@@@:");
        System.out.println(taskManger.getEpicById(idEpic));
        System.out.println();


        System.out.println("ПРОВЕРКА ИЗМЕНЕНИЯ СТАТУСА ЭПИКА");
        taskManger.clearEpic();     // очищаем список эпиков
        createListEpicTask(taskManger); // создаем новый список эпиков
        id = getSomeIdEpic(taskManger); // получаем некоторый id из taskManger
        epic = taskManger.getEpicById(id); // выбрали эпик из списка
        HashMap<Integer, Task> listSubtask = epic.getAllSubtask();// выбрали списк подзадач
        System.out.println("Статус всех подзадач эпика устанавливаем в DONE");
        for (Integer iter : listSubtask.keySet()) {
            task = listSubtask.get(iter);
            task.setStatus(StatusTask.DONE);
            listSubtask.put(iter, task);
        }
        epic.setListSubtask(listSubtask);
        System.out.println("Статус эпика становится:  " + epic.getStatusTask());

        System.out.println("Статус всех подзадач эпика устанавливаем в NEW");
        for (Integer iter : listSubtask.keySet()) {
            task = listSubtask.get(iter);
            task.setStatus(StatusTask.NEW);
            listSubtask.put(iter, task);
        }
        epic.setListSubtask(listSubtask);
        System.out.println("Статус эпика становится:  " + epic.getStatusTask());

        System.out.println("Статус всех подзадач эпика устанавливаем в DONE кроме последней с NEW");
        Integer idEndSubtask = 0;
        for (Integer iter : listSubtask.keySet()) {
            task = listSubtask.get(iter);
            task.setStatus(StatusTask.DONE);
            listSubtask.put(iter, task);
            idEndSubtask = iter;
        }
        task = listSubtask.get(idEndSubtask);
        task.setStatus(StatusTask.NEW);  // у последней подзадачи статус NEW
        listSubtask.put(idEndSubtask, task);
        epic.setListSubtask(listSubtask);
        System.out.println("Статус эпика становится:  " + epic.getStatusTask());

        System.out.println("Статус всех подзадач эпика устанавливаем в DONE кроме последней с IN_PROGRES");
        idEndSubtask = 0;
        for (Integer iter : listSubtask.keySet()) {
            task = listSubtask.get(iter);
            task.setStatus(StatusTask.DONE);
            listSubtask.put(iter, task);
            idEndSubtask = iter;
        }
        task = listSubtask.get(idEndSubtask);
        task.setStatus(StatusTask.IN_PROGRESS);  // у последней подзадачи статус IN_PROGRESS
        listSubtask.put(idEndSubtask, task);
        epic.setListSubtask(listSubtask);
        System.out.println("Статус эпика становится:  " + epic.getStatusTask());
        System.out.println();


        ////////// ТЕСТИРОВАНИЕ ПОДЗАДАЧ //////////////////////////////////////
        System.out.println();
        System.out.println("ТЕСТИРОВАНИЕ ПОДЗАДАЧ");
        System.out.println();


        System.out.println("ПОЛУЧЕНИЕ СПИСКА ВСЕХ ПОДЗАДАЧ ЭПИКА");
        taskManger.clearEpic();     // очищаем список эпиков
        createListEpicTask(taskManger); // создаем новый список эпиков
        idEpic = getSomeIdEpic(taskManger);// получаем некоторый id эпика из taskManger
        epic = (taskManger.getEpicById(idEpic)); // получаем эпик
        listSubtask = epic.getAllSubtask(); // список подзадач
        System.out.println("Список подзадач эпика:\n" + "Epic[" + idEpic
                + "]=" + listSubtask);
        System.out.println();

        System.out.println("УДАЛЕНИЕ ВСЕХ ПОДЗАДАЧ ЭПИКА");
        taskManger.clearEpic();     // очищаем список эпиков
        createListEpicTask(taskManger); // создаем новый список эпиков
        idEpic = getSomeIdEpic(taskManger);// получаем некоторый id эпика из taskManger
        epic = taskManger.getEpicById(idEpic);
        System.out.println("Список подзадач эпика до удаления:\n" + listSubtask);
        epic.clearSubtask();
        listSubtask = epic.getAllSubtask();
        System.out.println("Список подзадач эпика после удаления:\n" + listSubtask);
        System.out.println();

        System.out.println("ПОЛУЧЕНИЕ ПОДЗАДАЧИ ЭПИКА ПО Id");
        taskManger.clearEpic();     // очищаем список эпиков
        createListEpicTask(taskManger); // создаем новый список эпиков
        idEpic = getSomeIdEpic(taskManger);// получаем некоторый id эпика из taskManger
        epic = taskManger.getEpicById(idEpic);
        Integer idSubtask;
        for (Integer iter : epic.getAllSubtask().keySet()) {
            idSubtask = iter;
            task = epic.getSubtaskById(idSubtask);
            System.out.println("Подзадача эпика с Id"
                    + " эпика " + idEpic + " и Id подзадачи " + idSubtask + "\n" + task);
            System.out.println();
            break; // выбираем первый Id подзадачи
        }

        System.out.println("ОБНОВЛЕНИЕ ПОЛЯ ПОДЗАДАЧИ ЭПИКА");
        taskManger.clearEpic();     // очищаем список эпиков
        createListEpicTask(taskManger); // создаем новый список эпиков
        idEpic = getSomeIdEpic(taskManger);// получаем некоторый id эпика из taskManger
        epic = taskManger.getEpicById(idEpic);// достали эпик из taskManger
        listSubtask = epic.getAllSubtask(); // достали список задач из эпик
        for (int idSubTsk : listSubtask.keySet()) {
            task = epic.getSubtaskById(idSubTsk); // достали задачу из списка задач
            task.setTitle("@@@@@@@@@@"); // изменили наименование подзадачи
            epic.updateSubtask(idSubTsk, task); // обновили список задач эпик
            taskManger.updateEpic(idEpic, epic); // обновили спиок taskManger
            break; // выбираем первый Id подзадачи
        }
        System.out.println("Обновление поля Title подзадачи эпика c Id " + idEpic
                + " значением @@@@@@@@@@:\n" + taskManger.getEpicById(idEpic).toString() + " \n ");


        System.out.println("УДАЛЕНИЕ ПОДЗАДАЧИ ИЗ СПИСКА ПОДЗАДАЧ ЭПИКА");
        taskManger.clearEpic();     // очищаем список эпиков
        createListEpicTask(taskManger); // создаем новый список эпиков
        idEpic = getSomeIdEpic(taskManger);// получаем случайный id эпика из taskManger
        epic = (taskManger.getEpicById(idEpic));// достали эпик из taskManger
        listSubtask = epic.getAllSubtask(); // достали список задач из эпик
        System.out.println("Длина списка подзадач до удаления подзадачи: "
                + epic.getAllSubtask().size());
        for (Integer idSubTsk : listSubtask.keySet()) {
            epic.removeSubtaskById(idSubTsk); // удалили из списка подзадачу
            taskManger.updateEpic(idEpic, epic); // положили обратно в taskManger
            break; // взяли для обработки только первый элемент списка
        }
        System.out.println("Длина списка подзадач после удаления подзадачи: "
                + (taskManger.getEpicById(idEpic)).getAllSubtask().size());

    }


    public static HashMap<Integer, Task> createSubTask(int range) {
        HashMap<Integer, Task> listSubtask = new HashMap<>();
        for (int i = 0; i < range; i++) {
            Task subtask = new Task("nameSubTask" + i, "descriptionSubTask" + i);
            listSubtask.put(subtask.getUid(), subtask);
        }
        return listSubtask;
    }

    public static void createListEpicTask(TaskManger taskManger) {
        for (int i = 0; i < 5; i++) {
            Task task = new Task("nameTask" + i, "descriptionTask" + i);
            taskManger.addTask(task.getUid(), task);

            Epic epic = new Epic("nameEpic" + i, "descriptionEpic" + i);
            epic.setListSubtask(createSubTask(3));
            taskManger.addEpic(epic.getUid(), epic);
        }
    }

    public static Integer getSomeId(TaskManger taskManger) {
        Integer id = 0;
        for (Integer iter : taskManger.getAllTask().keySet()) {
            id = iter;
            break; // выбираем первый Id  задачи
        }
        return id;
    }

    public static Integer getSomeIdEpic(TaskManger taskManger) {
        Integer id = 0;
        for (Integer iter : taskManger.getAllEpic().keySet()) {
            id = iter;
            break; // выбираем первый Id эпика
        }
        return id;
    }


}
