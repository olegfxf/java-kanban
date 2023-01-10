package test;

import server.KVServer;
import manager.Manager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    static KVServer kvServer;
    Task savedTask;
    TaskManager inMemoryTaskManager = Manager.getDefault();
    Integer max_value = Integer.MAX_VALUE;

    Task task;
    Task task2;
    Task task3;
    Integer idTask;
    Integer idTask2;
    Integer idTask3;


    Epic epic;
    Epic epic2;
    Epic epic3;
    Integer idEpic;
    Integer idEpic2;
    Integer idEpic3;

    Subtask subtask1, subtask2, subtask3;
    Subtask subtask21, subtask22, subtask23;

    Integer idSubtask1, idSubtask2, idSubtask3;
    Integer idSubtask21, idSubtask22, idSubtask23;

    @BeforeAll
    static void beforeAll() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();

        task = new Task("nameTask" + 1, "descriptionTask" + 1);
        idTask = task.getUid();
        task.setStartTimeDuration(LocalDateTime.of(2022, 2, 2, 10, 0),
                Duration.ofDays(2));
        inMemoryTaskManager.addTask(idTask, task);
        task2 = new Task("nameTask" + 2, "descriptionTask" + 2);
        idTask2 = task2.getUid();
        task2.setStartTimeDuration(LocalDateTime.of(2022, 5, 2, 20, 0),
                Duration.ofDays(5));
        inMemoryTaskManager.addTask(idTask2, task2);
        task3 = new Task("nameTask" + 3, "descriptionTask" + 3);
        idTask3 = task3.getUid();
        task3.setStartTimeDuration(LocalDateTime.of(2022, 3, 2, 12, 0),
                Duration.ofDays(20));
        inMemoryTaskManager.addTask(idTask3, task3);

        epic = new Epic("nameEpic" + 1, "descriptionEpic" + 1);
        idEpic = epic.getUid();
        inMemoryTaskManager.addEpic(idEpic, epic);
        epic2 = new Epic("nameEpic" + 2, "descriptionEpic" + 2);
        idEpic2 = epic2.getUid();
        inMemoryTaskManager.addEpic(idEpic2, epic2);
        epic3 = new Epic("nameEpic" + 3, "descriptionEpic" + 3);
        idEpic3 = epic3.getUid();
        inMemoryTaskManager.addEpic(idEpic3, epic3);


        subtask1 = new Subtask("nameSubtask_" + 1, "descriptionSubtask_" + 1);
        idSubtask1 = subtask1.getUid();
        subtask1.setStartTimeDuration(LocalDateTime.of(2000, 4, 22, 10, 0),
                Duration.ofDays(2));
        subtask1.setIdEpic(idEpic);
        inMemoryTaskManager.addSubtask(idSubtask1, subtask1);
        subtask2 = new Subtask("nameSubtask_" + 2, "descriptionSubtask_" + 2);
        subtask2.setStartTimeDuration(LocalDateTime.of(2000, 3, 22, 10, 0),
                Duration.ofDays(2));
        idSubtask2 = subtask2.getUid();
        subtask2.setIdEpic(idEpic);
        inMemoryTaskManager.addSubtask(idSubtask2, subtask2);
        subtask3 = new Subtask("nameSubtask_" + 3, "descriptionSubtask_" + 3);
        subtask3.setStartTimeDuration(LocalDateTime.of(2000, 2, 22, 10, 0),
                Duration.ofDays(2));
        idSubtask3 = subtask3.getUid();
        subtask3.setIdEpic(idEpic);
        inMemoryTaskManager.addSubtask(idSubtask3, subtask3);

        subtask21 = new Subtask("nameSubtask" + 21, "descriptionSubtask" + 21);
        idSubtask21 = subtask21.getUid();
        subtask21.setIdEpic(idEpic2);
        inMemoryTaskManager.addSubtask(idSubtask21, subtask21);
        subtask22 = new Subtask("nameSubtask" + 22, "descriptionSubtask" + 22);
        idSubtask22 = subtask22.getUid();
        subtask22.setIdEpic(idEpic2);
        inMemoryTaskManager.addSubtask(idSubtask22, subtask22);
        subtask23 = new Subtask("nameSubtask" + 23, "descriptionSubtask" + 23);
        idSubtask23 = subtask23.getUid();
        subtask23.setIdEpic(idEpic2);
        inMemoryTaskManager.addSubtask(idSubtask23, subtask23);

        inMemoryTaskManager.getTaskById(idTask2);
        inMemoryTaskManager.getTaskById(idTask);
        inMemoryTaskManager.getTaskById(idTask3);

        inMemoryTaskManager.getTaskById(idTask);
        inMemoryTaskManager.getTaskById(idTask2);

        inMemoryTaskManager.getTaskById(idTask2);
        inMemoryTaskManager.getTaskById(idTask);

        inMemoryTaskManager.getEpicById(idEpic);
        inMemoryTaskManager.getEpicById(idEpic2);

        inMemoryTaskManager.getEpicById(idEpic2);
        inMemoryTaskManager.getEpicById(idEpic);

        inMemoryTaskManager.getEpicById(idEpic2);
        inMemoryTaskManager.getEpicById(idEpic3);

        inMemoryTaskManager.getSubtaskById(idSubtask3);
        inMemoryTaskManager.getSubtaskById(idSubtask1);
        inMemoryTaskManager.getSubtaskById(idSubtask2);

    }

    @AfterEach
    public void AfterEach() {
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpic();
    }

    @AfterAll
    static void afterAll(){ kvServer.stop(); }


    @Test
    void addTask() {
        // a. Со стандартным поведением.
        savedTask = inMemoryTaskManager.getTaskById(idTask);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают");

        Map<Integer, Task> tasks = inMemoryTaskManager.getAllTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(idTask), "Задачи не совпадают.");

        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        // запрашиваем задачу с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getTaskById(max_value));
        System.out.printf("\nТестирование addTask() с неверным идентификатором: "
                + exception2.getMessage());


        // перекрытие временных интервалов задач
        Task task4 = new Task("nameTask" + 4, "descriptionTask" + 4);
        Integer idTask4 = task4.getUid();
        task4.setStartTimeDuration(LocalDateTime.of(2022, 4, 25, 10, 0),
                Duration.ofDays(20));
        inMemoryTaskManager.addTask(idTask4, task4);

        Exception exception3 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getTaskById(idTask4));
        System.out.printf("\nТестирование addTask() для задач с перекрывающимися интервалами: "
                + exception3.getMessage() + ". Задача с некорректными временным характеристиками" +
                " не записана в список задач.");
        assertEquals(3, inMemoryTaskManager.getAllTask().size(),
                "Количество задач не равно 3.");

        // b. С пустым списком задач.
        inMemoryTaskManager.clearTask();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getTaskById(idTask));
        System.out.printf("\nТестирование addTask() с пустым списком: " + exception.getMessage());
    }

    @Test
    void getTaskById() {
        // a. Со стандартным поведением.
        Task requestTask = inMemoryTaskManager.getTaskById(idTask);
        assertNotNull(requestTask, "Задача не найдена.");
        assertEquals(task, requestTask, "Задачи не совпадают");

        Map<Integer, Task> tasks = inMemoryTaskManager.getAllTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(idTask), "Задачи не совпадают.");

        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        // запрашиваем задачу с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getTaskById(max_value));
        System.out.printf("\nТестирование getTaskById() с неверным идентификатором: "
                + exception2.getMessage());

        // b. С пустым списком задач.
        inMemoryTaskManager.clearTask();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getTaskById(idTask));
        System.out.printf("\nТестирование getTaskById() с пустым списком: "
                + exception.getMessage());

    }

    @Test
    void updateTask() {
        // a. Со стандартным поведением.
        Task task = inMemoryTaskManager.getTaskById(idTask);
        task.setTitle("testName");
        inMemoryTaskManager.updateTask(idTask, task);

        Task updatedTask = inMemoryTaskManager.getTaskById(idTask);
        assertNotNull(updatedTask, "Задача не найдена.");
        assertEquals("testName", updatedTask.getTitle(),
                "Наименования задач не совпадают");

        Map<Integer, Task> tasks = inMemoryTaskManager.getAllTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(updatedTask, tasks.get(idTask), "Задачи не совпадают.");

        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        task2.setTitle("updatedTitle");
        // обновляем задачу с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.updateTask(max_value, task2));
        System.out.printf("\nТестирование updateTask() с неверным идентификатором: "
                + exception2.getMessage());

        // b. С пустым списком задач.
        inMemoryTaskManager.clearTask();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.updateTask(idTask2, task2));
        System.out.printf("\nТестирование updateTask() с пустым списком: "
                + exception.getMessage());

    }

    @Test
    void removeTaskById() {
        // a. Со стандартным поведением.
        inMemoryTaskManager.removeTaskById(idTask);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getTaskById(idTask));
        System.out.printf("\nТестирование removeTaskById() cо стандартным поведением." +
                " Задача не найдена: " + exception.getMessage());

        Map<Integer, Task> tasks = inMemoryTaskManager.getAllTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач: "
                + tasks.size() + "Количество задач должнобыть равно 2.");

        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        // удаляем задачу с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.removeTaskById(max_value));
        System.out.printf("\nТестирование removeTaskById() с неверным идентификатором: "
                + exception2.getMessage());

        // b. С пустым списком задач.
        inMemoryTaskManager.clearTask();
        Exception exception3 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.removeTaskById(idTask));
        System.out.printf("\nТестирование removeTaskById() с пустым списком: "
                + exception3.getMessage());

    }

    @Test
    void getAllTask() {
        assertEquals(3, inMemoryTaskManager.getAllTask().size(),
                "Количество задач в списке не равно 3.");
    }

    @Test
    void clearTask() {
        inMemoryTaskManager.clearTask();
        assertEquals(0, inMemoryTaskManager.getAllTask().size(),
                "Список задач должен быть пустой");
    }


    ///////////////// epic testing /////////////////////////////
    // Для подзадач нужно дополнительно проверить наличие эпика,
    // а для эпика — расчёт статуса.

    @Test
    void addEpic() {
        // Эпики в список добавлены в beforeEach()
        // a. Со стандартным поведением.
        Epic savedEpic = inMemoryTaskManager.getEpicById(idEpic);
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают");

        Map<Integer, Epic> epics = inMemoryTaskManager.getAllEpic();
        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(3, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(idEpic), "Эпики не совпадают.");

        //c. С неверным идентификатором эпика (пустой и/или несуществующий идентификатор).
        // запрашиваем эпик с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getEpicById(max_value));
        System.out.printf("\nТестирование addEpic() с неверным идентификатором: " + exception2.getMessage());

        // Вставка эпика с перекрывающимися временными интервалами
        Epic epic4 = new Epic("nameEpic" + 4, "descriptionEpic" + 4);
        Integer idEpic4 = epic4.getUid();

        Subtask subtask41 = new Subtask("nameSubtask4" + 1, "descriptionSubtask4" + 1);
        Integer idSubtask41 = subtask41.getUid();
        subtask41.setStartTimeDuration(LocalDateTime.of(2000, 4, 2, 1, 0),
                Duration.ofDays(2));
        subtask41.setIdEpic(idEpic4);
        inMemoryTaskManager.addSubtask(idSubtask41, subtask41);

        Subtask subtask42 = new Subtask("nameSubtask4" + 2, "descriptionSubtask4" + 2);
        subtask42.setStartTimeDuration(LocalDateTime.of(2000, 4, 1, 20, 0),
                Duration.ofDays(2));
        Integer idSubtask42 = subtask42.getUid();
        subtask42.setIdEpic(idEpic4);
        inMemoryTaskManager.addSubtask(idSubtask42, subtask42);
        Subtask subtask43 = new Subtask("nameSubtask4" + 3, "descriptionSubtask4" + 3);
        subtask43.setStartTimeDuration(LocalDateTime.of(2000, 2, 22, 10, 0),
                Duration.ofDays(2));
        Integer idSubtask43 = subtask43.getUid();
        subtask43.setIdEpic(idEpic4);
        inMemoryTaskManager.addSubtask(idSubtask43, subtask43);

        Integer beginNumSubtask = inMemoryTaskManager.getAllSubtask().size();
        inMemoryTaskManager.addEpic(idEpic4, epic4);
        Integer finishNumSubtask = inMemoryTaskManager.getAllSubtask().size();
        assertEquals(beginNumSubtask, finishNumSubtask, "Эпик добавлен." +
                " Количество подзадач не должно увеличиться на "
                + (finishNumSubtask - beginNumSubtask));


        // b. С пустым списком эпиков.
        inMemoryTaskManager.clearEpic();
        //список подзадач пуст, ожидаем NullPointerException
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getEpicById(idEpic));
        System.out.printf("\nТестирование addEpic() с пустым списком: "
                + exception.getMessage());

    }

    @Test
    void getEpicById() {
        // a. Со стандартным поведением.
        Epic requestEpic = inMemoryTaskManager.getEpicById(idEpic);
        assertNotNull(requestEpic, "Эпик не найден.");
        assertEquals(epic, requestEpic, "Эпики не совпадают.");

        Map<Integer, Epic> epics = inMemoryTaskManager.getAllEpic();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(3, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(idEpic), "Эпики не совпадают.");

        //c. С неверным идентификатором эпика (пустой и/или несуществующий идентификатор).
        // запрашиваем эпик с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getEpicById(max_value));
        System.out.printf("\nТестирование getEpicById() с неверным идентификатором: "
                + exception2.getMessage());

        // b. С пустым списком эпиков.
        inMemoryTaskManager.clearEpic();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getEpicById(idEpic));
        System.out.printf("\nТестирование getEpicById() с пустым списком: "
                + exception.getMessage());

    }

    @Test
    void updateEpic() {
        // a. Со стандартным поведением.
        Epic epic = inMemoryTaskManager.getEpicById(idEpic);
        epic.setTitle("testName");
        inMemoryTaskManager.updateEpic(idEpic, epic);

        Epic updatedEpic = inMemoryTaskManager.getEpicById(idEpic);
        assertNotNull(updatedEpic, "Эпик не найден.");
        assertEquals("testName", updatedEpic.getTitle(),
                "Наименования эпиков не совпадают");

        Map<Integer, Epic> epics = inMemoryTaskManager.getAllEpic();
        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(3, epics.size(), "Неверное количество эпиков.");
        assertEquals(updatedEpic, epics.get(idEpic), "Эпики не совпадают.");

        //c. С неверным идентификатором эпика (пустой и/или несуществующий идентификатор).
        epic2.setTitle("updatedTitle");
        // обновляем эпик с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.updateEpic(max_value, epic2));
        System.out.printf("\nТестирование updateEpic() с неверным идентификатором: "
                + exception2.getMessage());

        // b. С пустым списком эпиков.
        inMemoryTaskManager.clearEpic();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.updateEpic(idEpic, epic));
        System.out.printf("\nТестирование updateEpic() с пустым списком: "
                + exception.getMessage());

    }

    @Test
    void removeEpicById() {
        // a. Со стандартным поведением.
        inMemoryTaskManager.removeEpicById(idEpic);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getEpicById(idEpic));
        System.out.printf("\nТестирование removeEpicById() cо стандартным поведением." +
                " Эпик не найден: " + exception.getMessage());

        Map<Integer, Epic> epics = inMemoryTaskManager.getAllEpic();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(2, epics.size(), "Количество эпиков не равно 2.");

        //c. С неверным идентификатором эпика (пустой и/или несуществующий идентификатор).
        // удаляем эпик с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.removeEpicById(max_value));
        System.out.printf("\nТестирование removeEpicById() с неверным идентификатором: "
                + exception2.getMessage());

        // b. С пустым списком эпиков.
        inMemoryTaskManager.clearEpic();
        Exception exception3 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.removeEpicById(idEpic));
        System.out.printf("\nТестирование removeEpicById() с пустым списком: "
                + exception3.getMessage());

    }

    @Test
    void getAllEpic() {
        assertEquals(3, inMemoryTaskManager.getAllEpic().size(),
                "Количество эпиков в списке не равно 3.");
    }

    @Test
    void clearEpic() {
        inMemoryTaskManager.clearEpic();
        assertEquals(0, inMemoryTaskManager.getAllEpic().size(),
                "Список эпиков должен быть пустой");
    }


    //////////////////////////// subtask testing /////////////////////////
    // Для подзадач нужно дополнительно проверить наличие эпика

    @Test
    void isPresentEpicForSubtask() {
        for (Integer idSubtask : inMemoryTaskManager.getAllSubtask().keySet()) {
            Integer idEpic = inMemoryTaskManager.getSubtaskById(idSubtask).getIdEpic();
            assertNotNull(inMemoryTaskManager.getEpicById(idEpic),
                    "Эпик с идентификатором: " + idEpic
                            + " для подзадачи c idSubtask "
                            + idSubtask + " не найден.");
        }
    }

    @Test
    void addSubtask() {


        // a. Со стандартным поведением.
        // метод addSubtask() уже активирован в beforeEach()
        Subtask savedSubtask = inMemoryTaskManager.getSubtaskById(idSubtask1);
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask1, savedSubtask, "Подзадачи не совпадают");

        Map<Integer, Subtask> subtasks = inMemoryTaskManager.getAllSubtask();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(6, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(idSubtask1), "Подзадачи не совпадают.");

        //c. С неверным идентификатором подзадачи (пустой и/или несуществующий идентификатор).
        // запрашиваем подзадачу с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getSubtaskById(max_value));
        System.out.printf("\nТестирование addSubtask() с неверным идентификатором: " + exception2.getMessage());

        // b. С пустым списком подзадач.
        inMemoryTaskManager.clearSubtaskEpic(idEpic);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getSubtaskById(idSubtask1));
        System.out.printf("\nТестирование addTask() с пустым списком: " + exception.getMessage());

    }

    @Test
    void getSubtaskById() {
        // a. Со стандартным поведением.
        Subtask requestSubtask = inMemoryTaskManager.getSubtaskById(idSubtask1);
        assertNotNull(requestSubtask, "Подзадача не найдена.");
        assertEquals(subtask1, requestSubtask, "Ползадачи не совпадают");

        Map<Integer, Subtask> subtasks = inMemoryTaskManager.getAllSubtask();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(6, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(idSubtask1), "Подзадачи не совпадают.");

        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        // запрашиваем подзадачу с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getSubtaskById(max_value));
        System.out.printf("\nТестирование getSubtaskById() с неверным идентификатором: " + exception2.getMessage());

        // b. С пустым списком подзадач.
        inMemoryTaskManager.clearSubtaskEpic(idEpic);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getSubtaskById(idSubtask1));
        System.out.printf("\nТестирование getSubtaskById() с пустым списком: " + exception.getMessage());

    }

    @Test
    void updateSubtaskById() {
        // a. Со стандартным поведением.
        Subtask subtask = inMemoryTaskManager.getSubtaskById(idSubtask1);
        subtask.setTitle("testName");
        inMemoryTaskManager.updateSubtaskById(idSubtask1, subtask);

        Subtask updatedSubtask = inMemoryTaskManager.getSubtaskById(idSubtask1);
        assertNotNull(updatedSubtask, "Подзадача не найдена.");
        assertEquals("testName", updatedSubtask.getTitle(), "Наименования " +
                "подзадач не совпадают");

        Map<Integer, Subtask> subtasks = inMemoryTaskManager.getAllSubtask();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(6, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(updatedSubtask, subtasks.get(idSubtask1), "Подзадачи не совпадают.");

        //c. С неверным идентификатором подзадачи (пустой и/или несуществующий идентификатор).
        subtask2.setTitle("updatedTitle");
        Subtask updatedSubtask2 = subtask2;
        // обновляем подзадачу с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.updateSubtaskById(max_value, updatedSubtask2));
        System.out.printf("\nТестирование updateSubtaskById() с неверным идентификатором: "
                + exception2.getMessage());

        // b. С пустым списком подзадач.
        inMemoryTaskManager.clearSubtaskEpic(idEpic);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.updateSubtaskById(idSubtask2, updatedSubtask2));
        System.out.printf("\nТестирование updateSubtaskById() с пустым списком: "
                + exception.getMessage());

    }

    @Test
    void removeSubtaskById() {
        // a. Со стандартным поведением.
        inMemoryTaskManager.removeSubtaskById(idSubtask1);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.getSubtaskById(idSubtask1));
        System.out.printf("\nТестирование removeSubtaskById() cо стандартным поведением."
                + " Подзадача не найдена: " + exception.getMessage());

        Map<Integer, Subtask> subtasks = inMemoryTaskManager.getAllSubtask();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(5, subtasks.size(), "Неверное количество подзадач: "
                + subtasks.size());

        //c. С неверным идентификатором подзадачи (пустой и/или несуществующий идентификатор).
        // удаляем подзадачу с неверным идентификатором max_value
        Exception exception2 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.removeSubtaskById(max_value));
        System.out.printf("\nТестирование removeTaskById() с неверным идентификатором: "
                + exception2.getMessage());

        // b. С пустым списком подзадач.
        inMemoryTaskManager.clearSubtaskEpic(idEpic);
        Exception exception3 = assertThrows(IllegalArgumentException.class,
                () -> inMemoryTaskManager.removeSubtaskById(idSubtask1));
        System.out.printf("\nТестирование removeTaskById() с пустым списком: " + exception3.getMessage());

    }

    @Test
    void getAllSubtaskEpic() {
        assertEquals(3, inMemoryTaskManager.getAllSubtaskEpic(idEpic).size(),
                "Количество подзадач в списке не равно 3.");
    }

    @Test
    void clearSubtaskEpic() {
        inMemoryTaskManager.clearSubtaskEpic(idEpic);
        assertEquals(0, inMemoryTaskManager.getAllSubtaskEpic(idEpic).size(),
                "Список задач должен быть пустой");
    }

    @Test
    void checkStatus() {
        // Тестирование реализовано в классе test.EpicTest
    }

    @Test
    void checkTaskSequence() {
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearSortedTask();

        task = new Task("nameTask" + 1, "descriptionTask" + 1);
        idTask = task.getUid();
        task.setStartTimeDuration(LocalDateTime.of(2022, 2, 2, 10, 0),
                Duration.ofDays(2));
        inMemoryTaskManager.addTask(idTask, task);
        task2 = new Task("nameTask" + 2, "descriptionTask" + 2);
        idTask2 = task2.getUid();
        task2.setStartTimeDuration(LocalDateTime.of(2022, 5, 2, 20, 0),
                Duration.ofDays(5));
        inMemoryTaskManager.addTask(idTask2, task2);
        task3 = new Task("nameTask" + 3, "descriptionTask" + 3);
        idTask3 = task3.getUid();
        task3.setStartTimeDuration(LocalDateTime.of(2022, 3, 2, 12, 0),
                Duration.ofDays(20));
        inMemoryTaskManager.addTask(idTask3, task3);

        inMemoryTaskManager.getPrioritizedTasks2();
        inMemoryTaskManager.getPrioritizedTasks();
    }


}