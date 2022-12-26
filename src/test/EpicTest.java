package test;

import manager.Manager;
import manager.TaskManager;
import model.Epic;
import model.StatusTask;
import model.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager inMemoryTaskManager = Manager.getDefault();
    Epic epic;
    Integer idEpic1;
    Integer idEpic2;
    Integer idSubtask1;
    Integer idSubtask2;
    Integer idSubtask3;


    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager.clearEpic();
        // создаем новый список эпиков
        epic = new Epic("nameEpic" + 1, "descriptionEpic" + 1);
        idEpic1 = epic.getUid();
        inMemoryTaskManager.addEpic(idEpic1, epic);


        epic = new Epic("nameEpic" + 2, "descriptionEpic" + 2);
        idEpic2 = epic.getUid();
        inMemoryTaskManager.addEpic(idEpic2, epic);


        // создаем новый список подзадач эпика 2
        Subtask subtask = new Subtask("nameSubtask" + 1, "descriptionSubtask" + 1);
        subtask.setIdEpic(idEpic2);
        idSubtask1 = subtask.getUid();
        inMemoryTaskManager.addSubtask(idSubtask1, subtask);

        subtask = new Subtask("nameSubtask" + 2, "descriptionSubtask" + 2);
        subtask.setIdEpic(idEpic2);
        idSubtask2 = subtask.getUid();
        inMemoryTaskManager.addSubtask(idSubtask2, subtask);

        subtask = new Subtask("nameSubtask" + 3, "descriptionSubtask" + 3);
        subtask.setIdEpic(idEpic2);
        idSubtask3 = subtask.getUid();
        inMemoryTaskManager.addSubtask(idSubtask3, subtask);
    }

    @AfterEach
    public void afterEach() {
        inMemoryTaskManager.clearEpic();
    }


    // a. Пустой список подзадач.
    @Test
    public void shouldBeStatusEpicDoneForEmptyListOfSubtasks() {
        Epic epic = inMemoryTaskManager.getEpicById(idEpic2);
        List<Subtask> listSubtask = inMemoryTaskManager.getAllSubtaskEpic(idEpic2);
        assertEquals(3, listSubtask.size(),
                "Список пдзадач должен состоять из 3 подзадач");

        // удаляем подзадачи
        inMemoryTaskManager.removeSubtaskById(idSubtask1);
        inMemoryTaskManager.removeSubtaskById(idSubtask2);
        inMemoryTaskManager.removeSubtaskById(idSubtask3);

        listSubtask = inMemoryTaskManager.getAllSubtaskEpic(idEpic2);
        assertEquals(0, listSubtask.size(),
                "Список пдзадач не должен иметь подзадач");

        assertEquals("DONE", epic.getStatus().toString(),
                "Статус эпика не DONE");
    }


    // b. Все подзадачи со статусом NEW.
    @Test
    public void shouldBeStatusEpicNewForAllSubtasksWithStatusNew() {
        Epic epic = inMemoryTaskManager.getEpicById(idEpic2);
        List<Subtask> listSubtask = inMemoryTaskManager.getAllSubtaskEpic(idEpic2);// выбрали списк подзадач
        // Статус всех подзадач эпика устанавливаем в NEW
        for (Subtask subtask : listSubtask) {
            subtask.setStatus(StatusTask.NEW);
            inMemoryTaskManager.updateSubtaskById(subtask.getUid(), subtask);
        }
        assertEquals("NEW", epic.getStatus().toString());
    }

    // c. Все подзадачи со статусом DONE.
    @Test
    public void shouldBeStatusEpicDoneForAllSubtasksWithStatusDone() {
        Epic epic = inMemoryTaskManager.getEpicById(idEpic2);
        List<Subtask> listSubtask = inMemoryTaskManager.getAllSubtaskEpic(idEpic2);
        // Статус всех подзадач эпика устанавливаем в DONE
        for (Subtask subtask : listSubtask) {
            subtask.setStatus(StatusTask.DONE);
            inMemoryTaskManager.updateSubtaskById(subtask.getUid(), subtask);
        }
        assertEquals("DONE", epic.getStatus().toString());
    }

    // d. Подзадачи со статусами NEW и DONE.
    @Test
    public void shouldBeStatusEpicIn_progressForSubstasksWithStatusNewAndDone() {
        Epic epic = inMemoryTaskManager.getEpicById(idEpic2);
        List<Subtask> listSubtask = inMemoryTaskManager.getAllSubtaskEpic(idEpic2);// выбрали списк подзадач
        // Статус 1 и 3 подзадач эпика устанавливаем в DONE
        for (Subtask subtask : listSubtask) {
            subtask.setStatus(StatusTask.DONE);
            inMemoryTaskManager.updateSubtaskById(subtask.getUid(), subtask);
        }
        // Статус 2 подзадачи эпика устанавливаем в NEW
        Subtask subtask = inMemoryTaskManager.getSubtaskById(idSubtask2);
        subtask.setStatusTask(StatusTask.NEW);
        inMemoryTaskManager.updateSubtaskById(idSubtask2, subtask);

        assertEquals("IN_PROGRESS", epic.getStatus().toString());
    }

    // e. Подзадачи со статусом IN_PROGRESS.
    @Test
    public void shouldBeStatusEpicIn_progressForSubstasksWithStatusIn_progress() {
        Epic epic = inMemoryTaskManager.getEpicById(idEpic2);
        List<Subtask> listSubtask = inMemoryTaskManager.getAllSubtaskEpic(idEpic2);
        // Статус всех подзадач эпика устанавливаем в IN_PROGRESS
        for (Subtask subtask : listSubtask) {
            subtask.setStatus(StatusTask.IN_PROGRESS);
            inMemoryTaskManager.updateSubtaskById(subtask.getUid(), subtask);
        }
        assertEquals("IN_PROGRESS", epic.getStatus().toString());
    }

}